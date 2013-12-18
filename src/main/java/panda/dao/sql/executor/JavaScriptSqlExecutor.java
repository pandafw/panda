package panda.dao.sql.executor;

import java.util.List;
import java.util.Map;

import panda.lang.Exceptions;

/**
 * Java language support SqlExecutor.
 * 
 * <pre>
 * SQL GRAMMER:
 *  
 * 1. SELECT column name auto mapping
 *   1.1 column name --&gt; JavaBean property name
 *     ex.) SELECT ID, NAME, ITEM_NAME FROM SAMPLE
 *       ID --&gt; id
 *       NAME --&gt; name
 *       ITEM_NAME --&gt; itemName
 * 
 *   1.2 AS ALIAS --&gt; JavaBean property name
 *    ex.) SELECT ID AS a.id, NAME AS a.name, ITEM_NAME AS a.item.name, B_ITEM_NAME AS a.b.itemName FROM SAMPLE
 *          --&gt;
 *         SELECT ID AS A_0_ID, NAME AS A_0_NAME, ITEM_NAME AS A_0_ITEM_0_NAME, ITEM_NAME AS A_0_B_0_ITEM_NAME FROM SAMPLE
 *      
 *       mapping: 
 *       A_0_ID --&gt; a.id
 *       A_0_NAME --&gt; a.name
 *       A_0_ITEM_0_NAME --&gt; a.item.name
 * 
 * 
 * 2. :xxx --&gt; PreparedStatement binding parameter (xxx: JavaBean property name)
 *    ex.) SELECT * FROM SAMPLE WHERE ID=:id
 * 
 * 3. Dynamic SQL
 *   a) import java class
 *       &lt;%@ import java.io.*; %&gt;
 * 
 *   b) define method
 *       &lt;%!
 *          private int add(int a, int b) {
 *          	return a + b;
 *          }  
 *       %&gt;
 * 
 *   c) property & method
 *       parameter - sql runtime parameter object
 *       bean  - property access of param   (@see PropertyAccessor)
 *       append - method: append(String), append(char)
 *  
 *   d) script 
 *       SELECT * FROM SAMPLE
 *       WHERE
 *       &lt;% 
 *          // #xxx --&gt; property name of runtime parameter
 *          if (#a.list != null &amp;&amp; !((List)#a.list).isEmpty()) { %&gt;
 *            AND LIST IN (:a.list)
 *       &lt;% } %&gt;
 *       &lt;%
 *          // the usage of append
 *          if (#name != null) {
 *            append(&quot;AND NAME=:name&quot;);
 *          }
 *       %&gt; 
 *       &lt;% 
 *          // the usage of param
 *          java.util.Map map = (java.util.Map)param;
 *          Integet id = (Integer)map.get(&quot;id);
 *          ...
 *       %&gt;
 *       &lt;% 
 *          // the usage of bean
 *          Integer id = (Integer)bean.get(&quot;id&quot;);
 *          ...
 *       %&gt;
 * 
 *   e) adjust SQL
 *    1) Delete 'AND/OR' after WHERE/(
 *     ex.) WHERE AND ID IS NULL --&gt; WHERE ID IS NULL
 *          (AND NAME IS NULL) --&gt; (NAME IS NULL)
 *  
 *    2) Delete ',' after SET
 *     ex.) SET ,ID=1 ,NAME='a' --&gt; SET ID = 1 ,NAME='a'
 * 
 * 
 * 
 * EXAMPLE:
 * -------- SQL --------
 * &lt;%@
 *    import java.util.List;
 * %&gt;
 * &lt;%!
 *    private boolean isNotEmpty(List list) {
 *      return list != null &amp;&amp; !list.isEmpty();
 *    }
 * %&gt;
 *   SELECT * FROM TEST 
 *   WHERE 
 * &lt;% if ((Integer)#id &gt; 0) { %&gt; ID=:id &lt;% } %&gt;
 * &lt;% if (isNotEmpty((List)#idList)) { %&gt; AND ID IN (:idList) &lt;% } %&gt;
 * &lt;% if (#name != null) { append(&quot; AND NAME=:name &quot;); } %&gt;
 *   ORDER BY 
 *     &lt;%= #orderCol == null ? &quot;ID&quot; : #orderCol %&gt;
 *     &lt;%= #orderDir == null ? &quot;&quot; : #orderDir %&gt;
 * 
 * -------- JAVA --------
 *   Class.forName(&quot;org.hsqldb.jdbcDriver&quot; );
 *   Connection connection = DriverManager.getConnection(&quot;jdbc:hsqldb:mem:test&quot;, &quot;sa&quot;, &quot;&quot;);
 *   SqlExecutor executor = new JavaScriptSqlExecutor(connection);
 *   
 *   TestA param = new TestA();
 *   param.id = 1001;
 *   
 *   TestB result = executor.queryForObject(sql, param, TestB.class);
 *   // result:{ &quot;id&quot;: 1001, &quot;name&quot;: &quot;test1001&quot;, ... }
 *   
 *   // queryForList
 *   TestA param = new TestA();
 *   param.idList = new List&lt;Integer&gt;();
 *   param.idList.add(1001);
 *   param.idList.add(1003);
 *   
 *   List&lt;TestB&gt; resultList = executor.queryForList(sql, param, TestB.class);
 *   // resultList:[{ &quot;id&quot;: 1001, &quot;name&quot;: &quot;test1001&quot;, ... }, { &quot;id&quot;: 1003, &quot;name&quot;: &quot;test1003&quot;, ... }]
 * 
 * </pre>
 * 
 * @author yf.frank.wang@gmail.com
 */
