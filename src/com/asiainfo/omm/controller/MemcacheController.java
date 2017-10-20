package com.asiainfo.omm.controller;

import java.util.HashMap;
import java.util.Map;

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
import com.asiainfo.omm.app.memchace.MemcacheTool;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.service.interfaces.IMemcacheSV;

/**
 * memcache控制器
 * 
 * @author oswin
 *
 */
@Controller
@RequestMapping("/memcache")
public class MemcacheController {

	private static final Logger logger = Logger.getLogger(MemcacheController.class);
	
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
		logger.debug("OMM平台本次请求memcache应用主页 ");
		model.addAttribute("info", MemcacheTool.getAllStats());
		return "apply/memcache";
	}
	
	@RequestMapping(value = "/info",method = RequestMethod.GET)
	@ResponseBody
	public Map info(){
		try {
			return MemcacheTool.getAllStats();
		} catch (Exception e) {
		}
		return new HashMap();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getValue",method = RequestMethod.POST)
	@ResponseBody
	public Map getValue(String ip,int port,String key){
		Map res = new HashMap();
		res.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
		try{
			res.put(OMMConstantEnum.RESULT_MSG, MemcacheTool.getValue(ip, port, key));
		}catch(Exception e){
			res.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			res.put(OMMConstantEnum.RESULT_MSG, e.getMessage());
		}
		return res;
	}
	
	/**
	 * 新增Memcahce配置信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addMemcahceConfig",method = RequestMethod.POST)
	public @ResponseBody JSONObject addMemcahceConfig(@RequestParam("memchaceInfo")String[] memcacheIpPorts){
		logger.info("OMM平台新增memcache配置信息");
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		if(memcacheIpPorts != null && memcacheIpPorts.length >0){
			IMemcacheSV memcacheSV = (IMemcacheSV)ServiceFactory.getService(IMemcacheSV.class);
			try {
				memcacheSV.addMemcahceConfig(memcacheIpPorts);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("OMM平台本次请求异常: " + e);
				json.put(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
				json.put(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
			}
		}
		return json;
	}
}
