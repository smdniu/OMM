package com.asiainfo.omm.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 应用管理操作
 * @author oswin
 *
 */
public interface IApplySV {

	/**
	 * 获取应用总数
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public int getMenuCount()throws Exception;
	
	/**
	 * 查询所有应用信息, 分页查询含头不含尾
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getAllMenu(int startIndex, int endIndex)throws Exception;
	
	/**
	 * 根据传入条件参数查询应用
	 * 
	 * @param account
	 * @param roleCode
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getMenuByCondition(String account,String roleCode, String appName, int startIndex, int endIndex)throws Exception;
	
	/**
	 * 删除应用信息,角色关联应用
	 * 
	 * @param memberId
	 * @throws Exception
	 */
	public void deleteMenu(String menuId)throws Exception;
}
