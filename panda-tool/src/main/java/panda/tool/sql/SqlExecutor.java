package panda.tool.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Statement;

import panda.args.Argument;
import panda.args.Option;
import panda.lang.Arrays;
import panda.lang.Strings;

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
	private String delimiter = ";";
	private String commiter = "/";
	private String commenter = "--";
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
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
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
	 * @param delimiter the delimiter to set
	 */
	@Option(opt='D', option="delimiter", arg="SYM", usage="The symbolic string of delimiter")
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
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
	 * @param ignoreError the ignoreError to set
	 */
	@Option(opt='Q', option="quiet", usage="Ignore error and continue.")
	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntSql = 0;

		if (Arrays.isEmpty(sqls) && source.isDirectory()) {
			println0("Executing: " + source.getPath());
		}
	}

	@Override
	protected void doProcess() throws Exception {
		if (Arrays.isEmpty(sqls)) {
			process(source);
		}
		else {
			for (String sql : sqls) {
				execSql(sql, false);
			}
		}
	}
	
	@Override
	protected void afterProcess() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(cntSql).append(" sql statements");
		if (Arrays.isEmpty(sqls)) {
			sb.append(" of ").append(cntFile).append(" files");
		}
		sb.append(" executed successfully");

		println0(sb.toString());
	}

	@Override
	protected void processFile(File file) throws Exception {
		execSql(file);
	}
	
	/**
	 * execSql
	 * 
	 * @param file sql file
	 * @throws Exception if an error occurs
	 */
	private void execSql(File file) throws Exception {
		println1("Executing sql file: " + file.getPath());

		BufferedReader br;
		if (Strings.isEmpty(charset)) {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}
		else {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
		}

		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.length() < 1 || line.startsWith(commenter)) {
				sb.append(" ");
			}
			else if (line.equals(commiter)) {
				if (sb.length() > 0) {
					execSql(sb.toString(), true);
					commit();
					sb = new StringBuilder();
				}
			}
			else if (line.startsWith(linker)) {
				if (sb.length() > 0) {
					execSql(sb.toString(), false);
					sb = new StringBuilder();
				}
				line = line.substring(1);
				String[] ss = line.split(delimiter);
				for (String s : ss) {
					if (Strings.isNotBlank(s)) {
						File inc = new File(file.getParent(), line);
						execSql(inc);
					}
				}
			}
			else {
				sb.append(" ").append(line);
			}
		}

		execSql(sb.toString(), false);
		commit();
	}

	private void execSql(String sqls, boolean single) throws Exception {
		sqls = sqls.trim();
		if (sqls.length() < 1) {
			return;
		}

		String[] ss;
		if (single) {
			if (sqls.endsWith(delimiter)) {
				sqls = sqls.substring(0, sqls.length() - 1);
			}
			ss = new String[] { sqls };
		}
		else {
			ss = sqls.split(delimiter);
		}

		Statement stm = connection.createStatement();
		for (String sql : ss) {
			sql = sql.trim();
			if ("exit".equalsIgnoreCase(sql)) {
				
			}
			else if ("commit".equalsIgnoreCase(sql)) {
				commit();
			}
			else if (sql.length() > 0) {
				try {
					println3("Execute SQL: " + sql);
					stm.executeUpdate(sql);
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
		if (stm != null) {
			try {
				stm.close();
			}
			catch (Exception e) {
				;
			}
		}
	}
}
