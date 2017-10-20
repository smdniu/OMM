package com.asiainfo.omm.app.userapp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.omm.app.userapp.bo.BOOmmMemberRelatRoleEngine;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberRelatRoleDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.DateUtil;

public class OmmMemberRelatRoleDAOImpl implements IOmmMemberRelatRoleDAO{

	public void updateMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception{
		if(memberRelatRoles != null && memberRelatRoles.length > 0){
			for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
				memberRelatRole.setUpdateTime(DateUtil.getNowTimestamp());
			}
			BOOmmMemberRelatRoleEngine.save(memberRelatRoles);
		}
	}
	
	public void addMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception{
		if(memberRelatRoles != null && memberRelatRoles.length > 0){
			for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
				memberRelatRole.setId(BOOmmMemberRelatRoleEngine.getNewId().longValue());
				memberRelatRole.setState("U");
			}
			BOOmmMemberRelatRoleEngine.save(memberRelatRoles);
		}
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleIds(String[] roleIds) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_State).append("= :state ")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_Roleid).append(" in ('");
		int count = 0;
		for(String roleId: roleIds){
			condition.append(roleId);
			if(count < roleIds.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')").append(" AND ").append(IBOOmmMemberRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_CreateTime ).append("<  sysdate")
				.append(" order by ").append(IBOOmmMemberRelatRoleValue.S_Id);
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId1(String RoleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_Roleid).append("= :RoleId ");
		params.put("RoleId", RoleId);
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId(String RoleId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_Roleid).append("= :RoleId ")
				 .append(" AND ").append(IBOOmmMemberRelatRoleValue.S_State).append("= :state ")
				 .append(" AND ").append(IBOOmmMemberRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				 .append(" AND ").append(IBOOmmMemberRelatRoleValue.S_CreateTime ).append("<  sysdate");
		params.put("RoleId", RoleId);
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberId1(String memberId) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_MemberId).append("= :memberId ");
		params.put("memberId", memberId);
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public void deleteMemberRelatRoleByMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception {
		if(memberRelatRoles != null && memberRelatRoles.length != 0){
			for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
				memberRelatRole.delete();
			}
			BOOmmMemberRelatRoleEngine.save(memberRelatRoles);
		}
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberId(String memberId) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_State).append("= :state ")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_MemberId).append("= :memberId ")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_CreateTime ).append("<  sysdate");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		params.put("memberId", memberId);
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberIds(String[] memberIds) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_State).append("= :state ")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_MemberId).append(" in ('");
		int count = 0;
		for(String memberId: memberIds){
			condition.append(memberId);
			if(count < memberIds.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')").append(" AND ").append(IBOOmmMemberRelatRoleValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMemberRelatRoleValue.S_CreateTime ).append("<  sysdate");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMemberRelatRoleEngine.getBeans(condition.toString(), params);
	}
}
