package com.asiainfo.omm.app.log.dao.interfaces;

import com.asiainfo.omm.app.log.ivalues.IBOOmmLogValue;

/**
 * OMM日志处理
 * 
 * @author oswin
 *
 */
public interface IOMMLogDAO {
	
	/**
	 * 根据参数查询日志信息
	 * 
	 * @param sessionId sessionId
	 * @param account 账户
	 * @param stateTime 开始时间
	 * @param endTime 结束时间
	 * @param start 分页开始
	 * @param end 分页结束
	 * @return 日志信息
	 */
	public IBOOmmLogValue[] getLogInfo(String sessionId, String account, String startTime, String endTime, int startIndex, int endIndex) throws Exception;

	/**
	 * 根据参数查询日志信息
	 * 
	 * @param sessionId sessionId
	 * @param account 账户
	 * @param stateTime 开始时间
	 * @param endTime 结束时间
	 * @param start 分页开始
	 * @param end 分页结束
	 * @return 日志信息
	 */
	public int getLogInfoCount(String sessionId, String account, String startTime, String endTime) throws Exception;

	
	/**
	 * 保存OMM异常日志
	 * 
	 * @param logs
	 * @throws Exception
	 */
	public void saveLog(IBOOmmLogValue log) throws Exception;
}
