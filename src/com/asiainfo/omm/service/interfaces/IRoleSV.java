package com.asiainfo.omm.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 角色管理页面sv
 * 
 * @author oswin
 *
 */
public interface IRoleSV {

	/**
	 * 获取所有角色信息:<br>
	 * TYPE: roleid
	 * NAME: name
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getAllRoleInfo()throws Exception;
	
	/**
	 * 查询角色信息
	 * 
	 * @param account
	 * @param roleCode
	 * @param appName
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getRoleInfoByCondition(String account, String roleCode, String appName, int startIndex, int endIndex) throws Exception;
	
	/**
	 * 保存角色信息
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void addRole(Map<String, String> role)throws Exception;
	
	/**
	 * 获取角色总数
	 * 
	 * @return
	 * @throws Exception
	 */
	public int getRoleInfoCount()throws Exception;
	
	/**
	 * 获取所有角色
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getAllRoleInfo(int startIndex, int endIndex) throws Exception;
	
	/**
	 * 删除角色以及对应关联用户, 和应用信息
	 * 
	 * @param roleId
	 * @throws Exception
	 */
	public void deleteRoleInfo(String roleId)throws Exception;
	
	/**
	 * 更具角色id获取角色信息, 关联应用信息
	 * 
	 * @param roleId
	 * @throws Exception
	 */
	public Map<String, String> getRoleInfoByRoleId(String roleId)throws Exception;
}
