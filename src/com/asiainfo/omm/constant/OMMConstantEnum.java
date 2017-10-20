package com.asiainfo.omm.constant;

/**
 * 
 * 
 * @author oswin
 *
 */
public interface OMMConstantEnum {
	/**
	 * member信息key值
	 */
	public final static String OMM_MEMBER_KEY = "ommMember";
	
	/**
	 * username
	 */
	public final static String OMM_USERNAME_KEY = "username";
	
	/**
	 * menu信息
	 */
	public final static String OMM_MENU_KEY = "ommMenu";
	
	/**
	 * role信息
	 */
	public final static String OMM_ROLE_KEY = "ommRole";
	
	/**
	 * MemberRelatRole信息
	 */
	public final static String OMM_MEMBER_RELAT_ROLE_KEY = "ommMemberRelatRole";
	
	/**
	 * MenuRelatRole信息
	 */
	public final static String OMM_MENU_RELAT_ROLE_KEY = "ommMenuRelatRole";
	
	/**
	 * 应用操作权限信息
	 */
	public final static String OMM_MENU_AUTH_KEY = "ommMenuAuth";
	
	/**
	 * 数据库权限信息
	 */
	public final static String OMM_MENU_DB_KEY = "ommMenuDB";
	
	/**
	 * memcache权限信息
	 */
	public final static String OMM_MENU_MEMCACHE_KEY = "ommMenuMemcache";
	
	/**
	 * redis权限信息
	 */
	public final static String OMM_MENU_REDIS_KEY = "ommMenuRedis";
	
	/**
	 * jvmcache权限信息
	 */
	public final static String OMM_MENU_JVMCACHE_KEY = "ommMenuJVMCache";
	
	/**
	 * 单点登录权限信息
	 */
	public final static String OMM_MENU_SINGLEPOINT_KEY = "ommMenuSinglePoint";
	
	public final static String OMM_MENU_DB_USERNAME = "username";
	public final static String OMM_MENU_DB_SQLTYPE = "sqltype";
	
	
	public final static String OMM_CODE = "code";
	
	public final static String OMM_MSG = "msg";
	
	public final static String OMM_REQUEST_IP = "requestIp";
	
	public final static String OMM_LOG_INFO = "logInfo";
	
	public final static String RESULT_CODE = "resultCode";
	
	public final static String RESULT_CODE_SUCCESS = "00000";
	
	public final static String RESULT_CODE_FAILURE = "99999";
	
	public final static String RESULT_MSG = "resultMsg";
	
	public final static String MEMBER_TOTAL_PAGE = "totalPage";
	
	public final static String OMM_CONFIG_IP = "omm_config_ip";
	public final static String OMM_CONFIG_DB = "omm_config_db";
	public final static String MEMCACHE_IP = "memcache_ip";
	public final static String CONFIG_DB = "config_db";
	public final static String OMM_DEPT_KEY = "OMM_DEPT";
	public final static String OMM_JVMCACHE_KEY = "OMM_JVMCACHE";
	public final static String OMM_LOG_KEY = "OMM_LOG";
	
	static class SqlType{
		public final static String SELECT = "SELECT";
		public final static String INSERT = "INSERT";
		public final static String UPDATE = "UPDATE";
		public final static String DELETE = "DELETE";
		public final static String CREATE = "CREATE";
		public final static String DROP = "DROP";
	}
	static class DBType{
		public final static String ORACLE = "ORACLE";
		public final static String SUNDB = "SUNDB";
		
	}
	
	
	/**
	 * 加密状态值
	 * 
	 * @author oswin
	 *
	 */
	public enum ENCRYPTTYPE{
		NOT("0", "NOT"), MD5("1", "MD5");
//		DES("0","DES"), DES3("1","DES3"), AES("2","AES"), RSA("3","RSA");
		//构造器默认也只能是private, 从而保证构造函数只能在内部使用
		private final String encrypt;
		private final String encryptType;
		
		private ENCRYPTTYPE(String encryptType, String encrypt) {
            this.encryptType = encryptType;
            this.encrypt = encrypt;
        }

		public String getEncrypt() {
			return encrypt;
		}

		public String getEncryptType() {
			return encryptType;
		}
	}
	
	
	/**
	 * 业务状态值
	 * 
	 * @author oswin
	 *
	 */
	public enum STATE{  
		U("U","可用"), E("E","废弃"), P("P","暂停");
		//构造器默认也只能是private, 从而保证构造函数只能在内部使用
		private final String state;
		private final String stateMsg;
		
		private STATE(String state, String stateMsg) {
            this.state = state;
            this.stateMsg = stateMsg;
        }

		public String getState() {
			return state;
		}

		public String getStateMsg() {
			return stateMsg;
		}
    } 
	
	/**
	 * 菜单属性枚举
	 * 
	 * @author oswin
	 *
	 */
	public enum MENUENUM{
		REDIS("0", "redis"),MEMCACHE("1", "memcache"),JVMCACHE("2", "jvmcache"),DB("3", "DB"),MEMBER("4", "member"),
		ROLE("5", "role"),APPLY("6", "apply"),LOG("7", "log"),SINGLEPOINT("8", "singlepoint");
		
		private final String menuType;
		private final String menu;
		
		private MENUENUM(String menuType, String menu){
			this.menuType = menuType;
			this.menu = menu;
		}

		public String getMenuType() {
			return menuType;
		}

		public String getMenu() {
			return menu;
		}
	}
}
