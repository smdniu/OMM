package com.asiainfo.omm.app.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ai.appframe2.common.ServiceManager;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.SessionFactory;
import com.asiainfo.omm.utils.TimeUtils;

public class DBTool {
	private static Logger log = Logger.getLogger(DBTool.class);
	
	public static Map<String,String> queryAllTableSql = new HashMap<String,String>();	
	static{
		queryAllTableSql.put(OMMConstantEnum.DBType.ORACLE, " SELECT TABLE_NAME FROM USER_TABLES ");
		queryAllTableSql.put(OMMConstantEnum.DBType.SUNDB, " SELECT TABLE_NAME FROM USER_TABLES ");
	}

	public static Map<String,Map<String,String>> getAllDB(){
		return OmmMutilDataSourceImpl.getAllDbAcct();
	}
	
	public static String getDbUserName(String ds){
		if(StringUtils.isBlank(ds)){
			return "";
		}
		Map<String,Map<String,String>> map = getAllDB();
		if(map.containsKey(ds)){
			return map.get(ds).get("username");
		}
		return "";
	}
	
	private static String getUserTableSql(String ds){
		if(StringUtils.isBlank(ds)){
			return "";
		}
		Map<String,Map<String,String>> map = getAllDB();
		if(map.containsKey(ds)){
			String url = map.get(ds).get("url");
			if(StringUtils.isBlank(url)){
				return "";
			}
			url = url.toUpperCase();
			if(url.indexOf(OMMConstantEnum.DBType.ORACLE)> -1){
				return queryAllTableSql.get(OMMConstantEnum.DBType.ORACLE);
			}else if(url.indexOf(OMMConstantEnum.DBType.SUNDB)> -1){
				return queryAllTableSql.get(OMMConstantEnum.DBType.SUNDB);
			}
		}
		return "";
	}
	
	public static Map<String,Object> getUserAllTables(String ds){
		String sql = DBTool.getUserTableSql(ds);
		return DBTool.execute(ds, sql, -1, -1);
	}
	
	public static Map<String,Object> executeAll(String ds,String[] sqls,int startIndex,int endIndex){
		Map<String,Object> result = new HashMap<String,Object>();
		if(sqls != null && sqls.length > 0){
			for(String sql : sqls){
				if(sql == null || sql.trim().length() == 0){
					continue;
				}
				sql = sql.trim();
				Map<String,Object> rs = DBTool.execute(ds, sql, startIndex, endIndex);
				result.put(TimeUtils.getTimeStrDefault()+"["+sql+"]", rs);
				if(OMMConstantEnum.RESULT_CODE_FAILURE.equals(String.valueOf(rs.get("resultCode")))){
					break;
				}
			}
		}
		return result;
	}
	
	public static Map<String,Object> executeOne(String ds,String sql,int startIndex,int endIndex){
		Map<String,Object> result = new HashMap<String,Object>();
		Map<String,Object> rs = DBTool.execute(ds, sql, startIndex, endIndex);
		result.put(TimeUtils.getTimeStrDefault()+"["+sql+"]", rs);
		return result;
	}
	
