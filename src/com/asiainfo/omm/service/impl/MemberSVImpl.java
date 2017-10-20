package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.bo.BOOmmMemberBean;
import com.asiainfo.omm.app.userapp.bo.BOOmmMemberRelatRoleBean;
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
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IMemberSV;
import com.asiainfo.omm.utils.DateUtil;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;
import com.asiainfo.omm.utils.TimeUtils;

public class MemberSVImpl implements IMemberSV {

	public void addMember(String account, String name, String password, String dept, String tel, String email, String ip, String createTime, String exptryTime, String encrypte, String state, String remark, String ext1, String ext2, String ext3, String[] roles) throws Exception {
		//确认角色信息是否存在
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		if(roles == null || roles.length <= 0){
			throw new Exception("未关联角色信息");
		}
		Map<String, String> roleMap = new HashMap<String, String>();
		for(String role: roles){
			IBOOmmRoleInfoValue[] roleBean = roleInfoSV.getRoleInfoByRoleCode(role);
			if(roleBean == null || roleBean.length <= 0){
				throw new Exception("未找到用户选中角色信息");
			}else if (roleBean.length > 1){
				throw new Exception("找到用户选中多个角色信息");
			}else{
				roleMap.put(role, String.valueOf(roleBean[0].getId()));
			}
		}
		IBOOmmMemberValue memberBean = new BOOmmMemberBean();
		IOmmMemberSV memberSV = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		memberBean.setAccount(account);
		memberBean.setName(name);
		memberBean.setPassword(password);
		memberBean.setDept(OMMEnumUtils.getDeptNameByDeptType(dept));
		memberBean.setTel(tel);
		memberBean.setEmail(email);
		memberBean.setIpAddress(ip);
		memberBean.setCreateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
		memberBean.setExpiryTime(DateUtil.getTimestamp(exptryTime, "yyyy-MM-dd"));
		memberBean.setEncryptType(encrypte);
		memberBean.setState(state);
		memberBean.setRemark(remark);
		memberBean.setExt1(ext1);
		memberBean.setExt2(ext2);
		memberBean.setExt3(ext3);
		long memberId = memberSV.addMember(memberBean);
		//保存用户关联角色信息
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = new BOOmmMemberRelatRoleBean[roles.length];
		for(int i =0; i < roles.length; i++){
			memberRelatRoles[i] = new BOOmmMemberRelatRoleBean();
			memberRelatRoles[i].setMemberId(String.valueOf(memberId));
			memberRelatRoles[i].setRoleid(roleMap.get(roles[i]));
			memberRelatRoles[i].setCreateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
			memberRelatRoles[i].setUpdateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
			memberRelatRoles[i].setExpiryTime(DateUtil.getTimestamp(exptryTime, "yyyy-MM-dd"));
		}
		memberRelatRoleSV.addMemberRelatRoles(memberRelatRoles);
	}
	
