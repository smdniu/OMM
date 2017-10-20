package com.asiainfo.omm.app.memchace.client.pool;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool.PoolableObjectFactory;

import com.asiainfo.omm.app.memchace.client.validate.ValidateFactory;

public class SocketPoolableObjectFactory  implements PoolableObjectFactory {
	private static transient Log log = LogFactory.getLog(SocketPoolableObjectFactory.class);

	private String host = null;
	private int port = 0;
	private int timeoutSeconds = 0;

	public SocketPoolableObjectFactory(String host, int port, int timeoutSeconds) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	}

	public int getTimeoutSeconds() {
		return this.timeoutSeconds;
	}

	public Object makeObject() throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("开始构造对象");
		}

		Socket socket = null;
		try {
			socket = new Socket();
			socket.setTcpNoDelay(true);
			socket.setKeepAlive(true);
			socket.connect(new InetSocketAddress(InetAddress.getByName(this.host), this.port),
					this.timeoutSeconds * 1000);
			socket.setSoTimeout(this.timeoutSeconds * 1000);
		} catch (Exception ex) {
			throw ex;
		}

		try {
			if (!(ValidateFactory.validateClose(socket)))
				throw new Exception("Socket:" + socket + ",验证失败");
		} catch (Exception ex) {
			if (socket != null) {
				socket.close();
				socket = null;
			}
			throw ex;
		}

		return socket;
	}

	public void destroyObject(Object object) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("销毁对象:" + object);
		}
		if ((object != null) && (object instanceof Socket))
			((Socket) object).close();
	}

	public boolean validateObject(Object object) {
		if (log.isDebugEnabled()) {
			log.debug("验证对象");
		}
		return true;
	}

	public void activateObject(Object object) throws Exception {
		if (log.isDebugEnabled())
			log.debug("激活对象:" + object);
	}

	public void passivateObject(Object object) throws Exception {
		if (log.isDebugEnabled())
			log.debug("去激活对象:" + object);
	}
}
