package com.asiainfo.omm.app.userapp.service.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;

/**
 * 人员关联角色查询操作
 * 
 * @author oswin
 *
 */
public interface IOmmMemberRelatRoleSV {

	/**
	 * 保存用户关联角色信息
	 * 
	 * @param memberRelatRole
	 * @throws Exception
	 */
	public void addMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRole) throws Exception ;
	
	/**
	 * 修改用户关联角色信息
	 * 
	 * @param memberRelatRole
	 * @throws Exception
	 */
	public void updateMemberRelatRoles(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception;
	
	/**
	 * 删除用户关联角色信息
	 * 
	 * @param memberId 用户信息id
	 * @throws Exception
	 */
	public void deleteMemberRelatRoleByMemberId(String memberId) throws Exception ;
	
	/**
	 * 根据用户id查询用户关联角色信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleById(String memberId) throws Exception ;
	
	/**
	 * 根据角色id查询用户关联用户信息
	 * 
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleId(String RoleId) throws Exception ;
	
	/**
	 * 新增用户关联角色信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] addMemberRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception ;
	
	/**
	 * 根据用户批量查询查询用户关联角色信息
	 * @param members
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByMemberIds(IBOOmmMemberValue[] members) throws Exception;
	
	/**
	 * 根据角色批量查询查询用户关联角色信息
	 * @param members
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoles(IBOOmmRoleInfoValue[] roles) throws Exception;
	
	/**
	 * 根据批量角色id查询用户关联用户信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoleByRoleIds(String[] roleIds) throws Exception ;
}
