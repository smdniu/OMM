package com.asiainfo.omm.app.memchace.client.validate;

import com.asiainfo.memcached.MemcachedConfigure;
import com.asiainfo.memcached.driver.IMemcachedDriver;
import com.asiainfo.memcached.validate.IValidate;
import com.asiainfo.omm.app.memchace.client.driver.MemcachedBufferedDriver;

import java.net.Socket;
import java.util.Properties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ValidateFactory {
	private static transient Log log = LogFactory.getLog(ValidateFactory.class);
	
	public static boolean validateClose(Socket socket) {
		if ((socket == null) || (!(socket.isConnected()))) {
			log.error("连接已经关闭或者连接不存在");
			return false;
		}
		try{
			IMemcachedDriver objMemcachedDriver = new MemcachedBufferedDriver();
			objMemcachedDriver.stats(socket);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	
	public static boolean validate(Socket socket) {
		if ((socket == null) || (!(socket.isConnected()))) {
			log.error("连接已经关闭或者连接不存在");
			return false;
		}
		boolean rtn = false;
		try {
			Properties properties = MemcachedConfigure.getProperties("server.validate", true);
			if ((properties == null) || (properties.size() == 0)) {
				if (log.isDebugEnabled()) {
					log.debug("不需要验证");
				}
				rtn = true;
			} else{
				IValidate objIValidate = new NormalValidateImpl();
				rtn = objIValidate.validate(socket, properties);
			}
		} catch (Exception ex) {
			log.error("验证发生错误:"+ex.getMessage());
			rtn = false;
		}
		return rtn;
	}
}
