package com.asiainfo.omm.exception;

import com.asiainfo.omm.constant.OMMExceptionEnum.ExceptionEnum;
import com.asiainfo.omm.utils.StringUtils;

/**
 * OMM异常
 * 
 * @author oswin
 *
 */
public class OMMException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	private String code = "99999";
	
	private ExceptionEnum enum1;
	
	public OMMException() {
		super("运维管理平台未定义异常信息");
	}

	private OMMException(String message, Throwable cause) {
		super(message, cause);
	}

	public OMMException(String message) {
		super(message);
	}

	public OMMException(Throwable cause) {
		super(cause);
	}

	/**
	 * 传入异常枚举值
	 * 
	 * @param enum1
	 */
	public OMMException(ExceptionEnum enum1) {
		super(enum1.getMsg());
		this.enum1 = enum1;
		this.code = enum1.getCode();
	}
	
	public OMMException(ExceptionEnum enum1, Throwable cause) {
		this(enum1.getMsg(), cause);
		this.enum1 = enum1;
		this.code = enum1.getCode();
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public ExceptionEnum getEnum1() {
		return enum1;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(getClass().getName());
        String message = getLocalizedMessage();
        
        if(StringUtils.isNotBlank(code)){
        	sb.append(code).append(": ");
        }
        if(StringUtils.isNotBlank(message)){
        	sb.append(message);
        }
        return sb.toString();
	}
}
