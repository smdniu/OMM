package com.asiainfo.omm.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.memchace.MemcacheTool;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.bo.UserInfoBean;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IUserServiceSV;

/**
 * 用户登录处理
 * 
 */
@Controller
@RequestMapping("/user")
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	/**
	 * 登录操作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 跳转页面
	 * @throws Exception
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletRequest request, Model model){
		try {
			logger.debug("OMM平台登录用户: " + username + "; 密码: " + password);
			if(username == null || "".equals(username.trim()) || "NULL".equalsIgnoreCase(username.trim()) ||
				password == null || "".equals(password.trim()) || "NULL".equalsIgnoreCase(password.trim())){
				logger.debug("OMM平台登录用户名/密码不能为空.");
				throw new OMMException(ExceptionEnum.OMM_USERNAME_OR_PASSWORD_IS_NULL);
			}
			IUserServiceSV memberSV = (IUserServiceSV)ServiceFactory.getService(IUserServiceSV.class);
			IBOOmmMemberValue member = memberSV.getMemberByAccount(username.trim());
			memberSV.checkPassword(password, member);
			UserInfoBean userInfo = memberSV.getMenuByMemberInfo(member);
			HttpSession session = request.getSession(Boolean.TRUE);
			logger.error("OMM平台登录请求新建session: " + session.getId());
			session.setAttribute(OMMConstantEnum.OMM_USERNAME_KEY, member.getName()+ "(账号" + member.getAccount() + ")");
			session.setAttribute(OMMConstantEnum.OMM_MEMBER_KEY, userInfo.getMember());
			session.setAttribute(OMMConstantEnum.OMM_MENU_KEY, userInfo.getMenusMap());
			session.setAttribute(OMMConstantEnum.OMM_ROLE_KEY, userInfo.getRoleInfos());
			session.setAttribute(OMMConstantEnum.OMM_MEMBER_RELAT_ROLE_KEY, userInfo.getMemberRelatRoles());
			session.setAttribute(OMMConstantEnum.OMM_MENU_RELAT_ROLE_KEY, userInfo.getMenuRelatRoles());
			session.setAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY, userInfo.getMenuAuth());
			session.setAttribute(OMMConstantEnum.OMM_MENU_DB_KEY, userInfo.getMenuDB());
			session.setAttribute(OMMConstantEnum.OMM_MENU_MEMCACHE_KEY, userInfo.getMenuMemcache());
			session.setAttribute(OMMConstantEnum.OMM_MENU_SINGLEPOINT_KEY, userInfo.getSinglepointAuth());
			session.setAttribute(OMMConstantEnum.OMM_MENU_REDIS_KEY, userInfo.getRedisAuth());
			
			//添加memcache主机到监控
			MemcacheTool.addServers(userInfo.getMenuMemcache());
			
			return "redirect:index";
		} catch (OMMException e) {
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
			model.addAttribute(OMMConstantEnum.OMM_CODE, e.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, e.getMessage());
			return "login";
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
			return "login";
		}
	}
	
	/**
	 * 主页面
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 跳转页面
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model){
		try {
			logger.debug("OMM平台用户本次请求主页面");
			HttpSession session = request.getSession(Boolean.TRUE);
			Map<String, IBOOmmMenuValue> menusMap = (Map<String, IBOOmmMenuValue>) session.getAttribute(OMMConstantEnum.OMM_MENU_KEY);
			if(menusMap == null){
				throw new OMMException(ExceptionEnum.OMM_SYSTEM_NOT_FOUND_INDEXMENU);
			}
			List<Map<String, Object>> menus = new ArrayList<Map<String,Object>>();
			Map<String, Object> tmpMenu = null; 
			IBOOmmMenuValue tmpMenuValue = null;
			
			for(String key: menusMap.keySet()){
				tmpMenuValue = menusMap.get(key);
				tmpMenu = new HashMap<String, Object>();
				/**
				 * chenggz
				 * menucode==8是单点登陆
				 * 获取页面上下拉列表中的username
				 */
			
				if(tmpMenuValue.getMenuType().equals("8")){
					List<String> usernameList = new ArrayList<String>();//保存下拉列表中的username
					List<Map> resultList = (List<Map>) session.getAttribute(OMMConstantEnum.OMM_MENU_SINGLEPOINT_KEY);
					for(Map imap:resultList){
						if(imap.get("code").equals(tmpMenuValue.getMenuCode())){
							List<Map<String,Object>> accPwdListTmp = (List<Map<String,Object>>)imap.get("accPwd");
							for(Map jmap:accPwdListTmp){
								String username = (String)jmap.get("username");
								String password = (String)jmap.get("password");
								usernameList.add(username);
							}
						}
						tmpMenu.put("usernameList", usernameList);
					}
//					model.addAttribute("usernameList",usernameList);
				}
				
				if(tmpMenuValue != null){
					
					tmpMenu.put("menuType", tmpMenuValue.getMenuType());
					tmpMenu.put("path", tmpMenuValue.getPath());
					tmpMenu.put("name", tmpMenuValue.getName());
					menus.add(tmpMenu);
				}
			}
			if(menus.size() == 0){
				throw new OMMException(ExceptionEnum.OMM_SYSTEM_NOT_FOUND_INDEXMENU);
			}
			model.addAttribute("menus", menus);
		} catch (OMMException e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, e.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, e.getMessage());
		} catch (Exception e) {
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
			model.addAttribute(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
		}
		return "index";
	}
	
	/**
	 * 登陆退出处理方法
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 返回退出跳转的页面
	 * @throws Exception
	 */
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request){
		HttpSession session = request.getSession(Boolean.FALSE);
		if(session != null){
			session.setAttribute("STATE", "logout");
			session.invalidate();
		}
		return "login";
	}
	
	/**
	 * 越权访问应用
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return 返回退出跳转的页面
	 * @throws Exception
	 */
	@RequestMapping(value = "/unauthorized", method = RequestMethod.GET)
	public String unauthorized(HttpServletRequest request){
		logger.error("OMM平台本次请求越权应用:" + request.getRequestURI());
		return "unauthoriz";
	}

	

	/**
	 * 修改密码操作
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/modifyPassword", method = RequestMethod.POST)
	public String modifyPassword(HttpServletRequest request, HttpServletResponse response){
		HttpSession session = request.getSession(Boolean.TRUE);
		if(session != null){
			logger.debug("OMM平台登录用户: " + session.getAttribute(OMMConstantEnum.OMM_USERNAME_KEY) + "修改密码, 退出系统!");
			session.invalidate();
		}
		return "login";
	}
}