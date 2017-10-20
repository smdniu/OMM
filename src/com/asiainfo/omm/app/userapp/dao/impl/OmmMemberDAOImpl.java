package com.asiainfo.omm.app.userapp.dao.impl;

import java.util.HashMap;
import java.util.Map;

import com.asiainfo.omm.app.userapp.bo.BOOmmMemberBean;
import com.asiainfo.omm.app.userapp.bo.BOOmmMemberEngine;
import com.asiainfo.omm.app.userapp.dao.interfaces.IOmmMemberDAO;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.DateUtil;

/**
 * 用户信息查询
 * 
 * @author oswin
 *
 */
public class OmmMemberDAOImpl implements IOmmMemberDAO {
	
	public IBOOmmMemberValue[] getMemberBymemberIds(String[] memberIds) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberValue.S_State).append( " = :state ")
				.append(" AND ").append(IBOOmmMemberValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMemberValue.S_CreateTime ).append("<  sysdate")
				.append(" AND ").append(IBOOmmMemberValue.S_Id).append(" in ('");
		int count = 0;
		for(String memberId: memberIds){
			condition.append(memberId);
			if(count < memberIds.length - 1){
				condition.append("', '");
			}
			count++;
		}
		condition.append("')").append(" order by ").append(IBOOmmMemberValue.S_Id).append(" desc");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMemberEngine.getBeans(condition.toString(), params);
	}
	
	public int getMemberCountByMemberCode(String memberCode)throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		condition.append(" AND ").append(IBOOmmMemberValue.S_State).append(" in ('")
		.append(OMMConstantEnum.STATE.U.getState()).append("','")
		.append(OMMConstantEnum.STATE.E.getState()).append("','")
		.append(OMMConstantEnum.STATE.P.getState()).append("')")
		.append(" AND (").append(IBOOmmMemberValue.S_Account).append( " like '%").append(memberCode).append("%' OR ")
		 .append(IBOOmmMemberValue.S_Name).append( " like '%").append(memberCode).append("%')");
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmMemberEngine.getBeansCount(condition.toString(), params);
	}
	
	public IBOOmmMemberValue[] getMemberById(String id) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberValue.S_Id).append("= :id ")
				 .append(" AND ").append(IBOOmmMemberValue.S_State).append("= :state ")
				 .append(" AND ").append(IBOOmmMemberValue.S_ExpiryTime ).append(">  sysdate")
				 .append(" AND ").append(IBOOmmMemberValue.S_CreateTime ).append("<  sysdate");
		params.put("id", id);
		params.put("state", OMMConstantEnum.STATE.U.getState());
		return BOOmmMemberEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMemberValue[] getMemberById1(String id) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberValue.S_Id).append("= :id ");
		params.put("id", id);
		return BOOmmMemberEngine.getBeans(condition.toString(), params);
	}
	
	public IBOOmmMemberValue[] getAllMember(int startIndex, int endIndex) throws Exception {
		StringBuilder condition = new StringBuilder(" SELECT * FROM  ( SELECT A.*, ROWNUM RN  FROM (SELECT * FROM OMM_MEMBER "); 
		condition.append(" WHERE ").append(IBOOmmMemberValue.S_State).append(" in ('")
				 .append(OMMConstantEnum.STATE.U.getState()).append("','")
				 .append(OMMConstantEnum.STATE.E.getState()).append("','")
				 .append(OMMConstantEnum.STATE.P.getState()).append("')")
				 .append(") A  WHERE ROWNUM < ").append(endIndex).append(")  WHERE RN >=").append(startIndex);
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmMemberEngine.getBeansFromSql(condition.toString(), params);
	}

	public IBOOmmMemberValue[] getMemberByAccount(String account) throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND ").append(IBOOmmMemberValue.S_State).append("= :state ").append(" AND (")
				 .append(IBOOmmMemberValue.S_Account).append("= :account ").append(" OR ")
				 .append(IBOOmmMemberValue.S_Name).append("= :account )")
				 .append(" AND ").append(IBOOmmMemberValue.S_ExpiryTime ).append(">  sysdate")
				.append(" AND ").append(IBOOmmMemberValue.S_CreateTime ).append("<  sysdate");
		params.put("state", OMMConstantEnum.STATE.U.getState());
		params.put("account", account);
		return BOOmmMemberEngine.getBeans(condition.toString(), params);
	}

	public long addMember(IBOOmmMemberValue member) throws Exception {
		member.setId(BOOmmMemberEngine.getNewId().longValue());
		member.setCreateTime(DateUtil.getNowTimestamp());
		member.setUpdateTime(DateUtil.getNowTimestamp());
		if(member.getExpiryTime() == null){
			member.setExpiryTime(DateUtil.getTimestamp("2099-12-31 23:59:59"));//yyyy-MM-dd HH:mm:ss
		}
		member.setState(OMMConstantEnum.STATE.U.getState());
		BOOmmMemberEngine.save(member);
		return member.getId();
	}

	public long updateMember(IBOOmmMemberValue member) throws Exception {
		member.setUpdateTime(DateUtil.getNowTimestamp());
		BOOmmMemberEngine.save(member);
		return member.getId();
	}

	public int getMemberCount() throws Exception {
		StringBuilder condition = new StringBuilder(" 1=1 ");
		condition.append(" AND ").append(IBOOmmMemberValue.S_State).append(" in ('")
		.append(OMMConstantEnum.STATE.U.getState()).append("','")
		.append(OMMConstantEnum.STATE.E.getState()).append("','")
		.append(OMMConstantEnum.STATE.P.getState()).append("')");
		Map<String, Object> params =new HashMap<String, Object>();
		return BOOmmMemberEngine.getBeansCount(condition.toString(), params);
	}

	public void delelteMember(IBOOmmMemberValue[] members) throws Exception {
		if(members != null && members.length != 0){
			for(IBOOmmMemberValue member: members){
				member.delete();
			}
			BOOmmMemberEngine.save(members);
		}
	}
	
	public IBOOmmMemberValue[] getMemberByAccountCode(String accountCode) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		condition.append(" AND (").append(IBOOmmMemberValue.S_Account).append( " like '%").append(accountCode)
				 .append("%' OR ").append(IBOOmmMemberValue.S_Name).append( " like '%").append(accountCode).append("%')");
		return BOOmmMemberEngine.getBeans(condition.toString(), params);
	}
}
