package com.asiainfo.omm.utils;

import javax.servlet.http.HttpSession;

import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;

public final class SessionFactory {

	private static final ThreadLocal<HttpSession> threadSession = new ThreadLocal<HttpSession>();
	
	public final static HttpSession  getSession() throws OMMException{
		HttpSession session = threadSession.get();
		if(session == null){
			throw new OMMException(ExceptionEnum.OMM_SYSTEM_NOT_FOUND_SESSION);
		}
		return threadSession.get();
	}
	
	public final static void setSession(HttpSession session) throws OMMException{
		if(session == null){
			throw new OMMException(ExceptionEnum.OMM_SYSTEM_NOT_FOUND_SESSION);
		}
	    threadSession.set(session);  
	}
}
