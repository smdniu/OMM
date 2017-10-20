package com.asiainfo.omm.app.userapp.service.impl;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberRelatRoleDAO;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuRelatRoleSV;

public class OmmMenuRelatRoleSVImpl implements IOmmMenuRelatRoleSV {

	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMembers(IBOOmmMemberValue[] members) throws Exception {
		IOmmMemberRelatRoleDAO memberDAO = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		int length = members.length;
		String[] memberIds = new String[length];
		for(int i = 0; i < length; i++){
			memberIds[i] = members[i].getId() + "";
		}
		memberDAO.getMemberRelatRoleByMemberIds(memberIds);
		IBOOmmMemberRelatRoleValue[]  memberRelatRoles = memberDAO.getMemberRelatRoleByMemberIds(memberIds);
		if(memberRelatRoles != null && memberRelatRoles.length != 0){
			IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
			return menuRelatRoleSV.getMenuRelatRoleByMemberRelatRole(memberRelatRoles);
		}
		return null;
	}

	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleInfos(IBOOmmRoleInfoValue[] roleInfos) throws Exception{
		IOmmMenuRelatRoleDAO dao = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		int length = roleInfos.length;
		String[] roleIds = new String[length];
		for(int i = 0; i < length; i++){
			roleIds[i] = roleInfos[i].getId() + "";
		}
		return dao.getMenuRelatRoleByRoleId(roleIds);
	}

	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenus(IBOOmmMenuValue[] menus) throws Exception{
		IOmmMenuRelatRoleDAO dao = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		int length = menus.length;
		String[] menuIds = new String[length];
		for(int i = 0; i < length; i++){
			menuIds[i] = menus[i].getId() + "";
		}
		return dao.getMenuRelatRoleByMenuId(menuIds);
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMemberId(String memberId) throws Exception{
		IOmmMemberRelatRoleDAO memberDAO = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		IBOOmmMemberRelatRoleValue[]  memberRelatRoles = memberDAO.getMemberRelatRoleByMemberId(memberId);
		if(memberRelatRoles != null && memberRelatRoles.length != 0){
			IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
			return menuRelatRoleSV.getMenuRelatRoleByMemberRelatRole(memberRelatRoles);
		}
		return null;
	}
	
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMemberRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception {
		IOmmMenuRelatRoleDAO dao = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		int length = memberRelatRoles.length;
		String[] roleIds = new String[length];
		for(int i = 0; i < length; i++){
			roleIds[i] = memberRelatRoles[i].getRoleid();
		}
		return dao.getMenuRelatRoleByRoleId(roleIds);
	}

	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String roleId)throws Exception {
		IOmmMenuRelatRoleDAO dao = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		return dao.getMenuRelatRoleByRoleId(roleId);
	}

	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String menuId) throws Exception {
		IOmmMenuRelatRoleDAO dao = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		return dao.getMenuRelatRoleByMenuId(menuId);
	}

}
