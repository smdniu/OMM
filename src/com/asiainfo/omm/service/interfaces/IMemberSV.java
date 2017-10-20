package com.asiainfo.omm.service.interfaces;

import java.util.List;
import java.util.Map;

/**
 * 用户配置操作
 * 
 * @author oswin
 *
 */
public interface IMemberSV {

	/**
	 * 根据用户id获取用户信息和关联角色信息:<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, String> getMemberAndRoleInfoByMemberId(String memberId)throws Exception;
	
	/**
	 * 删除用户信息,用户关联角色, 应用信息
	 * 
	 * @param memberId
	 * @throws Exception
	 */
	public void deleteMember(String memberId)throws Exception;
	
	/**
	 * 查询所有用户信息, 分页查询含头不含尾
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getAllMember(int startIndex, int endIndex)throws Exception;
	
	/**
	 * 根据传入条件参数查询用户
	 * 
	 * @param account
	 * @param roleCode
	 * @param appName
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, String>> getMemberByCondition(String account,String roleCode, String appName, int startIndex, int endIndex)throws Exception;
	
	/**
	 * 获取用户总数
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return
	 * @throws Exception
	 */
	public int getMemberCount()throws Exception;
	
	/**
	 * 获取用户总数
	 * 
	 * @param memberCode 用户code
	 * @return
	 * @throws Exception
	 */
	public int getMemberCountByMemberCode(String memberCode)throws Exception;
	
	/**
	 * 保存用户信息
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void addMember(String account, String name, String password, String dept, String tel, String email, String ip, String createTime, String exptryTime, String encrypte, String state, String remark, String ext1, String ext2, String ext3, String[] roles)throws Exception;
	
	/**
	 * 修改用户信息
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void updateMember(String id, String account, String name, String password, String dept, String tel, String email, String ip, String createTime, String exptryTime, String encrypte, String state, String remark, String ext1, String ext2, String ext3, String[] roles)throws Exception;
	
	/**
	 * 新增用户关联角色信息
	 * 
	 * @param memberRelatRole
	 * @throws Exception
	 */
	public void addMemberRelatRole(Map<String, String> memberRelatRole)throws Exception;
}
