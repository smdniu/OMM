package com.asiainfo.omm.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;
import com.asiainfo.omm.app.comm.service.interfaces.ICfgStaticDataSV;
import com.asiainfo.omm.app.jvmcache.service.interfaces.IJVMCacheDealSV;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMConstantEnum.ENCRYPTTYPE;
import com.asiainfo.omm.constant.OMMConstantEnum.STATE;

/**
 * omm平台枚举工具类
 * 
 * @author oswin
 *
 */
public final class OMMEnumUtils {

	private static final Logger logger = Logger.getLogger(OMMEnumUtils.class);
	
	private static final TreeMap<String, String> DEPT = new TreeMap<String,String>();
	
	private static final TreeMap<String, Object> JVMCACHE = new TreeMap<String, Object>();
	
	private static final TreeSet<String> LOGEXCEPTPATH = new TreeSet<String>();
	
	static{
		initDept();//初始化部门信息
		initJVMCache();//初始化JVMCache
		initLog();
	}
	
	/**
	 * 
	 * 初始化日志记录路径信息
	 * 
	 */
	private static void initLog(){
		ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV) ServiceFactory.getService(ICfgStaticDataSV.class);
		try {
			IBOCfgStaticDataValue[] cfgStaticDatas = cfgStaticDataSV.getValues(OMMConstantEnum.OMM_LOG_KEY, null);
			if(cfgStaticDatas != null && cfgStaticDatas.length > 0){
				for(IBOCfgStaticDataValue cfgStaticData: cfgStaticDatas){
					String path = cfgStaticData.getExternCodeType();
					if(StringUtils.isNotBlank(path)){
						String[] pathArr = path.split(",");
						if(pathArr != null && pathArr.length > 0){
							for(String p: pathArr){
								LOGEXCEPTPATH.add(p);
							}
						}
					}
				}
				logger.info("OMM平台初始化全量LogExceptPath信息:" + LOGEXCEPTPATH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static TreeSet<String> getLogexceptpath() {
		return LOGEXCEPTPATH;
	}



	/**
	 * 获取jvmcache信息
	 * 
	 * @return
	 */
	public static TreeMap<String, Object> getJvmcache() {
		return JVMCACHE;
	}

	
	
	/**
	 * 获取所有分组信息
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final List<Map<String, String>> getAllGroups(){
		List<Map<String, String>> groupIds = (List<Map<String, String>>) JVMCACHE.get("groupIds");
		if(groupIds == null){
			initJVMCache();
			groupIds = (List<Map<String, String>>) JVMCACHE.get("groupIds");
		}
		if(groupIds == null){
			groupIds = new ArrayList<Map<String,String>>();
		}
		return groupIds;
	}
	
	/**
	 * 根据分组id获取jvmcache的ip和port信息
	 * 
	 * @param groupCode
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final Map<String, Object> getJVMCacheIpPortInfoByGroupCode(String groupCode){
		Map<String,Map<String, Object>> groupIpPorts = (Map<String,Map<String, Object>>) JVMCACHE.get("groupIpPorts");
		if(groupIpPorts == null){
			initJVMCache();
			groupIpPorts = (Map<String,Map<String, Object>>) JVMCACHE.get("groupIpPorts");
		}
		if(groupIpPorts != null){
			Map<String, Object> tmp = groupIpPorts.get(groupCode);
			if(tmp == null){
				initJVMCache();
				groupIpPorts = (Map<String,Map<String, Object>>) JVMCACHE.get("groupIpPorts");
				tmp = groupIpPorts.get(groupCode);
			}
			return tmp;
		}
		return new HashMap<String, Object>();
	}
	
	@SuppressWarnings("unchecked")
	private static final synchronized void  initJVMCache(){
		try {
			ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV) ServiceFactory.getService(ICfgStaticDataSV.class);
			IBOCfgStaticDataValue[] cfgStaticDatas = cfgStaticDataSV.getValues(OMMConstantEnum.OMM_JVMCACHE_KEY, null);
			if(cfgStaticDatas != null && cfgStaticDatas.length > 0){
				List<Map<String, String>> groupIds = new ArrayList<Map<String,String>>();
				Map<String,Map<String, Object>> groupIpPorts = new HashMap<String, Map<String,Object>>();
				Map<String, String> groupId = null;
				Map<String, Object> groupIpPort = null;
				for(IBOCfgStaticDataValue value: cfgStaticDatas){
					groupId = new HashMap<String, String>();
					groupId.put("code", value.getCodeValue());
					groupId.put("name", value.getCodeName());
					groupIds.add(groupId);
					
					groupIpPort = new HashMap<String, Object>();
					groupIpPort.put("code", value.getCodeValue());
					groupIpPort.put("name", value.getCodeName());
					try {
						Class<IJVMCacheDealSV> clazz = (Class<IJVMCacheDealSV>) Class.forName(value.getCodeDesc());
						if(IJVMCacheDealSV.class.isAssignableFrom(clazz)){
							groupIpPort.put("classInstance", clazz.newInstance());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					String ipPortStr = value.getExternCodeType();
					String[] ipPortArr = ipPortStr.split(",");
					if(ipPortArr != null && ipPortArr.length > 0){
						List<Map<String, String>> ipPortList = new ArrayList<Map<String,String>>();
						Map<String, String> ipPortMap = null;
						for(String key: ipPortArr){
							String[] ipPort = key.split(":");
							if(ipPort != null && ipPort.length ==2){
								ipPortMap = new TreeMap<String, String>();
								ipPortMap.put("ip", ipPort[0]);
								ipPortMap.put("port", ipPort[1]);
								ipPortMap.put("state", OMMEnumUtils.getStateByType(value.getState()));
								ipPortList.add(ipPortMap);
							}
						}
						groupIpPort.put("ipPortList", ipPortList);
					}
					groupIpPorts.put(value.getCodeValue(), groupIpPort);
				}
				JVMCACHE.put("groupIds", groupIds);
				JVMCACHE.put("groupIpPorts", groupIpPorts);
				logger.info("OMM平台初始化全量JVMCache信息:" + JVMCACHE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private synchronized static final void initDept(){
		try {
			ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV) ServiceFactory.getService(ICfgStaticDataSV.class);
			IBOCfgStaticDataValue[] cfgStaticDatas = cfgStaticDataSV.getValues(OMMConstantEnum.OMM_DEPT_KEY, "DEPT");
			if(cfgStaticDatas != null && cfgStaticDatas.length > 0){
				for(IBOCfgStaticDataValue cfgStaticData: cfgStaticDatas){
					DEPT.put(cfgStaticData.getCodeValue(), cfgStaticData.getCodeName());
				}
				logger.info("OMM平台初始化全量DEPT信息:" + DEPT);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(getDeptInfo());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 获取部门信息:<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final List<Map<String, String>> getDeptInfo() throws Exception {
		List<Map<String, String>> deptList = new ArrayList<Map<String,String>>();
		Map<String, String> dept;
		for(String key: DEPT.keySet()){
			dept = new HashMap<String, String>();
			dept.put("TYPE", key);
			dept.put("NAME", DEPT.get(key));
			deptList.add(dept);
		}
		return deptList;
	}
	
	/**
	 * 获取部门信息:<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public static final String getDeptNameByDeptType(String type) throws Exception {
		String deptStr =  DEPT.get(type);
		if(StringUtils.isNotBlank(deptStr)){
			initDept();
			deptStr = DEPT.get(type);
		}
		if(deptStr == null){
			return "未知";
		}else{
			return DEPT.get(type);
		}
	}
	
	/**
	 * 获取加密信息:<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public final static List<Map<String, String>> getEncryptInfo(){
		List<Map<String, String>> encryptList = new ArrayList<Map<String,String>>();
		Map<String, String> encryptMap = null;
		for(OMMConstantEnum.ENCRYPTTYPE encrypt: OMMConstantEnum.ENCRYPTTYPE.values()){
			encryptMap = new HashMap<String, String>();
			encryptMap.put("TYPE", encrypt.getEncryptType());
			encryptMap.put("NAME", encrypt.getEncrypt());
			encryptList.add(encryptMap);
		}
		return encryptList;
	}
	
	/**
	 * 获取状态信息:<br>
	 * 
	 * @return
	 * @throws Exception
	 */
	public final static List<Map<String, String>> getStateInfo(){
		List<Map<String, String>> stateList = new ArrayList<Map<String,String>>();
		Map<String, String> stateMap = null;
		for(OMMConstantEnum.STATE state: OMMConstantEnum.STATE.values()){
			stateMap = new HashMap<String, String>();
			stateMap.put("TYPE", state.getState());
			stateMap.put("NAME", state.getStateMsg());
			stateList.add(stateMap);
		}
		return stateList;
	}
	
	/**
	 * 根据加密类型获取加密
	 * 
	 * @param type
	 * @return
	 */
	public final static String getEncryptByType(String type){
		if(OMMConstantEnum.ENCRYPTTYPE.NOT.getEncryptType().equals(type)){
			return "无";
		}else if (ENCRYPTTYPE.MD5.getEncryptType().equals(type)){
			return ENCRYPTTYPE.MD5.getEncrypt();
		}else{
			return "无";
		}
	}
	
	/**
	 * 根据加密类型获取加密
	 * 
	 * @param type
	 * @return
	 */
	public final static String getStateByType(String type){
		if(STATE.U.getState().equals(type)){
			return STATE.U.getStateMsg();
		} else if (OMMConstantEnum.STATE.P.getState().equals(type)){
			return STATE.P.getStateMsg();
		} else if (OMMConstantEnum.STATE.E.getState().equals(type)){
			return STATE.E.getStateMsg();
		} else{
			return STATE.U.getStateMsg();
		}
	}
}
