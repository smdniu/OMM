package com.asiainfo.omm.app.userapp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.omm.app.userapp.bo.BOOmmMenuRelatRoleEngine;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.constant.OMMConstantEnum;

public class OmmMenuRelatRoleDAOImpl implements IOmmMenuRelatRoleDAO {

	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId1(String menuId) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_MenuId).append(" = :menuId");
		params.put("menuId", menuId);
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}

	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String menuId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_State).append( " = :state")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_MenuId).append(" = :menuId");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		params.put("menuId", menuId);
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String[] menuIds) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_State).append( " = :state")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_MenuId).append(" in ('");
		int count = 0;
		for(String menuId: menuIds){
			condition.append(menuId);
			if(count < menuIds.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public void deleteMenuRelatRole(IBOOmmMenuRelatRoleValue[] menuRelatRoles) throws Exception{
		if(menuRelatRoles != null && menuRelatRoles.length != 0){
			for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
				menuRelatRole.delete();
			}
			BOOmmMenuRelatRoleEngine.save(menuRelatRoles);
		}
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId1(String roleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_RoleId).append( " = :roleId");
		params.put("roleId", roleId);
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String roleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_RoleId).append( " = :roleId")
				 .append(" AND ").append(IBOOmmMenuRelatRoleValue.S_State).append( " = :state")
				 .append(" AND ").append(IBOOmmMenuRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				 .append(" AND ").append(IBOOmmMenuRelatRoleValue.S_CreateTime ).append("<  sysdate");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		params.put("roleId", roleId);
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String[] roleIds) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_State).append( " = :state")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmMenuRelatRoleValue.S_RoleId).append(" in ('");
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
		return BOOmmMenuRelatRoleEngine.getBeans(condition.toString(), params);
	}

}
