package com.asiainfo.omm.filter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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

import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum;
import com.asiainfo.omm.utils.SessionFactory;
import com.asiainfo.omm.utils.StringUtils;

/**
 * OMM页面拦截器,拦截非法访问
 * 
 * @author oswin
 *
 */
public class FileViewFilter implements Filter {
	
	private static final Logger logger = Logger.getLogger(FileViewFilter.class);
	
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@SuppressWarnings("unchecked")
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String requestURI = request.getRequestURI();
		String requestPath = request.getContextPath();
		Thread.currentThread().setName(requestURI);
		if(!requestURI.startsWith(requestPath + "")){
			response.sendRedirect(requestPath + "/user/logout");
		}else{
			HttpSession session = request.getSession(Boolean.FALSE);
			try{
				if(session != null){
					session.setAttribute(OMMConstantEnum.OMM_REQUEST_IP, getIp(request));
					SessionFactory.setSession(session);
				}
				if(requestURI.equals(requestPath + "/") || requestURI.equals(requestPath + "/user/logout") || requestURI.startsWith(requestPath + "/biz")){
					filterChain.doFilter(request, response);
				} else if (session == null){
					logger.debug("OMM平台本次请求地址未找到session信息,跳转到登录页面.");
					response.sendRedirect(request.getContextPath() + "/");
				} else if(!requestURI.equals(requestPath + "/user/login") && (IBOOmmMemberValue) session.getAttribute(OMMConstantEnum.OMM_MEMBER_KEY) == null){
					logger.debug("OMM平台未登录, 无法访问!");
					response.sendRedirect(request.getContextPath() + "/");
				} else if (requestURI.equals(requestPath + "/user/login") || requestURI.equals(requestPath + "/user/index")){
					filterChain.doFilter(request, response);
				} else {
					String tmp = requestURI.substring(requestPath.length() + 1);
					String realTmp = tmp.substring(0, tmp.indexOf("/"));
					Map<String, IBOOmmMenuValue> menusMap = (Map<String, IBOOmmMenuValue>) session.getAttribute(OMMConstantEnum.OMM_MENU_KEY);
					if(menusMap != null && menusMap.containsKey(realTmp)){
//						logger.debug("OMM平台本次请求实际地址为:" + tmp + " session: " + session.getId());
						filterChain.doFilter(request, response);
					}else{
						logger.debug("OMM平台本次请求越权");
						request.getRequestDispatcher("/user/unauthorized").forward(request,response);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				logger.error("OMM平台本次请求异常: " + e);
				request.setAttribute(OMMConstantEnum.OMM_CODE, OMMExceptionEnum.ExceptionEnum.OMM_SYSTEM_ERROR.getCode());
				request.setAttribute(OMMConstantEnum.OMM_MSG, OMMExceptionEnum.ExceptionEnum.OMM_SYSTEM_ERROR.getMsg());
				response.sendRedirect(request.getContextPath() + "/user/index");
			} 
		}
	}
	
	private static String getIp(HttpServletRequest request){
		String ipAddress = getIp2(request);
		if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
            //根据网卡取本机配置的IP  
            InetAddress inet=null;  
            try {  
                inet = InetAddress.getLocalHost(); 
                ipAddress= inet.getHostAddress();
            } catch (UnknownHostException e) {  
                e.printStackTrace();  
            }  
        }  
		return ipAddress;
	}
	private static String getIp2(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if(StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
			//多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if(index != -1){
				return ip.substring(0,index);
			}else{
                return ip;
            }
		}
		ip = request.getHeader("X-Real-IP");
		if(StringUtils.isNotBlank(ip) && !"unKnown".equalsIgnoreCase(ip)){
			return ip;
		}
		return request.getRemoteAddr();
   	}
	public void destroy() {
		
	}
}