package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.userapp.bo.BOOmmMenuBean;
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
import com.asiainfo.omm.constant.OMMExceptionEnum;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IRoleSV;
import com.asiainfo.omm.utils.DateUtil;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;
import com.asiainfo.omm.utils.TimeUtils;


/**
 * 角色管理页面SVImpl
 * 
 * @author oswin
 *
 */
public class RoleSVImpl implements IRoleSV {

	public Map<String, String> getRoleInfoByRoleId(String roleId)throws Exception{
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleId1(roleId);
		if(roles == null || roles.length <= 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_ROLE);
		}else if(roles.length >1){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_FOUND_MULTI_MEMBER);
		}
		Map<String, String> roleMap = new HashMap<String, String>();
		roleMap.put("id", String.valueOf(roles[0].getId()));
		roleMap.put("roleCode", roles[0].getRoleCode());
		roleMap.put("name", roles[0].getName());
		roleMap.put("selectedState", roles[0].getState());
		roleMap.put("createTime", TimeUtils.getTimeStr(roles[0].getCreateTime(), "yyyy-MM-dd"));
		roleMap.put("expiryTime", TimeUtils.getTimeStr(roles[0].getExpiryTime(), "yyyy-MM-dd"));
		roleMap.put("ext1", roles[0].getExt1());
		roleMap.put("ext2", roles[0].getExt2());
		roleMap.put("ext3", roles[0].getExt3());
		roleMap.put("remark", roles[0].getRemark());
		
		
		return roleMap;
	}
	
	
	public List<Map<String, String>> getAllRoleInfo() throws Exception {
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IBOOmmRoleInfoValue[]  roles = roleInfoSV.getAllRoleInfo();
		List<Map<String, String>> roleList = new ArrayList<Map<String,String>>();
		if(roles != null && roles.length > 0){
			Map<String, String> roleMap = null;
			for(IBOOmmRoleInfoValue role: roles){
				roleMap = new HashMap<String, String>();
				roleMap.put("TYPE", role.getRoleCode());
				roleMap.put("NAME", role.getName());
				roleList.add(roleMap);
			}
		}
		return roleList;
	}
	
	public List<Map<String, String>> getAllRoleInfo(int startIndex, int endIndex) throws Exception{
		return getRoleInfoByCondition("", "", "", startIndex, endIndex);
	}
	
	public List<Map<String, String>> getRoleInfoByCondition(String account, String roleCode, String appName, int startIndex, int endIndex) throws Exception{
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleInfoCode(roleCode);
		if(roles == null || roles.length == 0){
			throw new OMMException(ExceptionEnum.USER_NOT_FOUND_ROLE);
		}
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		IOmmMemberSV memberSV = (IOmmMemberSV)ServiceFactory.getService(IOmmMemberSV.class);
		IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		Map<String, IBOOmmRoleInfoValue> roleMapTmp = new HashMap<String, IBOOmmRoleInfoValue>();
		Map<String, IBOOmmMenuValue> menuMapTmp = new HashMap<String, IBOOmmMenuValue>();
		Map<String, IBOOmmMemberValue> memberMapTmp = new HashMap<String, IBOOmmMemberValue>();
		TreeSet<Long> roleIdTmp = new TreeSet<Long>();
		Map<String, List<HashSet<String>>> roleMenuMemberTmp = new HashMap<String, List<HashSet<String>>>();
		List<HashSet<String>> menuMemberList = null;
		HashSet<String> memberIdTmp = null;
		HashSet<String> menuIdTmp = null;
		String flag = "";
		if(StringUtils.isNotBlank(account) && StringUtils.isBlank(appName)){
			IBOOmmMemberValue[] members = memberSV.getMemberByAccountCode(account);
			if(members == null || members.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MEMBER);
			}
			IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleSV.getMemberRelatRoleByMemberIds(members);
			if(memberRelatRoles == null || memberRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmMemberValue member: members){
				memberMapTmp.put(String.valueOf(member.getId()), member);
			}
			for(IBOOmmRoleInfoValue role: roles){
				flag = "";
				memberIdTmp = new HashSet<String>();
				menuIdTmp = new HashSet<String>();
				roleMapTmp.put(String.valueOf(role.getId()), role);
				for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
					if(memberRelatRole.getRoleid().equals(String.valueOf(role.getId()))){
						flag = String.valueOf(role.getId());
						roleIdTmp.add(role.getId());
						memberIdTmp.add(memberRelatRole.getMemberId());
					}else{
						break;
					}
				}
				if(StringUtils.isNotBlank(flag)){
					menuMemberList = new ArrayList<HashSet<String>>();
					menuMemberList.add(0, memberIdTmp);
					menuMemberList.add(1, menuIdTmp);
					roleMenuMemberTmp.put(flag, menuMemberList);
				}
			}
		}else if (StringUtils.isBlank(account) && StringUtils.isNotBlank(appName)){
			IBOOmmMenuValue[]  menus = menuSV.getMenuByAppName(appName);
			if(menus == null || menus.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles =  menuRelatRoleSV.getMenuRelatRoleByMenus(menus);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU_RELAT_ROLE);
			}
			for(IBOOmmMenuValue menu: menus){
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
			}
			
			for(IBOOmmRoleInfoValue role: roles){
				flag = "";
				memberIdTmp = new HashSet<String>();
				menuIdTmp = new HashSet<String>();
				roleMapTmp.put(String.valueOf(role.getId()), role);
				for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
					if(menuRelatRole.getRoleId().equals(String.valueOf(role.getId()))){
						flag = String.valueOf(role.getId());
						roleIdTmp.add(role.getId());
						menuIdTmp.add(menuRelatRole.getMenuId());
					}else{
						break;
					}
				}
				if(StringUtils.isNotBlank(flag)){
					menuMemberList = new ArrayList<HashSet<String>>();
					menuMemberList.add(0, memberIdTmp);
					menuMemberList.add(1, menuIdTmp);
					roleMenuMemberTmp.put(flag, menuMemberList);
				}
			}
		}else if(StringUtils.isNotBlank(account) && StringUtils.isNotBlank(appName)){
			IBOOmmMemberValue[] members = memberSV.getMemberByAccountCode(account);
			if(members == null || members.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MEMBER);
			}
			IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleSV.getMemberRelatRoleByMemberIds(members);
			if(memberRelatRoles == null || memberRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmMemberValue member: members){
				memberMapTmp.put(String.valueOf(member.getId()), member);
			}
			IBOOmmMenuValue[]  menus = menuSV.getMenuByAppName(appName);
			if(menus == null || menus.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU);
			}
			IBOOmmMenuRelatRoleValue[] menuRelatRoles =  menuRelatRoleSV.getMenuRelatRoleByMenus(menus);
			if(menuRelatRoles == null || menuRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU_RELAT_ROLE);
			}
			for(IBOOmmMenuValue menu: menus){
				menuMapTmp.put(String.valueOf(menu.getId()), menu);
			}
			for(IBOOmmRoleInfoValue role: roles){
				flag = "";
				memberIdTmp = new HashSet<String>();
				menuIdTmp = new HashSet<String>();
				roleMapTmp.put(String.valueOf(role.getId()), role);
				for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
					for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
						if(memberRelatRole.getRoleid().equals(String.valueOf(role.getId())) && menuRelatRole.getRoleId().equals(String.valueOf(role.getId()))){
							flag = String.valueOf(role.getId());
							roleIdTmp.add(role.getId());
							menuIdTmp.add(menuRelatRole.getMenuId());
							memberIdTmp.add(memberRelatRole.getMemberId());
						}else{
							break;
						}
					}
				}
				if(StringUtils.isNotBlank(flag)){
					menuMemberList = new ArrayList<HashSet<String>>();
					menuMemberList.add(0, memberIdTmp);
					menuMemberList.add(1, menuIdTmp);
					roleMenuMemberTmp.put(flag, menuMemberList);
				}
			}
		}else{
			for(IBOOmmRoleInfoValue role: roles){
				memberIdTmp = new HashSet<String>();
				menuIdTmp = new HashSet<String>();
				roleMapTmp.put(String.valueOf(role.getId()), role);
				roleIdTmp.add(role.getId());
				menuMemberList = new ArrayList<HashSet<String>>();
				menuMemberList.add(0, memberIdTmp);
				menuMemberList.add(1, menuIdTmp);
				roleMenuMemberTmp.put(String.valueOf(role.getId()), menuMemberList);
			}
		}
		List<Map<String, String>> roleList = new ArrayList<Map<String,String>>();
		Map<String, String> roleMap = null;
		if(roleIdTmp != null && roleIdTmp.size() != 0){
			for(Long keyTmp: roleIdTmp){
				String key = String.valueOf(keyTmp);
				roleMap = new HashMap<String, String>();
				IBOOmmRoleInfoValue roleTmp = roleMapTmp.get(key);
				menuMemberList = roleMenuMemberTmp.get(key);
				memberIdTmp = menuMemberList.get(0);
				menuIdTmp = menuMemberList.get(1);
				roleMap.put("pageId", "role" + roleTmp.getId());
				roleMap.put("id", roleTmp.getId()+ "");
				roleMap.put("code", roleTmp.getRoleCode());
				roleMap.put("name", roleTmp.getName());
				StringBuilder memberStr = new StringBuilder("");
				//获取角色关联用户
				if(memberIdTmp != null && memberIdTmp.size() != 0){
					int count = 0;
					for(String memberId: memberIdTmp){
						memberStr.append(memberMapTmp.get(memberId).getName());
						if(count < memberIdTmp.size() - 1){
							memberStr.append(",");
						}
						count++;
					}
				}else{
					IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleSV.getMemberRelatRoleByRoleId(String.valueOf(roleTmp.getId()));
					IBOOmmMemberValue[] members = memberSV.getMenuByMenuRelatRole(memberRelatRoles);
					if(members != null && members.length > 0){
						int count = 0;
						for(IBOOmmMemberValue member: members){
							memberStr.append(member.getName());
							if(count < members.length - 1){
								memberStr.append(",");
							}
							count++;
						}
					}else{
						memberStr.append("未关联用户");
					}
				}
				roleMap.put("relatMember", memberStr.toString());//关联用户
				StringBuilder menuStr = new StringBuilder("");
				if(menuIdTmp != null && menuIdTmp.size() != 0){
					int count = 0;
					for(String menuId: menuIdTmp){
						menuStr.append(menuMapTmp.get(menuId).getName());
						if(count < menuIdTmp.size() - 1){
							menuStr.append(",");
						}
						count++;
					}
				}else{
					IBOOmmMenuRelatRoleValue[] menuRelateRoles = menuRelatRoleSV.getMenuRelatRoleByRoleId(String.valueOf(roleTmp.getId()));
					IBOOmmMenuValue[]  menus = menuSV.getMenuByMenuRelatRole(menuRelateRoles);
					if(menus != null && menus.length > 0){
						int count = 0;
						for(IBOOmmMenuValue menu: menus){
							menuStr.append(menu.getName());
							if(count < menus.length - 1){
								menuStr.append(",");
							}
							count++;
						}
					}else{
						menuStr.append("未关联应用");
					}
				}
				roleMap.put("relatApp", menuStr.toString());//关联应用
				roleMap.put("createTime", DateUtil.getDateFormat(roleTmp.getCreateTime(), "yyyy-MM-dd"));
				roleMap.put("expiryTime", DateUtil.getDateFormat(roleTmp.getExpiryTime(), "yyyy-MM-dd"));
				roleMap.put("state", OMMEnumUtils.getStateByType(roleTmp.getState()));
				roleList.add(roleMap);
			}
		}
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		int size = roleList.size();
		if(endIndex > size){
			result =  new ArrayList<Map<String,String>>(roleList.subList(startIndex -1, roleList.size()));
		}else if(startIndex < roleList.size() && endIndex < size){
			result =  new ArrayList<Map<String,String>>(roleList.subList(startIndex -1, endIndex-1));
		}
		Map<String, String> count = new HashMap<String, String>();
		count.put("count", String.valueOf(roleList.size()));
		result.add(count);
		return result;
	}

	public static void main(String[] args) throws Exception {
		IRoleSV sv = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
		String account = "";
		String roleCode = "管理员";
		String appName = "";
		System.out.println(JSONObject.toJSONString(sv.getRoleInfoByCondition(account, roleCode, appName, 1, 21)));
	}
	
	public void deleteRoleInfo(String roleId)throws Exception{
		IOmmRoleInfoSV roleSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		roleSV.deleteRoleInfoByRoleId(roleId);
	}
	
	public void addRole(Map<String, String> role) throws Exception {
	}

	public int getRoleInfoCount()throws Exception{
		IOmmRoleInfoSV roleSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		return roleSV.getRoleInfoCount();
	}
}
