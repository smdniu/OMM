package com.asiainfo.omm.service.interfaces;

import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.bo.UserInfoBean;

public interface IUserServiceSV {
	
	/**
	 * 根据账号查询用户信息
	 * @param account
	 * @return
	 */
	public IBOOmmMemberValue getMemberByAccount(String account) throws Exception;
	
	/**
	 * 根据用户信息查询用户可用菜单
	 * 
	 * @param member
	 * @return
	 * @throws Exception
	 */
	public UserInfoBean getMenuByMemberInfo(IBOOmmMemberValue member) throws Exception;
	
	/**
	 * 判断密码是否正确
	 * 
	 * @param member
	 * @throws Exception
	 */
	public void checkPassword(String passWord, IBOOmmMemberValue member) throws Exception;
}
