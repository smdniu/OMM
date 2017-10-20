package com.asiainfo.omm.app.userapp.service.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;

/**
 * OMM应用关联菜单操作
 * 
 * @author oswin
 *
 */
public interface IOmmMenuRelatRoleSV {

	/**
	 * 根据角色信息查询用户关联应用信息
	 * @param memberId
	 * @return
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMemberRelatRole(IBOOmmMemberRelatRoleValue[] memberRelatRoles) throws Exception ;
	
	/**
	 * 根据角色id获取角色关联应用信息
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleId(String roleId) throws Exception ;
	
	/**
	 * 根据用户id获取角色关联应用信息
	 * 
	 * @param MemberId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMemberId(String MemberId) throws Exception ;
	
	/**
	 * 根据应用信息获取角色关联应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenus(IBOOmmMenuValue[] menus) throws Exception ;
	
	/**
	 * 根据用户信息获取角色关联应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMembers(IBOOmmMemberValue[] members) throws Exception ;
	
	/**
	 * 根据角色信息获取角色关联应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByRoleInfos(IBOOmmRoleInfoValue[] roleInfos) throws Exception ;
	
	/**
	 * 根据应用id获取角色关联应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoleByMenuId(String menuId) throws Exception ;
}
