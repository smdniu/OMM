package com.asiainfo.omm.app.comm.service.interfaces;

import java.util.List;

import com.asiainfo.omm.app.comm.ivalues.IBOCfgStaticDataValue;

/**
 * 静态数据表cfg_static_data数据操作
 * 
 * @author oswin
 *
 */
public interface ICfgStaticDataSV {

	/**
	 * 根据codeType和拓展codeType获取静态数据. 可为空(但不推荐)
	 * @param codeType
	 * @param externCodeType
	 * @return
	 * @throws Exception
	 */
	public IBOCfgStaticDataValue[] getValues(String codeType,String externCodeType) throws Exception;
	
	/**
	 * 新增静态数据
	 * 
	 * @param cfgStaticDatas
	 * @throws Exception
	 */
	public void addStaticData(List<IBOCfgStaticDataValue> cfgStaticDatas) throws Exception;
}
