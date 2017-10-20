package com.asiainfo.omm.app.userapp.dao.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;

/**
 * OMM应用关联角色操作
 * 
 * @author oswin
 *
 */
public interface IOmmMenuRelatRoleDAO {

	/**
	 * 根据应用id获取角色关联应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String menuId) throws Exception ;
	
	/**
	 * 根据角色id查询用户关联应用信息, 不关心状态
	 * @param roleId
	 * @return
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId1(String roleId) throws Exception ;
	
	/**
	 * 根据应用id查询用户关联应用信息, 不关心状态
	 * @param menuId
	 * @return
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId1(String menuId) throws Exception ;
	
	/**
	 * 删除角色关联应用信息
	 * @param 角色关联应用信息
	 * @return
	 */
	public void deleteMenuRelatRole(IBOOmmMenuRelatRoleValue[] menuRelatRoles) throws Exception ;
	
	/**
	 * 根据角色id查询用户关联应用信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String[] roleIds) throws Exception ;
	
	/**
	 * 根据应用id查询用户关联应用信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String[] menuIds) throws Exception ;
	
	/**
	 * 根据角色id获取角色关联应用信息
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String roleId) throws Exception ;
}
