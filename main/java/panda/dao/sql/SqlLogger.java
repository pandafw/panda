package panda.dao.sql;

import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import panda.log.Log;
import panda.log.Logs;


/**
 * SqlLogger
 * 
 * @author yf.frank.wang@gmail.com
 */
public abstract class SqlLogger {
	/**
	 * log for java.sql.Statement
	 */
	private static Log pslog = Logs.getLog(Statement.class);

	/**
	 * log for java.sql.ResultSet
	 */
	private static Log rslog = Logs.getLog(ResultSet.class);

	/**
	 * logResultHeader
	 * @param resultSet result set 
	 */
	public static void logResultHeader(ResultSet resultSet) {
		if (rslog.isDebugEnabled()) {
			try {
				ResultSetMetaData meta = resultSet.getMetaData();
				
				StringBuilder sb = new StringBuilder();
				sb.append("Result Header: [");
				int cnt = meta.getColumnCount();
				for (int i = 1; i <= cnt; i++) {
					sb.append(meta.getColumnLabel(i));
					if (i < cnt) {
						sb.append(", ");
					}
				}
				sb.append("]");
				rslog.debug(sb.toString());
			}
			catch (SQLException e) {
				rslog.warn("SQLException", e);
			}
		}
	}
	
	/**
	 * logResultValues
	 * @param resultSet result set 
	 */
	public static void logResultValues(ResultSet resultSet) {
		if (rslog.isDebugEnabled()) {
			try {
				ResultSetMetaData meta = resultSet.getMetaData();
	
				StringBuilder sb = new StringBuilder();
				sb.append("Result Values: [");
				int cnt = meta.getColumnCount();
				for (int i = 1; i <= cnt; i++) {
					sb.append(String.valueOf(resultSet.getObject(i)));
					if (i < cnt) {
						sb.append(", ");
					}
				}
				sb.append("]");
				rslog.debug(sb.toString());
			}
			catch (SQLException e) {
				rslog.warn("SQLException", e);
			}
		}		
	}

	/**
	 * logSatement
	 * @param sql sql statement 
	 */
	public static void logSatement(String sql) {
		if (pslog.isDebugEnabled()) {
			pslog.debug("Statement: " + sql);
		}
	}
	
	/**
	 * logParameters
	 * @param ps PreparedStatement
	 */
	public static void logParameters(PreparedStatement ps) {
		if (pslog.isDebugEnabled()) {
			try {
				ParameterMetaData pmd = ps.getParameterMetaData();

				if (pmd.getParameterCount() > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("Parameters: [");
		
					for (int i = 1; i <= pmd.getParameterCount(); i++) {
						if (i > 1) {
							sb.append(", ");
						}
						try {
							sb.append(pmd.getParameterClassName(i));
						}
						catch (SQLException e) {
							sb.append('?');
						}
					}
		
					sb.append("]");
					pslog.debug(sb.toString());
				}
			}
			catch (SQLException e) {
				pslog.warn("SQLException", e);
			}
		}
	}
}
