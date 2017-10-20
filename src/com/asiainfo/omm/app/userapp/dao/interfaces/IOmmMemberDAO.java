package com.asiainfo.omm.app.userapp.dao.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;


/**
 * 用户信息查询
 * 
 * @author oswin
 *
 */
public interface IOmmMemberDAO {
	
	/**
	 * 根据用户信息id批量查询用户信息
	 * 
	 * @param memberId 
	 * @return
	 */
	public IBOOmmMemberValue[] getMemberBymemberIds(String[] memberIds) throws Exception ;
	
	/**
	 * 删除角色信息
	 * 
	 * @param members
	 * @throws Exception
	 */
	public void delelteMember(IBOOmmMemberValue[] members) throws Exception;
	
	/**
	 * 根据Id查询用户信息, 不关心用户状态
	 * @param id
	 * @return
	 */
	public IBOOmmMemberValue[] getMemberById1(String id) throws Exception;
	
	/**
	 * 根据Id查询用户信息, 用户状态为U
	 * @param account
	 * @return
	 */
	public IBOOmmMemberValue[] getMemberById(String id) throws Exception;
	/**
	 * 获取用户总数
	 * 
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
	 * 新增用户信息
	 * 
	 * @param member
	 * @return 返回主键id
	 * @throws Exception
	 */
	public long addMember(IBOOmmMemberValue member)throws Exception;
	
	/**
	 * 修改用户信息,
	 * 
	 * @param member
	 * @return 返回主键id
	 * @throws Exception
	 */
	public long updateMember(IBOOmmMemberValue member)throws Exception;
	
	/**
	 * 根据账号查询用户信息
	 * @param account
	 * @return
	 */
	public IBOOmmMemberValue[] getMemberByAccount(String account) throws Exception ;

	/**
	 * 根据用户code查询用户信息, 不关心用户状态
	 * @param accountCode 账户名或用户名
	 * @return
	 */
	public IBOOmmMemberValue[] getMemberByAccountCode(String accountCode) throws Exception ;
	
	/**
	 * 查询所有用户, 分页查询含头不含尾
	 * 
	 * @return
	 */
	public IBOOmmMemberValue[] getAllMember(int startIndex, int endIndex) throws Exception ;
}
