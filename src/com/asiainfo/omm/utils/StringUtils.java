package com.asiainfo.omm.utils;

public final class StringUtils {

	/**
	 * 当字符串为null或为空时返回true
	 * 
	 * @param str
	 * @return
	 */
	public final static boolean isBlank(String str){
		if(str == null || "".equals(str.trim())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 	
	 * 当字符串不为null和空时返回true
	 */
	public final static boolean isNotBlank(String str){
		if(str != null && !"".equals(str.trim())){
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	
}