public class JavaScriptSqlExecutor extends SimpleSqlExecutor {
	/**
	 * Constructor
	 * @param sqlManager sqlManager
	 */
	protected JavaScriptSqlExecutor(JavaScriptSqlManager sqlManager) {
		super(sqlManager);
	}

	/**
	 * @return the sqlManager
	 */
	public JavaScriptSqlManager getJavaScriptSqlManager() {
		return (JavaScriptSqlManager)getSqlManager();
	}

	/**
	 * parseSqlStatement
	 * @param sql sql
	 * @param parameter parameter object
	 * @param sqlParams sql parameter list
	 * @return translated sql
	 * @throws Exception if an error occurs
	 */
	@Override
	protected String parseSqlStatement(String sql, Object parameter, List<JdbcSqlParameter> sqlParams) {
		Map<String, Class<JdbcSqlParser>> sqlParserCache = getJavaScriptSqlManager().getSqlParserCache();
		Class<JdbcSqlParser> parserClass = sqlParserCache.get(sql);
		if (parserClass == null) {
			synchronized (sqlParserCache) {
				parserClass = sqlParserCache.get(sql);
				if (parserClass == null) {
					parserClass = createSqlParserClass(sql);
					sqlParserCache.put(sql, parserClass);
				}
			}
		}
		
		try {
			JdbcSqlParser parser = parserClass.newInstance();
			return parser.parse(this, parameter, sqlParams);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * createSqlParser
	 * @param sql sql
	 * @return JdbcSqlParser instance
	 */
	@SuppressWarnings("unchecked")
	protected Class<JdbcSqlParser> createSqlParserClass(String sql) {
		String packageName = JavaScriptSqlParser.class.getPackage().getName();
		String simpleName = JavaScriptSqlParser.class.getSimpleName() + getJavaScriptSqlManager().getSequence();
		String className = packageName + "." + simpleName;

		StringBuilder head = new StringBuilder();
		StringBuilder func = new StringBuilder();
		StringBuilder body = new StringBuilder();

		StringBuilder str = new StringBuilder();
		for (int i = 0; i < sql.length(); i++) {
			char c = sql.charAt(i);
			if (c == '<') {
				int lp = i;
				if (i + 1 < sql.length() && sql.charAt(i + 1) == '%') {
					body.append("append(\"" + str.toString() + "\");\n");
					str = new StringBuilder();

					for (i += 2; i < sql.length(); i++) {
						c = sql.charAt(i);
						if (c == '"') {
							str.append(c);
							int start = i;
							for (i++; i < sql.length(); i++) {
								c = sql.charAt(i);
								if (c == '\\') {
									str.append(c);
									if (i + 1 >= sql.length()) {
										throw new IllegalArgumentException(
												"Illegal java statement (" + i
														+ "): unexpected character \\ reached.");
									}
									i++;
									str.append(sql.charAt(i));
								}
								else if (c == '"') {
									str.append(c);
									break;
								}
								else {
									str.append(c);
								}
							}
							if (i >= sql.length()) {
								throw new IllegalArgumentException("Illegal java statement ("
										+ start + "): unexpected end of string reached.");
							}
						}
						else if (c == '#') {
							int start = i;
							for (i++; i < sql.length(); i++) {
								c = sql.charAt(i);
								if (!Character.isJavaIdentifierPart(c) && c != '.') {
									break;
								}
							}
							if (i <= start + 1) {
								throw new IllegalArgumentException("Illegal java statement ("
										+ start + "): unexpected character # reached.");
							}
							String name = sql.substring(start + 1, i);
							str.append("(bean.get(\"" + name + "\"))");
							i--;
						}
						else if (c == '%') {
							if (i + 1 < sql.length() && sql.charAt(i + 1) == '>') {
								i++;
								break;
							}
							else {
								str.append(c);
							}
						}
						else {
							str.append(c);
						}
					}

					if (i >= sql.length()) {
						throw new IllegalArgumentException("Illegal sql statement (" + lp
								+ "): unexpected end of <% reached.");
					}

					if (str.length() > 0) {
						c = str.charAt(0);
						if (c == '@') {
							head.append(str.substring(1)).append('\n');
						}
						else if (c == '!') {
							func.append(str.substring(1)).append('\n');
						}
						else if (c == '=') {
							body.append("append(\"\" + (" + str.substring(1) + "));\n");
						}
						else {
							body.append(str).append('\n');
						}
					}
					str = new StringBuilder();
				}
				else {
					appendChar(str, c);
				}
			}
			else {
				appendChar(str, c);
			}
		}
		if (str.length() > 0) {
			body.append("append(\"" + str.toString() + "\");\n");
		}

		StringBuilder src = new StringBuilder();

		src.append("package ").append(packageName).append(";\n");
		src.append(head);

		src.append("public class ").append(simpleName).append(" extends ").append(
			JavaScriptSqlParser.class.getName()).append(" {\n");

		src.append("  public ").append(simpleName).append("() {\n");
		src.append("    super();\n");
		src.append("  }\n");

		src.append(func);

		src.append("  protected void parse() throws Exception {\n");
		src.append(body);
		src.append("  }\n");

		src.append("}\n");

		try {
			Class clazz = getJavaScriptSqlManager().getDynamicClassLoader().loadClass(className, src.toString());
			return clazz;
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	private void appendChar(StringBuilder sb, char c) {
		if (c == '\\') {
			sb.append("\\\\");
		}
		else if (c == '\r') {
			sb.append("\\r");
		}
		else if (c == '\n') {
			sb.append("\\n");
		}
		else if (c == '\t') {
			sb.append("\\t");
		}
		else if (c == '"') {
			sb.append("\\\"");
		}
		else {
			sb.append(c);
		}
	}
}
