package panda.tool.codegen;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;

import panda.dao.sql.Sqls;
import panda.io.Streams;
import panda.io.stream.CsvReader;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.EntityProperty;
import panda.tool.codegen.bean.Module;
import panda.tool.codegen.bean.Resource;
import panda.util.tool.AbstractCommandTool;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * A source generator for SQL scripts.
 */
public class DDLGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for DDLGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new DDLGenerator();

			cgm.execute(cg, args);
		}


		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("t", "dbtype", "Database type (e.g: oracle)");
			addCommandLineOption("l", "locale", "Resource locale (e.g: ja zh_CN)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("t")) {
				setParameter("dbtype", cl.getOptionValue("t").trim());
			}
			else {
				errorRequired(options, "dbtype");
			}

			if (cl.hasOption("l")) {
				setParameter("locale", cl.getOptionValue("l").trim());
			}
		}
	}

	protected final static String[] DBTYPE_ALL = {
		"hsqldb", "mysql", "oracle", "postgre"
		// not supported yet
		// , "mssql", "sybase"
		//"cloudscape", "db2", "db2mf", "derby", 
	};

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String[] dbtypes;
	private String locale = "";
	
	private static class Handler {
		public String dbtype;
		public List<List<String>> typeMapping;
		public Template tplTable;
		public Template tplPK;
		public Template tplUK;
		public Template tplFK;
		public Template tplSEQ;
		public Template tplDropTBL;
		public Template tplDropFK;
		public Template tplDropSEQ;
		public Template tplPatch;
	}
	
	private Map<String, Handler> handlers;
	
	private Handler handler;
	
	private int cntTable = 0;
	
	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		if (locale != null) {
			this.locale = locale;
		}
	}

	/**
	 * @param dbtype the dbtype to set
	 */
	public void setDbtype(String dbtype) {
		if (dbtype != null) {
			dbtype = dbtype.toLowerCase();
			if ("all".equals(dbtype)) {
				dbtypes = DBTYPE_ALL;
			}
			else {
				dbtypes = Strings.split(dbtype, ", ");
			}
		}
	}

	@Override
	protected void checkParameters() throws Exception {
		super.checkParameters();

		AbstractCommandTool.checkRequired(dbtypes, "dbtype");
		for (String t : dbtypes) {
			if (!Arrays.contains(DBTYPE_ALL, t)) {
				throw new IllegalArgumentException("Illegal dbtype [" + t 
					+ "]: must be " + Strings.join(DBTYPE_ALL, ", "));
			}
		}
	}

	protected Template loadTemplate(Configuration cfg, String m, String name) throws Exception {
		Template t = null;
		try {
			t = cfg.getTemplate("ddl/" + m + "/" + name);
		}
		catch (ParseException ex) {
			throw ex;
		}
		catch (IOException ex) {
			t = cfg.getTemplate("ddl/" + name);
		}
		return t;
	}

	private List<List<String>> loadDataTypeMapping(String dbtype) throws Exception {
		String file = "ddl/" + dbtype + "/DataTypes.csv";
		InputStream is = this.getClass().getResourceAsStream(file);
		if (is == null) {
			throw new RuntimeException("Failed to load " + file);
		}

		CsvReader cr = null;
		try {
			cr = new CsvReader(new InputStreamReader(is));
			cr.skipLine();
			return cr.readAll();
		}
		finally {
			Streams.safeClose(cr);
			Streams.safeClose(is);
		}
	}
	
	protected Handler createHandler(Configuration cfg, String dbtype) throws Exception {
		Handler h = new Handler();
		
		h.dbtype = dbtype;
		h.typeMapping = loadDataTypeMapping(dbtype);

		h.tplTable = loadTemplate(cfg, dbtype, "Table.sql.ftl");
		h.tplPK = loadTemplate(cfg, dbtype, "Table-PrimaryKey.sql.ftl");
		h.tplUK = loadTemplate(cfg, dbtype, "Table-UniqueKey.sql.ftl");
		h.tplFK = loadTemplate(cfg, dbtype, "Table-ForeignKey.sql.ftl");
		h.tplDropTBL = loadTemplate(cfg, dbtype, "Drop-Table.sql.ftl");
		h.tplDropFK = loadTemplate(cfg, dbtype, "Drop-ForeignKey.sql.ftl");

		if (dbtype.equalsIgnoreCase("Oracle")) {
			h.tplSEQ = loadTemplate(cfg, dbtype, "Table-Sequence.sql.ftl");
			h.tplDropSEQ = loadTemplate(cfg, dbtype, "Drop-Sequence.sql.ftl");
		}

		try {
			h.tplPatch = loadTemplate(cfg, dbtype, "Patch.sql.ftl");
		}
		catch (IOException e) {
			h.tplPatch = null;
		}
		return h;
	}

	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		handlers = new HashMap<String, Handler>();
		
		for (String t : dbtypes) {
			handlers.put(t, createHandler(cfg, t));
		}
	}
	
	@Override
	protected void processModule(Module module) throws Exception {
		Map<String, Entity> tables = new HashMap<String, Entity>();
		
		for (Entity model : module.getModelList()) {
			if (Boolean.TRUE.equals(model.getGenerate())) {
				tables.put(model.getTable(), model);
			}
		}
		
		for (String t : dbtypes) {
			handler = handlers.get(t);
			for (Entity model : tables.values()) {
				print2("Processing table - " + model.getTable() + " [" + t + "]");
				prepareModel(model, handler);
				prepareResource(module, model);
				processModel(module, model, handler);
				cntTable++;
			}
		}
	}

	private void concatScripts(String dbtype, String file, String[] scripts) throws Exception {
		if (Arrays.isEmpty(scripts)) {
			return;
		}
		
		OutputStream fos = null;
		try {
			for (String s : scripts) {
				File f = new File(out, dbtype + '/' + s);
				if (f.exists()) {
					if (fos == null) {
						File fout = new File(out, dbtype + '/' + file);
						print3("Generate - " + fout.getPath());
						fos = new FileOutputStream(fout);
					}
					Streams.copy(f, fos);
				}
			}
		}
		finally {
			Streams.safeClose(fos);
		}
	}
	
	private String[] globScripts(String dbtype, String dir) throws Exception {
		File fd = new File(out, dbtype + "/" + dir);
		if (fd.exists()) {
			List<String> ss = new ArrayList<String>();
			String[] fs = fd.list();
			for (String s : fs) {
				if (s.endsWith(".sql")) {
					ss.add(dir + '/' + s);
				}
			}
			return ss.toArray(new String[ss.size()]);
		}
		return null;
	}
	
	private void mergeScripts() throws Exception {
		for (String t : dbtypes) {
			print2("Merge scripts for " + t);
			
			concatScripts(t, "table.sql", globScripts(t, "table"));
			concatScripts(t, "primarykey.sql", globScripts(t, "primarykey"));
			concatScripts(t, "uniquekey.sql", globScripts(t, "uniquekey"));
			concatScripts(t, "foreignkey.sql", globScripts(t, "foreignkey"));
			concatScripts(t, "sequence.sql", globScripts(t, "sequence"));
			concatScripts(t, "patch.sql", globScripts(t, "patch"));
			concatScripts(t, "drop-table.sql", globScripts(t, "drop-table"));
			concatScripts(t, "drop-foreignkey.sql", globScripts(t, "drop-foreignkey"));
			concatScripts(t, "drop-sequence.sql", globScripts(t, "drop-sequence"));
			concatScripts(t, "all.sql", 
				new String[] { "table.sql", "primarykey.sql", "uniquekey.sql", "foreignkey.sql", "sequence.sql", "patch.sql" });
			concatScripts(t, "drop-all.sql", 
				new String[] { "drop-sequence.sql", "drop-foreignkey.sql", "drop-table.sql" });
		}
	}
	
	@Override
	protected void postProcess() throws Exception {
		mergeScripts();
		print0(cntModule + " modules processed, " + cntFile + " SQL scripts of " + (int)(cntTable / dbtypes.length) + " tables generated successfully.");
	}

	private Map<String, Object> getWrapper(Module module, Entity model, Handler handler) {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		if ("true".equals(module.getProps().getProperty("source.datetime"))) {
			wrapper.put("date", Calendar.getInstance().getTime());
		}
		wrapper.put("dbtype", handler.dbtype);
		wrapper.put("module", module);
		wrapper.put("props", module.getProps());
		wrapper.put("model", model);
		wrapper.put("gen", this);
		
		return wrapper;
	}
	
	private Entity findResourceModel(Module module, Entity model) {
		for (Resource r : module.getResourceList()) {
			if (locale.equals(Strings.defaultString(r.getLocale()))) {
				for (Entity m : r.getEntityList()) {
					if (m.getName().equals(model.getName())) {
						return m; 
					}
				}
			}
		}
		return null;
	}
	
	private void prepareResource(Module module, Entity model) throws Exception {
		Entity rm = findResourceModel(module, model);
		if (rm != null) {
			if (Strings.isNotEmpty(rm.getLabel())) {
				model.setLabel(rm.getLabel());
			}
			for (EntityProperty p : model.getPropertyList()) {
				if (Strings.isEmpty(p.getLabel())) {
					for (EntityProperty rp : rm.getPropertyList()) {
						if (p.getName().equals(rp.getName())) {
							p.setLabel(rp.getLabel());
							break;
						}
					}
				}
			}
		}
		
		Properties props = module.getProps();
		for (EntityProperty p : model.getPropertyList()) {
			if (Strings.isEmpty(p.getLabel())) {
				p.setLabel(props.getProperty("model.label." + p.getName()));
			}
			p.setLabel(Sqls.escapeString(p.getLabel()));
		}
	}
	
	private void prepareModel(Entity model, Handler handler) throws Exception {
		for (EntityProperty mp : model.getPropertyList()) {
			if (Strings.isEmpty(mp.getColumn())) {
				continue;
			}
			if (Strings.isEmpty(mp.getDbType())) {
				if (Strings.isEmpty(mp.getJdbcType())) {
					throw new IllegalArgumentException("empty jdbcType of [" + mp.getName() + "] - " + model.getName());
				}
				String dbType = null;
				String dbSize = null;
				for (List<String> line : handler.typeMapping) {
					if (line.size() > 1) {
						if (mp.getJdbcType().equalsIgnoreCase(line.get(0))) {
							dbType = line.get(1);
							if (line.size() > 2) {
								dbSize = line.get(2);
							}
							break;
						}
					}
				}
				if (dbType == null) {
					throw new IllegalArgumentException("can not find dbType for (" + mp.getJdbcType() + ") of [" + mp.getName() + "] - " + model.getName());
				}
				mp.setNativeType(dbType);
				if ("-".equals(dbSize)) {
					mp.setSize(null);
				}
				else if (dbSize != null && mp.getSize() == null) {
					mp.setSize(dbSize);
				}
			}
			else {
				mp.setNativeType(mp.getDbType());
			}
		}
	}
	
	private void processModel(Module module, Entity model, Handler handler) throws Exception {
		Map<String, Object> wrapper = getWrapper(module, model, handler);

		processTpl(handler.dbtype + ".table", model.getTable() + ".sql", wrapper, handler.tplTable);
		processTpl(handler.dbtype + ".drop-table", model.getTable() + "-Drop.sql", wrapper, handler.tplDropTBL);

		if (!model.getPrimaryKeyList().isEmpty()/* && StringUtils.isEmpty(model.getIdentity())*/) {
			processTpl(handler.dbtype + ".primarykey", model.getTable() + "-PK.sql", wrapper, handler.tplPK);
		}
		if (!model.getUniqueKeyMap().isEmpty()) {
			processTpl(handler.dbtype + ".uniquekey", model.getTable() + "-UK.sql", wrapper, handler.tplUK);
		}
		if (!model.getForeignKeyList().isEmpty()) {
			processTpl(handler.dbtype + ".foreignkey", model.getTable() + "-FK.sql", wrapper, handler.tplFK);
			processTpl(handler.dbtype + ".drop-foreignkey", model.getTable() + "-Drop-FK.sql", wrapper, handler.tplDropFK);
		}
		if (Strings.isNotEmpty(model.getIdentity())) {
			if (handler.tplSEQ != null) {
				processTpl(handler.dbtype + ".sequence", model.getTable() + "-SEQ.sql", wrapper, handler.tplSEQ);
			}
			if (handler.tplDropSEQ != null) {
				processTpl(handler.dbtype + ".drop-sequence", model.getTable() + "-Drop-SEQ.sql", wrapper, handler.tplDropSEQ);
			}
		}

		if (handler.tplPatch != null) {
			processTpl(handler.dbtype + ".patch", model.getTable() + "-Patch.sql", wrapper, handler.tplPatch);
		}
	}

	public String escapeTableName(String tn) {
		if ("mysql".equals(handler.dbtype)) {
			return '`' + tn + '`';
		}
		return tn;
	}

	public String escapeColumnName(String cn) {
		if ("mysql".equals(handler.dbtype)) {
			return '`' + cn + '`';
		}
		return cn;
	}

	public String escapeTableColumnName(String tn, String cn) {
		String tcn = tn + '.' + cn;
		if ("mysql".equals(handler.dbtype)) {
			return '`' + tcn + '`';
		}
		return tcn;
	}
}
