package com.asiainfo.omm.app.memchace.client.policy;

import com.asiainfo.memcached.MemcachedConfigure;
import com.asiainfo.memcached.policy.IPolicy;
import com.asiainfo.memcached.policy.RoundRobinPolicy;
import com.asiainfo.memcached.policy.Server;
import com.asiainfo.omm.app.memchace.client.pool.SocketObjectPool;
import com.asiainfo.omm.app.memchace.client.pool.SocketPoolableObjectFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LoadBalanceFactory {
	private static transient Log log = LogFactory.getLog(LoadBalanceFactory.class);

	private static LoadBalanceFactory instance = null;

	private IPolicy objIPolicy = null;

	private Map<String,Server> servers = new HashMap<String,Server>();
	
	private int timeoutSeconds = 30;
	
	private static Boolean isInit = Boolean.FALSE;

	private String serverName = null;

	public static LoadBalanceFactory getInstance() throws Exception {
		if (instance == null) {
			synchronized (isInit) {
				if (isInit.equals(Boolean.FALSE)) {
					instance = new LoadBalanceFactory();
					isInit = Boolean.TRUE;
				}
			}
		}
		return instance;
	}

	private LoadBalanceFactory() throws Exception {
		synchronized (this) {
			try {
				this.objIPolicy = ((IPolicy) Class
						.forName(MemcachedConfigure.getProperties().getProperty("server.policy")).newInstance());
			} catch (Exception ex) {
				log.error("转换出错,取默认配置", ex);
				this.objIPolicy = new RoundRobinPolicy();
			}

			String strTimeoutSeconds = MemcachedConfigure.getProperties().getProperty("server.conn.timeout");
			if ((!(StringUtils.isBlank(strTimeoutSeconds))) && (StringUtils.isNumeric(strTimeoutSeconds))) {
				timeoutSeconds = Integer.parseInt(strTimeoutSeconds);
			}

			this.serverName = MemcachedConfigure.getProperties().getProperty("server.name");
			if (StringUtils.isBlank(this.serverName))
				this.serverName = "";
		}
	}
	
	public void addServer(String host) throws Exception {
		log.info("添加["+host+"]到memcache监控");
		if(!StringUtils.isBlank(host)){
			String[] tmp = host.split(":");
			if(tmp.length == 2){
				Server server = new Server(tmp[0], Integer.parseInt(tmp[1]), timeoutSeconds);
				if(!this.servers.containsKey(host)){
					this.servers.put(host,server);
					addPool(makePool(server));
				}
			}
		}
	}

	public SocketObjectPool makePool(Server server) throws Exception {
		SocketPoolableObjectFactory factory = new SocketPoolableObjectFactory(server.getHost(), server.getPort(),
				server.getTimeoutSeconds());
		SocketObjectPool pool = new SocketObjectPool(factory);
		return pool;
	}

	public SocketObjectPool getSocketObjectPool() throws Exception {
		if (this.objIPolicy.size() == 0) {
			throw new Exception(this.serverName + "均衡工厂中已经没有可使用的pool了");
		}
		SocketObjectPool rtn = (SocketObjectPool) this.objIPolicy.getPolicyObject();
		if (rtn == null) {
			throw new Exception(this.serverName + "均衡工厂中已经没有可使用的pool了");
		}
		return rtn;
	}

	public void deletePool(SocketObjectPool objSocketObjectPool) {
		synchronized (this.objIPolicy) {
			if (this.objIPolicy.contains(objSocketObjectPool)) {
				if (log.isErrorEnabled()) {
					log.error("删除连接池:" + objSocketObjectPool);
				}

				this.objIPolicy.remove(objSocketObjectPool);
				objSocketObjectPool.clear();
				objSocketObjectPool = null;
			}
		}
	}

	public void addPool(SocketObjectPool objSocketObjectPool) {
		synchronized (this.objIPolicy) {
			this.objIPolicy.add(objSocketObjectPool);
		}
	}

	public Collection<Server> getAllServers() {
		return this.servers.values();
	}

	public Object[] getArivableServers() {
		return this.objIPolicy.toArray();
	}
}
