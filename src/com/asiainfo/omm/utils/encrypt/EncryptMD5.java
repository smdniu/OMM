package com.asiainfo.omm.utils.encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.exception.OMMException;

import sun.misc.BASE64Encoder;

/**
 * MD5加密
 * 
 * @author oswin
 *
 */
public final class EncryptMD5 {
	
	public final static String md5Encode(String message) throws OMMException {
		try{
            MessageDigest md;  
            md = MessageDigest.getInstance("md5");  
            byte m5[] = md.digest(message.getBytes());  
            BASE64Encoder encoder = new BASE64Encoder();  
            return encoder.encode(m5);  
        } catch (NoSuchAlgorithmException e) { 
        	throw new OMMException(ExceptionEnum.OMM_ENCRYPT_MD5_ENCODE);
        }  
	}
}
