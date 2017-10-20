package com.asiainfo.omm.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 应用操作
 * 
 * @author oswin
 *
 */
public interface IMenuSV {
	
	/**
	 * 获取所有jvmcache信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAllJVMCaches()throws Exception;
	

	/**
	 * 获取所有应用信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getAllMenus() throws Exception;
	
	/**
	 * 根据id获取新增应用信息
	 * 
	 * @param menuId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getMenusById(String menuId) throws Exception;
}
