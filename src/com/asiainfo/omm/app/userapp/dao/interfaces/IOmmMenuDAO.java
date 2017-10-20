package com.asiainfo.omm.app.userapp.dao.interfaces;

import java.util.List;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;

/**
 * OMM应用菜单查询
 * 
 * @author oswin
 *
 */
public interface IOmmMenuDAO {
	
	/**
	 * 更新应用信息
	 * 
	 * @param menus
	 * @return
	 * @throws Exception
	 */
	public void updateMenu(IBOOmmMenuValue[] menus) throws Exception ;
	
	/**
	 * 新增应用
	 * 
	 * @param menu
	 * @throws Exception
	 */
	public void addMenu(IBOOmmMenuValue menu) throws Exception ;
	
	/**
	 * 删除应用
	 * 
	 * @param menu
	 * @throws Exception
	 */
	public void deleteMenu(IBOOmmMenuValue[] menus) throws Exception ;
	

	/**
	 * 根据应用id查询应用信息
	 * @param account
	 * @return
	 */
	public IBOOmmMenuValue[] getMenuByMenuId(List<String> menuIds) throws Exception ;
	
	/**
	 * 根据应用id查询应用信息, 忽略状态信息, 删除应用时使用
	 * 
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuValue[] getMenuByMenuId1(String menuId) throws Exception ;
	
	/**
	 * 根据appname获取应用信息
	 * 
	 * @param appName 可以是应用名, 也可以是应用code
	 * @return
	 * @throws Exception
	 */
	public IBOOmmMenuValue[] getMenuByAppName(String appName) throws Exception ;
	
	/**
	 * 获取应用总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getMenuCount() throws Exception;
}
