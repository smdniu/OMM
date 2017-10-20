package com.asiainfo.omm.app.comm.service.impl;

import java.util.List;

import com.ai.appframe2.service.ServiceFactory;
import com.asiainfo.omm.app.comm.bo.BOCfgStaticDataBean;
import com.asiainfo.omm.app.comm.dao.interfaces.ICfgStaticDataDAO;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;
import com.asiainfo.omm.app.comm.service.interfaces.ICfgStaticDataSV;

public class CfgStaticDataSVImpl implements ICfgStaticDataSV {

	public IBOCfgStaticDataValue[] getValues(String codeType, String externCodeType) throws Exception {
		ICfgStaticDataDAO dao = (ICfgStaticDataDAO) ServiceFactory.getService(ICfgStaticDataDAO.class);
		return dao.getValues(codeType, externCodeType);
	}

	public void addStaticData(List<IBOCfgStaticDataValue> cfgStaticDatas) throws Exception{
		ICfgStaticDataDAO dao = (ICfgStaticDataDAO) ServiceFactory.getService(ICfgStaticDataDAO.class);
		if(cfgStaticDatas != null && cfgStaticDatas.size() > 0){
			dao.addStaticData(cfgStaticDatas.toArray(new BOCfgStaticDataBean[0]));
		}
	}
}
