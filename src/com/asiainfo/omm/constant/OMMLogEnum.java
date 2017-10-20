package com.asiainfo.omm.constant;

public interface OMMLogEnum {

	/**
	 * 日志类型枚举
	 * 
	 * @author oswin
	 *
	 */
	public enum LOG{
		WEB("0","WEB"), SRV("1","SRV"), DAO("2","DAO");
		private final String log;
		private final String logType;
		
		private LOG(String logType, String log) {
			this.log = log;
			this.logType = logType;
		}
		public String getLog() {
			return log;
		}
		public String getLogType() {
			return logType;
		}
	}
}
