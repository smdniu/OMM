package com.asiainfo.omm.app.memchace;

import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;


import com.asiainfo.memcached.MemcachedConfigure;
import com.asiainfo.memcached.driver.IMemcachedDriver;
import com.asiainfo.memcached.policy.Server;
import com.asiainfo.omm.app.memchace.client.CheckTask;
import com.asiainfo.omm.app.memchace.client.driver.MemcachedBufferedDriver;
import com.asiainfo.omm.app.memchace.client.policy.LoadBalanceFactory;
import com.asiainfo.omm.app.memchace.client.pool.SocketObjectPool;
import com.asiainfo.omm.app.memchace.client.validate.ValidateFactory;
import com.asiainfo.omm.constant.OMMConstantEnum;
import com.asiainfo.omm.utils.SessionFactory;

public class MemcacheTool {
	private static IMemcachedDriver driver = new MemcachedBufferedDriver();
	static{
		try {
			Timer timer = new Timer(true);
			int delay = 0;
			int period = 0;
			try {
				String strDelay = MemcachedConfigure.getProperties("server.checktask", true)
						.getProperty("delay");
				String strPeriod = MemcachedConfigure.getProperties("server.checktask", true)
						.getProperty("period");
				delay = Integer.parseInt(strDelay) * 1000;
				period = Integer.parseInt(strPeriod) * 1000;
			} catch (Exception ex) {
				delay = 5000;
				period = 30000;
			}
			timer.schedule(new CheckTask(), delay, period);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addServers(List<String> hosts){
		try{
			if(hosts != null && !hosts.isEmpty()){
				for(int i = 0 ;i<hosts.size();i++){
					LoadBalanceFactory.getInstance().addServer(hosts.get(i));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map getStat(String ip,int port){
		Map stat = null;
		try{
			Object[] ok_servers = LoadBalanceFactory.getInstance().getArivableServers();
			for(int i = 0 ;i<ok_servers.length;i++){
				SocketObjectPool objSocketObjectPool = (SocketObjectPool) ok_servers[i];
				Socket socket = null;
				try{
					
					if(!(ip+":"+port).equals(objSocketObjectPool.getHost()+":"+objSocketObjectPool.getPort())){
						continue;
					}
					socket = (Socket) objSocketObjectPool.borrowObject();
					stat = driver.stats(socket);
					stat.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_SUCCESS);
					if(ValidateFactory.validate(socket)){
						stat.put(OMMConstantEnum.RESULT_MSG, "验证通过");
					}else{
						stat.put(OMMConstantEnum.RESULT_MSG, "验证失败");
					}
					break;
				}catch(Exception e){
					stat = new HashMap();
					stat.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
					stat.put(OMMConstantEnum.RESULT_MSG, "连接失败");
				}finally{
					if ((socket != null) && (objSocketObjectPool != null)) {
						objSocketObjectPool.returnObject(socket);
					}
				}
			}
		}catch(Exception e){
			stat = new HashMap();
			stat.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			stat.put(OMMConstantEnum.RESULT_MSG, "连接失败");
		}
		if(stat == null){
			stat = new HashMap();
			stat.put(OMMConstantEnum.RESULT_CODE, OMMConstantEnum.RESULT_CODE_FAILURE);
			stat.put(OMMConstantEnum.RESULT_MSG, "连接失败");
		}
		return stat;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Map getAllStats(){
		Map<String,Map> stats = new HashMap<String,Map>();
		try {
			List<String> all_servers = (List<String>)SessionFactory.getSession().getAttribute(OMMConstantEnum.OMM_MENU_MEMCACHE_KEY);
			for (Iterator iter = all_servers.iterator(); iter.hasNext();) {
				String host = (String) iter.next();
				String[] temp = host.split(":");
				if(temp.length != 2){
					continue;
				}
				String ip = temp[0];
				int port = Integer.parseInt(temp[1]);
				stats.put(host, getStat(ip,port));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return stats;
	}
	
	public static Object getValue(String ip,int port,String key) throws Exception{
		try{
			Object[] ok_servers = LoadBalanceFactory.getInstance().getArivableServers();
			for(int i = 0 ;i<ok_servers.length;i++){
				SocketObjectPool objSocketObjectPool = (SocketObjectPool) ok_servers[i];
				Socket socket = null;
				try{
					
					if(!(ip+":"+port).equals(objSocketObjectPool.getHost()+":"+objSocketObjectPool.getPort())){
						continue;
					}
					socket = (Socket) objSocketObjectPool.borrowObject();
					return driver.get(key);
				}catch(Exception e){
					throw e;
				}finally{
					if ((socket != null) && (objSocketObjectPool != null)) {
						objSocketObjectPool.returnObject(socket);
					}
				}
			}
		}catch(Exception e){
			throw e;
		}
		return null;
	}
	
	
	public static void main(String[] args) throws Exception {
		
		while(true){
			System.out.println(MemcacheTool.getAllStats());
			Thread.sleep(1000);
		}
		
	}
}	
