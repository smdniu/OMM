package com.asiainfo.omm.app.userapp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.omm.app.userapp.bo.BOOmmRoleInfoEngine;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmRoleInfoDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.constant.OMMConstantEnum;

public class OmmRoleInfoDAOImpl implements IOmmRoleInfoDAO {
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId(String roleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_Id).append("= :id ")
				 .append(" AND ").append(IBOOmmRoleInfoValue.S_State).append("= :state ")
				 .append(" AND ").append(IBOOmmRoleInfoValue.S_ExpiryTime ).append(">  sysdate")
				 .append(" AND ").append(IBOOmmRoleInfoValue.S_CreateTime ).append("<  sysdate");
		params.put("id", roleId);
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId1(String roleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_Id).append("= :id ");
		params.put("id", roleId);
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}
	
	public void deleteRoleInfoByRoleId(IBOOmmRoleInfoValue[] roles) throws Exception{
		if(roles != null && roles.length != 0){
			for(IBOOmmRoleInfoValue role: roles){
				role.delete();
			}
			BOOmmRoleInfoEngine.save(roles);
		}
	}
	
	public IBOOmmRoleInfoValue[] getAllRoleInfo(int startIndex, int endIndex) throws Exception{
		StringBuilder condition = new StringBuilder(" SELECT * FROM  ( SELECT A.*, ROWNUM RN  FROM (SELECT * FROM OMM_ROLE_INFO "); 
		condition.append(" WHERE ").append(IBOOmmRoleInfoValue.S_State).append(" in ('")
				 .append(OMMConstantEnum.STATE.U.getState()).append("','")
				 .append(OMMConstantEnum.STATE.E.getState()).append("','")
				 .append(OMMConstantEnum.STATE.P.getState()).append("')")
				 .append(") A  WHERE ROWNUM < ").append(endIndex).append(")  WHERE RN >=").append(startIndex);
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmRoleInfoEngine.getBeansFromSql(condition.toString(), params);
	
	}

	public IBOOmmRoleInfoValue[] getAllRoleInfo() throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_State).append( " = :state ")
				 .append(" AND ").append(IBOOmmRoleInfoValue.S_ExpiryTime ).append("> sysdate")
				 .append(" AND ").append(IBOOmmRoleInfoValue.S_CreateTime ).append("< sysdate");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleIds(String[] roleIds)throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_State).append( " = :state ")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_Id).append(" in ('");
		int count = 0;
		for(String roleId: roleIds){
			condition.append(roleId);
			if(count < roleIds.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}

	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCode(String roleCode) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_State).append( " = :state ")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_RoleCode).append(" =:roleCode");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		params.put("roleCode", roleCode);
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCodes(String[] roleCodes) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_State).append( " = :state ")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmRoleInfoValue.S_RoleCode).append(" in ('");
		int count = 0;
		for(String roleCode: roleCodes){
			condition.append(roleCode);
			if(count < roleCodes.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}

	public IBOOmmRoleInfoValue[] getRoleInfoByRoleInfoCode(String roleInfoCode) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND (").append(IBOOmmRoleInfoValue.S_RoleCode).append( " like '%").append(roleInfoCode)
				 .append("%' OR ").append(IBOOmmRoleInfoValue.S_Name).append( " like '%").append(roleInfoCode).append("%')");
		return BOOmmRoleInfoEngine.getBeans(condition.toString(), params);
	}
	
	public int getRoleInfoCount()throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		condition.append(" AND ").append(IBOOmmRoleInfoValue.S_State).append(" in ('")
		.append(OMMConstantEnum.STATE.U.getState()).append("','")
		.append(OMMConstantEnum.STATE.E.getState()).append("','")
		.append(OMMConstantEnum.STATE.P.getState()).append("')");
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmRoleInfoEngine.getBeansCount(condition.toString(), params);
	}
}
