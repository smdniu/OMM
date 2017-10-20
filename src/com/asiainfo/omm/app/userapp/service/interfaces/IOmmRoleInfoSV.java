package com.asiainfo.omm.app.userapp.service.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;

/**
 * OMM角色操作
 * 
 * @author oswin
 *
 */
public interface IOmmRoleInfoSV {

	/**
	 * 根据用户关联角色信息查询角色信息<br>
	 * Roleid
	 * @param account
	 * @return
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByMemberRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception ;
	
	/**
	 * 根据应用信息查询角色关联应用信息
	 * 
	 * @param menuRelatRoles
	 * @return
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByMenuRelatRole(IBOOmmMenuRelatRoleValue[] menuRelatRoles) throws Exception ;

	/**
	 * 根据多个角色code获取角色信息, 状态为U
	 * 
	 * @param roleCode
	 * @return
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCodes(String[] roleCode) throws Exception ;
	
	
	/**
	 * 根据角色code获取角色信息, 状态为U
	 * 
	 * @param roleCode
	 * @return
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleCode(String roleCode) throws Exception;
	
	/**
	 * 根据角色id删除角色
	 * 
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRoleInfoByRoleId(String roleId) throws Exception;
	
	/**
	 * 根据角色id获取角色
	 * 
	 * @param roleId
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId(String roleId) throws Exception;
	
	/**
	 * 根据角色id查询角色信息,不关心状态
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleId1(String roleId) throws Exception;
	
	/**
	 * 根据多个角色id获取角色
	 * 
	 * @param roleId
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleIds(String[] roleIds) throws Exception;
	
	/**
	 * 根据roleCode获取角色信息
	 * 
	 * @param roleCode 可以是角色名名, 也可以是角色code
	 * @return
	 * @throws Exception
	 */
	public IBOOmmRoleInfoValue[] getRoleInfoByRoleInfoCode(String roleInfoCode) throws Exception ;
	
	/**
	 * 获取角色总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRoleInfoCount()throws Exception;
	
	/**
	 * 查询所有角色, 分页查询含头不含尾
	 * 
	 * @return
	 */
	public IBOOmmRoleInfoValue[] getAllRoleInfo(int startIndex, int endIndex) throws Exception;
	
	/**
	 * 查询所有角色
	 * 
	 * @return
	 */
	public IBOOmmRoleInfoValue[] getAllRoleInfo() throws Exception;
}
