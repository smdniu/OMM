package com.asiainfo.omm.listener;

import java.net.InetAddress;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMLogEnum;
import com.asiainfo.omm.controller.UserController;
import com.asiainfo.omm.service.interfaces.ILogAppSV;

public class OMMSessionListener implements HttpSessionListener {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	/* Session创建事件 */
	public void sessionCreated(HttpSessionEvent event) {
	}

	/* Session失效事件 */
	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		if(session != null){
			IBOOmmMemberValue member = (IBOOmmMemberValue) session.getAttribute(OMMConstantEnum.OMM_MEMBER_KEY);
			ILogAppSV logAppSV = (ILogAppSV)ServiceFactory.getService(ILogAppSV.class);
			if(member != null){
				if(!"logout".equalsIgnoreCase(String.valueOf(session.getAttribute("STATE")))){
					try {
						logger.debug("OMM平台登录用户: " + session.getAttribute(OMMConstantEnum.OMM_USERNAME_KEY) + "session过期退出系统, 失效session: "  + session.getId());
						logAppSV.saveLog(member.getAccount(), member.getName(), "session失效,退出OMM平台!", "expirySession", InetAddress.getLocalHost().getHostAddress(), session.getId(), "00000", "OMM平台session过期, 退出成功!", "", OMMLogEnum.LOG.WEB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else{
					try {
						logger.debug("OMM平台登录用户: " + session.getAttribute(OMMConstantEnum.OMM_USERNAME_KEY) + "正常退出系统, 失效session: "  + session.getId());
						logAppSV.saveLog(member.getAccount(), member.getName(), "正常退出OMM平台!", "/OMM/user/logout", InetAddress.getLocalHost().getHostAddress(), session.getId(), "00000", "OMM平台退出成功!", "", OMMLogEnum.LOG.WEB);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}else{
				logger.debug("OMM平台失效session:" +  session.getId());
			}
		}
	}
}