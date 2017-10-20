package com.asiainfo.omm.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.appframe2.service.ServiceFactory;
import com.ai.secframe.service.common.impl.DESPlus;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.TimeUtils;
import com.asiainfo.pce.util.json.source.JSONObject;
/**
 * singlepoint 单点登陆
 * @author chenggz
 *
 */
@Controller
@RequestMapping("/singlepoint")
public class SinglepointController {
	private static final Logger logger = Logger.getLogger(SinglepointController.class);
	/**
	 * 新宽带单点登陆
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value ="/index" ,method = RequestMethod.POST)
	public @ResponseBody JSONObject index(HttpServletRequest request,HttpServletResponse response,@RequestParam("code")String code,@RequestParam("menuName")String menuName){
		logger.debug("单点登陆@Controller ");
		String path = "";
		String freshCode = request.getParameter("code");
		JSONObject json = new JSONObject();
		try {
		IOmmMenuSV menuSv = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		long timeKey= TimeUtils.getDefaultSysDate().getTime();
		String kdUrl ="";
		String  aa = "&code="+code+"&timeKey="+timeKey;
		String encryptCode = new DESPlus().encrypt(aa);
		logger.debug("code和timeKey拼装后加密为========="+encryptCode);
		HttpSession session = request.getSession(Boolean.TRUE);
		List<Map> resultList = (List<Map>) session.getAttribute(OMMConstantEnum.OMM_MENU_SINGLEPOINT_KEY);
		for(Map imap:resultList){
				List<Map<String,Object>> accPwdListTmp = (List<Map<String,Object>>)imap.get("accPwd");
//				String menuName1 = (String)imap.get("menuName");
				for(Map jmap:accPwdListTmp){
					if(((String)jmap.get("username")).equals(code)&&((String)imap.get("menuName")).contains(menuName)){
						String password = (String)jmap.get("password");
						kdUrl = (String) imap.get("url");
					}
				}
			}
		logger.debug("kd跳转url为========="+kdUrl);
		json.put("result", "success");
		json.put("url2des", kdUrl);
		return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.put("result", "fail");
		}
		return json;		
	}
	
	/**
	 * 新增单点登录配置信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "/addSinglePointConfig", method = RequestMethod.POST)
	public @ResponseBody JSONObject addSinglePointConfig(){
		logger.info("OMM平台新增单点登录配置信息");
		JSONObject json = new JSONObject();
		json.put(OMMConstantEnum.OMM_CODE, "00000");
		json.put(OMMConstantEnum.OMM_MSG, "成功");
		
		return json;
	}
}
