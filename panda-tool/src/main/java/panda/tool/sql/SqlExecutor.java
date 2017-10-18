package panda.tool.sql;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import panda.args.Argument;
import panda.args.Option;
import panda.dao.sql.SqlIterator;
import panda.dao.sql.Sqls;
import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.Systems;

/**
 * SqlExecutor.
 */
public class SqlExecutor extends AbstractSqlTool {
	public static void main(String[] args) {
		new SqlExecutor().execute(args);
	}

	/**
	 * Constructor
	 */
	public SqlExecutor() {
		includes = new String[] { "**/*.sql" };
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String[] sqls;
	private String charset;
	private String linker = "@";
	private String commiter = "/";
	private String commenter = "--";
	private boolean stdin = false;
	private boolean ignoreError = false;

	private int cntSql;

	/**
	 * @return the charset
	 */
	public String getCharset() {
		return charset;
	}

	/**
	 * @return the linker
	 */
	public String getLinker() {
		return linker;
	}

	/**
	 * @return the commiter
	 */
	public String getCommiter() {
		return commiter;
	}

	/**
	 * @return the commenter
	 */
	public String getCommenter() {
		return commenter;
	}

	/**
	 * @return the stdin
	 */
	public boolean isStdin() {
		return stdin;
	}

	/**
	 * @return the ignoreError
	 */
	public boolean isIgnoreError() {
		return ignoreError;
	}

	/**
	 * @param charset the charset to set
	 */
	@Option(opt='C', option="charset", arg="CHARSET", usage="The charset of the resource source file")
	public void setCharset(String charset) {
		this.charset = charset;
	}

	/**
	 * @return the sql
	 */
	public String[] getSqls() {
		return sqls;
	}

	/**
	 * @param sqls the sqls to set
	 */
	@Argument(name="SQL", usage="The SQL to execute")
	public void setSqls(String[] sqls) {
		this.sqls = sqls;
	}

	/**
	 * @param linker the linker to set
	 */
	@Option(opt='L', option="linker", arg="SYM", usage="The symbolic string of linker")
	public void setLinker(String linker) {
		this.linker = linker;
	}

	/**
	 * @param commiter the commiter to set
	 */
	@Option(opt='O', option="commiter", arg="SYM", usage="The symbolic string of commiter")
	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}

	/**
	 * @param commenter the commenter to set
	 */
	@Option(opt='M', option="commenter", arg="SYM", usage="The symbolic string of commenter")
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	/**
	 * @param stdin the stdin to set
	 */
	@Option(opt='I', option="stdin", usage="Use stdin")
	public void setStdin(boolean stdin) {
		this.stdin = stdin;
	}

	/**
	 * @param ignoreError the ignoreError to set
	 */
	@Option(opt='Q', option="quiet", usage="Ignore error and continue")
	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntSql = 0;

		if (Arrays.isEmpty(sqls) && source.isDirectory() && !stdin) {
			println0("Executing: " + source.getPath());
		}
	}

	@Override
	protected void doProcess() throws Exception {
		if (Arrays.isNotEmpty(sqls)) {
			processSqls(sqls);
		}
		else if (stdin) {
			processStdin();
		}
		else {
			process(source);
		}
	}
	
	@Override
	protected void afterProcess() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(cntSql).append(" sql statements");
		if (Arrays.isEmpty(sqls) || !stdin) {
			sb.append(" of ").append(cntFile).append(" files");
		}
		
		sb.append(" executed successfully");

		println0(sb.toString());
	}

	@Override
	protected void processFile(File file) throws Exception {
		println1("Executing sql file: " + file.getPath());

		execSql(file.getParent(), new FileInputStream(file));
	}
	
	private void processStdin() throws Exception {
		execSql(Systems.USER_DIR, System.in);
	}
	
	private void processSqls(String[] sqls) throws Exception {
		for (String sql : sqls) {
			execSql(Systems.USER_DIR, new StringReader(sql));
		}
	}

	private void execSql(String dir, InputStream in) throws Exception {
		Reader r;
		if (Strings.isEmpty(charset)) {
			r = (new InputStreamReader(in));
		}
		else {
			r = (new InputStreamReader(in, charset));
		}
		
		execSql(dir, r);
	}
	
	private void execSql(String dir, Reader r) throws Exception {
		SqlIterator si = new SqlIterator(r);
		
		Statement stm = connection.createStatement();
		try {
			while (si.hasNext()) {
				String sql = si.next();
				if ("exit".equalsIgnoreCase(sql)) {
					break;
				}
				else if ("rollback".equalsIgnoreCase(sql)) {
					rollback();
				}
				else if ("commit".equalsIgnoreCase(sql)) {
					commit();
				}
				else if (sql.startsWith(linker)) {
					String s = sql.substring(1);
					if (Strings.isNotBlank(s)) {
						File f = new File(dir, s);
						processFile(f);
					}
				}
				else if (sql.length() > 0) {
					try {
						println3("Execute SQL: " + sql);
						stm.execute(sql);
						ResultSet rs = stm.getResultSet();
						if (rs != null) {
							printResultSet(rs);
						}
						else {
							println0("> Updated: " + stm.getUpdateCount());
						}
						cntSql++;
					}
					catch (Exception e) {
						println2("Error: " + e.getMessage());
						if (!ignoreError) {
							throw e;
						}
					}
				}
			}
		}
		finally {
			Sqls.safeClose(stm);
			Streams.safeClose(si);
		}
	}

	private void printResultSet(ResultSet rs) throws Exception {
		ResultSetMetaData meta = rs.getMetaData();
		int cnt = meta.getColumnCount();

		String[] row = new String[cnt];
		for (int i = 1; i <= cnt; i++) {
			row[i - 1] = meta.getColumnLabel(i);
		}
		
		String c = Strings.join(row, " | ");
		String h = Strings.repeat('-', c.length());
		println0(h);
		println0(c);
		println0(h);
		
		while (rs.next()) {
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
			String d = Strings.join(row, " | ");
			println0(d);
		}
		println0("");
	}
}
