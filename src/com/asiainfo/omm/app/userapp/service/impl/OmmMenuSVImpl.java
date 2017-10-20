package com.asiainfo.omm.app.userapp.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuDAO;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.utils.DateUtil;
import com.asiainfo.omm.utils.StringUtils;

public class OmmMenuSVImpl implements IOmmMenuSV {

	public void addMenu(IBOOmmMenuValue menu) throws Exception{
		IOmmMenuDAO menuDAO = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		menuDAO.addMenu(menu);
	}
	
	public void updateMenu(IBOOmmMenuValue[] menus) throws Exception{
		IOmmMenuDAO menuDAO = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		menuDAO.updateMenu(menus);
	}
	
	public IBOOmmMenuValue[] getMenuByMenuId1(String menuId) throws Exception {
		IOmmMenuDAO menuDAO = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		return menuDAO.getMenuByMenuId1(menuId);
	}

	public int getMenuCount() throws Exception{
		IOmmMenuDAO menuDAO = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		return menuDAO.getMenuCount();
	}
	
	public void deleteMenu(String menuId) throws Exception{
		IOmmMenuDAO menuDAO = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		IBOOmmMenuValue[] menus = menuDAO.getMenuByMenuId1(menuId);
		menuDAO.deleteMenu(menus);
		
		//删除角色关联应用信息
		IOmmMenuRelatRoleDAO menuRelatRoleDAO = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		IBOOmmMenuRelatRoleValue[]  menuRelatRoles = menuRelatRoleDAO.getMenuRelatRoleByMenuId1(menuId);
		menuRelatRoleDAO.deleteMenuRelatRole(menuRelatRoles);
	}
	
	public IBOOmmMenuValue[] getMenuByMenuRelatRole(IBOOmmMenuRelatRoleValue[] menuRelatRoles) throws Exception {
		IOmmMenuDAO dao = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		String tmp = "";
		List<String> menuIds = new ArrayList<String>();
		for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
			tmp = menuRelatRole.getMenuId();
			if(!menuIds.contains(tmp)){
				menuIds.add(tmp);
			}
		}
		return dao.getMenuByMenuId(menuIds);
	}
	
	public IBOOmmMenuValue[] getMenuByAppName(String appName) throws Exception{
		if(StringUtils.isBlank(appName)){
			appName = "";
		}
		IOmmMenuDAO dao = (IOmmMenuDAO)ServiceFactory.getService(IOmmMenuDAO.class);
		return dao.getMenuByAppName(appName.trim());
	}
}
