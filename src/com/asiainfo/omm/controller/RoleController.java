package com.asiainfo.omm.controller;

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
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IRoleSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

/**
 * 角色控制器
 * 
 * @author oswin
 *
 */
@Controller
@RequestMapping("/role")
public class RoleController {

	private static final Logger logger = Logger.getLogger(RoleController.class);
	
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
		logger.debug("OMM平台本次请求角色配置应用主页 ");
		try {
			HttpSession session = request.getSession(Boolean.TRUE);
			IRoleSV roleSV = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
			int nowPage = 1;
			int pageNumber = 20;
			int startIndex = (nowPage - 1)*pageNumber +1;
			int endIndex = startIndex + pageNumber;
			int roleCount = roleSV.getRoleInfoCount();
			List<Map<String, String>> roles = roleSV.getAllRoleInfo(startIndex, endIndex);
			int totalPage = roleCount/pageNumber + 1;
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			model.addAttribute("limitRank", menuAuth.get("role"));
			model.addAttribute("roleCount", roleCount);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("nowPage", nowPage);
			model.addAttribute("roles", roles.subList(0, roles.size() - 1));
		} catch (OMMException e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, e.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			model.addAttribute(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			model.addAttribute(OMMConstantEnum.OMM_MSG, "角色查询失败:" + ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
		}
		return "apply/role";
	}
	
	/**
	 * 删除用户
	 * 
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
	public @ResponseBody JSONObject delete(@RequestParam("id") String id, HttpServletRequest request){
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
		if(!menuAuth.get("role").contains("DELETE")){
			json.put("code", "99999");
			json.put("msg", "登录用户无删除角色的权限!");
			return json;
		}
		logger.debug("OMM平台本次请求删除角色: id: " + id + "");
		IRoleSV roleSV = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
		try {
			roleSV.deleteRoleInfo(id);
			json.put("code", "00000");
			json.put("msg", "角色[" + id + "]删除成功");
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put("code", "99999");
			json.put("msg", "角色[" + id +"]删除失败:" + e.getMessage());
			logger.debug("OMM平台本次请求删除角色: " + id + "失败, 错误原因为:");
			e.printStackTrace();
		}
		return json;
	}
	
	@RequestMapping(value = "/selectRole", method = RequestMethod.POST)
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
			IRoleSV roleSV = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
			List<Map<String, String>> roles = roleSV.getRoleInfoByCondition(account, roleCode, appName, startIndex, endIndex);
			int roleCount = Integer.valueOf(roles.get(roles.size() - 1).get("count"));
			int totalPage = roleCount/pageNumber + 1;
			json.put(OMMConstantEnum.OMM_CODE, "00000");
			json.put(OMMConstantEnum.OMM_MSG, "角色查询成功");
			json.put("roleCount", roleCount);
			json.put("pageNumber", pageNumber);
			json.put("totalPage", totalPage);
			json.put("nowPage", nowPage);
			json.put("result", roles.subList(0, roles.size() - 1));
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put(OMMConstantEnum.OMM_CODE, "99999");
			json.put(OMMConstantEnum.OMM_MSG, "角色查询失败:" + e.getMessage());
			logger.debug("OMM平台本次请求查询角色失败, 错误原因为:");
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 角色新增, 修改页面
	 * 
	 * @param account
	 * @param roleCode
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/rolePage", method = RequestMethod.POST)
	public String addPage(@RequestParam("type") String type, @RequestParam("index") String index, Model model, HttpServletRequest request){
		try {
			HttpSession session = request.getSession();
			logger.debug("OMM平台本次请求角色配置角色新增/修改页面 " + session.getId() + "; type: " + type + "; index: " + index);
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			if(!menuAuth.get("role").contains("INSERT") && !menuAuth.get("role").contains("UPDATE")){
				return "unauthoriz";
			}
			
//			角色信息, 角色关联应用信息
			if("add".equalsIgnoreCase(type)){
				model.addAttribute("selectedState", OMMConstantEnum.STATE.U.getState());
			} else if ("update".equalsIgnoreCase(type)){
				IRoleSV roleSV = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
				Map<String, String> roleMap = roleSV.getRoleInfoByRoleId(index);
				for(String key: roleMap.keySet()){
					model.addAttribute(key, roleMap.get(key));
				}
			}else{
				model.addAttribute(OMMConstantEnum.OMM_CODE, "99999");
				model.addAttribute(OMMConstantEnum.OMM_MSG, "操作类型不正确!");
			}
			model.addAttribute("states", OMMEnumUtils.getStateInfo());
			model.addAttribute("menus", "");
			model.addAttribute("type", type.toUpperCase());
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
		return "apply/plugin/roleplugin";
	}
}
