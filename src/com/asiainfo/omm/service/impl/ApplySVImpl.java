package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuRelatRoleValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmRoleInfoValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMemberSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuRelatRoleSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmRoleInfoSV;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IApplySV;
import com.asiainfo.omm.utils.DateUtil;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

/**
 * 应用管理操作
 * @author oswin
 *
 */
public class ApplySVImpl implements IApplySV {

	public List<Map<String, String>> getAllMenu(int startIndex, int endIndex) throws Exception {
		return getMenuByCondition("", "", "", startIndex, endIndex);
	}

	public int getMenuCount() throws Exception {
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		return menuSV.getMenuCount();
	}

	public static void main(String[] args) throws Exception {
		IApplySV sv = (IApplySV) ServiceFactory.getService(IApplySV.class);
		String account = "";
		String roleCode = "";
		String appName = "";
		List<Map<String, String>> list = sv.getMenuByCondition(account, roleCode, appName, 1, 21);
		System.out.println(list.get(list.size() -1).get("count"));
		System.out.println(JSONObject.toJSONString(list.subList(0, list.size() -1)));
	}
	
	public List<Map<String, String>> getMenuByCondition(String account, String roleCode, String appName, int startIndex, int endIndex) throws Exception {
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		IBOOmmMenuValue[]  menus = menuSV.getMenuByAppName(appName);
		if(menus == null || menus.length == 0){
			throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU);
		}
		IOmmMemberSV memberSV = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
		Map<String, IBOOmmRoleInfoValue> roleMapTmp = new HashMap<String, IBOOmmRoleInfoValue>();
		Map<String, IBOOmmMenuValue> menuMapTmp = new HashMap<String, IBOOmmMenuValue>();
		TreeSet<Long> menuIdTmp = new TreeSet<Long>();
		Map<String, HashSet<String>> roleMenuMemberTmp = new HashMap<String, HashSet<String>>();
		HashSet<String> roleIdTmp = null;
		String flag = "";
		if(StringUtils.isNotBlank(roleCode) && StringUtils.isBlank(account)){
			IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleInfoCode(roleCode);
			if(roles == null || roles.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_ROLE);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByRoleInfos(roles);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmRoleInfoValue role: roles){
				roleMapTmp.put(String.valueOf(role.getId()), role);
			}
			for(IBOOmmMenuValue menu: menus){
				flag = "";
				roleIdTmp = new HashSet<String>();
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
				for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
					if(menuRelatRole.getMenuId().equals(String.valueOf(menu.getId()))){
						flag = String.valueOf(menu.getId());
						roleIdTmp.add(String.valueOf(menuRelatRole.getRoleId()));
//						menuIdTmp.add(String.valueOf(menu.getId()));
						menuIdTmp.add(menu.getId());
					}else{
						break;
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
		}else if (StringUtils.isBlank(roleCode) && StringUtils.isNotBlank(account)){
			IBOOmmMemberValue[] members = memberSV.getMemberByAccountCode(account);
			if(members == null || members.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MEMBER);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByMembers(members);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmMenuValue menu: menus){
				flag = "";
				roleIdTmp = new HashSet<String>();
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
				for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
					if(menuRelatRole.getMenuId().equals(String.valueOf(menu.getId()))){
						flag = String.valueOf(menu.getId());
						roleIdTmp.add(String.valueOf(menuRelatRole.getRoleId()));
						menuIdTmp.add(menu.getId());
					}else{
						break;
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
		}else if(StringUtils.isNotBlank(roleCode) && StringUtils.isNotBlank(account)){
			IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleInfoCode(roleCode);
			if(roles == null || roles.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_ROLE);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByRoleInfos(roles);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmRoleInfoValue role: roles){
				roleMapTmp.put(String.valueOf(role.getId()), role);
			}
			IBOOmmMemberValue[] members = memberSV.getMemberByAccountCode(account);
			if(members == null || members.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MEMBER);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles1 = menuRelatRoleSV.getMenuRelatRoleByMembers(members);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmMenuValue menu: menus){
				flag = "";
				roleIdTmp = new HashSet<String>();
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
				for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
					if(menuRelatRole.getMenuId().equals(String.valueOf(menu.getId()))){
						for(IBOOmmMenuRelatRoleValue menuRelatRole1: menuRelatRoles1){
							if(menuRelatRole1.getMenuId().equals(String.valueOf(menu.getId()))){
								flag = String.valueOf(menu.getId());
								roleIdTmp.add(String.valueOf(menuRelatRole.getRoleId()));
								roleIdTmp.add(String.valueOf(menuRelatRole1.getRoleId()));
								menuIdTmp.add(menu.getId());
							}else{
								break;
							}
						}
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
		}else{
			for(IBOOmmMenuValue menu: menus){
				roleIdTmp = new HashSet<String>();
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
				menuIdTmp.add(menu.getId());
				roleMenuMemberTmp.put(String.valueOf(menu.getId()), roleIdTmp);
			}
		}
		
		List<Map<String, String>> menuList = new ArrayList<Map<String,String>>();
		Map<String, String> menuMap = null;
		if(menuIdTmp != null && menuIdTmp.size() >0){
			for(Long keyTmp: menuIdTmp){
				String key = String.valueOf(keyTmp);
				IBOOmmMenuValue menu = menuMapTmp.get(key);
				menuMap = new HashMap<String, String>();
				menuMap.put("pageId", "menu" + menu.getId());
				menuMap.put("id", menu.getId()+ "");
				menuMap.put("name", menu.getName());
				menuMap.put("path", menu.getPath());
				menuMap.put("createTime", DateUtil.getDateFormat(menu.getCreateTime(), "yyyy-MM-dd"));
				menuMap.put("expiryTime", DateUtil.getDateFormat(menu.getExpiryTime(), "yyyy-MM-dd"));
				menuMap.put("menuType", menu.getMenuType());
				menuMap.put("state", OMMEnumUtils.getStateByType(menu.getState()));
				StringBuffer roles = new StringBuffer("");
				roleIdTmp = roleMenuMemberTmp.get(key);
				if(roleIdTmp != null && roleIdTmp.size() != 0){
					int count = 0;
					for(String roleId: roleIdTmp){
						roles.append(roleMapTmp.get(roleId).getName());
						if(count < roleIdTmp.size() - 1){
							roles.append(",");
						}
						count++;
					}
				}else{
					IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByMenuId(String.valueOf(menu.getId()));
					//根据应用查角色
					if(menuRelatRoles == null || menuRelatRoles.length == 0){
						roles.append("未归属角色");
					}else{
						//根据角色关联信息查角色
						IBOOmmRoleInfoValue[] roleInfos = roleInfoSV.getRoleInfoByMenuRelatRole(menuRelatRoles);
						if(roleInfos != null){
							int count = 0;
							for(IBOOmmRoleInfoValue roleInfo: roleInfos){
								roles.append(roleInfo.getName()) ;
								if(count < roleInfos.length - 1){
									roles.append(",");
								}
								count++;
							}
						}else{
							roles.append("未归属角色");
						}
					}
				}
				menuMap.put("roles", roles.toString());
				menuList.add(menuMap);
			}
		}
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		int size = menuList.size();
		if(endIndex > size){
			result =  new ArrayList<Map<String,String>>(menuList.subList(startIndex -1, menuList.size()));
		}else if(startIndex < menuList.size() && endIndex < size){
			result =  new ArrayList<Map<String,String>>(menuList.subList(startIndex -1, endIndex-1));
		}
		Map<String, String> count = new HashMap<String, String>();
		count.put("count", String.valueOf(menuList.size()));
		result.add(count);
		return result;
	}

	
	
	public void deleteMenu(String menuId) throws Exception {
		IOmmMenuSV menuSV = (IOmmMenuSV) ServiceFactory.getService(IOmmMenuSV.class);
		menuSV.deleteMenu(menuId);
	}
}
