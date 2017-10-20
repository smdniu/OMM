package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.log.bo.BOOmmLogBean;
import com.asiainfo.omm.app.log.ivalues.IBOOmmLogValue;
import com.asiainfo.omm.app.log.service.interfaces.IOMMLogSV;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMemberValue;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum;
import com.asiainfo.omm.constant.OMMLogEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.service.interfaces.ILogAppSV;
import com.asiainfo.omm.utils.OMMEnumUtils;
import com.asiainfo.omm.utils.SessionFactory;

/**
 * OMM日志处理
 * 
 * @author oswin
 *
 */
public class LogAppSVImpl implements ILogAppSV {
	
	public void saveLog(String pathname, String path, String code, String msg, String requesparam, OMMLogEnum.LOG logEnum) throws OMMException{
		HttpSession session = SessionFactory.getSession();
		IBOOmmMemberValue member = (IBOOmmMemberValue) session.getAttribute(OMMConstantEnum.OMM_MEMBER_KEY);
		String requestIp = (String) session.getAttribute(OMMConstantEnum.OMM_REQUEST_IP);
		
		saveLog(member.getAccount(), member.getName(), pathname, path, requestIp, session.getId(), code, msg, requesparam, logEnum);
	}

	public void saveLog(String pathname, String path, ExceptionEnum exeEnum, String requesparam, OMMLogEnum.LOG logEnum) throws Exception {
		saveLog(pathname, path, exeEnum.getCode(), exeEnum.getMsg(), requesparam, logEnum);
	}
	
	public void saveLog(String account, String name, String pathname, String path, String requestIp, String session, String code, String msg, String requesparam, OMMLogEnum.LOG logEnum) throws OMMException{
		IOMMLogSV sv = (IOMMLogSV)ServiceFactory.getService(IOMMLogSV.class);
		try {
			IBOOmmLogValue log = new BOOmmLogBean();
			log.setAccount(account);
			log.setName(name);
			log.setPathname(pathname);
			log.setPath(path);
			log.setSessionId(session);
			log.setCode(code);
			log.setMsg(msg);
			log.setRequestIp(requestIp);
			log.setRequesParam(requesparam);
			log.setLogType(logEnum.getLog());
			sv.saveLog(log);
		} catch (Exception e1) {
			throw new OMMException(ExceptionEnum.LOG_SAVE_EXCEPTION, e1);
		}
	}
	
	public void saveLog(String account, String name, String pathname, String path, String requestIp, String session, OMMExceptionEnum.ExceptionEnum exeEnum, String requesparam, OMMLogEnum.LOG logEnum) throws Exception {
		saveLog(account, name, pathname, path, requestIp, session, exeEnum.getCode(), exeEnum.getMsg(), requesparam, logEnum);
	}
}
