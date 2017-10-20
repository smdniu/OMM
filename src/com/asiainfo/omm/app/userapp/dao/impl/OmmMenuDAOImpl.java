package com.asiainfo.omm.app.userapp.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asiainfo.omm.app.userapp.bo.BOOmmMenuEngine;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.DateUtil;

public class OmmMenuDAOImpl implements IOmmMenuDAO {

	public void addMenu(IBOOmmMenuValue menu) throws Exception {
		if(menu != null && menu.getId() > 0){
			menu.setCreateTime(DateUtil.getNowTimestamp());
			menu.setState(OMMConstantEnum.STATE.U.getState());
			menu.setUpdateTime(DateUtil.getNowTimestamp());
			menu.setExpiryTime(DateUtil.getTimestamp("2099-12-31 23:59:59"));
			BOOmmMenuEngine.save(menu);
		}
	}
	
	public void updateMenu(IBOOmmMenuValue[] menus) throws Exception{
		if(menus != null && menus.length != 0){
			for(IBOOmmMenuValue menu:menus){
				menu.setUpdateTime(DateUtil.getNowTimestamp());
			}
			BOOmmMenuEngine.save(menus);
		}
	}
	
	public int getMenuCount() throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		condition.append(" AND ").append(IBOOmmMenuValue.S_State).append(" in ('")
				 .append(OMMConstantEnum.STATE.U.getState()).append("','")
				 .append(OMMConstantEnum.STATE.E.getState()).append("','")
				 .append(OMMConstantEnum.STATE.P.getState()).append("')");
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmMenuEngine.getBeansCount(condition.toString(), params);
	}
	
	public void deleteMenu(IBOOmmMenuValue[] menus) throws Exception {
		if(menus != null && menus.length != 0){
			for(IBOOmmMenuValue menu: menus){
				menu.delete();
			}
			BOOmmMenuEngine.save(menus);
		}
	}
	
	public IBOOmmMenuValue[] getMenuByMenuId1(String menuId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuValue.S_Id).append(" =:menuId");
		params.put("menuId", menuId);
		return BOOmmMenuEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMenuValue[] getMenuByMenuId(List<String> menuIds) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMenuValue.S_State).append( " = :state")
		.append(" AND ").append(IBOOmmMenuValue.S_ExpiryTime ).append(">  sysdate")
		.append(" AND ").append(IBOOmmMenuValue.S_CreateTime ).append("<  sysdate")
		.append(" AND ").append(IBOOmmMenuValue.S_Id).append(" in ('");
		int count = 0;
		for(String menuId: menuIds){
			condition.append(menuId);
			if(count < menuIds.size() - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')").append(" order by ").append(IBOOmmMenuValue.S_MenuType);
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMenuEngine.getBeans(condition.toString(), params);
	}

	public IBOOmmMenuValue[] getMenuByAppName(String appName) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND (").append(IBOOmmMenuValue.S_MenuCode).append( " like '%").append(appName)
				 .append("%' OR ").append(IBOOmmMenuValue.S_Name).append( " like '%").append(appName).append("%')");
		return BOOmmMenuEngine.getBeans(condition.toString(), params);
	}

}
