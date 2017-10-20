package com.asiainfo.omm.bo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;

/**
 * 用户登录信息, 包括用户, 用户关联角色, 
 * 
 * @author oswin
 *
 */
public final class UserInfoBean {
	
	private static final Logger logger = Logger.getLogger(UserInfoBean.class);

	private final IBOOmmMemberValue member;
	
	private final IBOOmmRoleInfoValue[] roleInfos;
	
	private final IBOOmmMemberRelatRoleValue[] memberRelatRoles;
	
	private final IBOOmmMenuRelatRoleValue[] menuRelatRoles;
	
	private final Map<String, IBOOmmMenuValue> menusMap;
	
	private final Map<String, String> menuAuth;
	
	private final Map<String, Map<String,Object>> menuDB;
	
	private final List<String> menuMemcache;
	
	private final List<Map<String,Object>> singlepointAuth;
	
	//redis权限
	private final List<String> redisAuth;

	public UserInfoBean(IBOOmmMemberValue member,
			IBOOmmRoleInfoValue[] roleInfos,
			IBOOmmMemberRelatRoleValue[] memberRelatRoles,
			IBOOmmMenuRelatRoleValue[] menuRelatRoles,
			Map<String, IBOOmmMenuValue> menusMap,
			Map<String, String> menuAuth,
			Map<String, Map<String, Object>> menuDB,
			List<String> menuMemcache,
			List<Map<String, Object>> singlepointAuth, List<String> redisAuth) {
		super();
		this.member = member;
		this.roleInfos = roleInfos;
		this.memberRelatRoles = memberRelatRoles;
		this.menuRelatRoles = menuRelatRoles;
		this.menusMap = menusMap;
		this.menuAuth = menuAuth;
		this.menuDB = menuDB;
		this.menuMemcache = menuMemcache;
		this.singlepointAuth = singlepointAuth;
		this.redisAuth = redisAuth;
		logger.info("session中ommMember::" + member);
		logger.info("session中ommMenu::" + menusMap);
		logger.info("session中ommRole::" + roleInfos);
		logger.info("session中ommMemberRelatRole::" + memberRelatRoles);
		logger.info("session中ommMenuRelatRole::" + menuRelatRoles);
		logger.info("session中ommMenuAuth::" + JSONObject.toJSONString(menuAuth));
		logger.info("session中ommMenuDB::" + JSONObject.toJSONString(menuDB));
		logger.info("session中ommMenuRedis::" + JSONObject.toJSONString(redisAuth));
		logger.info("session中ommMenuMemcache::" + JSONObject.toJSONString(menuMemcache));
		logger.info("session中ommMenuSinglePoint::" + JSONObject.toJSONString(singlepointAuth));
		
	}
	
	public List<String> getRedisAuth() {
		return redisAuth;
	}

	public List<Map<String, Object>> getSinglepointAuth() {
		return singlepointAuth;
	}

	public List<String> getMenuMemcache() {
		return menuMemcache;
	}

	public Map<String, IBOOmmMenuValue> getMenusMap() {
		return menusMap;
	}

	public IBOOmmMemberValue getMember() {
		return member;
	}

	public IBOOmmRoleInfoValue[] getRoleInfos() {
		return roleInfos;
	}


	public IBOOmmMemberRelatRoleValue[] getMemberRelatRoles() {
		return memberRelatRoles;
	}

	public IBOOmmMenuRelatRoleValue[] getMenuRelatRoles() {
		return menuRelatRoles;
	}

	public Map<String, String> getMenuAuth() {
		return menuAuth;
	}
	
	public Map<String, Map<String, Object>> getMenuDB() {
		return menuDB;
	}

	@Override
	public String toString() {
		return "UserInfoBean [member=" + member + ", memberRelatRoles="
				+ Arrays.toString(memberRelatRoles) + ", menuAuth=" + menuAuth
				+ ", menuDB=" + menuDB + ", menuMemcache=" + menuMemcache
				+ ", menuRelatRoles=" + Arrays.toString(menuRelatRoles)
				+ ", menusMap=" + menusMap + ", roleInfos="
				+ Arrays.toString(roleInfos) + "]";
	}
}
