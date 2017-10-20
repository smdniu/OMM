package com.asiainfo.omm.app.userapp.dao.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;

/**
 * 人员关联角色查询操作
 * 
 * @author oswin
 *
 */
public interface IOmmMemberRelatRoleDAO {

	/**
	 * 保存用户关联角色信息
	 * 
	 * @param memberRelatRole
	 * @throws Exception
	 */
	public void addMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception;
	
	/**
	 * 修改用户关联角色信息
	 * 
	 * @param memberRelatRole
	 * @throws Exception
	 */
	public void updateMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception;
	
	/**
	 * 根据角色id查询用户关联用户信息,排除状态信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId1(String RoleId) throws Exception ;
	
	/**
	 * 根据角色id查询用户关联用户信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId(String RoleId) throws Exception ;
	
	/**
	 * 根据批量角色id查询用户关联用户信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleIds(String[] roleIds) throws Exception ;
	
	/**
	 * 删除用户关联角色信息
	 * 
	 * @param memberId 用户信息id
	 * @throws Exception
	 */
	public void deleteMemberRelatRoleByMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception ;
	
	/**
	 * 根据用户id获取关联角色信息, 排除状态, 和时间限制
	 * 
	 * @param memberId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberId1(String memberId) throws Exception ;
	
	/**
	 * 根据用户id查询用户关联角色信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberId(String memberId) throws Exception ;
	
	/**
	 * 根据用户id批量查询查询用户关联角色信息
	 * @param memberIds
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberIds(String[] memberIds) throws Exception;
}
