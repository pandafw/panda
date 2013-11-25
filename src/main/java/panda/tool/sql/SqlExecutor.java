package panda.tool.sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Statement;

import org.apache.commons.cli.CommandLine;

import panda.lang.Strings;
import panda.util.tool.AbstractCommandTool;

/**
 * SqlExecutor.
 */
public class SqlExecutor extends AbstractSqlTool {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	protected abstract static class Main extends AbstractSqlTool.Main {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("c", "charset", "The charset of source file");

			addCommandLineOption("ie", "ignoreError", "Ignore error and continue.");

			addCommandLineOption("si", "linker", "The symbolic string of linker");

			addCommandLineOption("sd", "delimiter", "The symbolic string of delimiter");

			addCommandLineOption("sc", "commiter", "The symbolic string of commiter");

			addCommandLineOption("sm", "commenter", "The symbolic string of commenter");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("c")) {
				setParameter("charset", cl.getOptionValues("c"));
			}

			if (cl.hasOption("ie")) {
				setParameter("ignoreError", true);
			}

			if (cl.hasOption("sl")) {
				setParameter("linker", cl.getOptionValues("sl"));
			}

			if (cl.hasOption("sd")) {
				setParameter("delimiter", cl.getOptionValues("sd"));
			}

			if (cl.hasOption("sc")) {
				setParameter("commiter", cl.getOptionValues("sc"));
			}

			if (cl.hasOption("sm")) {
				setParameter("commenter", cl.getOptionValues("sm"));
			}
		}
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
	private String sql;
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
	public void setCharset(String charset) {
		this.charset = charset;
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
	 * @return the source or sql
	 */
	public String getSource() {
		if (sql != null) {
			return sql;
		}
		else {
			return source.getPath();
		}
	}
	
	/**
	 * @param s the source or sql to set
	 */
	public void setSource(String s) {
		File f = new File(s);
		if (f.exists()) {
			this.source = f;
		}
		else {
			this.sql = s;
		}
	}
	
	/**
	 * @param linker the linker to set
	 */
	public void setLinker(String linker) {
		this.linker = linker;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @param commiter the commiter to set
	 */
	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}

	/**
	 * @param commenter the commenter to set
	 */
	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	/**
	 * @param ignoreError the ignoreError to set
	 */
	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

	@Override
	protected void checkParameters() throws Exception {
		super.checkParameters();
		
		if (sql == null && source == null) {
			throw new IllegalArgumentException("parameter [sql/source] is required.");
		}

		AbstractCommandTool.checkRequired(includes, "includes");
		for (int i = 0; i < includes.length; i++) {
			String s = includes[i];
			includes[i] = Strings.replaceChars(s, '/', File.separatorChar);
		}
	}

	@Override
	protected void beforeProcess() throws Exception {
		super.beforeProcess();
		
		cntSql = 0;

		if (sql == null && source.isDirectory()) {
			println0("Executing: " + source.getPath());
		}
	}

	@Override
	protected void doProcess() throws Exception {
		if (sql != null) {
			execSql(sql, false);
		}
		else {
			process(source);
		}
	}
	
	@Override
	protected void afterProcess() throws Exception {
		StringBuilder sb = new StringBuilder();
		
		sb.append(cntSql).append(" sql statements");
		if (sql == null) {
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
	 * @param connection connection
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
