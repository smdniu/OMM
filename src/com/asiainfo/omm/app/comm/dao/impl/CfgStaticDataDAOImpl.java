package com.asiainfo.omm.app.comm.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.asiainfo.omm.app.comm.bo.BOCfgStaticDataBean;
import com.asiainfo.omm.app.comm.bo.BOCfgStaticDataEngine;
import com.asiainfo.omm.app.comm.dao.interfaces.ICfgStaticDataDAO;
import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;

public class CfgStaticDataDAOImpl implements ICfgStaticDataDAO{

	public IBOCfgStaticDataValue[] getValues(String codeType,String externCodeType) throws Exception {
		StringBuffer condition = new StringBuffer(" 1=1 ");
		Map<String,Object> parameter = new HashMap<String,Object>();
		if(!StringUtils.isBlank(codeType)){
			condition.append(" and "+BOCfgStaticDataBean.S_CodeType+" = :codeType");
			parameter.put("codeType", codeType);
		}
		if(!StringUtils.isBlank(externCodeType)){
			condition.append(" and "+BOCfgStaticDataBean.S_ExternCodeType+" = :externCodeType");
			parameter.put("externCodeType", externCodeType);
		}
		condition.append(" AND ").append(BOCfgStaticDataBean.S_State).append("=:state");
		parameter.put("state", "U");
		return BOCfgStaticDataEngine.getBeans(condition.toString(), parameter);
	}
	
	public void addStaticData(IBOCfgStaticDataValue[] cfgStaticDatas) throws Exception{
		if(cfgStaticDatas != null && cfgStaticDatas.length > 0){
			BOCfgStaticDataEngine.save(cfgStaticDatas);
		}
	}
}
