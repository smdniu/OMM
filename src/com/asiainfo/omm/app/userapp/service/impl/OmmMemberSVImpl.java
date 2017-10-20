package com.asiainfo.omm.app.userapp.service.impl;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberDAO;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMemberSV;
import com.asiainfo.omm.constant.OMMExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.utils.OMMEncrypt;
import com.asiainfo.omm.utils.StringUtils;

public class OmmMemberSVImpl implements IOmmMemberSV {
	
	public IBOOmmMemberValue[] getMenuByMenuRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception{
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		int length = memberRelatRoles.length;
		String[] memberIds = new String[length];
		for(int i = 0; i < length; i++){
			memberIds[i] = memberRelatRoles[i].getRoleid();
		}
		return dao.getMemberBymemberIds(memberIds);
	}

	
	public void delelteMember(String id) throws Exception{
		IOmmMemberDAO memberDAO = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		IBOOmmMemberValue[] members = memberDAO.getMemberById1(id);
		memberDAO.delelteMember(members);
		
		//删除应用关联角色信息
		IOmmMemberRelatRoleDAO memberRelatRoleDAO = (IOmmMemberRelatRoleDAO)ServiceFactory.getService(IOmmMemberRelatRoleDAO.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleDAO.getMemberRelatRoleByMemberId1(id);
		memberRelatRoleDAO.deleteMemberRelatRoleByMemberRelatRoles(memberRelatRoles);
	}
	
	public IBOOmmMemberValue[] getMemberById(String id) throws Exception{
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getMemberById(id);
	}
	
	public IBOOmmMemberValue[] getMemberById1(String id) throws Exception{
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getMemberById1(id);
	}
	
	public IBOOmmMemberValue[] getAllMember(int startIndex, int endIndex) throws Exception {
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getAllMember(startIndex, endIndex);
	}
	
	public IBOOmmMemberValue getMemberByAccount(String account) throws Exception {
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		IBOOmmMemberValue[] members = dao.getMemberByAccount(account);
		if(members == null || members.length <= 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_MEMBER);
		}else if(members.length >1){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_FOUND_MULTI_MEMBER);
		}
		return members[0];
	}

	public long addMember(IBOOmmMemberValue member) throws Exception {
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		String encrypt = member.getEncryptType();
		if(encrypt == null || "".equals(encrypt.trim()) || "NULL".equalsIgnoreCase(encrypt.trim())){
			encrypt = "0";
		}
		member.setPassword(OMMEncrypt.encryptStr(encrypt, member.getPassword()));
		member.setEncryptType(encrypt);
		return dao.addMember(member);
	}

	public long updateMember(IBOOmmMemberValue member)throws Exception{
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.updateMember(member);
	}
	public int getMemberCount() throws Exception {
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getMemberCount();
	}
	
	public IBOOmmMemberValue[] getMemberByAccountCode(String accountCode) throws Exception{
		if(StringUtils.isBlank(accountCode)){
			accountCode = "";
		}
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getMemberByAccountCode(accountCode.trim());
	}
	
	public int getMemberCountByMemberCode(String memberCode)throws Exception{
		IOmmMemberDAO dao = (IOmmMemberDAO)ServiceFactory.getService(IOmmMemberDAO.class);
		return dao.getMemberCountByMemberCode(memberCode);
	}
}
