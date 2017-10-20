package com.asiainfo.omm.constant;

/**
 * OMM异常枚举值
 * 
 * @author oswin
 *
 */
public interface OMMExceptionEnum {
	//user:1 web:2 redis:3 memchace:4 jvmcahce:5 db:6 log:7
	/**
	 * OMM异常
	 * @author oswin
	 *
	 */
	public enum ExceptionEnum {
		OMM_SUCCES("00000", "succes"),
		
		OMM_SYSTEM_ERROR("00001","OMM平台系统出错, 请稍后再试!"),
		OMM_PASSWORD_IS_WRONG("00002","OMM平台登录密码不正确!"),
		OMM_USERNAME_OR_PASSWORD_IS_NULL("00003","OMM平台登录用户名/密码不能为空!"),
		OMM_SYSTEM_NOT_FOUND_SESSION("00004","OMM平台未初始化session!"),
		OMM_SYSTEM_NOT_FOUND_INDEXMENU("00004", "用户未找到可用应用"),
		
		
		USER_NOT_FOUND_MEMBER("10001","根据用户名未找到用户信息!"), 
		USER_FOUND_MULTI_MEMBER("10002","找到多个用户信息!"),
		USER_NOT_FOUND_ROLE("10003","未找到角色信息!"),
		USER_NOT_FOUND_RELAT_ROLE("10004","未找到用户关联角色信息!"),
		USER_FOUND_MEMBER_INVALID("10005","用户未生效或已失效!"),
		USER_NOT_CAN_DELETE_SELF("10006","用户无法删除自己!"),
		USER_NOT_FOUND_MENU("10007","未找到应用信息!"),
		USER_NOT_FOUND_MENU_RELAT_ROLE("10008","未找到应用关联角色信息!"),
		
		LOG_SAVE_EXCEPTION("700001", "OMM平台保存日志出错"),
		
		OMM_NOT_FOUND_ENCRYPT("80001","OMM平台未找打加解密方式"),
		OMM_ENCRYPT_MD5_ENCODE("80002","OMM平台MD5加密失败"),
		OMM_ENCRYPT_MD5_UNABLE_DECODE("80003","OMM平台MD5不支持解密");
		
		//构造器默认也只能是private, 从而保证构造函数只能在内部使用
		private final String code;
		private final String msg;
		
		private ExceptionEnum(String code,String msg) {
	        this.code = code;
	        this.msg = msg;
	    }

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	/**
	 * OMM运行时异常
	 * 
	 * @author oswin
	 *
	 */
	public enum RuntimeExceptionEnum {
		OMM_SUCCES("00000", "succes"),
		
		NOT_FOUND_Member("10000","用户信息未找到!"), 
		ERROR("10000","查询用户信息失败");
		
		//构造器默认也只能是private, 从而保证构造函数只能在内部使用
		private final String code;
		private final String msg;
		
		private RuntimeExceptionEnum(String code,String msg) {
	        this.code = code;
	        this.msg = msg;
	    }

		public String getCode() {
			return code;
		}

		public String getMsg() {
			return msg;
		}
	}
	
	
	
}
