package com.asiainfo.omm.app.log.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.log.dao.interfaces.IOMMLogDAO;
import com.asiainfo.omm.app.log.ivalues.IBOOmmLogValue;
import com.asiainfo.omm.app.log.service.interfaces.IOMMLogSV;

/**
 * OMM日志处理
 * 
 * @author oswin
 *
 */
public class OMMLogSVImpl implements IOMMLogSV {

	public List<HashMap<Object,Object>> getLogInfo(String sessionId, String account, String startTime, String endTime, int startIndex, int endIndex) throws Exception{
		IOMMLogDAO dao = (IOMMLogDAO)ServiceFactory.getService(IOMMLogDAO.class);
		IBOOmmLogValue[] data = dao.getLogInfo(sessionId, account, startTime, endTime, startIndex, endIndex);
		List<HashMap<Object,Object>> list = new ArrayList<HashMap<Object,Object>>();
		if(data != null && data.length>0){
			for(IBOOmmLogValue value : data){		
				list.add(value.getProperties());
			}
		}
		return list;
	}
	
	public int getLogInfoCount(String sessionId, String account, String startTime, String endTime) throws Exception{
		IOMMLogDAO dao = (IOMMLogDAO)ServiceFactory.getService(IOMMLogDAO.class);
		return dao.getLogInfoCount(sessionId, account, startTime, endTime);
	}
	
	
	public void saveLog(IBOOmmLogValue log) throws Exception {
		if(log != null){
			IOMMLogDAO dao = (IOMMLogDAO)ServiceFactory.getService(IOMMLogDAO.class);
			dao.saveLog(log);
		}
	}
}
