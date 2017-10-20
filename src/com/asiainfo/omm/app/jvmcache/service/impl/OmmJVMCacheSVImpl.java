package com.asiainfo.omm.app.jvmcache.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.comm.bo.BOCfgStaticDataBean;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;
import com.asiainfo.omm.app.comm.service.interfaces.ICfgStaticDataSV;
import com.asiainfo.omm.app.jvmcache.service.interfaces.IOmmJVMCacheSV;
import com.asiainfo.omm.constant.OMMConstantEnum;

public class OmmJVMCacheSVImpl implements IOmmJVMCacheSV {
	
	public void addJVMCacheGroup(String groupCode, String groupName, String className, String ipPort) throws Exception{
		ICfgStaticDataSV cfgStaticDataSV = (ICfgStaticDataSV)ServiceFactory.getService(ICfgStaticDataSV.class);
		List<IBOCfgStaticDataValue> cfgStaticDatas = new ArrayList<IBOCfgStaticDataValue>();
		BOCfgStaticDataBean cfgStaticData = new BOCfgStaticDataBean();
		cfgStaticData.setCodeType(OMMConstantEnum.OMM_JVMCACHE_KEY);
		cfgStaticData.setCodeValue(groupCode);
		cfgStaticData.setCodeName(groupName);
		cfgStaticData.setCodeDesc(className);
		cfgStaticData.setSortId(0);
		cfgStaticData.setState(OMMConstantEnum.STATE.U.getState());
		cfgStaticData.setExternCodeType(ipPort);
		cfgStaticDatas.add(cfgStaticData);
		cfgStaticDataSV.addStaticData(cfgStaticDatas);
	}
}
