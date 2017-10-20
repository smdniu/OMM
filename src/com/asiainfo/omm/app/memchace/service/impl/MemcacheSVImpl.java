package com.asiainfo.omm.app.memchace.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.appframe.ext.flyingserver.org.apache.commons.lang.StringUtils;
import com.asiainfo.omm.app.comm.dao.interfaces.ICfgStaticDataDAO;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;
import com.asiainfo.omm.app.memchace.service.interfaces.IMemcacheSV;
import com.asiainfo.omm.constant.OMMConstantEnum;;

public class MemcacheSVImpl implements IMemcacheSV{
	
	public List<String> getMemcacheAddress() throws Exception {
		List<String> result = new ArrayList<String>();
		ICfgStaticDataDAO dao = (ICfgStaticDataDAO)ServiceFactory.getService(ICfgStaticDataDAO.class);
		IBOCfgStaticDataValue[] values = dao.getValues(OMMConstantEnum.OMM_CONFIG_IP, OMMConstantEnum.MEMCACHE_IP);
		if(values == null || values.length == 0){
			return result;
		}
		for(IBOCfgStaticDataValue value :values){
			if("U".equals(value.getState()) && !StringUtils.isBlank(value.getCodeValue())){
				result.add(value.getCodeValue());
			}
		}
		return result;
	}
}


