package panda.gems.admin.action;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.dao.DaoClient;
import panda.dao.sql.SqlDaoClient;
import panda.dao.sql.SqlIterator;
import panda.dao.sql.Sqls;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.annotation.At;
import panda.mvc.annotation.Redirect;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;


@At("${!!super_path|||'/super'}/sql")
@Auth(AUTH.SUPER)
@To(Views.SFTL)
public class SqlExecuteAction extends BaseAction {
	public static class Option {
		protected boolean autoCommit = true;
		protected boolean ignoreError = false;
		protected String sql;
		protected int fetchLimit = 100;

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

	@At("")
	@Redirect(toslash=true)
	public void input(@Param Option o) throws Exception {
	}
	
	/**
	 * execute
	 * @param o the input option
	 * @return results
	 * @throws Exception if an error occurs
	 */
	@At
	@TokenProtect
	public Object execute(@Param Option o) throws Exception {
		if (Strings.isEmpty(o.sql)) {
			return null;
		}
		
		@SuppressWarnings("resource")
		SqlIterator si = new SqlIterator(o.sql);
		if (!si.hasNext()) {
			return null;
		}
		
		Connection con = getDataSource().getConnection();
		try {
			con.setAutoCommit(o.autoCommit);
			
			List<Result> results = new ArrayList<Result>();
			while (si.hasNext()) {
				String s = si.next();
				if ("exit".equalsIgnoreCase(s)) {
					break;
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

	private Result execSql(Connection con, String sql, int fetchLimit) {
		Result r = new Result(sql);

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
			StringBuilder sb = new StringBuilder();
			sb.append(e.getClass().getName()).append(": ").append(e.getMessage());
			sb.append(Streams.EOL).append(Exceptions.getStackTrace(e));
			r.setError(sb.toString());
		}
		finally {
			Sqls.safeClose(st);
		}
		return r;
	}
}
