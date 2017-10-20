package com.asiainfo.omm.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.ai.appframe2.service.ServiceFactory;
import com.alibaba.fastjson.JSONObject;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;
import com.asiainfo.omm.app.comm.service.interfaces.ICfgStaticDataSV;
import com.asiainfo.omm.app.userapp.ivalues.IBOOmmMenuValue;
import com.asiainfo.omm.app.userapp.service.interfaces.IOmmMenuSV;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMConstantEnum.MENUENUM;
import com.asiainfo.omm.constant.OMMConstantEnum.STATE;
import com.asiainfo.omm.service.interfaces.IMenuSV;
import com.asiainfo.omm.utils.OMMEnumUtils;

public class MenuSVImpl implements IMenuSV {
	
	public Map<String, Object> getAllJVMCaches()throws Exception{
		
		return null;
	}
	
	public Map<String, Object> getMenusById(String menuId) throws Exception{
		Map<String, Object> menuAllAuth = new HashMap<String, Object>();
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		IBOOmmMenuValue[]  menus = menuSV.getMenuByMenuId1(menuId);
		if(menus != null && menus.length > 0){
			for(IBOOmmMenuValue menu: menus){
				if(MENUENUM.REDIS.getMenuType().equals(menu.getMenuType())){
					List<Map<String, String>> redis = new ArrayList<Map<String,String>>();
					Map<String, String> ipPortMap = null;
					String authStr = menu.getMenuAuth();
					String[] authArr = authStr.split(",");
					if(authArr != null && authArr.length > 0){
						for(String key: authArr){
							String[] ipPort = key.split(":");
							if(ipPort != null && ipPort.length ==2){
								ipPortMap = new TreeMap<String, String>();
								ipPortMap.put("ip", ipPort[0]);
								ipPortMap.put("port", ipPort[1]);
								ipPortMap.put("state", STATE.U.getStateMsg());
								redis.add(ipPortMap);
							}
						}
					}
					menuAllAuth.put(MENUENUM.REDIS.getMenu(), redis);
				} else if(OMMConstantEnum.MENUENUM.MEMCACHE.getMenuType().equals(menu.getMenuType())){
					List<Map<String, String>> memcache = new ArrayList<Map<String,String>>();
					Map<String, String> ipPortMap;
					String authStr = menu.getMenuAuth();
					String[] authArr = authStr.split(",");
					if(authArr != null && authArr.length > 0){
						for(String key: authArr){
							String[] ipPort = key.split(":");
							if(ipPort != null && ipPort.length ==2){
								ipPortMap = new TreeMap<String, String>();
								ipPortMap.put("ip", ipPort[0]);
								ipPortMap.put("port", ipPort[1]);
								ipPortMap.put("state", STATE.U.getStateMsg());
								memcache.add(ipPortMap);
							}
						}
					}
					menuAllAuth.put(OMMConstantEnum.MENUENUM.MEMCACHE.getMenu(), memcache);
				} else if(MENUENUM.JVMCACHE.equals(menu.getMenuType())){
					menuAllAuth.put(MENUENUM.JVMCACHE.getMenu(), OMMEnumUtils.getAllGroups());
				} else if(MENUENUM.DB.equals(menu.getMenuType())){
					ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV)ServiceFactory.getService(ICfgStaticDataSV.class);
					IBOCfgStaticDataValue[]  dbBeans = cfgStaticDataSV.getValues(OMMConstantEnum.OMM_CONFIG_DB, OMMConstantEnum.CONFIG_DB);
					List<Map<String, String>> db = new ArrayList<Map<String,String>>();
					if(dbBeans != null && dbBeans.length > 0){
						for(IBOCfgStaticDataValue dbBean: dbBeans){
							Map<String, String> ipPortMap = new TreeMap<String, String>();
							ipPortMap.put("dbAcct", dbBean.getCodeValue());
							ipPortMap.put("min", dbBean.getCodeName());
							ipPortMap.put("max", dbBean.getCodeDesc());
							ipPortMap.put("state", STATE.U.getStateMsg());
							db.add(ipPortMap);
						}
					}
					menuAllAuth.put(MENUENUM.DB.getMenu(), db);
				}else if(MENUENUM.SINGLEPOINT.equals(menu.getMenuType())){
					Map<String, Object> singlepoint = new HashMap<String, Object>();
					String authStr = menu.getMenuAuth();
					JSONObject json = JSONObject.parseObject(authStr);
					singlepoint.putAll(json);
					menuAllAuth.put(MENUENUM.SINGLEPOINT.getMenu(), singlepoint);
				}
			}
		}
		return menuAllAuth;
	}

	public Map<String, Object> getAllMenus() throws Exception {
		IOmmMenuSV menuSV = (IOmmMenuSV)ServiceFactory.getService(IOmmMenuSV.class);
		IBOOmmMenuValue[] menus = menuSV.getMenuByAppName("");
		Map<String, Object> menuAllAuth = new HashMap<String, Object>();
		if(menus != null && menus.length > 0){
//			REDIS("0", "redis"),MEMCACHE("1", "memcache"),JVMCACHE("2", "jvmcache"),DB("3", "DB");SINGLEPOINT("8", "singlepoint")
			for(IBOOmmMenuValue menu: menus){
				if(MENUENUM.REDIS.getMenuType().equals(menu.getMenuType())){
					List<Map<String, String>> redis = new ArrayList<Map<String,String>>();
					Map<String, String> ipPortMap = null;
					String authStr = menu.getMenuAuth();
					String[] authArr = authStr.split(",");
					if(authArr != null && authArr.length > 0){
						for(String key: authArr){
							String[] ipPort = key.split(":");
							if(ipPort != null && ipPort.length ==3){
								ipPortMap = new TreeMap<String, String>();
								ipPortMap.put("ip", ipPort[0]);
								ipPortMap.put("port", ipPort[1]);
								ipPortMap.put("state", OMMEnumUtils.getStateByType(ipPort[2]));
								redis.add(ipPortMap);
							}
						}
					}
					menuAllAuth.put(MENUENUM.REDIS.getMenu(), redis);
				} else if(OMMConstantEnum.MENUENUM.MEMCACHE.getMenuType().equals(menu.getMenuType())){
					List<Map<String, String>> memcache = new ArrayList<Map<String,String>>();
					Map<String, String> ipPortMap = null;
					String authStr = menu.getMenuAuth();
					String[] authArr = authStr.split(",");
					if(authArr != null && authArr.length > 0){
						for(String key: authArr){
							String[] ipPort = key.split(":");
							if(ipPort != null && ipPort.length ==3){
								ipPortMap = new TreeMap<String, String>();
								ipPortMap.put("ip", ipPort[0]);
								ipPortMap.put("port", ipPort[1]);
								ipPortMap.put("state", OMMEnumUtils.getStateByType(ipPort[2]));
								memcache.add(ipPortMap);
							}
						}
					}
					menuAllAuth.put(OMMConstantEnum.MENUENUM.MEMCACHE.getMenu(), memcache);
				} else if(MENUENUM.JVMCACHE.getMenuType().equals(menu.getMenuType())){
					menuAllAuth.put(MENUENUM.JVMCACHE.getMenu(), OMMEnumUtils.getAllGroups());
				}
			}
		}
		ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV)ServiceFactory.getService(ICfgStaticDataSV.class);
		IBOCfgStaticDataValue[]  dbBeans = cfgStaticDataSV.getValues(OMMConstantEnum.OMM_CONFIG_DB, OMMConstantEnum.CONFIG_DB);
		List<Map<String, String>> db = new ArrayList<Map<String,String>>();
		if(dbBeans != null && dbBeans.length > 0){
			for(IBOCfgStaticDataValue dbBean: dbBeans){
				Map<String, String> ipPortMap = new TreeMap<String, String>();
				ipPortMap.put("dbAcct", dbBean.getCodeValue());
				ipPortMap.put("min", dbBean.getCodeName());
				ipPortMap.put("max", dbBean.getCodeDesc());
				ipPortMap.put("state", OMMEnumUtils.getStateByType(dbBean.getState().toUpperCase()));
				db.add(ipPortMap);
			}
		}
		menuAllAuth.put(MENUENUM.DB.getMenu(), db);
		Map<String, Object> singlepoint = new HashMap<String, Object>();
		menuAllAuth.put(MENUENUM.SINGLEPOINT.getMenu(), singlepoint);
		return menuAllAuth;
	}
}