	private static Map<String,Object> execute(String ds,String sql,int startIndex,int endIndex){
		if(log.isInfoEnabled()){
			log.info("DbTool::execute::sql:::"+sql);
		}
		
		Map<String,Object> result = new HashMap<String,Object>();
		result.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
		
		String sqlType = "";
		try{
			sqlType = verifySql(sql,ds);
			result.put(OMMConstantEnum.RESULT_MSG, "执行成功");
		}catch(Exception e){
			result.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			result.put(OMMConstantEnum.RESULT_MSG, e.getMessage());
			return result;
		}
		Connection conn = null;
		try{
			if(OMMConstantEnum.SqlType.SELECT.equals(sqlType)){
				conn = ServiceManager.getSession().getConnection(ds);
				int count = getCount(sql, conn);
				Map<String,Object> rs = query(sql, conn, startIndex, endIndex);
				result.put("count", count);
				if(rs.containsKey("colums")){
					result.put("colums", rs.get("colums"));
				}
				if(rs.containsKey("data")){
					result.put("data", rs.get("data"));
				}
			}else if(OMMConstantEnum.SqlType.INSERT.equals(sqlType)){
				conn = ServiceManager.getSession().getNewConnection(ds);
				int count = insert(sql, conn);
				result.put("count", count);
			}else if(OMMConstantEnum.SqlType.UPDATE.equals(sqlType)){
				conn = ServiceManager.getSession().getNewConnection(ds);
				int count = update(sql, conn);
				result.put("count", count);
			}else if(OMMConstantEnum.SqlType.DELETE.equals(sqlType)){
				conn = ServiceManager.getSession().getNewConnection(ds);
				int count = delete(sql, conn);
				result.put("count", count);
			}
			conn.commit();
		}catch(Exception e){
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			result.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			result.put(OMMConstantEnum.RESULT_MSG, e.getMessage().trim());
		}finally{
			if(conn != null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	private static Map<String,Object> query(String sql,Connection conn,int startIndex,int endIndex) throws Exception{
		Map<String,Object> result = new HashMap<String,Object>();
		ResultSet rs = null;
		try{
			String final_sql = "";
			if(startIndex == -1 && endIndex == -1){
				final_sql = sql;
			}else{
				final_sql = "SELECT * FROM ( SELECT ROWNUM ROWNUM_ ,ROW_.* FROM ("+sql+
				           ") ROW_ WHERE ROWNUM <= "+endIndex+" ) WHERE ROWNUM_ >= "+startIndex;
			}
			if(log.isInfoEnabled()){
				log.info("DbTool::select::final_sql:::"+final_sql);
			}
			rs = conn.createStatement().executeQuery(final_sql);
			if(rs == null){
				return result;
			}
			List<String> colums = new ArrayList<String>();
			ResultSetMetaData rsmd = rs.getMetaData();
			int cc = rsmd.getColumnCount();
			for(int i = 1 ;i <= cc ; i++){
				if("ROWNUM_".equals(rsmd.getColumnName(i))){
					continue;
				}
				colums.add(rsmd.getColumnName(i));
			}
			List<Map<String,Object>> data = new ArrayList<Map<String,Object>>();
			while(rs.next()){
				Map<String,Object> row = new HashMap<String,Object>();
				for(String colum:colums){
					row.put(colum, rs.getObject(colum));
				}
				data.add(row);
			}
			result.put("colums", colums);
			result.put("data", data);
		}catch(Exception e){
			throw e;
		}finally{
			if(rs != null){
				rs.close();
			}
		}
		return result;
	}
	//插入
	private static int insert(String sql,Connection conn) throws Exception{
		return save(sql,conn);
	}
	//更新
	private static int update(String sql,Connection conn) throws Exception{
		return save(sql,conn);
	}
	//删除
	private static int delete(String sql,Connection conn) throws Exception{
		return save(sql,conn);
	}
	
	private static int save(String sql,Connection conn) throws Exception{
		int count = 0;
		try{
			count = conn.createStatement().executeUpdate(sql);
		}catch(Exception e){
			throw e;
		}
		return count;
	}
	
	private static int getCount(String sql,Connection conn) throws Exception{
		int count = 0;
		ResultSet rs = null;
		try{
			String final_sql = "select count(*) from ("+sql+")";
			if(log.isInfoEnabled()){
				log.info("DbTool::getCount::final_sql:::"+final_sql);
			}
			rs = conn.createStatement().executeQuery(final_sql);
			if(rs.next()){
				count = rs.getInt(1);
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(rs != null){
				rs.close();
			}
		}
		return count;
	}
	
	//校验sql权限
	@SuppressWarnings("unchecked")
	private static String verifySql(String sql,String ds) throws Exception{
		String sqlType = "";
		if(StringUtils.isBlank(sql)){
			throw new Exception("sql为空");
		}
		String[] sql_arr = sql.trim().toUpperCase().split(" ");
		if(sql_arr.length == 0){
			throw new Exception("sql格式不正确");
		}
		sqlType =  sql_arr[0];
		
		//判断权限
		String[] db_authorize = new String[]{};
		try{
			Map<String,Map<String,Object>> db = (Map<String,Map<String,Object>>)SessionFactory.getSession().getAttribute(OMMConstantEnum.OMM_MENU_DB_KEY);
			Map<String,Object> db_info = db.get(ds);
			db_authorize = (String[])db_info.get(OMMConstantEnum.OMM_MENU_DB_SQLTYPE);
		}catch(Exception e){
		}
		//db_authorize = new String[]{"SELECT","INSERT","UPDATE","DELETE"};
		if(ArrayUtils.indexOf(db_authorize, sqlType) < 0){
			throw new Exception("sql无权限执行");
		}
		
		if(ArrayUtils.indexOf(new String[]{"UPDATE","DELETE"}, sqlType) >-1){
			if(sql.toUpperCase().indexOf("WHERE ") < 0){
				throw new Exception("请添加where条件");
			}
		}
		return sqlType;
	}
	
}
