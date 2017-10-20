package com.asiainfo.omm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.log.ivalues.IBOOmmLogValue;
import com.asiainfo.omm.app.log.service.interfaces.IOMMLogSV;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.pce.util.json.source.JSON;

/**
 * 日志控制器
 * 
 * @author oswin
 */
@Controller
@RequestMapping("/log")
public class LogController {
	
	private static final Logger logger = Logger.getLogger(LogController.class);

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
		logger.debug("OMM平台本次请求日志应用主页 ");
		return "apply/log";
	}
	
	@RequestMapping(value = "/info", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> info(String account, String startTime, String endTime,int startIndex,int endIndex){
		Map<String,Object> res = new HashMap<String,Object>();
		res.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
		res.put(OMMConstantEnum.RESULT_MSG, "查询成功");
		String sessionId = null;
		try{
			IOMMLogSV sv = (IOMMLogSV)ServiceFactory.getService(IOMMLogSV.class);
			int count = sv.getLogInfoCount(sessionId, account, startTime, endTime);
			List<HashMap<Object,Object>> data = sv.getLogInfo(sessionId, account, startTime, endTime, startIndex, endIndex);
			res.put("count", count);
			res.put("data", data);
		}catch(Exception e){
			e.printStackTrace();
			res.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			res.put(OMMConstantEnum.RESULT_MSG, e.getMessage());
		}
		return res;
	}
}
