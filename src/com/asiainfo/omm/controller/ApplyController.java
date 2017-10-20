package com.asiainfo.omm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.appframe2.service.ServiceFactory;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IApplySV;
import com.asiainfo.omm.service.interfaces.IMemberSV;
import com.asiainfo.omm.service.interfaces.IMenuSV;
import com.asiainfo.omm.service.interfaces.IRoleSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

/**
 * 应用控制器
 * 
 * @author oswin
 */
@Controller
@RequestMapping("/apply")
public class ApplyController {
	
	private static final Logger logger = Logger.getLogger(ApplyController.class);
	
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
			HttpSession session = request.getSession(Boolean.TRUE);
			logger.debug("OMM平台本次请求应用配置主页 " + session.getId());
			IApplySV applySV = (IApplySV)ServiceFactory.getService(IApplySV.class);
			int nowPage = 1;
			int pageNumber = 20;
			int startIndex = (nowPage - 1)*pageNumber +1;
			int endIndex = startIndex + pageNumber;
			int applyCount = applySV.getMenuCount();
			List<Map<String, String>> applys = applySV.getAllMenu(startIndex, endIndex);
			int totalPage = applyCount/pageNumber + 1;
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			model.addAttribute("limitRank", menuAuth.get("apply"));
			model.addAttribute("applyCount", applyCount);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("nowPage", nowPage);
			model.addAttribute("applys", applys.subList(0, applys.size() - 1));
		} catch (OMMException e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, e.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, "应用查询失败:" + ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
		}
		return "apply/apply";
	}
	
	/**
	 * 删除用户
	 * 
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteApply", method = RequestMethod.POST)
	public @ResponseBody JSONObject delete(@RequestParam("id") String id, HttpServletRequest request){
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
		if(!menuAuth.get("apply").contains("DELETE")){
			json.put("code", "99999");
			json.put("msg", "登录用户无删除应用的权限!");
			return json;
		}
		logger.debug("OMM平台本次请求删除用户: id: " + id + "");
		IApplySV applySV = (IApplySV)ServiceFactory.getService(IApplySV.class);
		try {
			applySV.deleteMenu(id);
			json.put("code", "00000");
			json.put("msg", "应用[" + id +"]删除成功");
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put("code", "99999");
			json.put("msg", "应用[" + id +"]删除失败:" + e.getMessage());
			logger.debug("OMM平台本次请求删除应用: " + id + "失败, 错误原因为:");
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value = "/selectApply", method = RequestMethod.POST)
	public @ResponseBody JSONObject select(@RequestParam("account") String account, @RequestParam("roleCode") String roleCode, @RequestParam("appName") String appName, HttpServletRequest request){
		JSONObject json = new JSONObject();
		try {
			String nowPageStr = request.getParameter("nowPage");
			int nowPage = 1;
			if(StringUtils.isNotBlank(nowPageStr) && !"0".equals(nowPageStr)){
				try {
					nowPage = Integer.parseInt(nowPageStr);
					if(nowPage < 0 ){
						nowPage = 1;
					}
				} catch (Exception e) {
					nowPage = 1;
				}
			}
			int pageNumber = 20;
			int startIndex = (nowPage - 1) * pageNumber + 1;
			int endIndex = startIndex + pageNumber;
			IApplySV applySV = (IApplySV)ServiceFactory.getService(IApplySV.class);
			List<Map<String, String>> applys = applySV.getMenuByCondition(account, roleCode, appName, startIndex, endIndex);
			int applyCount = Integer.valueOf(applys.get(applys.size() - 1).get("count"));
			int totalPage = applyCount/pageNumber + 1;
			json.put(OMMConstantEnum.OMM_CODE, "00000");
			json.put(OMMConstantEnum.OMM_MSG, "应用查询成功");
			json.put("applyCount", applyCount);
			json.put("pageNumber", pageNumber);
			json.put("totalPage", totalPage);
			json.put("nowPage", nowPage);
			json.put("result", applys.subList(0, applys.size() - 1));
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put(OMMConstantEnum.OMM_CODE, "99999");
			json.put(OMMConstantEnum.OMM_MSG, "应用查询失败:" + e.getMessage());
			logger.debug("OMM平台本次请求查询应用失败, 错误原因为:");
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 应用新增, 修改页面
	 * 
	 * @param account
	 * @param roleCode
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/applyPage", method = RequestMethod.POST)
	public String addPage(@RequestParam("type") String type, @RequestParam("index") String index, @RequestParam("menuType") String menuType, Model model, HttpServletRequest request){
		try {
			HttpSession session = request.getSession();
			logger.debug("OMM平台本次请求应用配置应用新增/修改页面 " + session.getId() + "; type: " + type + "; index: " + index);
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			if(!menuAuth.get("apply").contains("INSERT") && !menuAuth.get("apply").contains("UPDATE")){
				return "unauthoriz";
			}
			IMenuSV menuSV = (IMenuSV)ServiceFactory.getService(IMenuSV.class);
			if("add".equalsIgnoreCase(type)){
				Map<String, Object> menus = menuSV.getAllMenus();
				for(String key: menus.keySet()){
					model.addAttribute(key, menus.get(key));
				}
			} else if ("update".equalsIgnoreCase(type)){
				Map<String, Object> menus = menuSV.getMenusById(index);
				for(String key: menus.keySet()){
					model.addAttribute(key, menus.get(key));
				}
			}else{
				model.addAttribute(OMMConstantEnum.OMM_CODE, "99999");
				model.addAttribute(OMMConstantEnum.OMM_MSG, "操作类型不正确!");
			}
			model.addAttribute("type", type.toUpperCase());
			model.addAttribute("menuType", menuType);
			model.addAttribute("menuId", index);
			logger.info("OMM平台本次请求 model:" + JSONObject.toJSONString(model));
		} catch (OMMException e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, e.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
		}
		return "apply/plugin/applyplugin";
	}
	
	/**
	 * 新增db配置信息
	 * 
	 * @return
	 */
	public @ResponseBody JSONObject addApplyConfig(){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		return json;
	}
}
