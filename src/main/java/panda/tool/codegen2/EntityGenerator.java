package panda.tool.codegen2;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import freemarker.template.Configuration;
import freemarker.template.Template;

import panda.dao.DaoTypes;
import panda.dao.entity.annotation.Column;
import panda.dao.entity.annotation.Comment;
import panda.dao.entity.annotation.FK;
import panda.dao.entity.annotation.ForeignKeys;
import panda.dao.entity.annotation.Id;
import panda.dao.entity.annotation.Index;
import panda.dao.entity.annotation.Indexes;
import panda.dao.entity.annotation.Join;
import panda.dao.entity.annotation.JoinColumn;
import panda.dao.entity.annotation.Joins;
import panda.dao.entity.annotation.PK;
import panda.dao.entity.annotation.Table;
import panda.dao.query.BooleanCondition;
import panda.dao.query.ComparableCondition;
import panda.dao.query.GenericQuery;
import panda.dao.query.ObjectCondition;
import panda.dao.query.StringCondition;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.EntityProperty;
import panda.tool.codegen.bean.Module;

/**
 * entity source generator
 */
public class EntityGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for ModelGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new EntityGenerator();

			cgm.execute(cg, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected Template tplEntityBean;
	protected Template tplEntityQuery;

	protected int cntEntity = 0;

	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		tplEntityBean = cfg.getTemplate("entity/EntityBean.java.ftl");
		tplEntityQuery = cfg.getTemplate("entity/EntityQuery.java.ftl");
	}
	
	@Override
	protected void processModule(Module module) throws Exception {
		for (Entity entity : module.getEntityList()) {
			if (Boolean.TRUE.equals(entity.getGenerate())) {
				print2("Processing entity - " + entity.getName());

				if (entity.getPrimaryKeyList().size() < 1) {
					throw new IllegalArgumentException("missing primary key: " + entity.getName());
				}

				processJavaEntity(module, entity);

				cntEntity++;
			}
		}
	}

	@Override
	protected void postProcess() throws Exception {
		print0(cntModule + " modules processed, " + cntFile + " files of " + cntEntity + " entities generated successfully.");
	}

	protected void prepareImportList(Collection<EntityProperty> ps, Set<String> imports) {
		for (EntityProperty p : ps) {
			String type = p.getFullJavaType();
			if (type.endsWith("[]")) {
				type = type.substring(0, type.length() - 2);
			}
			addImportType(imports, type);
		}
	}

	private void addFieldCondition(Set<String> imports, EntityProperty p) {
		if ("boolean".equals(p.getFieldKind())) {
			imports.add(BooleanCondition.class.getName());
		}
		else if ("date".equals(p.getFieldKind())
				|| "number".equals(p.getFieldKind())) {
			imports.add(ComparableCondition.class.getName());
		}
		else if ("string".equals(p.getFieldKind())) {
			imports.add(StringCondition.class.getName());
		}
		else {
			imports.add(ObjectCondition.class.getName());
		}
	}

	protected Set<String> setJavaEntityQueryImportList(Map<String, Object> wrapper, Entity entity) {
		Set<String> imports = new TreeSet<String>();

		Set<EntityProperty> ps = new HashSet<EntityProperty>();
		for (EntityProperty p : entity.getPropertyList()) {
			if (p.isDbColumn() || p.isJoinColumn()) {
				if (!"bin".equals(p.getFieldKind())) {
					ps.add(p);
				}
				addFieldCondition(imports, p);
			}
		}
//		for (EntityProperty p : entity.getJoinColumnList()) {
//			if (!"bin".equals(p.getFieldKind())) {
//				ps.add(p);
//			}
//			addFieldCondition(imports, p);
//		}
		for (EntityProperty p : ps) {
			String type = p.getFullJavaType();
			if (type.endsWith("[]")) {
				type = type.substring(0, type.length() - 2);
			}
			if (!Strings.contains(type, '<')) {
				addImportType(imports, type);
			}
		}

		imports.add(entity.getName());
		imports.add(GenericQuery.class.getName());
		if (Strings.isNotEmpty(entity.getBaseQueryClass())) {
			imports.add(entity.getBaseQueryClass());
		}

		setImports(wrapper, imports);
		
		return imports;
	}

	protected void setJavaEntityBeanImportList(Map<String, Object> wrapper, Entity entity) {
		Set<String> imports = new TreeSet<String>();

		prepareImportList(entity.getPropertyList(), imports);

		if (Strings.isEmpty(entity.getBaseBeanClass())) {
			imports.add(Serializable.class.getName());
		}
		else {
			imports.add(entity.getBaseBeanClass());
		}

		if (Strings.isNotEmpty(entity.getComment())) {
			imports.add(Comment.class.getName());
		}
		if (Strings.isNotEmpty(entity.getTable())) {
			imports.add(Table.class.getName());
		}
		if (Strings.isNotEmpty(entity.getIdentity())) {
			imports.add(Id.class.getName());
		}
		if (entity.getPrimaryKeyList().size() > 1 
				|| entity.getPrimaryKeyList().get(0) != entity.getIdentityProperty()) {
			imports.add(PK.class.getName());
		}
		if (!entity.getJoinMap().isEmpty()) {
			imports.add(Joins.class.getName());
			imports.add(Join.class.getName());
			imports.add(JoinColumn.class.getName());
		}
		if (!entity.getForeignKeyMap().isEmpty()) {
			imports.add(ForeignKeys.class.getName());
			imports.add(FK.class.getName());
		}
		if (!entity.getUniqueKeyMap().isEmpty()) {
			imports.add(Indexes.class.getName());
			imports.add(Index.class.getName());
		}
		if (!entity.getColumnList().isEmpty()) {
			imports.add(Column.class.getName());
			for (EntityProperty p : entity.getColumnList()) {
				if (Strings.isNotEmpty(p.getComment())) {
					imports.add(Comment.class.getName());
				}
				if (Strings.isNotEmpty(p.getJdbcType())) {
					imports.add(DaoTypes.class.getName());
				}
			}
		}

		imports.add(Objects.class.getName());

		for (EntityProperty p : entity.getPropertyList()) {
			if (Collections.isNotEmpty(p.getValidatorList())
					|| (p.isDbColumn() && !"String".equals(p.getType()))) {
				imports.add(Validators.class.getName());
				imports.add(Validates.class.getName());
				imports.add(Validate.class.getName());
				break;
			}
		}

		setImports(wrapper, imports);
	}

	protected Map<String, Object> getWrapper(Module module, Entity entity) {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		if ("true".equals(module.getProps().getProperty("source.datetime"))) {
			wrapper.put("date", Calendar.getInstance().getTime());
		}
		wrapper.put("module", module);
		wrapper.put("props", module.getProps());
		wrapper.put("entity", entity);
		wrapper.put("gen", this);
		
		return wrapper;
	}
	
	protected void processJavaEntity(Module module, Entity entity) throws Exception {
		Map<String, Object> wrapper = getWrapper(module, entity);

		String pkg = Classes.getPackageName(entity.getBaseBeanClass());
		
		checkLicense(module, pkg);
		
		setJavaEntityBeanImportList(wrapper, entity);
		processTpl(entity.getPackage(), entity.getSimpleName() + ".java", 
			wrapper, tplEntityBean, true);

		setJavaEntityQueryImportList(wrapper, entity);
		processTpl(entity.getPackage() + ".query", entity.getSimpleName() + "Query.java", 
			wrapper, tplEntityQuery);
	}

	protected void setImports(Map<String, Object> wrapper, Object imports) {
		wrapper.put("imports", imports);
	}

	private static Map<String, String> vtmap = new HashMap<String, String>();
	static {
		vtmap.put("cast", "Validators.CAST");
		vtmap.put("required", "Validators.REQUIRED");
		vtmap.put("empty", "Validators.EMPTY");

		vtmap.put("el", "Validators.EL");
		vtmap.put("regex", "Validators.REGEX");
		vtmap.put("email", "Validators.EMAIL");
		vtmap.put("filename", "Validators.FILENAME");
		vtmap.put("creditcardno", "Validators.CREDITCARDNO");

		vtmap.put("binary", "Validators.BINARY");
		vtmap.put("date", "Validators.DATE");
		vtmap.put("byte", "Validators.NUMBER");
		vtmap.put("short", "Validators.NUMBER");
		vtmap.put("int", "Validators.NUMBER");
		vtmap.put("long", "Validators.NUMBER");
		vtmap.put("number", "Validators.NUMBER");

		vtmap.put("stringlength", "Validators.STRING");
		vtmap.put("stringrequired", "Validators.STRING");

		vtmap.put("decimal", "Validators.DECIMAL");

		vtmap.put("file", "Validators.FILE");
		vtmap.put("filelength", "Validators.FILE");
		vtmap.put("image", "Validators.IMAGE");

		vtmap.put("constant", "Validators.CONSTANT");
		vtmap.put("prohibited", "Validators.PROHIBITED");

		vtmap.put("visit", "Validators.VISIT");
	}
	public String validatorType(String alias) {
		String vt = vtmap.get(alias);
		if (vt == null) {
			throw new IllegalArgumentException("Illegal validator type: " + alias);
		}
		return vt;
	}

	private static Map<String, String> vmmap = new HashMap<String, String>();
	static {
		vmmap.put("cast-boolean", "Validators.MSGID_CAST_BOOLEAN");
		vmmap.put("cast-Boolean", "Validators.MSGID_CAST_BOOLEAN");

		vmmap.put("cast-char", "Validators.MSGID_CAST_CHAR");
		vmmap.put("cast-Character", "Validators.MSGID_CAST_CHAR");

		vmmap.put("cast-byte", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-Byte", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-short", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-Short", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-int", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-Integer", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-long", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-Long", "Validators.MSGID_CAST_NUMBER");
		vmmap.put("cast-BigInteger", "Validators.MSGID_CAST_NUMBER");

		vmmap.put("cast-float", "Validators.MSGID_CAST_DECIMAL");
		vmmap.put("cast-Float", "Validators.MSGID_CAST_DECIMAL");
		vmmap.put("cast-double", "Validators.MSGID_CAST_DECIMAL");
		vmmap.put("cast-Double", "Validators.MSGID_CAST_DECIMAL");
		vmmap.put("cast-BigDecimal", "Validators.MSGID_CAST_DECIMAL");

		vmmap.put("cast-Date", "Validators.MSGID_CAST_DATE");
		vmmap.put("cast-Calendar", "Validators.MSGID_CAST_DATE");
		vmmap.put("cast-FileItem", "Validators.MSGID_CAST_FILE");

		vmmap.put("stringlength", "Validators.MSGID_STRING_LENTH");
		vmmap.put("filelength", "Validators.MSGID_FILE_LENTH");
		vmmap.put("constant", "Validators.MSGID_CONSTANT");
		vmmap.put("prohibited", "Validators.MSGID_PROHIBITED");

		vmmap.put("email", "Validators.MSGID_EMAIL");
		vmmap.put("password", "Validators.MSGID_PASSWORD");
		vmmap.put("byte", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("short", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("int", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("long", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("float", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("double", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("decimal", "Validators.MSGID_NUMBER_RANGE");
		vmmap.put("number", "Validators.MSGID_NUMBER_RANGE");
	}

	public String validatorMsgId(String alias) {
		if (alias.startsWith("#")) {
			return alias;
		}
		
		String vm = vmmap.get(alias);
		if (vm == null) {
			throw new IllegalArgumentException("Illegal validator msg: " + alias);
		}
		return vm;
	}
}
