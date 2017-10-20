package com.asiainfo.omm.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.ai.appframe2.service.ServiceFactory;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMLogEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.service.interfaces.ILogAppSV;
import com.asiainfo.omm.utils.OMMEnumUtils;

/**
 * 日志记录拦截器
 * 
 * @author oswin
 *
 */
public class LogRecordFilter implements Filter {

	private static final Logger logger = Logger.getLogger(LogRecordFilter.class);

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestURI = request.getRequestURI();
		String requestPath = request.getContextPath();
		//屏蔽心跳检测日志
		String tmp1 = requestURI.substring(requestPath.length());
		boolean flag = OMMEnumUtils.getLogexceptpath().contains(tmp1);
		if(!requestURI.startsWith(requestPath + "/biz") && !requestURI.equals(requestPath + "/") && !flag){
			logger.debug("OMM平台本次请求地址:" + requestURI + "; 请求参数为:" + JSONObject.toJSONString(request.getParameterMap()));
		}
		filterChain.doFilter(request, response);
		HttpSession session = request.getSession(Boolean.FALSE);
		if (session != null && !requestURI.equals(requestPath + "/") && !requestURI.equals(requestPath + "/user/logout") && !requestURI.equals(requestPath + "/memcache/info") && !requestURI.startsWith(requestPath + "/biz")) {
			if(flag){
				return;
			}
			IBOOmmMemberValue member = (IBOOmmMemberValue) session.getAttribute(OMMConstantEnum.OMM_MEMBER_KEY);
			Map<String, IBOOmmMenuValue> menusMap = (Map<String, IBOOmmMenuValue>) session.getAttribute(OMMConstantEnum.OMM_MENU_KEY);
			ILogAppSV logAppSV = (ILogAppSV) ServiceFactory.getService(ILogAppSV.class);
			logger.debug("OMM平台本次请求地址[" + requestURI + "]记录日志");
			if(member != null && requestURI.equals(requestPath + "/user/login")){
				try {
					logAppSV.saveLog("OMM平台登录", requestURI, ExceptionEnum.OMM_SUCCES.getCode(), ExceptionEnum.OMM_SUCCES.getMsg(), JSONObject.toJSONString(request.getParameterMap()), OMMLogEnum.LOG.WEB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(member != null && requestURI.equals(requestPath + "/user/index")){
				try {
					logAppSV.saveLog( "OMM平台主页", requestURI, ExceptionEnum.OMM_SUCCES.getCode(), ExceptionEnum.OMM_SUCCES.getMsg(), JSONObject.toJSONString(request.getParameterMap()), OMMLogEnum.LOG.WEB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			String tmp = requestURI.substring(requestPath.length() + 1);
			String realTmp = tmp.substring(0, tmp.indexOf("/"));
			if (member != null && menusMap!= null && menusMap.containsKey(realTmp)) {
				IBOOmmMenuValue menu = menusMap.get(realTmp);
				try {
					logAppSV.saveLog(menu.getName(), requestURI, ExceptionEnum.OMM_SUCCES.getCode(), ExceptionEnum.OMM_SUCCES.getMsg(), JSONObject.toJSONString(request.getParameterMap()), OMMLogEnum.LOG.WEB);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
//			else {
//				try {
//					logAppSV.saveLog("未知访问", "未知访问", "未知访问", path, requestIp, session, exeEnum, requesparam, log);
//					logAppSV.saveLog("未知访问", requestURI, ExceptionEnum.OMM_SUCCES.getCode(), ExceptionEnum.OMM_SUCCES.getMsg(), JSONObject.toJSONString(request.getParameterMap()), OMMLogEnum.LOG.WEB);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
	}

	public void destroy() {
	}
}
