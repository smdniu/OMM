package com.asiainfo.omm.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.db.DBTool;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.SessionFactory;

/**
 * 数据库应用控制器
 * 
 * @author oswin
 */
@Controller
@RequestMapping("/db")
public class DBController {
	
	private static final Logger logger = Logger.getLogger(DBController.class);

	/**
	 * 主页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 跳转页面
	 * @throws Exception
	 */
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(Model model){
		logger.debug("OMM平台本次请求数据库应用主页 ");
		try{
			HttpSession session = SessionFactory.getSession();
			Object db = session.getAttribute(OMMConstantEnum.OMM_MENU_DB_KEY);
			if(db == null){
				throw new Exception("获取用户数据权限为空");
			}
			model.addAttribute("ds", session.getAttribute(OMMConstantEnum.OMM_MENU_DB_KEY));
			return "apply/db";
		}catch(Exception e){
			return "redirect:/unauthorized"; 
		}
		
	}
	
	/**
	 * 执行sql
	 * @param sql
	 * @param ds
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	@RequestMapping(value="/execute",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> execute(String sql,String ds,int startIndex,int endIndex,HttpServletRequest request){
		String[] sqls = sql.split(";");
		return DBTool.executeAll(ds, sqls, startIndex, endIndex);
	}
	
	/**
	 * 获取用户所有表
	 * @param ds
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/getUserAllTable",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getTable(String ds,HttpServletRequest request){
		return DBTool.getUserAllTables(ds);
	}
	
	/**
	 * 新增db配置信息
	 * 
	 * @return
	 */
	@RequestMapping(value="/addDbConfig",method = RequestMethod.POST)
	public @ResponseBody JSONObject addDbConfig(){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		return json;
	}
}
