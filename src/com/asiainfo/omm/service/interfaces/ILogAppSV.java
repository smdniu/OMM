package com.asiainfo.omm.service.interfaces;

import java.util.List;
import java.util.Map;

import com.asiainfo.omm.constant.OMMLogEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;

/**
 * OMM日志处理
 * 
 * @author oswin
 *
 */
public interface ILogAppSV {
	
	/**
	 * 保存日志
	 * 
	 * @param account 账户
	 * @param name 用户名
	 * @param role 角色
	 * @param pathname 应用名
	 * @param path 应用路径
	 * @param session session
	 * @param code 
	 * @param msg
	 * @param e 异常信息
	 * @throws Exception
	 */
	public void saveLog(String pathname, String path, String code, String msg, String requesparam, OMMLogEnum.LOG log) throws Exception;

	/**
	 * 保存日志
	 * 
	 * @param account 账户
	 * @param name 用户名
	 * @param role 角色
	 * @param pathname 应用名
	 * @param path 应用路径
	 * @param session session
	 * @param exeEnum 返回正常结果集
	 * @param e
	 * @throws Exception
	 */
	public void saveLog(String pathname, String path, ExceptionEnum exeEnum, String requesparam, OMMLogEnum.LOG log) throws Exception;
	
	/**
	 * 保存日志
	 * 
	 * @param account
	 * @param name
	 * @param pathname
	 * @param path
	 * @param requestIp
	 * @param session
	 * @param exeEnum
	 * @param requesparam
	 * @param log
	 * @throws Exception
	 */
	public void saveLog(String account, String name, String pathname, String path, String requestIp, String session, ExceptionEnum exeEnum, String requesparam, OMMLogEnum.LOG log) throws Exception;

	/**
	 * 保存日志
	 * 
	 * @param account
	 * @param name
	 * @param pathname
	 * @param path
	 * @param requestIp
	 * @param session
	 * @param code
	 * @param msg
	 * @param requesparam
	 * @param log
	 * @throws Exception
	 */
	public void saveLog(String account, String name, String pathname, String path, String requestIp, String session, String code, String msg, String requesparam, OMMLogEnum.LOG log) throws Exception;

}
