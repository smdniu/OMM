package com.asiainfo.omm.utils;

import org.apache.log4j.Logger;

import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;
import com.asiainfo.omm.utils.encrypt.EncryptMD5;

/**
 * 运维平台加解密
 * 
 * @author oswin
 *
 */
public final class OMMEncrypt {

	private static final Logger logger = Logger.getLogger(OMMEncrypt.class);
	
	/**
	 * 加密字符串
	 * 
	 * @param encryptType
	 * @param encryptStr
	 * @return
	 * @throws OMMException 
	 */
	public final static String encryptStr(String encrypt, String encryptStr) throws OMMException{
		if(encrypt == null || "".equals(encrypt.trim()) || "NULL".equalsIgnoreCase(encrypt.trim())){
			logger.debug("OMM平台未传入加密方式, 默认不加密");
			encrypt = "0";
		}
		if(OMMConstantEnum.ENCRYPTTYPE.MD5.getEncryptType().equals(encrypt.trim())){
			return EncryptMD5.md5Encode(encryptStr);
		}else if(OMMConstantEnum.ENCRYPTTYPE.NOT.getEncryptType().equals(encrypt.trim())){
			return encryptStr;
		}else{
			throw new OMMException(ExceptionEnum.OMM_NOT_FOUND_ENCRYPT);
		}
	}
	
	/**
	 * 解密字符串
	 * 
	 * @param encryptType
	 * @param encryptStr
	 * @return
	 * @throws OMMException 
	 */
	public final static String dencryptStr(String encrypt, String encryptStr) throws OMMException{
		if(encrypt == null || "".equals(encrypt.trim()) || "NULL".equalsIgnoreCase(encrypt.trim())){
			logger.debug("OMM平台未传入加密方式, 默认不解密");
			encrypt = "0";
		}
		if(OMMConstantEnum.ENCRYPTTYPE.MD5.getEncryptType().equals(encrypt.trim())){
			throw new OMMException(ExceptionEnum.OMM_ENCRYPT_MD5_UNABLE_DECODE);
		}else if(OMMConstantEnum.ENCRYPTTYPE.NOT.getEncryptType().equals(encrypt.trim())){
			return encryptStr;
		}else{
			throw new OMMException(ExceptionEnum.OMM_NOT_FOUND_ENCRYPT);
		}
	}
}
