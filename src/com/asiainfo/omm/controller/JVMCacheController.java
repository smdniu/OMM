package com.asiainfo.omm.controller;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

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
import com.asiainfo.omm.service.interfaces.IJVMCacheSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.StringUtils;

/**
 * APPframe缓存查询
 * 
 * @author oswin
 *
 */
@Controller
@RequestMapping("/jvmCache")
public class JVMCacheController {
	
	private static final Logger logger = Logger.getLogger(JVMCacheController.class);

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
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model){
		logger.debug("OMM平台本次请求APPframe缓存查询应用主页 ");
		List<Map<String, String>> groups = OMMEnumUtils.getAllGroups();
		model.addAttribute(MENUENUM.JVMCACHE.getMenu(), groups);
		Map<String, String> ipPortMap = null;
		if(groups != null && groups.size() > 0){
			ipPortMap = groups.get(0);
			Map<String, Object>  jvmcacheIpPort = OMMEnumUtils.getJVMCacheIpPortInfoByGroupCode(ipPortMap.get("code"));
			if(jvmcacheIpPort != null){
				List<Map<String, String>> ipPortList = (List<Map<String, String>>) jvmcacheIpPort.get("ipPortList");
				TreeSet<String> ipPortSet = new TreeSet<String>();
				for(Map<String, String> ipPort: ipPortList){
					ipPortSet.add(ipPort.get("ip") + ":" + ipPort.get("port"));
				}
				model.addAttribute("jvmCacheIpPorts", ipPortSet);
			}
		}
		
		return "apply/jvmcache";
	}
	
	/**
	 * 根据分组code获取jvmIpPort信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/getJVMCacheInfoByGroupId", method = RequestMethod.POST)
	public @ResponseBody JSONObject getJVMCacheInfoByGroupId(@RequestParam("groupCode") String groupCode){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		if(StringUtils.isBlank(groupCode)){
			json.put(OMMConstantEnum.OMM_CODE, "99999");
			json.put(OMMConstantEnum.OMM_MSG, "未选中分组!");
		} else {
			json.put("result", OMMEnumUtils.getJVMCacheIpPortInfoByGroupCode(groupCode));
		}
		logger.info("OMM平台本次请求根据分组code获取jvmIpPort信息: " + json);
		return json;
	}
	
	/**
	 * 根据分组code获取jvmIpPort信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addJVMCacheGroup", method = RequestMethod.POST)
	public @ResponseBody JSONObject addJVMCacheGroup(@RequestParam("CODE") String code, @RequestParam("NAME") String name, @RequestParam("CLASSNAME") String className, @RequestParam("IPPORT") String ipPort){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		try {
			if(StringUtils.isBlank(code)){
				json.put(OMMConstantEnum.OMM_CODE, "99999");
				json.put(OMMConstantEnum.OMM_MSG, "分组code不能为空!");
			} else if(StringUtils.isBlank(name)){
				json.put(OMMConstantEnum.OMM_CODE, "99999");
				json.put(OMMConstantEnum.OMM_MSG, "分组名不能为空!");
			}else if(StringUtils.isBlank(className)){
				json.put(OMMConstantEnum.OMM_CODE, "99999");
				json.put(OMMConstantEnum.OMM_MSG, "分组名处理类不正确!");
			}else if(StringUtils.isBlank(ipPort)){
				json.put(OMMConstantEnum.OMM_CODE, "99999");
				json.put(OMMConstantEnum.OMM_MSG, "分组ipPort不能为空!");
			}else {
				IJVMCacheSV JVMCacheSV = (IJVMCacheSV)ServiceFactory.getService(IJVMCacheSV.class);
				JVMCacheSV.addJVMCacheGroup(code, name, className, ipPort);
				json.put("result", OMMEnumUtils.getJVMCacheIpPortInfoByGroupCode(code));
			}
		} catch (OMMException e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			json.put(OMMConstantEnum.OMM_CODE, e.getCode());
			json.put(OMMConstantEnum.OMM_MSG, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("OMM平台本次请求异常: " + e);
			json.put(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
			json.put(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
		}
		logger.info("OMM平台本次请求新增jvmIpPort信息: " + json);
		return json;
	}
	
	/**
	 * 新增JVMCache配置信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addJVMCacheConfig", method = RequestMethod.POST)
	public @ResponseBody JSONObject addJVMCacheConfig(){
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		
		return json;
	}
}
