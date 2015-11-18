package panda.wing.action.tool;

import java.io.BufferedReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import panda.dao.DaoClient;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.Sqls;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;


@At("${super_context}/sql")
@Auth(AUTH.SUPER)
@Ok(View.SFTL)
public class SqlExecuteAction extends AbstractAction {
	public static class Option {
		protected String commenter = "--";
		protected String delimiter = ";";
		protected boolean autoCommit = true;
		protected boolean ignoreError = false;
		protected String sql;
		protected int fetchLimit = 100;

		/**
		 * @return the commenter
		 */
		public String getCommenter() {
			return commenter;
		}

		/**
		 * @param commenter the commenter to set
		 */
		public void setCommenter(String commenter) {
			this.commenter = Strings.stripToNull(commenter);
		}

		/**
		 * @return the delimiter
		 */
		public String getDelimiter() {
			return delimiter;
		}

		/**
		 * @param delimiter the delimiter to set
		 */
		public void setDelimiter(String delimiter) {
			this.delimiter = Strings.stripToNull(delimiter);
		}

		/**
		 * @return the ignoreError
		 */
		public boolean isIgnoreError() {
			return ignoreError;
		}

		/**
		 * @param ignoreError the ignoreError to set
		 */
		public void setIgnoreError(boolean ignoreError) {
			this.ignoreError = ignoreError;
		}

		/**
		 * @return the autoCommit
		 */
		public boolean isAutoCommit() {
			return autoCommit;
		}

		/**
		 * @param autoCommit the autoCommit to set
		 */
		public void setAutoCommit(boolean autoCommit) {
			this.autoCommit = autoCommit;
		}

		/**
		 * @return the sql
		 */
		public String getSql() {
			return sql;
		}

		/**
		 * @param sql the sql to set
		 */
		public void setSql(String sql) {
			this.sql = Strings.stripToNull(sql);
		}

		/**
		 * @return the fetchLimit
		 */
		public int getFetchLimit() {
			return fetchLimit;
		}

		/**
		 * @param fetchLimit the fetchLimit to set
		 */
		public void setFetchLimit(int fetchLimit) {
			this.fetchLimit = fetchLimit;
		}
	}
	
	public static class Result {
		private String sql;
		private int updateCount;
		private String error;
		private List<String[]> resultSet;
		
		/**
		 * @param sql sql
		 */
		public Result(String sql) {
			this.sql = sql;
		}
		/**
		 * @return the sql
		 */
		public String getSql() {
			return sql;
		}
		/**
		 * @param sql the sql to set
		 */
		public void setSql(String sql) {
			this.sql = sql;
		}
		/**
		 * @return the updateCount
		 */
		public int getUpdateCount() {
			return updateCount;
		}
		/**
		 * @param updateCount the updateCount to set
		 */
		public void setUpdateCount(int updateCount) {
			this.updateCount = updateCount;
		}
		/**
		 * @return the error
		 */
		public String getError() {
			return error;
		}
		/**
		 * @param error the error to set
		 */
		public void setError(String error) {
			this.error = error;
		}
		/**
		 * @return the resultSet
		 */
		public List<String[]> getResultSet() {
			return resultSet;
		}
		/**
		 * @param resultSet the resultSet to set
		 */
		public void setResultSet(List<String[]> resultSet) {
			this.resultSet = resultSet;
		}
	}

	private DataSource dataSource;

	/**
	 */
	public SqlExecuteAction() {
	}

	/**
	 * @return the dataSource
	 */
	public DataSource getDataSource() {
		if (dataSource == null) {
			DaoClient dc = getDaoClient();
			if (dc instanceof SqlDaoClient) {
				dataSource = ((SqlDaoClient)dc).getDataSource();
			}
		}
		return dataSource;
	}

	/**
	 * @param dataSource the dataSource to set
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * execute
	 * @return INPUT
	 * @throws Exception if an error occurs
	 */
	@At("")
	public Object execute(@Param Option o) throws Exception {
		String sql = removeComment(o.sql, o.commenter);
		
		if (Strings.isEmpty(sql)) {
			return null;
		}
		
		Connection con = getDataSource().getConnection();
		
		try {
			con.setAutoCommit(o.autoCommit);
			
			List<Result> results = new ArrayList<Result>();
			String[] ss = sql.split(o.delimiter);
			for (String s : ss) {
				s = s.trim();
				if ("exit".equalsIgnoreCase(s)) {
				}
				else if ("commit".equalsIgnoreCase(s)) {
					Result r = new Result(s);
					results.add(r);
					try {
						con.commit();
					}
					catch (Exception e) {
						r.setError(Exceptions.getStackTrace(e));
						if (!o.ignoreError) {
							break;
						}
					}
				}
				else if (s.length() > 0) {
					Result r = execSql(con, s, o.fetchLimit);
					if (r == null) {
						if (!o.ignoreError) {
							break;
						}
					}
					else {
						results.add(r);
					}
				}
			}
			return results;
		}
		finally {
			Sqls.safeClose(con);
		}
	}

	private String removeComment(String sql, String commenter) throws Exception {
		if (Strings.isNotEmpty(sql)) {
			BufferedReader br = new BufferedReader(new StringReader(sql));
	
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				String lt = line.trim();
				if (lt.length() > 0 && !lt.startsWith(commenter)) {
					sb.append(line).append("\r\n");
				}
			}
			sql = sb.toString();
		}
		return sql;
	}

	private Result execSql(Connection con, String sql, int fetchLimit) throws Exception {
		Result r = new Result(sql);

		sql = Strings.replaceChars(sql, "\r\n\t", " ");
		Statement st = null;
		try {
			st = con.createStatement();
			st.execute(sql);
			
			ResultSet rs = st.getResultSet();
			if (rs != null) {
				List<String[]> list = new ArrayList<String[]>();
				r.setResultSet(list);

				String[] row;

				ResultSetMetaData meta = rs.getMetaData();
				int cnt = meta.getColumnCount();

				row = new String[cnt];
				for (int i = 1; i <= cnt; i++) {
					row[i - 1] = meta.getColumnLabel(i);
				}
				list.add(row);
				
				while (rs.next() && (fetchLimit < 1 || list.size() - 1 < fetchLimit)) {
					row = new String[cnt];
					for (int i = 1; i <= cnt; i++) {
						int type = meta.getColumnType(i);
						if (Sqls.isBinaryType(type)) {
							byte[] bs = rs.getBytes(i);
							if (bs != null) {
								row[i - 1] = "(" + bs.length + "B)";
							}
						}
						else {
							row[i - 1] = rs.getString(i);
						}
					}
					list.add(row);
				}
			}
			else {
				r.setUpdateCount(st.getUpdateCount());
			}
		}
		catch (Exception e) {
			r.setError(e.getClass().getName() 
				+ ": " + e.getMessage() + "\r\n"
				+ Exceptions.getStackTrace(e));
			return null;
		}
		finally {
			Sqls.safeClose(st);
		}
		return r;
	}
}
