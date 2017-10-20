package com.asiainfo.omm.app.userapp.service.impl;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMemberRelatRoleSV;

public class OmmMemberRelatRoleSVImpl implements IOmmMemberRelatRoleSV{

	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleIds(String[] roleIds) throws Exception {
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		return dao.getMemberRelatRoleByRoleIds(roleIds);
	}
	
	public void updateMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception{
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		dao.updateMemberRelatRoles(memberRelatRoles);
	}

	
	public void addMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception{
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		dao.addMemberRelatRoles(memberRelatRoles);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoles(IBOOmmRoleInfoValue[] roles) throws Exception{
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		int length = roles.length;
		String[] roleIds = new String[length];
		for(int i = 0; i < length; i++){
			roleIds[i] = String.valueOf(roles[i].getId());
		}
		return dao.getMemberRelatRoleByRoleIds(roleIds);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleById(String memberId) throws Exception {
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		return dao.getMemberRelatRoleByMemberId(memberId);
	}

	public IBOOmmMemberRelatRoleValue[] addMemberRelatRole(
			IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception {
		return null;
	}

	public void deleteMemberRelatRoleByMemberId(String memberId)throws Exception {
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = dao.getMemberRelatRoleByMemberId1(memberId);
		dao.deleteMemberRelatRoleByMemberRelatRoles(memberRelatRoles);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberIds(IBOOmmMemberValue[] members) throws Exception{
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		int length = members.length;
		String[] memberIds = new String[length];
		for(int i = 0; i < length; i++){
			memberIds[i] = String.valueOf(members[i].getId());
		}
		return dao.getMemberRelatRoleByMemberIds(memberIds);
	}

	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId(String RoleId)throws Exception {
		IOmmMemberRelatRoleDAO dao = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		return dao.getMemberRelatRoleByRoleId(RoleId);
	}
}
