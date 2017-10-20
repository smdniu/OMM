package com.asiainfo.omm.app.log.dao.impl;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import com.asiainfo.omm.app.log.bo.BOOmmLogEngine;
import com.asiainfo.omm.app.log.dao.interfaces.IOMMLogDAO;
import com.asiainfo.omm.app.log.ivalues.IBOOmmLogValue;
import com.asiainfo.omm.utils.DateUtil;
import com.asiainfo.omm.utils.StringUtils;

/**
 * OMM日志处理
 * 
 * @author oswin
 *
 */
public class OMMLogDAOImpl implements IOMMLogDAO{
	
	public IBOOmmLogValue[] getLogInfo(String sessionId, String account, String startTime, String endTime, int startIndex, int endIndex) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		if(StringUtils.isNotBlank(sessionId)){
			params.put("sessionId", sessionId);
			condition.append(" AND ").append(IBOOmmLogValue.S_SessionId).append( " = :sessionId ");
		}
		if(StringUtils.isNotBlank(account)){
			params.put("account", "%"+account+"%");
			condition.append(" AND ( ").append(IBOOmmLogValue.S_Account).append( " like :account or ")
			                           .append(IBOOmmLogValue.S_Name).append(" like :account )");
		}
		if(StringUtils.isNotBlank(startTime)){
			params.put("startTime", startTime);
			condition.append(" AND ").append(IBOOmmLogValue.S_LogDate).append(" > to_date( :startTime,'YYYY-MM-DD HH24:MI:SS')");
		}
		if(StringUtils.isNotBlank(endTime)){
			params.put("endTime", endTime);
			condition.append(" AND ").append(IBOOmmLogValue.S_LogDate).append(" < to_date( :endTime,'YYYY-MM-DD HH24:MI:SS')");
		}
		return BOOmmLogEngine.getBeans(null, condition.toString(), params, startIndex, endIndex, false);
	}
	
	public int getLogInfoCount(String sessionId, String account, String startTime, String endTime) throws Exception{
		StringBuilder condition = new StringBuilder(" 1=1 ");
		Map<String, Object> params =new HashMap<String, Object>();
		if(StringUtils.isNotBlank(sessionId)){
			params.put("sessionId", sessionId);
			condition.append(" AND ").append(IBOOmmLogValue.S_SessionId).append( " = :sessionId ");
		}
		if(StringUtils.isNotBlank(account)){
			params.put("account", "%"+account+"%");
			condition.append(" AND ( ").append(IBOOmmLogValue.S_Account).append( " like :account or ")
            						   .append(IBOOmmLogValue.S_Name).append(" like :account )");
		}
		if(StringUtils.isNotBlank(startTime)){
			params.put("startTime", startTime);
			condition.append(" AND ").append(IBOOmmLogValue.S_LogDate).append(" > to_date( :startTime,'YYYY-MM-DD HH24:MI:SS')");
		}
		if(StringUtils.isNotBlank(endTime)){
			params.put("endTime", endTime);
			condition.append(" AND ").append(IBOOmmLogValue.S_LogDate).append(" < to_date( :endTime,'YYYY-MM-DD HH24:MI:SS')");
		}
		return BOOmmLogEngine.getBeansCount(condition.toString(), params);
	}

	
	public void saveLog(IBOOmmLogValue log) throws Exception {
		if(log != null){
			log.setId(BOOmmLogEngine.getNewId().longValue());
			log.setLogDate(DateUtil.getNowTimestamp());//yyyy-MM-dd HH:mm:ss
			try {
				log.setServiceIp(InetAddress.getLocalHost().getHostAddress());
			} catch (Exception e) {
				log.setServiceIp("0");
			}
			BOOmmLogEngine.save(log);
		}
	}
}
