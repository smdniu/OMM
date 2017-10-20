package com.asiainfo.omm.app.userapp.service.impl;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberRelatRoleDAO;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMenuRelatRoleDAO;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmRoleInfoDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmRoleInfoSV;
import com.asiainfo.omm.utils.StringUtils;

/**
 * OMM角色操作
 * 
 * @author oswin
 *
 */
public class OmmRoleInfoSVImpl implements IOmmRoleInfoSV {

	public IBOOmmRoleInfoValue[] getRoleInfoByRoleIds(String[] roleIds) throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleIds(roleIds);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByMenuRelatRole(IBOOmmMenuRelatRoleValue[] menuRelatRoles) throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		int length = menuRelatRoles.length;
		String[] roleIds = new String[length];
		for(int i = 0; i < length; i++){
			roleIds[i] = menuRelatRoles[i].getRoleId();
		}
		return dao.getRoleInfoByRoleIds(roleIds);
	}

	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId(String roleId) throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleId(roleId);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId1(String roleId) throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleId1(roleId);
	}
	
	public void deleteRoleInfoByRoleId(String roleId) throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		IBOOmmRoleInfoValue[] roles = dao.getRoleInfoByRoleId1(roleId);
		dao.deleteRoleInfoByRoleId(roles);
		
		//查询角色关联用户
		IOmmMemberRelatRoleDAO memberRelatRoleDAO = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleDAO.getMemberRelatRoleByRoleId1(roleId);
		memberRelatRoleDAO.deleteMemberRelatRoleByMemberRelatRoles(memberRelatRoles);
		
		//查询角色关联应用
		IOmmMenuRelatRoleDAO menuRelatRoleDAO = (IOmmMenuRelatRoleDAO)ServiceFactory.getService(IOmmMenuRelatRoleDAO.class);
		IBOOmmMenuRelatRoleValue[]  menuRelatRoles = menuRelatRoleDAO.getMenuRelatRoleByRoleId1(roleId);
		menuRelatRoleDAO.deleteMenuRelatRole(menuRelatRoles);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByMemberRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception {
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		int length = memberRelatRoles.length;
		String[] roleIds = new String[length];
		for(int i = 0; i < length; i++){
			roleIds[i] = memberRelatRoles[i].getRoleid();
		}
		return dao.getRoleInfoByRoleIds(roleIds);
	}

	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCodes(String[] roleCodes)throws Exception {
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleCodes(roleCodes);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCode(String roleCode)throws Exception {
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleCode(roleCode);
	}
	
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleInfoCode(String roleInfoCode) throws Exception{
		if(StringUtils.isBlank(roleInfoCode)){
			roleInfoCode = "";
		}
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoByRoleInfoCode(roleInfoCode.trim());
	}
	
	public int getRoleInfoCount()throws Exception{
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getRoleInfoCount();
	}

	public IBOOmmRoleInfoValue[] getAllRoleInfo(int startIndex, int endIndex) throws Exception {
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getAllRoleInfo(startIndex, endIndex);
	}
	
	public IBOOmmRoleInfoValue[] getAllRoleInfo() throws Exception {
		IOmmRoleInfoDAO dao = (IOmmRoleInfoDAO)ServiceFactory.getService(IOmmRoleInfoDAO.class);
		return dao.getAllRoleInfo();
	}
}
