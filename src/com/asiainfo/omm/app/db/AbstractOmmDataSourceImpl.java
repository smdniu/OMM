package com.asiainfo.omm.app.db;

import com.ai.appframe2.complex.datasource.DataBaseConnectURL;
import com.ai.appframe2.complex.datasource.interfaces.IDataSource;
import com.ai.appframe2.complex.util.RuntimeServerUtil;
import com.ai.appframe2.complex.util.e.K;
import com.ai.appframe2.complex.xml.XMLHelper;
import com.ai.appframe2.complex.xml.cfg.defaults.Pool;
import com.ai.appframe2.complex.xml.cfg.defaults.Property;
import com.ai.appframe2.util.locale.AppframeLocaleFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractOmmDataSourceImpl implements IDataSource {
	private static transient Log log = LogFactory.getLog(AbstractOmmDataSourceImpl.class);

	protected static final HashMap DATASOURCE_MAP = new HashMap();
	protected static final HashMap URL_MAP = new HashMap();
	//数据库中code 与 user_name的对应关系
	protected static final Map<String,Map<String,String>> DB_ACCT_NAME = new HashMap<String,Map<String,String>>();
	protected static String primaryDataSource = null;
	

	public AbstractOmmDataSourceImpl() throws Exception {
		Pool[] pool = XMLHelper.getInstance().getDefaults().getDatasource().getPools();
		
		List left = new ArrayList();
		Pool primary = null;
		for (int i = 0; i < pool.length; ++i) {
			if ((!(StringUtils.isBlank(pool[i].getPrimary()))) && (pool[i].getPrimary().equalsIgnoreCase("true"))) {
				if (primary == null) {
					primary = pool[i];
					continue;
				}

				String msg = AppframeLocaleFactory
						.getResource("com.ai.appframe2.complex.datasource.impl.multiple_dspool");
				throw new Exception(msg);
			}
			left.add(pool[i]);
		}
		if (primary == null) {
			String msg = AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.multiple_dspool");
			throw new Exception(msg);
		}

		primaryDataSource = primary.getName();

		boolean isPrefetch = false;
		Property[] clazzProperty = XMLHelper.getInstance().getDefaults().getDatasource().getClazz().getProperties();
		if (clazzProperty != null){
			for (int i = 0; i < clazzProperty.length; ++i) {
				if ((!(clazzProperty[i].getName().equalsIgnoreCase("prefetch")))
						|| (!(clazzProperty[i].getValue().equalsIgnoreCase("true"))))
					continue;
				isPrefetch = true;
				break;
			}
		}
		HashMap map;
		Iterator iter;
		if (isPrefetch) {
			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.prefetch_type"));
			}

			Properties prefetchPoolProperties = new Properties();
			for (int i = 0; i < clazzProperty.length; ++i) {
				if (clazzProperty[i].getName().indexOf("prefetch.") != -1) {
					prefetchPoolProperties.setProperty(
							StringUtils.replace(clazzProperty[i].getName(), "prefetch.", "").trim(),
							clazzProperty[i].getValue());
				}
			}

			if (log.isDebugEnabled()) {
				log.debug(
						AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.prefetch_set_over",
								new String[]{maskPassword(prefetchPoolProperties).toString()}));
			}

			javax.sql.DataSource prefetchDataSource = BasicDataSourceFactory
					.createDataSource(k(prefetchPoolProperties));
			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory
						.getResource("com.ai.appframe2.complex.datasource.impl.prefetch_ds_create_succeed"));
			}

			String dbAcctTable = "cfg_db_acct";
			String dbUrlTable = "cfg_db_url";
			String dbRelatTable = "cfg_db_relat";
			for (int i = 0; i < clazzProperty.length; ++i) {
				if (clazzProperty[i].getName().equalsIgnoreCase("tableName")) {
					dbAcctTable = clazzProperty[i].getValue().trim();
				} else if (clazzProperty[i].getName().equalsIgnoreCase("urlTableName")) {
					dbUrlTable = clazzProperty[i].getValue().trim();
				} else if (clazzProperty[i].getName().equalsIgnoreCase("relatTableName")) {
					dbRelatTable = clazzProperty[i].getValue().trim();
				}

			}

			left.add(primary);
			
			//添加所有连接
			left.addAll(getAllDbAcct(prefetchDataSource.getConnection(),dbAcctTable));
			
			map = getPoolCompletionInfoWhenPrefetch(prefetchDataSource, dbAcctTable, dbUrlTable, dbRelatTable,
					(Pool[]) (Pool[]) left.toArray(new Pool[0]));
			Set keys = map.keySet();
			for (iter = keys.iterator(); iter.hasNext();) {
				Pool item = (Pool) iter.next();
				try {
					Properties leftPoolProperties = (Properties) map.get(item);
					javax.sql.DataSource leftDataSource = BasicDataSourceFactory
							.createDataSource(k(leftPoolProperties));

					if ((!(StringUtils.isBlank(item.getInit()))) && (item.getInit().equalsIgnoreCase("true"))) {
						initConnection(leftDataSource);

						if (log.isDebugEnabled()) {
							log.debug(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.impl.prefetch_ds_init",
									new String[]{item.getName().trim()}));
						}
					}

					DATASOURCE_MAP.put(item.getName().trim(), leftDataSource);

					StringBuilder sb = new StringBuilder();
					Set set = leftPoolProperties.keySet();
					for (Iterator tmp = set.iterator(); tmp.hasNext();) {
						String tmpItem = (String) tmp.next();
						if (!(tmpItem.equalsIgnoreCase("password"))) {
							sb.append(tmpItem + "=" + leftPoolProperties.getProperty(tmpItem) + ",");
						}
					}
					log.error(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.prefetch_ds_create_attr",
							new String[]{item.getName().trim(), sb.toString()}));
				} catch (Exception ex) {
					log.error(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.prefetch_ds_create_failed",
							new String[]{item.getName().trim()}), ex);
				}

			}

			((BasicDataSource) prefetchDataSource).close();
			prefetchDataSource = null;
			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory
						.getResource("com.ai.appframe2.complex.datasource.impl.prefetch_ds_destroy_succeed"));
			}
		} else {
			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.common_type"));
			}

			Properties basePoolProperties = new Properties();
			Property[] baseProperty = primary.getProperties();
			for (int i = 0; i < baseProperty.length; ++i) {
				basePoolProperties.setProperty(baseProperty[i].getName(), baseProperty[i].getValue());
			}

			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.common_set_over",
						new String[]{primary.getName().trim(), maskPassword(basePoolProperties).toString()}));
			}

			javax.sql.DataSource baseDataSource = BasicDataSourceFactory.createDataSource(k(basePoolProperties));
			DATASOURCE_MAP.put(primary.getName().trim(), baseDataSource);

			URL_MAP.put(primary.getName().trim(), basePoolProperties.getProperty("url"));

			if (log.isDebugEnabled()) {
				log.debug(AppframeLocaleFactory.getResource(
						"com.ai.appframe2.complex.datasource.impl.common_baseds_create_succeed",
						new String[]{primary.getName().trim()}));
			}

			Property[] tableProperties = XMLHelper.getInstance().getDefaults().getDatasource().getClazz()
					.getProperties();
			String tableName = "cfg_db_acct";
			for (int i = 0; i < tableProperties.length; ++i) {
				if (tableProperties[i].getName().equalsIgnoreCase("tableName")) {
					tableName = tableProperties[i].getValue().trim();
					break;
				}
			}
			//添加所有连接
			left.addAll(getAllDbAcct(baseDataSource.getConnection(),tableName));
			
			map = getPoolCompletionInfo(baseDataSource, tableName, (Pool[]) (Pool[]) left.toArray(new Pool[0]));
			Set keys = map.keySet();
			for (iter = keys.iterator(); iter.hasNext();) {
				Pool item = (Pool) iter.next();
				try {
					Properties leftPoolProperties = (Properties) map.get(item);
					javax.sql.DataSource leftDataSource = BasicDataSourceFactory
							.createDataSource(k(leftPoolProperties));

					if ((!(StringUtils.isBlank(item.getInit()))) && (item.getInit().equalsIgnoreCase("true"))) {
						initConnection(leftDataSource);
						if (log.isDebugEnabled()) {
							log.debug(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.impl.common_ds_create_succeed",
									new String[]{item.getName().trim()}));
						}
					}

					DATASOURCE_MAP.put(item.getName().trim(), leftDataSource);

					log.debug(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.common_ds_create_succeed",
							new String[]{item.getName().trim()}));
				} catch (Exception ex) {
					log.error(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.prefetch_ds_create_failed",
							new String[]{item.getName().trim()}), ex);
				}
			}
		}
	}

	private void initConnection(javax.sql.DataSource dataSource) throws Exception {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
		} catch (Exception ex) {
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	private HashMap getPoolCompletionInfo(javax.sql.DataSource baseDataSource, String tableName, Pool[] pool)
			throws Exception {
		HashMap rtn = new HashMap();
		for (int i = 0; i < pool.length; ++i) {
			Connection conn = null;
			PreparedStatement ptmt = null;
			ResultSet rs = null;
			try {
				conn = baseDataSource.getConnection();
				ptmt = conn.prepareStatement("select * from " + tableName + " where db_acct_code = ? and state ='U'");
				ptmt.setString(1, pool[i].getName().trim());
				rs = ptmt.executeQuery();

				Properties properties = new Properties();
				int j = 0;
				while (rs.next()) {
					if (j > 1) {
						throw new Exception(AppframeLocaleFactory.getResource(
								"com.ai.appframe2.complex.datasource.key_record_nomatch",
								new String[]{pool[i].getName().trim(), tableName}));
					}

					String host = rs.getString("HOST");
					if (host.equalsIgnoreCase("MYSQL_JDBC")) {
						properties.put("maxActive", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("initialSize", rs.getString("DEFAULT_CONN_MIN"));
						properties.put("maxIdle", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("username", rs.getString("USERNAME"));
						properties.put("password", rs.getString("PASSWORD"));
						String url = rs.getString("SID");
						properties.put("url", url);

						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("MYSQL_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else if (host.equalsIgnoreCase("DB2_JDBC")) {
						properties.put("maxActive", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("initialSize", rs.getString("DEFAULT_CONN_MIN"));
						properties.put("maxIdle", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("username", rs.getString("USERNAME"));
						properties.put("password", rs.getString("PASSWORD"));
						String url = rs.getString("SID");
						properties.put("url", url);

						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("DB2_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else if (host.equalsIgnoreCase("SYBASE_JDBC")) {
						properties.put("maxActive", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("initialSize", rs.getString("DEFAULT_CONN_MIN"));
						properties.put("maxIdle", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("username", rs.getString("USERNAME"));
						properties.put("password", rs.getString("PASSWORD"));
						String url = rs.getString("SID");
						properties.put("url", url);

						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("SYBASE_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else {
						properties.put("maxActive", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("initialSize", rs.getString("DEFAULT_CONN_MIN"));
						properties.put("maxIdle", rs.getString("DEFAULT_CONN_MAX"));
						properties.put("username", rs.getString("USERNAME"));
						properties.put("password", rs.getString("PASSWORD"));
						String url = "jdbc:oracle:thin:@" + rs.getString("HOST") + ":" + rs.getString("PORT") + ":"
								+ rs.getString("SID");
						properties.put("url", url);

						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost(rs.getString("HOST"));
						objDataBaseConnectURL.setPort(rs.getString("PORT"));
						objDataBaseConnectURL.setSid(rs.getString("SID"));
						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					}

					++j;
				}

				Property[] property = pool[i].getProperties();
				for (int k = 0; k < property.length; ++k) {
					properties.put(property[k].getName(), property[k].getValue());
					if (!(log.isDebugEnabled()))
						continue;
					log.debug(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.reset_prop_according",
							new String[]{property[k].getName(), property[k].getValue()}));
				}

				String prefix = "db.cfg." + pool[i].getName().trim() + ".";
				Properties systemProp = System.getProperties();
				Set systemKeys = systemProp.keySet();
				for (Iterator iter = systemKeys.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					if (StringUtils.indexOf(item, prefix) != -1) {
						String key = StringUtils.replace(item, prefix, "").trim();
						String value = systemProp.getProperty(item);
						properties.put(key, value);
						if (log.isDebugEnabled()) {
							log.debug(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.impl.reset_prop_info",
									new String[]{key, value}));
						}
					}
				}

				if (log.isDebugEnabled()) {
					log.debug(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.conn_set_prop_info",
							new String[]{pool[i].getName().trim(), maskPassword(properties).toString()}));
				}
				
				Map<String,String> map = DB_ACCT_NAME.get(pool[i].getName());
				if(map == null){
					map = new HashMap<String,String>();
				}
				Set<Object> propKey = properties.keySet();
				for (Iterator<Object> iter = propKey.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					map.put(item, properties.getProperty(item));
				}
				DB_ACCT_NAME.put(pool[i].getName(), map);
				
				rtn.put(pool[i], properties);
			} catch (Exception ex) {
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}

		return rtn;
	}

	private HashMap getPoolCompletionInfoWhenPrefetch(javax.sql.DataSource baseDataSource, String dbAcctTable,
			String dbUrlTable, String dbRelatTable, Pool[] pool) throws Exception {
		HashMap rtn = new HashMap();

		String serverName = RuntimeServerUtil.getServerName();

		if (StringUtils.isBlank(serverName)) {
			log.fatal(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.geturl_fatal"));
			throw new Exception(
					AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.get_servername_failed"));
		}

		serverName = serverName.trim();

		System.setProperty("appframe.oracle.session.module.name", "JTC " + serverName);
		System.setProperty("appframe.oracle.session.action.name", "JAVA");

		boolean isExPproperties = false;
		HashMap selectedPool = new HashMap();
		InputStream input = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("system/service/exe.properties");
		if (input != null) {
			System.out.println(AppframeLocaleFactory
					.getResource("com.ai.appframe2.complex.datasource.impl.use_exefile_byprocess"));
			isExPproperties = true;
			Properties p = new Properties();
			p.load(input);

			String ds = p.getProperty(serverName + ".datasource");
			if (StringUtils.isBlank(ds)) {
				throw new Exception(AppframeLocaleFactory.getResource(
						"com.ai.appframe2.complex.datasource.impl.server_nods_warn", new String[]{serverName}));
			}

			serverName = p.getProperty(serverName + ".relat");
			if (StringUtils.isBlank(serverName)) {
				throw new Exception(AppframeLocaleFactory.getResource(
						"com.ai.appframe2.complex.datasource.impl.server_relatnods_warn", new String[]{serverName}));
			}

			String[] dsArray = StringUtils.split(ds, ",");
			for (int i = 0; i < dsArray.length; ++i) {
				selectedPool.put(dsArray[i], null);
			}
			System.out.println(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.use_ds",
					new String[]{StringUtils.join(dsArray, "  ")}));
		} else {
			isExPproperties = false;
		}

		for (int i = 0; i < pool.length; ++i) {
//			if ((isExPproperties) && (!(selectedPool.containsKey(pool[i].getName())))) {
//				continue;
//			}
			Connection conn = null;
			PreparedStatement ptmt = null;
			ResultSet rs = null;
			try {
				conn = baseDataSource.getConnection();
				ptmt = conn.prepareStatement("select * from " + dbAcctTable + " where db_acct_code = ? and state ='U'");
				ptmt.setString(1, pool[i].getName().trim());
				rs = ptmt.executeQuery();

				Properties properties = new Properties();
				int j = 0;
				while (rs.next()) {
					if (j > 1) {
						throw new Exception(AppframeLocaleFactory.getResource(
								"com.ai.appframe2.complex.datasource.key_record_nomatch",
								new String[]{pool[i].getName().trim(), "cfg_db_acct"}));
					}

					String url = null;
					String isAdvance = pool[i].getIsAdvanceUrl();
					String host = rs.getString("HOST");

					if (host.equalsIgnoreCase("MYSQL_JDBC")) {
						if ((!(StringUtils.isBlank(isAdvance))) && (((isAdvance.trim().equalsIgnoreCase("true"))
								|| (isAdvance.trim().equalsIgnoreCase("y"))))) {
							throw new Exception(AppframeLocaleFactory
									.getResource("com.ai.appframe2.complex.datasource.impl.advance_mysql"));
						}

						url = rs.getString("SID");
						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("MYSQL_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else if (host.equalsIgnoreCase("DB2_JDBC")) {
						if ((!(StringUtils.isBlank(isAdvance))) && (((isAdvance.trim().equalsIgnoreCase("true"))
								|| (isAdvance.trim().equalsIgnoreCase("y"))))) {
							throw new Exception(AppframeLocaleFactory
									.getResource("com.ai.appframe2.complex.datasource.impl.advance_db2"));
						}

						url = rs.getString("SID");
						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("DB2_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else if (host.equalsIgnoreCase("SYBASE_JDBC")) {
						if ((!(StringUtils.isBlank(isAdvance))) && (((isAdvance.trim().equalsIgnoreCase("true"))
								|| (isAdvance.trim().equalsIgnoreCase("y"))))) {
							throw new Exception(AppframeLocaleFactory
									.getResource("com.ai.appframe2.complex.datasource.impl.advance_sysbase"));
						}

						url = rs.getString("SID");
						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost("SYBASE_JDBC");
						objDataBaseConnectURL.setPort("0");
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else if ((!(StringUtils.isBlank(isAdvance))) && (((isAdvance.trim().equalsIgnoreCase("true"))
							|| (isAdvance.trim().equalsIgnoreCase("y"))))) {
						if (log.isDebugEnabled()) {
							log.debug(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.impl.advance_url_info",
									new String[]{pool[i].getName().trim()}));
						}

						String advUrl = getAdvanceUrlByDbAcctCode(conn, serverName, dbUrlTable, dbRelatTable,
								pool[i].getName().trim());
						if (advUrl.startsWith("jdbc:oracle")) {
							url = advUrl;
						} else {
							url = "jdbc:oracle:thin:@" + advUrl;
						}

						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(true);
						objDataBaseConnectURL.setAdvanceUrl(advUrl);
						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					} else {
						url = "jdbc:oracle:thin:@" + rs.getString("HOST") + ":" + rs.getString("PORT") + ":"
								+ rs.getString("SID");
						DataBaseConnectURL objDataBaseConnectURL = new DataBaseConnectURL();
						objDataBaseConnectURL.setIsAdvanceUrl(false);
						objDataBaseConnectURL.setHost(rs.getString("HOST"));
						objDataBaseConnectURL.setPort(rs.getString("PORT"));
						objDataBaseConnectURL.setSid(rs.getString("SID"));

						URL_MAP.put(pool[i].getName(), objDataBaseConnectURL);
					}

					properties.put("maxActive", rs.getString("DEFAULT_CONN_MAX"));
					properties.put("initialSize", rs.getString("DEFAULT_CONN_MIN"));
					properties.put("maxIdle", rs.getString("DEFAULT_CONN_MAX"));
					properties.put("username", rs.getString("USERNAME"));
					properties.put("password", rs.getString("PASSWORD"));
					properties.put("url", url);

					++j;
				}

				Property[] property = pool[i].getProperties();
				for (int k = 0; k < property.length; ++k) {
					properties.put(property[k].getName(), property[k].getValue());
					if (!(log.isDebugEnabled()))
						continue;
					log.debug(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.reset_prop_info",
							new String[]{property[k].getName(), property[k].getValue()}));
				}

				try {
					Properties cfgDbJdbcParameter = getCfgDbJdbcParameter(conn, isExPproperties, serverName,
							pool[i].getName().trim());
					if ((cfgDbJdbcParameter != null) && (!(cfgDbJdbcParameter.isEmpty()))) {
						if (cfgDbJdbcParameter.containsKey("username")) {
							throw new Exception(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.table_param_error", new String[]{"username"}));
						}
						if (cfgDbJdbcParameter.containsKey("password")) {
							throw new Exception(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.table_param_error", new String[]{"password"}));
						}

						Set tmpKey = cfgDbJdbcParameter.keySet();
						for (Iterator iter = tmpKey.iterator(); iter.hasNext();) {
							String item = (String) iter.next();
							properties.setProperty(item, cfgDbJdbcParameter.getProperty(item));
						}

						System.out.println(AppframeLocaleFactory.getResource(
								"com.ai.appframe2.complex.datasource.impl.query_bydb_enable",
								new String[]{serverName, pool[i].getName().trim()}));
					}

				} catch (Throwable ex) {
					log.error(AppframeLocaleFactory
							.getResource("com.ai.appframe2.complex.datasource.impl.reset_conn_error"), ex);
				}

				String prefix = "db.cfg." + pool[i].getName().trim() + ".";
				Properties systemProp = System.getProperties();
				Set systemKeys = systemProp.keySet();
				for (Iterator iter = systemKeys.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					if (StringUtils.indexOf(item, prefix) != -1) {
						String key = StringUtils.replace(item, prefix, "").trim();
						String value = systemProp.getProperty(item);
						properties.put(key, value);
						if (log.isDebugEnabled()) {
							log.debug(AppframeLocaleFactory.getResource(
									"com.ai.appframe2.complex.datasource.impl.reset_prop_info",
									new String[]{key, value}));
						}
					}
				}

				if (log.isDebugEnabled()) {
					log.debug(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.conn_set_prop_info",
							new String[]{pool[i].getName().trim(), properties.toString()}));
				}
				
				Map<String,String> map = DB_ACCT_NAME.get(pool[i].getName());
				if(map == null){
					map = new HashMap<String,String>();
				}
				Set<Object> propKey = properties.keySet();
				for (Iterator<Object> iter = propKey.iterator(); iter.hasNext();) {
					String item = (String) iter.next();
					map.put(item, properties.getProperty(item));
				}
				DB_ACCT_NAME.put(pool[i].getName(), map);
				
				rtn.put(pool[i], properties);
			} catch (Exception ex) {
			} finally {
				if (rs != null) {
					rs.close();
				}
				if (ptmt != null) {
					ptmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			}
		}

		return rtn;
	}

	private Properties getCfgDbJdbcParameter(Connection conn, boolean isExPproperties, String serverName,
			String dbAcctCode) throws Exception {
		if (isExPproperties) {
			serverName = RuntimeServerUtil.getServerName();
		}

		Properties p = new Properties();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement(
					"select * from cfg_db_jdbc_parameter where server_name = ? and db_acct_code = ? and state = 'U' ");
			ptmt.setString(1, serverName);
			ptmt.setString(2, dbAcctCode);
			rs = ptmt.executeQuery();

			while (rs.next()) {
				String name = rs.getString("NAME");
				String value = rs.getString("VALUE");
				if ((!(StringUtils.isBlank(name))) && (!(StringUtils.isBlank(value))))
					p.setProperty(name.trim(), value.trim());
			}
		} catch (Exception ex) {
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
		}

		return p;
	}

	
	private String getAdvanceUrlByDbAcctCode(Connection conn, String serverName, String dbUrlTable, String dbRelatTable,
			String dbAcctCode) throws Exception {
		String url = null;

		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement("select c2.url as URL from " + dbRelatTable + " c1," + dbUrlTable
					+ " c2 where c1.url_name = c2.name and c1.db_acct_code = ? and c1.server_name = ? and c1.state='U' and c2.state = 'U' ");

			ptmt.setString(1, dbAcctCode);
			ptmt.setString(2, serverName);
			rs = ptmt.executeQuery();

			int j = 0;
			while (rs.next()) {
				if (j > 1) {
					log.fatal(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.advance_attr_error",
							new String[]{dbAcctCode, serverName, dbRelatTable}));
					throw new Exception(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.advance_attr_error",
							new String[]{dbAcctCode, serverName, dbRelatTable}));
				}

				url = rs.getString("URL").trim();

				++j;
			}

			if (j == 0) {
				log.fatal(AppframeLocaleFactory.getResource(
						"com.ai.appframe2.complex.datasource.impl.advance_attr_error_nodata",
						new String[]{dbAcctCode, serverName}));
				throw new Exception(AppframeLocaleFactory.getResource(
						"com.ai.appframe2.complex.datasource.impl.advance_attr_error_nodata",
						new String[]{dbAcctCode, serverName}));
			}
		} catch (Exception ex) {
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
		}

		return url;
	}

	private Properties k(Properties p) throws Exception {
		Set keys = p.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			p.setProperty(item, K.k_s(p.getProperty(item)));
		}
		return p;
	}

	private Properties maskPassword(Properties p) throws Exception {
		Properties rtn = new Properties();

		Set keys = p.keySet();
		for (Iterator iter = keys.iterator(); iter.hasNext();) {
			String item = (String) iter.next();
			if (!(StringUtils.contains(item, "password"))) {
				rtn.setProperty(item, p.getProperty(item));
			}
		}
		return rtn;
	}
	
	private List<Pool> getAllDbAcct(Connection conn,String dbAcctTable) throws Exception{
		List<Pool> allDB = new ArrayList<Pool>();
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			ptmt = conn.prepareStatement("select * from cfg_static_data c1, "+dbAcctTable+" c2 "+
										 "where c1.code_value = c2.db_acct_code and c2.state='U' "+
										 "and c1.code_type='omm_config_db' and  c1.state ='U'");
			rs = ptmt.executeQuery();

			int j = 0;
			while (rs.next()) {
				String db_acct_code = rs.getString("CODE_VALUE").trim();
				String username = rs.getString("USERNAME").trim();
				String initialSize = rs.getString("CODE_NAME").trim();
				String maxActive = rs.getString("CODE_DESC").trim();
				
				Property initialSizeP = new Property();
				initialSizeP.setName("initialSize");
				initialSizeP.setValue(initialSize);
				
				Property maxActiveP = new Property();
				maxActiveP.setName("maxActive");
				maxActiveP.setValue(maxActive);
				
				Pool p = new Pool();
				p.setName(db_acct_code);
				p.addProperty(initialSizeP);
				p.addProperty(maxActiveP);
				
				allDB.add(p);

				++j;
			}
			if(j==0){
				if (log.isInfoEnabled()){
					log.info("没有获取到额外的数据源配置");
				}
			}
		} catch (Exception ex) {
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null) {
				ptmt.close();
			}
		}
		return allDB;
	}
	

	public void start() throws Exception {
		if (log.isInfoEnabled())
			log.info(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.ds_start_warn"));
	}

	public void stop() throws Exception {
		Iterator iter;
		if ((DATASOURCE_MAP != null) && (DATASOURCE_MAP.size() != 0)) {
			Set keys = DATASOURCE_MAP.keySet();
			for (iter = keys.iterator(); iter.hasNext();) {
				Object item = iter.next();
				BasicDataSource objBasicDataSource = (BasicDataSource) DATASOURCE_MAP.get(item);
				objBasicDataSource.close();
				if (log.isInfoEnabled())
					log.info(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.ds_stop_warn",
							new String[]{item.toString()}));
			}
		}
	}

	public HashMap getURLMap() throws Exception {
		return URL_MAP;
	}
}