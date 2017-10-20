package com.asiainfo.omm.app.memchace.client.driver;

import com.ai.appframe2.common.SessionManager;
import com.ai.appframe2.complex.center.CenterFactory;
import com.ai.appframe2.complex.trace.TraceFactory;
import com.ai.appframe2.privilege.UserInfoInterface;
import com.asiainfo.memcached.MemcachedConfigure;
import com.asiainfo.memcached.driver.IMemcachedDriver;
import com.asiainfo.memcached.driver.MemTrace;
import com.asiainfo.memcached.driver.io.IOFactory;
import com.asiainfo.memcached.driver.listener.MemcachedTransactionFactory;
import com.asiainfo.memcached.exception.NormalException;
import com.asiainfo.memcached.exception.ReadNullPointException;
import com.asiainfo.memcached.exception.SocketFlushException;
import com.asiainfo.memcached.policy.Server;
import com.asiainfo.omm.app.memchace.client.policy.LoadBalanceFactory;
import com.asiainfo.omm.app.memchace.client.pool.SocketObjectPool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MemcachedBufferedDriver implements IMemcachedDriver {
	private static transient Log log = LogFactory.getLog(MemcachedBufferedDriver.class);
	private static final int F_COMPRESSED = 2;
	public static final byte[] BYTE_GET = {103, 101, 116, 32};
	public static final byte[] BYTE_SET = {115, 101, 116, 32};
	public static final byte[] BYTE_DELETE = {100, 101, 108, 101, 116, 101, 32};
	public static final byte[] BYTE_CRLF = {13, 10};
	public static final byte[] BYTE_SPACE = {32};
	public static final String SERVER_STATUS_DELETED = "DELETED";
	public static final String SERVER_STATUS_NOT_FOUND = "NOT_FOUND";
	public static final String SERVER_STATUS_STORED = "STORED";
	public static final String SERVER_STATUS_ERROR = "ERROR";
	public static final String SERVER_STATUS_END = "END";
	public static final String SERVER_STATUS_VALUE = "VALUE";
	public static final String ENCODING_TYPE = "UTF-8";
	public static int MAX_BYTE_SIZE = 5242880;

	public static int COMPRESS_THRESHOLD = 102400;

	public boolean set(String key, Object obj) throws Exception {
		if ((obj == null) || (key == null) || ("".equals(key))) {
			throw new Exception("key和value不能为空");
		}

		boolean rtn = false;

		Socket socket = null;
		SocketObjectPool objSocketObjectPool = null;
		try {
			objSocketObjectPool = LoadBalanceFactory.getInstance().getSocketObjectPool();
			socket = (Socket) objSocketObjectPool.borrowObject();

			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());

			key = encodeKey(key);
			int flag = 0;

			byte[] bs = object2Byte(obj);

			if (bs.length > COMPRESS_THRESHOLD) {
				log.error("key为" + key + "的值对象数据超过" + COMPRESS_THRESHOLD + "字节,达到" + bs.length + "字节");
			}

			if (bs.length > COMPRESS_THRESHOLD) {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(bs.length);
				GZIPOutputStream gos = new GZIPOutputStream(bos);
				gos.write(bs, 0, bs.length);
				gos.finish();

				bs = bos.toByteArray();
				flag |= 2;
			}

			if (bs.length >= MAX_BYTE_SIZE) {
				throw new NormalException("不能超过" + MAX_BYTE_SIZE + "字节");
			}

			out.write(BYTE_SET);
			out.write(key.getBytes());
			out.write(BYTE_SPACE);
			out.write(String.valueOf(flag).getBytes());
			out.write(BYTE_SPACE);
			out.write("0".getBytes());
			out.write(BYTE_SPACE);
			out.write(String.valueOf(bs.length).getBytes());
			out.write(BYTE_CRLF);

			out.write(bs);
			out.write(BYTE_CRLF);
			out.flush();

			String ret = readLine(in);
			rtn = "STORED".equals(ret);
			if (!(rtn)) {
				throw new Exception("set出现错误:" + ret);
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			if ((socket != null) && (objSocketObjectPool != null)) {
				objSocketObjectPool.returnObject(socket);
			}
		}
		return rtn;
	}

	public Object get(String key) throws Exception {
		Object rtn = null;
		if (MemcachedTransactionFactory.existTransaction()) {
			rtn = MemcachedTransactionFactory.getFromCache(key);
			if (rtn == null) {
				rtn = _get(key);
				MemcachedTransactionFactory.putIntoCache(key, rtn);
			} else if (rtn.equals(MemcachedTransactionFactory.NULL_OBJECT)) {
				rtn = null;
			}
		} else {
			rtn = _get(key);
		}
		return rtn;
	}

	private Object _get(String key) throws Exception {
		if (StringUtils.isBlank(key)) {
			return null;
		}

		Object rtn = null;

		long start = 0L;

		MemTrace objMemTrace = null;
		if (TraceFactory.isEnableTrace()) {
			objMemTrace = new MemTrace();
			start = System.currentTimeMillis();
			objMemTrace.setCreateTime(start);
			if (CenterFactory.isSetCenterInfo()) {
				objMemTrace.setCenter(
						CenterFactory.getCenterInfo().getRegion() + "," + CenterFactory.getCenterInfo().getCenter());
			}

			UserInfoInterface user = SessionManager.__getUserWithOutLog();
			if ((user != null) && (user.getCode() != null)) {
				objMemTrace.setCode(user.getCode());
			}

			objMemTrace.setIn(new Object[]{key});
		}

		Socket socket = null;
		SocketObjectPool objSocketObjectPool = null;
		try {
			long getTimeStart = 0L;
			if (objMemTrace != null) {
				getTimeStart = System.currentTimeMillis();
			}

			objSocketObjectPool = LoadBalanceFactory.getInstance().getSocketObjectPool();
			socket = (Socket) objSocketObjectPool.borrowObject();

			if ((objMemTrace != null) && (socket != null) && (socket.getRemoteSocketAddress() != null)) {
				InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
				objMemTrace.setHost(address.getHostName() + ":" + address.getPort());
				if (getTimeStart > 0L) {
					objMemTrace.setGetTime((int) (System.currentTimeMillis() - getTimeStart));
				}

			}

			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			key = encodeKey(key);
			out.write(BYTE_GET);
			out.write(key.getBytes());
			out.write(BYTE_CRLF);
			try {
				out.flush();
			} catch (Exception ex) {
				throw new SocketFlushException(ex);
			}

			rtn = getObjectFromStream(in, out);

			if (objMemTrace != null) {
				objMemTrace.setSuccess(true);
				objMemTrace.setUseTime((int) (System.currentTimeMillis() - start));
				if (rtn != null) {
					objMemTrace.setCount(1);
				}
				TraceFactory.addTraceInfo(objMemTrace);
			}
		} catch (Exception ex) {
			if (ex instanceof ReadNullPointException) {
				String host = objSocketObjectPool.getHost();
				int port = objSocketObjectPool.getPort();
				int timeoutSeconds = objSocketObjectPool.getTimeoutSeconds();
				synchronized (this) {
					log.error("发生ReadNullPointException异常，删除连接池:", ex);
					LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
					log.error("立即建立连接池[" + host + ":" + port + "]");
					LoadBalanceFactory.getInstance()
							.addPool(LoadBalanceFactory.getInstance().makePool(new Server(host, port, timeoutSeconds)));
				}
			} else if (ex instanceof SocketFlushException) {
				String host = objSocketObjectPool.getHost();
				int port = objSocketObjectPool.getPort();
				int timeoutSeconds = objSocketObjectPool.getTimeoutSeconds();
				synchronized (this) {
					log.error("发生SocketFlushException异常，删除连接池:", ex);
					LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
					log.error("立即建立连接池[" + host + ":" + port + "]");
					LoadBalanceFactory.getInstance()
							.addPool(LoadBalanceFactory.getInstance().makePool(new Server(host, port, timeoutSeconds)));
				}
			} else {
				log.error("发生异常，删除连接池:", ex);
				LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
			}

			if (objMemTrace != null)
				;
			throw ex;
		} finally {
			if ((socket != null) && (objSocketObjectPool != null)) {
				objSocketObjectPool.returnObject(socket);
			}
		}
		return rtn;
	}

	public Map get(String[] key) throws Exception {
		Map rtn = null;
		if (MemcachedTransactionFactory.existTransaction()) {
			rtn = MemcachedTransactionFactory.getFromCache(key);
			if (rtn == null) {
				rtn = _get(key);
				MemcachedTransactionFactory.putIntoCache(key, rtn);
			} else if (rtn.equals(MemcachedTransactionFactory.NULL_OBJECT)) {
				rtn = null;
			}
		} else {
			rtn = _get(key);
		}
		return rtn;
	}

	private Map _get(String[] key) throws Exception {
		if ((key == null) || (key.length == 0)) {
			return new HashMap();
		}

		Map rtn = null;

		long start = 0L;

		MemTrace objMemTrace = null;
		if (TraceFactory.isEnableTrace()) {
			objMemTrace = new MemTrace();
			start = System.currentTimeMillis();
			objMemTrace.setCreateTime(start);
			if (CenterFactory.isSetCenterInfo()) {
				objMemTrace.setCenter(
						CenterFactory.getCenterInfo().getRegion() + "," + CenterFactory.getCenterInfo().getCenter());
			}

			UserInfoInterface user = SessionManager.__getUserWithOutLog();
			if ((user != null) && (user.getCode() != null)) {
				objMemTrace.setCode(user.getCode());
			}

			objMemTrace.setIn(new Object[]{key});
		}

		Socket socket = null;
		SocketObjectPool objSocketObjectPool = null;
		try {
			long getTimeStart = 0L;
			if (objMemTrace != null) {
				getTimeStart = System.currentTimeMillis();
			}

			objSocketObjectPool = LoadBalanceFactory.getInstance().getSocketObjectPool();
			socket = (Socket) objSocketObjectPool.borrowObject();

			if ((objMemTrace != null) && (socket != null) && (socket.getRemoteSocketAddress() != null)) {
				InetSocketAddress address = (InetSocketAddress) socket.getRemoteSocketAddress();
				objMemTrace.setHost(address.getHostName() + ":" + address.getPort());
				if (getTimeStart > 0L) {
					objMemTrace.setGetTime((int) (System.currentTimeMillis() - getTimeStart));
				}

			}

			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write(BYTE_GET);

			for (int i = 0; i < key.length; ++i) {
				if (StringUtils.isBlank(key[i])) {
					throw new Exception("获得多个值,其中有key为空的情况");
				}
				key[i] = encodeKey(key[i]);
				out.write(key[i].getBytes());
				if (i != key.length - 1) {
					out.write(BYTE_SPACE);
				}
			}

			out.write(BYTE_CRLF);
			try {
				out.flush();
			} catch (Exception ex) {
				throw new SocketFlushException(ex);
			}

			rtn = getObjectArrayFromStream(in, out);

			if (objMemTrace != null) {
				objMemTrace.setSuccess(true);
				objMemTrace.setUseTime((int) (System.currentTimeMillis() - start));
				if (rtn != null) {
					int count = 0;
					Collection c = rtn.values();
					for (Iterator iter = c.iterator(); iter.hasNext();) {
						Object item = iter.next();
						if (item != null) {
							++count;
						}
					}
					objMemTrace.setCount(count);
				}
				TraceFactory.addTraceInfo(objMemTrace);
			}
		} catch (Exception ex) {
			if (ex instanceof ReadNullPointException) {
				String host = objSocketObjectPool.getHost();
				int port = objSocketObjectPool.getPort();
				int timeoutSeconds = objSocketObjectPool.getTimeoutSeconds();
				synchronized (this) {
					log.error("发生ReadNullPointException异常，删除连接池:", ex);
					LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
					log.error("立即建立连接池[" + host + ":" + port + "]");
					LoadBalanceFactory.getInstance()
							.addPool(LoadBalanceFactory.getInstance().makePool(new Server(host, port, timeoutSeconds)));
				}
			} else if (ex instanceof SocketFlushException) {
				String host = objSocketObjectPool.getHost();
				int port = objSocketObjectPool.getPort();
				int timeoutSeconds = objSocketObjectPool.getTimeoutSeconds();
				synchronized (this) {
					log.error("发生SocketFlushException异常，删除连接池:", ex);
					LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
					log.error("立即建立连接池[" + host + ":" + port + "]");
					LoadBalanceFactory.getInstance()
							.addPool(LoadBalanceFactory.getInstance().makePool(new Server(host, port, timeoutSeconds)));
				}
			} else {
				log.error("发生异常，删除连接池:", ex);
				LoadBalanceFactory.getInstance().deletePool(objSocketObjectPool);
			}

			if (objMemTrace != null)
				;
			throw ex;
		} finally {
			if ((socket != null) && (objSocketObjectPool != null)) {
				objSocketObjectPool.returnObject(socket);
			}
		}
		return rtn;
	}

	public Object get(Socket socket, String key) throws Exception {
		Object rtn = null;
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			key = encodeKey(key);
			out.write(BYTE_GET);
			out.write(key.getBytes());
			out.write(BYTE_CRLF);

			out.flush();
			rtn = getObjectFromStream(in, out);
		} catch (Exception ex) {
			throw ex;
		}
		return rtn;
	}

	public Map get(Socket socket, String[] key) throws Exception {
		if ((key == null) || (key.length == 0)) {
			return null;
		}

		Map rtn = null;
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write(BYTE_GET);

			for (int i = 0; i < key.length; ++i) {
				if (StringUtils.isBlank(key[i])) {
					throw new Exception("获得多个值,其中有key为空的情况");
				}
				key[i] = encodeKey(key[i]);
				out.write(key[i].getBytes());
				if (i != key.length - 1) {
					out.write(BYTE_SPACE);
				}
			}

			out.write(BYTE_CRLF);

			out.flush();
			rtn = getObjectArrayFromStream(in, out);
		} catch (Exception ex) {
			throw ex;
		}
		return rtn;
	}

	public boolean delete(String key) throws Exception {
		boolean rtn = false;

		Socket socket = null;
		SocketObjectPool objSocketObjectPool = null;
		try {
			objSocketObjectPool = LoadBalanceFactory.getInstance().getSocketObjectPool();
			socket = (Socket) objSocketObjectPool.borrowObject();

			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			key = encodeKey(key);
			out.write(BYTE_DELETE);
			out.write(key.getBytes());
			out.write(BYTE_CRLF);
			out.flush();

			String ret = readLine(in);
			rtn = ("DELETED".equals(ret)) || ("NOT_FOUND".equals(ret));
		} catch (Exception ex) {
			throw ex;
		} finally {
			if ((socket != null) && (objSocketObjectPool != null)) {
				objSocketObjectPool.returnObject(socket);
			}
		}
		return rtn;
	}

	public HashMap stats() throws Exception {
		HashMap rtn = new HashMap();

		Socket socket = null;
		SocketObjectPool objSocketObjectPool = null;
		try {
			objSocketObjectPool = LoadBalanceFactory.getInstance().getSocketObjectPool();
			socket = (Socket) objSocketObjectPool.borrowObject();
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats".getBytes());
			out.write(BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!("END".equals(cmd))) {
				String[] yh = StringUtils.split(cmd, " ");
				rtn.put(yh[1], yh[2]);
				cmd = readLine(in);
			}

		} catch (Exception ex) {
			throw ex;
		} finally {
			if ((socket != null) && (objSocketObjectPool != null)) {
				objSocketObjectPool.returnObject(socket);
			}
		}
		return rtn;
	}

	public HashMap stats(Socket socket) throws Exception {
		HashMap rtn = new HashMap();
		try {
			BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
			BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
			out.write("stats".getBytes());
			out.write(MemcachedBufferedDriver.BYTE_CRLF);

			out.flush();

			rtn.put("host", socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
			String cmd = readLine(in);
			while (!("END".equals(cmd))) {
				String[] yh = StringUtils.split(cmd, " ");
				rtn.put(yh[1], yh[2]);
				cmd = readLine(in);
			}
		} catch (Exception ex) {
			throw ex;
		}
		return rtn;
	}

	private Object getObjectFromStream(InputStream in, OutputStream out)
			throws ReadNullPointException, IOException, ClassNotFoundException {
		String cmd = readLine(in);

		if (cmd == null) {
			throw new ReadNullPointException("读取命令出现返回空指针");
		}

		if (cmd.startsWith("VALUE")) {
			String[] part = StringUtils.split(cmd, " ");
			int flag = Integer.parseInt(part[2]);
			int length = Integer.parseInt(part[3]);

			byte[] bs = new byte[length];

			int count = 0;
			while (count < bs.length) {
				count += in.read(bs, count, bs.length - count);
			}
			if (count != bs.length) {
				throw new IOException("读取数据长度错误");
			}

			readLine(in);

			String endstr = readLine(in);
			if ("END".equals(endstr)) {
				if ((flag & 0x2) != 0) {
					GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(bs));
					ByteArrayOutputStream bos = new ByteArrayOutputStream(bs.length);

					count = 0;
					byte[] tmp = new byte[2048];
					while ((count = gzi.read(tmp)) != -1) {
						bos.write(tmp, 0, count);
					}

					bs = bos.toByteArray();
					gzi.close();
				}
				try{
					return byte2Object(bs);
				}catch(Exception e){
					e.printStackTrace();
					return new String(bs);
				}
			}

			throw new IOException("结束标记错误");
		}

		return null;
	}

	private HashMap getObjectArrayFromStream(InputStream in, OutputStream out)
			throws ReadNullPointException, IOException, ClassNotFoundException {
		HashMap map = new HashMap();
		while (true) {
			String cmd = readLine(in);

			if (cmd == null) {
				throw new ReadNullPointException("读取命令出现返回空指针");
			}

			if (cmd.startsWith("VALUE")) {
				String[] part = StringUtils.split(cmd, " ");
				String key = part[1].trim();
				int flag = Integer.parseInt(part[2]);
				int length = Integer.parseInt(part[3]);

				byte[] bs = new byte[length];

				int count = 0;
				while (count < bs.length) {
					count += in.read(bs, count, bs.length - count);
				}
				if (count != bs.length) {
					throw new IOException("读取数据长度错误");
				}

				if ((flag & 0x2) != 0) {
					GZIPInputStream gzi = new GZIPInputStream(new ByteArrayInputStream(bs));
					ByteArrayOutputStream bos = new ByteArrayOutputStream(bs.length);

					count = 0;
					byte[] tmp = new byte[2048];
					while ((count = gzi.read(tmp)) != -1) {
						bos.write(tmp, 0, count);
					}

					bs = bos.toByteArray();
					gzi.close();
				}

				map.put(decodeKey(key), byte2Object(bs));

				cmd = readLine(in);
			} else {
				if ("END".equals(cmd)) {
					break;
				}

				throw new IOException("结束标记错误");
			}
		}

		return map;
	}

	private String decodeKey(String key) throws UnsupportedEncodingException {
		return URLDecoder.decode(key, "UTF-8");
	}

	private String encodeKey(String key) throws UnsupportedEncodingException {
		return URLEncoder.encode(key, "UTF-8");
	}

	private String readLine(InputStream in) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		boolean eol = false;
		byte[] b = new byte[1];
		while (in.read(b, 0, 1) != -1) {
			if (b[0] == 13) {
				eol = true;
			} else {
				if ((eol) && (b[0] == 10)) {
					break;
				}

				eol = false;
			}

			bos.write(b, 0, 1);
		}

		if (bos.size() == 0) {
			return null;
		}
		return bos.toString().trim();
	}

	private byte[] object2Byte(Object o) throws IOException {
		return IOFactory.object2bytes(o);
	}

	private Object byte2Object(byte[] b) throws IOException, ClassNotFoundException {
		return IOFactory.bytes2object(b);
	}

	static {
		try {
			String compress = MemcachedConfigure.getProperties().getProperty("server.compress_threshold");
			if ((!(StringUtils.isBlank(compress))) && (StringUtils.isNumeric(compress))) {
				COMPRESS_THRESHOLD = Integer.parseInt(compress);
			}

			String max = MemcachedConfigure.getProperties().getProperty("server.server.max_byte_size");
			if ((!(StringUtils.isBlank(max))) && (StringUtils.isNumeric(max)))
				MAX_BYTE_SIZE = Integer.parseInt(max);
		} catch (Exception ex) {
			log.error("获得压缩阀值或者最大字节数出错,采用默认值,不影响系统运行", ex);
		}
	}
}