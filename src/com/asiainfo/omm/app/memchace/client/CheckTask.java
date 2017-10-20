package com.asiainfo.omm.app.memchace.client;

import com.asiainfo.memcached.policy.Server;
import com.asiainfo.omm.app.memchace.client.policy.LoadBalanceFactory;
import com.asiainfo.omm.app.memchace.client.pool.SocketObjectPool;
import com.asiainfo.omm.app.memchace.client.validate.ValidateFactory;

import java.net.Socket;
import java.util.Collection;
import java.util.Iterator;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CheckTask extends TimerTask {
	private static transient Log log = LogFactory.getLog(CheckTask.class);
		
	public void run() {
		Thread.currentThread().setName("memcached检查线程");

		if (log.isDebugEnabled())
			log.debug("检查线程开始工作");
		Object[] ok_servers;
		Iterator iter;
		try {
			ok_servers = LoadBalanceFactory.getInstance().getArivableServers();
			for(int i = 0 ;i<ok_servers.length;i++){
				SocketObjectPool objSocketObjectPool = (SocketObjectPool) ok_servers[i];
				Socket socket = null;
				try {
					socket = (Socket) objSocketObjectPool.borrowObject();
					if (!ValidateFactory.validateClose(socket)) {
						throw new Exception("Socket:" + socket + ",验证失败");
					} 
				} catch (Exception ex1) {
					if (objSocketObjectPool != null) {
						if (log.isDebugEnabled()) {
							log.debug("检查线程发现连接池:" + objSocketObjectPool.toString() + "验证失败,"+ex1.getMessage());
						}
						LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
					}
				}finally{
					if ((socket != null) && (objSocketObjectPool != null)){
						objSocketObjectPool.returnObject(socket);
					}
				}
			}
			ok_servers = LoadBalanceFactory.getInstance().getArivableServers();
			Collection<Server> all_servers = LoadBalanceFactory.getInstance().getAllServers();
			String key;
			for (iter = all_servers.iterator(); iter.hasNext();) {
				Server item = (Server) iter.next();
				key = item.getHost()+":"+item.getPort();
				if (!(isArivableServer(item, ok_servers))) {
					SocketObjectPool objSocketObjectPool = LoadBalanceFactory.getInstance().makePool(item);
					Socket socket = null;
					try {
						socket = (Socket) objSocketObjectPool.borrowObject();

						if (ValidateFactory.validateClose(socket)) {
							LoadBalanceFactory.getInstance().addPool(objSocketObjectPool);
							if (log.isDebugEnabled())
								log.debug("检查线程发现连接池:" + objSocketObjectPool.toString() + "验证成功");
						} else {
							throw new Exception("Socket:" + socket + ",验证失败");
						}
					} catch (Exception ex1) {
						if (objSocketObjectPool != null) {
							if (log.isDebugEnabled()) {
								log.debug("检查线程发现连接池:" + objSocketObjectPool.toString() + "验证失败,"+ex1.getMessage());
							}
							objSocketObjectPool.clear();
							objSocketObjectPool = null;
						}
					} finally {
						if ((socket != null) && (objSocketObjectPool != null)){
							objSocketObjectPool.returnObject(socket);
						}
						
					}
				}
			}
		} catch (Throwable ex) {
			log.debug("检查线程检查时候发现外层异常", ex);
		}

		if (log.isDebugEnabled())
			log.debug("检查线程完成工作");
	}

	public boolean isArivableServer(Server server, Object[] objects) {
		boolean rtn = false;
		for (int i = 0; i < objects.length; ++i) {
			SocketObjectPool objSocketObjectPool = (SocketObjectPool) objects[i];
			if ((!(objSocketObjectPool.getHost().equalsIgnoreCase(server.getHost())))
					|| (objSocketObjectPool.getPort() != server.getPort()))
				continue;
			rtn = true;
			break;
		}

		return rtn;
	}

}