	public void updateMember(String id, String account, String name, String password, String dept, String tel, String email, String ip, String createTime, String exptryTime, String encrypte, String state, String remark, String ext1, String ext2, String ext3, String[] roles) throws Exception {
		IOmmMemberSV memberSV = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		IBOOmmMemberValue[] members = memberSV.getMemberById1(id);
		if(members == null || members.length <= 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_MEMBER);
		}else if(members.length >1){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_FOUND_MULTI_MEMBER);
		}
		//确认角色信息是否存在
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		if(roles == null){
			throw new Exception("未关联角色信息");
		}
		Map<String, String> roleMap = new HashMap<String, String>();
		for(String role: roles){
			IBOOmmRoleInfoValue[] roleBean = roleInfoSV.getRoleInfoByRoleCode(role);
			if(roleBean == null || roleBean.length <= 0){
				throw new Exception("未找到用户选中角色信息");
			}else if (roleBean.length > 1){
				throw new Exception("找到用户选中多个角色信息");
			}else{
				roleMap.put(role, String.valueOf(roleBean[0].getId()));
			}
		}
		IBOOmmMemberValue memberBean = members[0];
		memberBean.setAccount(account);
		memberBean.setName(name);
		memberBean.setPassword(password);
		memberBean.setDept(OMMEnumUtils.getDeptNameByDeptType(dept));
		memberBean.setTel(tel);
		memberBean.setEmail(email);
		memberBean.setIpAddress(ip);
		memberBean.setCreateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
		memberBean.setExpiryTime(DateUtil.getTimestamp(exptryTime, "yyyy-MM-dd"));
		memberBean.setEncryptType(encrypte);
		memberBean.setState(state);
		memberBean.setRemark(remark);
		memberBean.setExt1(ext1);
		memberBean.setExt2(ext2);
		memberBean.setExt3(ext3);
		memberSV.updateMember(memberBean);
		//保存用户关联角色信息
		//1. 删除用户原本关联用户信息
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		memberRelatRoleSV.deleteMemberRelatRoleByMemberId(id);
		//2.新增关联用户信息
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = new BOOmmMemberRelatRoleBean[roles.length];
		for(int i =0; i < roles.length; i++){
			memberRelatRoles[i] = new BOOmmMemberRelatRoleBean();
			memberRelatRoles[i].setMemberId(id);
			memberRelatRoles[i].setRoleid(roleMap.get(roles[i]));
			memberRelatRoles[i].setCreateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
			memberRelatRoles[i].setUpdateTime(DateUtil.getTimestamp(createTime, "yyyy-MM-dd"));
			memberRelatRoles[i].setExpiryTime(DateUtil.getTimestamp(exptryTime, "yyyy-MM-dd"));
		}
		memberRelatRoleSV.addMemberRelatRoles(memberRelatRoles);
	}
	
	public Map<String, String> getMemberAndRoleInfoByMemberId(String memberId)throws Exception{
		IOmmMemberSV memberSV = (IOmmMemberSV)ServiceFactory.getService(IOmmMemberSV.class);
		IBOOmmMemberValue[] members = memberSV.getMemberById1(memberId);
		if(members == null || members.length <= 0){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_NOT_FOUND_MEMBER);
		}else if(members.length >1){
			throw new OMMException(OMMExceptionEnum.ExceptionEnum.USER_FOUND_MULTI_MEMBER);
		}
		Map<String, String> memberMap = new HashMap<String, String>();
		memberMap.put("id", members[0].getId() + "");
		memberMap.put("account", members[0].getAccount());
		memberMap.put("name", members[0].getName());
		memberMap.put("tel", members[0].getTel());
		memberMap.put("email", members[0].getEmail());
		memberMap.put("ip", members[0].getIpAddress());
		memberMap.put("dept", members[0].getDept());
		memberMap.put("createTime", TimeUtils.getTimeStr(members[0].getCreateTime(), "yyyy-MM-dd"));
		memberMap.put("expiryTime", TimeUtils.getTimeStr(members[0].getExpiryTime(), "yyyy-MM-dd"));
		memberMap.put("encrypt", members[0].getEncryptType());
		memberMap.put("state", members[0].getState());
		memberMap.put("remark", members[0].getRemark());
		memberMap.put("ext1", members[0].getExt1());
		memberMap.put("ext2", members[0].getExt2());
		memberMap.put("ext3", members[0].getExt3());
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		IBOOmmMemberRelatRoleValue[] memberRelatRoles = memberRelatRoleSV.getMemberRelatRoleById(memberId);
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IBOOmmRoleInfoValue[] roles = roleInfoSV.getRoleInfoByMemberRelatRole(memberRelatRoles);
		memberMap.put("selectedRole", "");
		if(roles != null && roles.length > 0){
			StringBuffer sb = new StringBuffer("");
			for(IBOOmmRoleInfoValue role: roles){
				sb.append(role.getRoleCode()).append(";");
			}
			memberMap.put("selectedRole", sb.toString());
		}
		return memberMap;
	}
	
	public void deleteMember(String memberId)throws Exception{
		IOmmMemberSV memberSV = (IOmmMemberSV)ServiceFactory.getService(IOmmMemberSV.class);
		memberSV.delelteMember(memberId);
		//根据用户查角色
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		memberRelatRoleSV.deleteMemberRelatRoleByMemberId(memberId);
	}
	
	public void addMemberRelatRole(Map<String, String> memberRelatRole) throws Exception {
		
	}

	public List<Map<String, String>> getAllMember(int startIndex, int endIndex) throws Exception {
		return getMemberByCondition("", "", "", startIndex, endIndex);
	}


	public int getMemberCount() throws Exception {
		IOmmMemberSV sv = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		return sv.getMemberCount();
	}
	
	public int getMemberCountByMemberCode(String memberCode)throws Exception{
		IOmmMemberSV sv = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		return sv.getMemberCountByMemberCode(memberCode);
	}

	public List<Map<String, String>> getMemberByCondition(String account, String roleCode, String appName, int startIndex, int endIndex) throws Exception {
		IOmmMemberSV memberSV = (IOmmMemberSV) ServiceFactory.getService(IOmmMemberSV.class);
		IBOOmmMemberValue[] members = memberSV.getMemberByAccountCode(account);
		if(members == null || members.length == 0){
			throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MEMBER);
		}
		IOmmRoleInfoSV roleInfoSV = (IOmmRoleInfoSV)ServiceFactory.getService(IOmmRoleInfoSV.class);
		IOmmMemberRelatRoleSV memberRelatRoleSV = (IOmmMemberRelatRoleSV)ServiceFactory.getService(IOmmMemberRelatRoleSV.class);
		IOmmMenuRelatRoleSV menuRelatRoleSV = (IOmmMenuRelatRoleSV)ServiceFactory.getService(IOmmMenuRelatRoleSV.class);
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		Map<String, IBOOmmRoleInfoValue> roleMapTmp = new HashMap<String, IBOOmmRoleInfoValue>();
		Map<String, IBOOmmMemberValue> memberMapTmp = new HashMap<String, IBOOmmMemberValue>();
		TreeSet<Long> memberIdTmp = new TreeSet<Long>();
		Map<String, HashSet<String>> roleMenuMemberTmp = new HashMap<String, HashSet<String>>();
		HashSet<String> roleIdTmp = null;
		String flag = "";
		if(StringUtils.isNotBlank(roleCode) && StringUtils.isBlank(appName)){
			IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleInfoCode(roleCode);
			if(roles == null || roles.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_ROLE);
			}
			IBOOmmMemberRelatRoleValue[] memberRelatRoles =  memberRelatRoleSV.getMemberRelatRoleByRoles(roles);
			if(memberRelatRoles == null || memberRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmRoleInfoValue role: roles){
				roleMapTmp.put(String.valueOf(role.getId()), role);
			}
			for(IBOOmmMemberValue member: members){
				flag = "";
				roleIdTmp = new HashSet<String>();
				memberMapTmp.put(String.valueOf(member.getId()), member);
				for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
					if(memberRelatRole.getMemberId().equals(String.valueOf(member.getId()))){
						flag = String.valueOf(member.getId());
						roleIdTmp.add(String.valueOf(memberRelatRole.getRoleid()));
						memberIdTmp.add(member.getId());
					}else{
						break;
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
		}else if (StringUtils.isBlank(roleCode) && StringUtils.isNotBlank(appName)){
			IBOOmmMenuValue[]  menus = menuSV.getMenuByAppName(appName);
			if(menus == null || menus.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU);
			}
			for(IBOOmmMemberValue member: members){
				flag = "";
				roleIdTmp = new HashSet<String>();
				memberMapTmp.put(String.valueOf(member.getId()), member);
				IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByMemberId(String.valueOf(member.getId()));
				if(menuRelatRoles == null || menuRelatRoles.length == 0){
					continue;
				}
				for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
					for(IBOOmmMenuValue menu: menus){
						if(menuRelatRole.getMenuId().equals(String.valueOf(menu.getId()))){
							flag = String.valueOf(member.getId());
							memberIdTmp.add(member.getId());
						}else{
							break;
						}
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
		}else if(StringUtils.isNotBlank(roleCode) && StringUtils.isNotBlank(appName)){
			IBOOmmRoleInfoValue[]  roles = roleInfoSV.getRoleInfoByRoleInfoCode(roleCode);
			if(roles == null || roles.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_ROLE);
			}
			IBOOmmMemberRelatRoleValue[] memberRelatRoles =  memberRelatRoleSV.getMemberRelatRoleByRoles(roles);
			if(memberRelatRoles == null || memberRelatRoles.length ==0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_RELAT_ROLE);
			}
			for(IBOOmmRoleInfoValue role: roles){
				roleMapTmp.put(String.valueOf(role.getId()), role);
			}
			IBOOmmMenuValue[]  menus = menuSV.getMenuByAppName(appName);
			if(menus == null || menus.length == 0){
				throw new OMMException(ExceptionEnum.USER_NOT_FOUND_MENU);
			}
			for(IBOOmmMemberValue member: members){
				flag = "";
				roleIdTmp = new HashSet<String>();
				memberMapTmp.put(String.valueOf(member.getId()), member);
				IBOOmmMenuRelatRoleValue[] menuRelatRoles = menuRelatRoleSV.getMenuRelatRoleByMemberId(String.valueOf(member.getId()));
				if(menuRelatRoles == null || menuRelatRoles.length == 0){
					continue;
				}
				for(IBOOmmMemberRelatRoleValue memberRelatRole: memberRelatRoles){
					if(memberRelatRole.getMemberId().equals(String.valueOf(member.getId()))){
						for(IBOOmmMenuRelatRoleValue menuRelatRole: menuRelatRoles){
							for(IBOOmmMenuValue menu: menus){
								if(menuRelatRole.getMenuId().equals(String.valueOf(menu.getId()))){
									flag = String.valueOf(member.getId());
									memberIdTmp.add(member.getId());
									roleIdTmp.add(String.valueOf(memberRelatRole.getRoleid()));
								}else{
									break;
								}
							}
						}
					}
				}
				if(StringUtils.isNotBlank(flag)){
					roleMenuMemberTmp.put(flag, roleIdTmp);
				}
			}
			
		}else{
			for(IBOOmmMemberValue member: members){
				roleIdTmp = new HashSet<String>();
				memberMapTmp.put(String.valueOf(member.getId()), member);
				memberIdTmp.add(member.getId());
				roleMenuMemberTmp.put(String.valueOf(member.getId()), roleIdTmp);
			}
		}
		
		List<Map<String, String>> memberList = new ArrayList<Map<String,String>>();
		Map<String, String> memberMap = null;
		if(memberIdTmp != null && memberIdTmp.size() != 0){
			for(Long keyTmp: memberIdTmp){
				String key = String.valueOf(keyTmp);
				IBOOmmMemberValue member = memberMapTmp.get(key);
				memberMap = new HashMap<String, String>();
				memberMap.put("pageId", "member" + member.getId());
				memberMap.put("id", member.getId()+ "");
				memberMap.put("account", member.getAccount());
				memberMap.put("name", member.getName());
				memberMap.put("dept", member.getDept());
				memberMap.put("tel", member.getTel());
				memberMap.put("email", member.getEmail());
				memberMap.put("createTime", DateUtil.getDateFormat(member.getCreateTime(), "yyyy-MM-dd"));
				memberMap.put("expiryTime", DateUtil.getDateFormat(member.getExpiryTime(), "yyyy-MM-dd"));
				memberMap.put("encrypt", OMMEnumUtils.getEncryptByType(member.getEncryptType()));
				memberMap.put("state", OMMEnumUtils.getStateByType(member.getState()));
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
					//根据用户查角色
					IBOOmmMemberRelatRoleValue[] memberRelatRoles =  memberRelatRoleSV.getMemberRelatRoleById(String.valueOf(member.getId()));
					if(memberRelatRoles == null || memberRelatRoles.length == 0){
						roles.append("未关联角色");
					}else{
						//根据角色关联信息查角色
						IBOOmmRoleInfoValue[] roleInfos = roleInfoSV.getRoleInfoByMemberRelatRole(memberRelatRoles);
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
							roles.append("未关联角色");
						}
					}
				}
				memberMap.put("roles", roles.toString());
				memberList.add(memberMap);
			}
		}
		List<Map<String, String>> result = new ArrayList<Map<String,String>>();
		int size = memberList.size();
		if(endIndex > size){
			result =  new ArrayList<Map<String,String>>(memberList.subList(startIndex -1, memberList.size()));
		}else if(startIndex < memberList.size() && endIndex < size){
			result =  new ArrayList<Map<String,String>>(memberList.subList(startIndex -1, endIndex-1));
		}
		Map<String, String> count = new HashMap<String, String>();
		count.put("count", String.valueOf(memberList.size()));
		result.add(count);
		return result;
	}
	
	public static void main(String[] args) throws Exception {
//		IMemberSV sv = (IMemberSV) ServiceFactory.getService(IMemberSV.class);
//		String account = "";
//		String roleCode = "";
//		String appName = "";
//		System.out.println(JSONObject.toJSONString(sv.getMemberByCondition(account, roleCode, appName, 1, 21)));
	}
}
