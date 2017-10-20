package com.asiainfo.omm.app.memchace.client.validate;

import com.asiainfo.memcached.driver.IMemcachedDriver;
import com.asiainfo.memcached.validate.IValidate;
import com.asiainfo.omm.app.memchace.client.driver.MemcachedBufferedDriver;

import java.net.Socket;
import java.util.Properties;

public class NormalValidateImpl implements IValidate {

	public boolean validate(Socket socket, Properties properties) throws Exception {
		boolean rtn = false;
		try {
			String key = properties.getProperty("key");
			String value1 = properties.getProperty("value");

			IMemcachedDriver objMemcachedDriver = new MemcachedBufferedDriver();
			String value2 = (String) objMemcachedDriver.get(socket, key);
			if (value2 == null) {
				throw new Exception("在memcached中没有设置对应的key的value");
			}
			if (value2.equals(value1))
				rtn = true;
		} catch (Exception ex) {
			throw ex;
		}
		return rtn;
	}
}
