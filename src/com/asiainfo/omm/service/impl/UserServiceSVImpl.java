package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMemberRelatRoleSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMemberSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuRelatRoleSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmRoleInfoSV;
import com.asiainfo.omm.bo.UserInfoBean;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IUserServiceSV;
import com.asiainfo.omm.utils.OMMEncrypt;
import com.asiainfo.omm.utils.StringUtils;

public class UserServiceSVImpl implements IUserServiceSV {

	public IBOOmmMemberValue getMemberByAccount(String account) throws Exception {
		IOmmMemberSV sv = (IOmmMemberSV)ServiceFactory.getService(IOmmMemberSV.class);
		return sv.getMemberByAccount(account);
	}

	
	public UserInfoBean getMenuByMemberInfo(IBOOmmMemberValue member) throws Exception {
		//根据用户查角色
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles =  memberRelatRoleSV.getMemberRelatRoleById(String.valueOf(member.getId()));
		if(memberRelatRoles == null || memberRelatRoles.length == 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
		}
		//根据角色关联信息查角色
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IBOOmmRoleInfoValue[] roleInfos = roleInfoSV.getRoleInfoByMemberRelatRole(memberRelatRoles);
		if(roleInfos == null || roleInfos.length == 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_ROLE);
		}
		//根据角色关联信息查应用
		IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
		IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByMemberRelatRole(memberRelatRoles);
		IBOOmmMenuValue[] menus = null;
		Map<String, IBOOmmMenuValue> menusMap = null; 
		Map<String, String> menuAuth = new LinkedHashMap<String, String>();
		//memcache权限
		TreeSet<String> memecacheAuthTmp = new TreeSet<String>();
		//redis权限
		TreeSet<String> redisAuthTmp = new TreeSet<String>();
		//单点登录权限
		List<Map<String,Object>> singlepointAuthTmp = new ArrayList<Map<String,Object>>();
		//数据库权限
		Map<String,Map<String,Object>> menuDB = new HashMap<String,Map<String,Object>>();
		if(menuRelatRoles != null && menuRelatRoles.length != 0){
			//根据应用查应用信息
			IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
			menus = menuSV.getMenuByMenuRelatRole(menuRelatRoles);
			if(menus != null && menus.length != 0){
				menusMap = new LinkedHashMap<String, IBOOmmMenuValue>(); 
				for(IBOOmmMenuValue menu: menus){
					String tmp = menu.getPath();
					if(tmp.startsWith("/")){
						tmp = tmp.substring(1);
					}
					menu.setPath(tmp);
					for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
						if(String.valueOf(menu.getId()).equals(menuRelatRole.getMenuId())){
							String limitRank = menuRelatRole.getLimitRank();
							if(MENUENUM.MEMCACHE.getMenuType().equals(menu.getMenuType())){//放入memcache可操作信息
								if(StringUtils.isNotBlank(limitRank)){
									String[] limit = limitRank.split(",");
									for(String key: limit){
										memecacheAuthTmp.add(key.trim());
									}
								}
							}else if(MENUENUM.REDIS.getMenuType().equals(menu.getMenuType())){//放入memcache可操作信息
								if(StringUtils.isNotBlank(limitRank)){
									String[] limit = limitRank.split(",");
									for(String key: limit){
										redisAuthTmp.add(key.trim());
									}
								}
							}else if(MENUENUM.SINGLEPOINT.getMenuType().equals(menu.getMenuType())){//查询用户可操作单点登录信息
								Map<String,Object> singlepointMapTmp = new HashMap<String, Object>();
								singlepointMapTmp.put("code", menu.getMenuCode());
								singlepointMapTmp.put("menuName", menu.getName());
								singlepointMapTmp.put("url", menu.getExt1());
								if(StringUtils.isNotBlank(limitRank)){
									List<Map<String,Object>> accPwdListTmp = new ArrayList<Map<String,Object>>();
									String[] limit = limitRank.split(",");
									for(String key: limit){
										String[] keyTmp = key.split(":");
										if(keyTmp != null && keyTmp.length == 2){
											Map<String,Object> accPwdMapTmp = new HashMap<String, Object>();
											accPwdMapTmp.put("username", keyTmp[0]);
											accPwdMapTmp.put("password", keyTmp[1]);
											accPwdListTmp.add(accPwdMapTmp);
										}
									}
									singlepointMapTmp.put("accPwd", accPwdListTmp);
								}
								singlepointAuthTmp.add(singlepointMapTmp);
							}else if(MENUENUM.DB.getMenuType().equals(menu.getMenuType())){//查询用户可操作数据库信息
								if(StringUtils.isNotBlank(limitRank)){
									String[] limit = limitRank.split(",");
									for(String key: limit){
										String[] keyTmp = key.split(":");
										if(keyTmp != null && keyTmp.length == 3){
											//根据配置获取用户的数据库权限
											Map<String,Object> dbTmp = new HashMap<String,Object>();
											dbTmp.put(OMMConstantEnum.OMM_MENU_DB_USERNAME, keyTmp[1]);
											String[] sqlType = keyTmp[2].split(";");
											if(sqlType != null && sqlType.length != 0){
												dbTmp.put(OMMConstantEnum.OMM_MENU_DB_SQLTYPE, sqlType);
											}
											menuDB.put(keyTmp[0], dbTmp);
										}
									}
								}
							}else{
								if(StringUtils.isBlank(limitRank)){
									limitRank = "SELECT";
								}else{
									if(!limitRank.contains("SELECT")){
										limitRank = "SELECT," + limitRank;
									}
								}
								menuAuth.put(tmp, limitRank.toUpperCase());
							}
						}
					}
					menusMap.put(tmp, menu);
				}
			}
		}
		return new UserInfoBean(member, roleInfos, memberRelatRoles, menuRelatRoles, menusMap, menuAuth, menuDB, new ArrayList<String>(memecacheAuthTmp), singlepointAuthTmp, new ArrayList<String>(redisAuthTmp));
	}

	public void checkPassword(String password, IBOOmmMemberValue member) throws Exception {
		String realPassword = password.trim();
		realPassword = OMMEncrypt.encryptStr(member.getEncryptType(),realPassword);
		if(!member.getPassword().equals(realPassword)){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.OMM_PASSWORD_IS_WRONG);
		}
	}
}
