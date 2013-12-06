package panda.tool.codegen;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import panda.dao.criteria.Query;
import panda.dao.criteria.condition.BooleanCondition;
import panda.dao.criteria.condition.ComparableCondition;
import panda.dao.criteria.condition.ObjectCondition;
import panda.dao.criteria.condition.StringCondition;
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
import panda.dao.sql.JdbcTypes;
import panda.lang.Classes;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.EntityProperty;
import panda.tool.codegen.bean.Module;
import freemarker.template.Configuration;
import freemarker.template.Template;

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
	protected Template tplEntityValidate;

	protected int cntEntity = 0;

	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		tplEntityBean = cfg.getTemplate("entity/EntityBean.java.ftl");
		tplEntityQuery = cfg.getTemplate("entity/EntityQuery.java.ftl");
		tplEntityValidate = cfg.getTemplate("entity/Entity-validation.xml.ftl");
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
		for (EntityProperty p : entity.getColumnList()) {
			if (!"bin".equals(p.getFieldKind())) {
				ps.add(p);
			}
			addFieldCondition(imports, p);
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
		
		imports.add(Query.class.getName());
		imports.add(entity.getBaseQueryClass());

		setImports(wrapper, imports);
		
		return imports;
	}

	protected void setJavaEntityBeanImportList(Map<String, Object> wrapper, Entity entity) {
		Set<String> imports = new TreeSet<String>();

		prepareImportList(entity.getPropertyList(), imports);

		if (Strings.isNotEmpty(entity.getBaseBeanClass())) {
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
					imports.add(JdbcTypes.class.getName());
				}
			}
		}

		imports.add(Objects.class.getName());

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
		processTpl(entity.getPackage(), entity.getName() + ".java", 
			wrapper, tplEntityBean, true);

		setJavaEntityQueryImportList(wrapper, entity);
		processTpl(entity.getPackage() + ".query", entity.getName() + "Query.java", 
			wrapper, tplEntityQuery);

		if (!entity.getPropertyList().isEmpty()) {
			processTpl(entity.getPackage(), entity.getName() + "-validation.xml", 
				wrapper, tplEntityValidate);
		}
	}

	protected void setImports(Map<String, Object> wrapper, Object imports) {
		wrapper.put("imports", imports);
	}

}
