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
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.IMemberSV;
import com.asiainfo.omm.service.interfaces.IRoleSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

/**
 * 用户控制器
 * 
 * @author oswin
 */
@Controller
@RequestMapping("/member")
public class MemberController {
	
	private static final Logger logger = Logger.getLogger(MemberController.class);

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
			logger.debug("OMM平台本次请求用户配置应用主页 " + session.getId());
			IMemberSV memberSV = (IMemberSV)ServiceFactory.getService(IMemberSV.class);
			int nowPage = 1;
			int pageNumber = 20;
			int startIndex = (nowPage - 1)*pageNumber +1;
			int endIndex = startIndex + pageNumber;
			int memberCount = memberSV.getMemberCount();
			List<Map<String, String>> members = memberSV.getAllMember(startIndex, endIndex);
			int totalPage = memberCount/pageNumber + 1;
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			model.addAttribute("limitRank", menuAuth.get("member"));
			model.addAttribute("memberCount", memberCount);
			model.addAttribute("pageNumber", pageNumber);
			model.addAttribute("totalPage", totalPage);
			model.addAttribute("nowPage", nowPage);
			model.addAttribute("members", members.subList(0, members.size() - 1));
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
		return "apply/member";
	}
	
	/**
	 * 删除用户
	 * 
	 * @return 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/deleteMem", method = RequestMethod.POST)
	public @ResponseBody JSONObject delete(@RequestParam("id") String id, HttpServletRequest request){
		JSONObject json = new JSONObject();
		HttpSession session = request.getSession();
		Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
		if(!menuAuth.get("member").contains("DELETE")){
			json.put("code", "99999");
			json.put("msg", "登录用户无删除用户的权限!");
			return json;
		}
		IBOOmmMemberValue member = (IBOOmmMemberValue) session.getAttribute(OMMConstantEnum.OMM_MEMBER_KEY);
		logger.debug("OMM平台本次请求删除用户: id: " + id + "");
		if(id != null && id.trim().equalsIgnoreCase(String.valueOf(member.getId()))){
			json.put("code", "99999");
			json.put("msg", "请不要删除自己,否则将无法登陆系统");
			return json;
		} else {
			IMemberSV memberSV = (IMemberSV)ServiceFactory.getService(IMemberSV.class);
			try {
				memberSV.deleteMember(id);
				json.put("code", "00000");
				json.put("msg", "用户[" + member.getName() +"]删除成功");
			}catch (OMMException e) {
				json.put(OMMConstantEnum.OMM_CODE, e.getCode());
				json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
				logger.error("OMM平台本次请求异常: " + e);
				e.printStackTrace();
			} catch (Exception e) {
				json.put("code", "99999");
				json.put("msg", "用户[" + member.getName() +"]删除失败:" + e.getMessage());
				logger.debug("OMM平台本次请求删除用户: " + member.getName() + "失败, 错误原因为:");
				e.printStackTrace();
			}
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/selectMem", method = RequestMethod.POST)
	public @ResponseBody JSONObject select(@RequestParam("account") String account, @RequestParam("roleCode") String roleCode, @RequestParam("appName") String appName, HttpServletRequest request){
		HttpSession session = request.getSession(Boolean.TRUE);
		logger.debug("OMM平台本次请求用户配置搜索功能 " + session.getId());
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
			IMemberSV memberSV = (IMemberSV)ServiceFactory.getService(IMemberSV.class);
			List<Map<String, String>> members = memberSV.getMemberByCondition(account, roleCode, appName, startIndex, endIndex);
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			int memberCount = Integer.valueOf(members.get(members.size() - 1).get("count"));
			int totalPage = memberCount/pageNumber + 1;
			json.put("limitRank", menuAuth.get("member"));
			json.put("memberCount", memberCount);
			json.put("pageNumber", pageNumber);
			json.put("totalPage", totalPage);
			json.put("nowPage", nowPage);
			json.put("result", members.subList(0, members.size() - 1));
			json.put(OMMConstantEnum.OMM_CODE, "00000");
			json.put(OMMConstantEnum.OMM_MSG, "用户[" + account +"]查询成功");
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put(OMMConstantEnum.OMM_CODE, "99999");
			json.put(OMMConstantEnum.OMM_MSG, "用户[" + account +"]查询失败:" + e.getMessage());
			logger.debug("OMM平台本次请求删除用户: " + account + "失败, 错误原因为:");
			e.printStackTrace();
		}
		System.out.println("json:" + json);
		return json;
	}
	
	/**
	 * 用户新增, 修改页面
	 * 
	 * @param account
	 * @param roleCode
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/merberPage", method = RequestMethod.POST)
	public String addPage(@RequestParam("type") String type, @RequestParam("index") String index, Model model, HttpServletRequest request){
		try {
			HttpSession session = request.getSession();
			logger.debug("OMM平台本次请求用户配置用户新增/修改页面 " + session.getId() + "; type: " + type + "; index: " + index);
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			if(!menuAuth.get("member").contains("INSERT") && !menuAuth.get("member").contains("UPDATE")){
				return "unauthoriz";
			}
			IRoleSV roleSV = (IRoleSV)ServiceFactory.getService(IRoleSV.class);
//			部门.加密方式,状态,用户角色
			if("add".equalsIgnoreCase(type)){
				model.addAttribute("selectedEncrypt", OMMConstantEnum.ENCRYPTTYPE.NOT.getEncryptType());
				model.addAttribute("selectedState", OMMConstantEnum.STATE.U.getState());
				model.addAttribute("selectedDept", "");
				model.addAttribute("selectedRole", "");
			} else if ("update".equalsIgnoreCase(type)){
				IMemberSV memberSV = (IMemberSV)ServiceFactory.getService(IMemberSV.class);
				Map<String, String> memberRole = memberSV.getMemberAndRoleInfoByMemberId(index);
				for(String key: memberRole.keySet()){
					model.addAttribute(key, memberRole.get(key));
				}
				model.addAttribute("selectedEncrypt", memberRole.get("encrypt"));
				model.addAttribute("selectedState", memberRole.get("state"));
				model.addAttribute("selectedDept", memberRole.get("dept"));
				model.addAttribute("selectedRole", memberRole.get("selectedRole"));
			}else{
				model.addAttribute(OMMConstantEnum.OMM_CODE, "99999");
				model.addAttribute(OMMConstantEnum.OMM_MSG, "操作类型不正确!");
			}
			model.addAttribute("encrypts", OMMEnumUtils.getEncryptInfo());
			model.addAttribute("states", OMMEnumUtils.getStateInfo());
			model.addAttribute("roles", roleSV.getAllRoleInfo());
			model.addAttribute("depts", OMMEnumUtils.getDeptInfo());
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
		return "apply/plugin/memberplugin";
	}
	
	/**
	 * 新增或修改用户信息
	 * 
	 * @param id
	 * @param account
	 * @param name
	 * @param password
	 * @param repassword
	 * @param dept
	 * @param tel
	 * @param email
	 * @param ip
	 * @param createTime
	 * @param exptryTime
	 * @param encrypte
	 * @param state
	 * @param remark
	 * @param ext1
	 * @param ext2
	 * @param ext3
	 * @param roles
	 * @param model
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/addMember",  method = RequestMethod.POST)
	public @ResponseBody JSONObject addMember(@RequestParam("TYPE") String type,@RequestParam("ID") String id, @RequestParam("ACCOUNT") String account, @RequestParam("NAME") String name, @RequestParam("PASSWORD") String password, @RequestParam("REPASSWORD") String repassword, @RequestParam("DEPT") String dept, @RequestParam("TEL") String tel, @RequestParam("EMAIL") String email, @RequestParam("IP") String ip, @RequestParam("CREATETIME") String createTime, @RequestParam("EXPIRYTIME") String exptryTime, @RequestParam("ENCRYPTE") String encrypte, @RequestParam("STATE") String state, @RequestParam("REMARK") String remark, @RequestParam("EXT1") String ext1, @RequestParam("EXT2") String ext2, @RequestParam("EXT3") String ext3, @RequestParam("ROLE") String[] roles , Model model, HttpServletRequest request){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		try {
			HttpSession session = request.getSession();
			logger.debug("OMM平台本次请求用户新增/修改 " + session.getId() + "; type:" + type + "; id:" + id);
			Map<String, String> menuAuth = (Map<String, String>) session.getAttribute(OMMConstantEnum.OMM_MENU_AUTH_KEY);
			if(!menuAuth.get("member").contains("INSERT") && !menuAuth.get("member").contains("UPDATE")){
				json.put(OMMConstantEnum.OMM_CODE, "99999");
				json.put(OMMConstantEnum.OMM_MSG, "登录用户无新增/修改用户的权限!");
			}
			if(StringUtils.isBlank(password) || !password.equals(repassword)){
				json.put(OMMConstantEnum.OMM_CODE, "00001");
				json.put(OMMConstantEnum.OMM_MSG, "两次输入密码不一致");
			}else{
				IMemberSV memberSV = (IMemberSV)ServiceFactory.getService(IMemberSV.class);
				if("add".equalsIgnoreCase(type)){
					memberSV.addMember(account, name, password, dept, tel, email, ip, createTime, exptryTime, encrypte, state, remark, ext1, ext2, ext3, roles);
				}else if ("update".equalsIgnoreCase(type)){
					memberSV.updateMember(id, account, name, password, dept, tel, email, ip, createTime, exptryTime, encrypte, state, remark, ext1, ext2, ext3, roles);
				}else{
					json.put(OMMConstantEnum.OMM_CODE, "99999");
					json.put(OMMConstantEnum.OMM_MSG, "操作类型不正确");
				}
			}
		}catch (OMMException e) {
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
			logger.error("OMM平台本次请求异常: " + e);
			e.printStackTrace();
		} catch (Exception e) {
			json.put(OMMConstantEnum.OMM_CODE, "99999");
			json.put(OMMConstantEnum.OMM_MSG, "用户[" + account +"]查询失败:" + e.getMessage());
			logger.debug("OMM平台本次请求删除用户: " + account + "失败, 错误原因为:");
			e.printStackTrace();
		}
		return json;
	}
}
