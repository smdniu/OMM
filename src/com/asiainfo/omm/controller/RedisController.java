package com.asiainfo.omm.controller;

import java.util.HashMap;
import java.util.List;
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
import com.asiainfo.omm.app.redis.service.interfaces.IRedisSV;
import com.asiainfo.omm.app.redis.utils.FastJsonUtil;
import com.asiainfo.omm.app.redis.utils.ServersUtil;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.service.interfaces.IMemcacheSV;
import com.asiainfo.omm.utils.SessionFactory;

/**
 * redis控制器
 * 
 * @author oswin
 *
 */
@Controller
@RequestMapping("/redis")
public class RedisController {

	private static final Logger logger = Logger.getLogger(RedisController.class);
	IRedisSV redisSV = (IRedisSV) ServiceFactory.getService(IRedisSV.class);
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
		logger.debug("OMM平台本次请求redis应用主页 ");
		model.addAttribute("info", redisSV.getInfos());
		return "apply/redis";
	}
	
	@RequestMapping(value = "/info",method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public JSONObject info(){
		JSONObject json = new JSONObject();
		try {
			Map<String,Map> infos = redisSV.getInfos();
			for (String key : infos.keySet()) {
				json.put(key, infos.get(key));
			}
		} catch (Exception e) {
		}
		return json;
	}
	
	//查询某个连接详情
	@RequestMapping(value = "/infoDetil",method = RequestMethod.POST,produces="text/html;charset=UTF-8")
	@ResponseBody
	public JSONObject infoDetil(String ip,int port){
		JSONObject json = new JSONObject();
		try {
			Map<String, String> infoMap = redisSV.getInfo(ip, port);
			for (String key : infoMap.keySet()) {
				json.put(key, infoMap.get(key));
			}
		} catch (Exception e) {
		}
		return json;
	}
	
	//根据key查询value
	@RequestMapping(value = "/getValue",method={RequestMethod.GET,RequestMethod.POST},produces="text/html;charset=UTF-8")
	@ResponseBody
	public JSONObject getValue(String ip,int port,String key) throws Exception{
		JSONObject json = new JSONObject();
		try{
			json.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
			json.put(OMMConstantEnum.RESULT_MSG, redisSV.getValue(ip, port, key));
		}catch(Exception e){
			json.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			json.put(OMMConstantEnum.RESULT_MSG, e.getMessage());
		}
		return json;
	}
	
	
	//异步请求所有的ip:port
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/getServers",method = RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public JSONObject getServers(){
		JSONObject json = new JSONObject();
		try {
			List<String> servers = (List<String>) SessionFactory.getSession().getAttribute(OMMConstantEnum.OMM_MENU_REDIS_KEY);
			String server = servers.toString();
			server = server.substring(1,server.length()-1);
			json.put("server", server);
		} catch (Exception e) {
		}
		return json;
	}
	
	
	/**
	 * 新增redis配置信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addRedisConfig",method = RequestMethod.POST)
	public @ResponseBody JSONObject addRedisConfig(@RequestParam("redisInfo")String[] redisIpPorts){
		logger.info("OMM平台新增Redis配置信息");
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		if(redisIpPorts != null && redisIpPorts.length >0){
			com.asiainfo.omm.service.interfaces.IRedisSV redisSV = (com.asiainfo.omm.service.interfaces.IRedisSV)ServiceFactory.getService(com.asiainfo.omm.service.interfaces.IRedisSV.class);
			try {
				redisSV.addRedisConfig(redisIpPorts);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("OMM平台本次请求异常: " + e);
				json.put(OMMConstantEnum.OMM_CODE, ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
				json.put(OMMConstantEnum.OMM_MSG, ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
			}
		}
		return json;
	}
	
	public static void main(String[] args) {
//		List list = new ArrayList();
//		A a = new A(1,"孙梦迪");
//		A a1 = new A(2,"孙心雨");
//		list.add(a);
//		list.add(a1);
//		System.out.println(FastJsonUtil.toJSONString(list));
//		
		List servers = ServersUtil.servers;
		String s = servers.toString();
		s = s.substring(1, s.length()-1);
		System.out.println(s);
		
	}
	
}


class A{
	private int id;
	private String name;
	public A() {
		// TODO Auto-generated constructor stub
	}
	
	public A(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}

