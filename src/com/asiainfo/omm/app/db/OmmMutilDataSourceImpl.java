package com.asiainfo.omm.app.db;

import com.ai.appframe2.bo.dialect.DialectFactory;
import com.ai.appframe2.complex.datasource.interfaces.IDataSource;
import com.ai.appframe2.util.locale.AppframeLocaleFactory;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OmmMutilDataSourceImpl extends AbstractOmmDataSourceImpl implements IDataSource {
	
	public OmmMutilDataSourceImpl() throws Exception {
		super();
	}

	private static transient Log log = LogFactory.getLog(OmmMutilDataSourceImpl.class);

	private static Map PRINT_STACK_DS_MAP = null;
	private static long PRINT_ENDTIME = System.currentTimeMillis();

	public DataSource getDataSource(String ds) throws Exception {
		return ((DataSource) DATASOURCE_MAP.get(ds.trim()));
	}
	
	public static Map<String,Map<String,String>> getAllDbAcct(){
		return DB_ACCT_NAME;
	}

	public Connection getConnectionFromDataSource(String ds) throws Exception {
		if ((PRINT_STACK_DS_MAP != null) && (PRINT_STACK_DS_MAP.containsKey(ds))
				&& (System.currentTimeMillis() <= PRINT_ENDTIME)) {
			log.error("DataSoutcePrintStack", new Exception("DataSoutcePrintStack=" + ds));
		}

		Connection rtn = null;
		try {
			DataSource objDataSource = (DataSource) DATASOURCE_MAP.get(ds.trim());
			if (objDataSource == null) {
				if (URL_MAP.containsKey(ds)) {
					log.error(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.getConnByds_error", new String[]{ds}));
				} else {
					log.error(AppframeLocaleFactory.getResource(
							"com.ai.appframe2.complex.datasource.impl.getConnByds_failed", new String[]{ds}));
				}
			}
			rtn = objDataSource.getConnection();
			rtn.setAutoCommit(false);
		} catch (Exception ex) {
			log.error(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.getConnByds_failed",
					new String[]{ds}), ex);
			throw ex;
		}

		if (log.isDebugEnabled()) {
			try {
				DatabaseMetaData dmd = rtn.getMetaData();
				if (dmd.getDatabaseProductName().toUpperCase().indexOf("ORACLE") != -1)
					printPhysicalConnectionInfo(rtn, ds);
			} catch (Exception ex) {
				log.error(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.print_physical"),
						ex);
			}
		}

		return rtn;
	}

	public String getPrimaryDataSource() throws Exception {
		if (primaryDataSource == null) {
			throw new Exception(
					AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.miss_base_dsname"));
		}
		return primaryDataSource;
	}

	private void printPhysicalConnectionInfo(Connection conn, String ds) throws Exception {
		PreparedStatement ptmt = null;
		ResultSet rs = null;
		try {
			String sql_default = "SELECT to_number(substr(dbms_session.unique_session_id,1,4),'xxxx') FROM dual";
			String sql_sybase = "SELECT @@SPID AS SID";
			if (DialectFactory.getDialect().getDatabaseType().equalsIgnoreCase("SYBASE")) {
				ptmt = conn.prepareStatement(sql_sybase);
			} else {
				ptmt = conn.prepareStatement(sql_default);
			}
			rs = ptmt.executeQuery();
			while (rs.next()) {
				log.debug(AppframeLocaleFactory.getResource("com.ai.appframe2.complex.datasource.impl.ds_sid_error",
						new String[]{ds, rs.getString(1)}));
			}
		} catch (Exception ex) {
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ptmt != null)
				ptmt.close();
		}
	}

	static {
		String[] printStackDs = null;
		String dsList = System.getProperty("appframe.printStack.dsList");
		if (!(StringUtils.isBlank(dsList))) {
			printStackDs = StringUtils.split(dsList.trim(), ",");
		}

		String durationSeconds = System.getProperty("appframe.printStack.durationSeconds");

		long tmpDurationSeconds = 300L;
		if ((!(StringUtils.isBlank(durationSeconds))) && (StringUtils.isNumeric(durationSeconds))) {
			tmpDurationSeconds = Long.parseLong(durationSeconds);
		}

		PRINT_ENDTIME = System.currentTimeMillis() + tmpDurationSeconds * 1000L;

		if ((printStackDs != null) && (printStackDs.length > 0)) {
			PRINT_STACK_DS_MAP = new HashMap();
			for (int i = 0; i < printStackDs.length; ++i) {
				if (!(StringUtils.isBlank(printStackDs[i]))) {
					PRINT_STACK_DS_MAP.put(printStackDs[i].trim(), printStackDs[i].trim());
				}
			}

			log.error("use appframe.printStack.dsList=" + StringUtils.join(printStackDs, ",") + ",endTime="
					+ new Date(PRINT_ENDTIME));
		}
	}
}
