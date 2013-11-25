package panda.tool.codegen;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import panda.dao.Dao;
import panda.dao.DaoException;
import panda.dao.criteria.condition.BooleanCondition;
import panda.dao.criteria.condition.ComparableCondition;
import panda.dao.criteria.condition.ObjectCondition;
import panda.dao.criteria.condition.StringCondition;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.CycleDetector;
import panda.lang.Strings;
import panda.tool.codegen.bean.Model;
import panda.tool.codegen.bean.ModelProperty;
import panda.tool.codegen.bean.Module;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * model source generator
 */
public abstract class ModelGenerator extends AbstractCodeGenerator {
	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected Template tplModelBean;
	protected Template tplModelMetaData;
	protected Template tplModelExample;
	protected Template tplModelValidate;
	protected Template tplModelDAO;

	protected int cntModel = 0;
	
	protected Template loadTemplate(Configuration cfg, String type, String name) throws Exception {
		String[] ss = Strings.split(type, '/');
		for (int i = ss.length; i > 0; i--) {
			String s = Strings.join(Arrays.subarray(ss, 0, i), '/');
			try {
				return cfg.getTemplate("model/" + s + "/" + name);
			}
			catch (FileNotFoundException e) {
			}
		}
		return cfg.getTemplate("model/" + name);
	}

	protected void loadTemplates(Configuration cfg, String type) throws Exception {
		tplModelBean = loadTemplate(cfg, type, "ModelBean.java.ftl");
		tplModelMetaData = loadTemplate(cfg, type, "ModelMetaData.java.ftl");
		tplModelExample = loadTemplate(cfg, type, "ModelExample.java.ftl");
		tplModelValidate = loadTemplate(cfg, type, "Model-validation.xml.ftl");
		tplModelDAO = loadTemplate(cfg, type, "ModelDAO.java.ftl");
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getBaseModelDAOClass() {
		return null;
	}

	@SuppressWarnings("rawtypes")
	protected Class getBaseExampleClass() {
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	protected Class getBaseMetaDataClass() {
		return null;
	}
	
	@Override
	protected void processModule(Module module) throws Exception {
		for (Model model : module.getModelList()) {
			if (Boolean.TRUE.equals(model.getGenerate())) {
				print2("Processing model - " + model.getName());

				if (model.getPrimaryKeyList().size() < 1) {
					throw new Exception("missing primary key");
				}

				if (Strings.isEmpty(model.getBaseDaoClass())) {
					model.setBaseDaoClass(getBaseModelDAOClass().getName());
				}
				if (Strings.isEmpty(model.getBaseExampleClass())) {
					model.setBaseExampleClass(getBaseExampleClass().getName());
				}
				if (Strings.isEmpty(model.getBaseMetaDataClass())) {
					model.setBaseMetaDataClass(getBaseMetaDataClass().getName());
				}
				processJavaModel(module, model);
				processModelDao(module, model);

				cntModel++;
			}
		}
	}

	@Override
	protected void postProcess() throws Exception {
		print0(cntModule + " modules processed, " + cntFile + " files of " + cntModel + " models generated successfully.");
	}

	/**
	 * add type to set
	 * @param imports import set
	 * @param type java type
	 */
	public static void addImportType(Set<String> imports, String type) {
		if (type.endsWith(Classes.ARRAY_SUFFIX)) {
			type = type.substring(0, type.length() - Classes.ARRAY_SUFFIX.length());
		}
		
		int lt = type.indexOf('<');
		int gt = type.lastIndexOf('>');
		
		if (lt > 0 && gt > 0 && gt > lt) {
			addImportType(imports, type.substring(0, lt));
			type = type.substring(lt + 1, gt);
			String[] ts = Strings.split(type, ", ");
			for (String t : ts) {
				addImportType(imports, t);
			}
		}
		else {
			if (type.indexOf(".") > 0 && !type.startsWith("java.lang.")) {
				imports.add(type);
			}
		}
	}
	
	protected void prepareImportList(List<ModelProperty> ps, Set<String> imports) {
		for (ModelProperty p : ps) {
			String type = p.getFullJavaType();
			if (type.endsWith("[]")) {
				type = type.substring(0, type.length() - 2);
			}
			addImportType(imports, type);
		}
	}

	private void addFieldRestrict(Set<String> imports, ModelProperty p) {
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

	protected Set<String> setJavaModelExampleImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = new TreeSet<String>();

		Set<ModelProperty> ps = new HashSet<ModelProperty>();
		for (ModelProperty p : model.getColumnList()) {
			if (!"bin".equals(p.getFieldKind())) {
				ps.add(p);
			}
			addFieldRestrict(imports, p);
		}
		for (ModelProperty p : model.getSqlExpressionList()) {
			if (!"bin".equals(p.getFieldKind())) {
				ps.add(p);
			}
			addFieldRestrict(imports, p);
		}
		for (ModelProperty p : model.getJoinColumnList()) {
			if (!"bin".equals(p.getFieldKind())) {
				ps.add(p);
			}
			addFieldRestrict(imports, p);
		}
		
		List<ModelProperty> pl = new ArrayList<ModelProperty>();
		pl.addAll(ps);
		
		prepareImportList(pl, imports);
		
		imports.add(model.getBaseExampleClass());
		imports.add(model.getModelMetaDataClass());

		setImports(wrapper, imports);
		
		return imports;
	}

	protected void setJavaModelMetaDataImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = new TreeSet<String>();

		prepareImportList(model.getPropertyList(), imports);
		
		imports.add(Map.class.getName());
		imports.add(HashMap.class.getName());
		imports.add(model.getBaseMetaDataClass());
		imports.add(model.getModelBeanClass());
		imports.add(model.getModelExampleClass());
		imports.add(model.getModelDaoClass());

		setImports(wrapper, imports);
	}

	protected void setJavaModelBeanImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = new TreeSet<String>();

		prepareImportList(model.getOrgPropertyList(), imports);

		if (Strings.isNotEmpty(model.getBaseBeanClass())) {
			imports.add(model.getBaseBeanClass());
		}

		imports.add(CycleDetector.class.getName());

		setImports(wrapper, imports);
	}

	protected Set<String> setModelDaoImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = new TreeSet<String>();

		prepareImportList(model.getPrimaryKeyList(), imports);
		
		imports.add(Dao.class.getName());
		imports.add(DaoException.class.getName());
		imports.add(model.getModelBeanClass());
		imports.add(model.getModelExampleClass());
		imports.add(model.getBaseDaoClass());

		setImports(wrapper, imports);
		
		return imports;
	}

	protected Map<String, Object> getWrapper(Module module, Model model) {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		if ("true".equals(module.getProps().getProperty("source.datetime"))) {
			wrapper.put("date", Calendar.getInstance().getTime());
		}
		wrapper.put("module", module);
		wrapper.put("props", module.getProps());
		wrapper.put("model", model);
		wrapper.put("gen", this);
		
		return wrapper;
	}
	
	protected void processJavaModel(Module module, Model model) throws Exception {
		Map<String, Object> wrapper = getWrapper(module, model);

		String pkg = Classes.getPackageName(model.getModelBeanClass());
		
		checkLicense(module, pkg);
		
		setJavaModelBeanImportList(wrapper, model);
		processTpl(Classes.getPackageName(model.getModelBeanClass()), 
			Classes.getShortClassName(model.getModelBeanClass()) + ".java", 
			wrapper, tplModelBean, true);

		setJavaModelMetaDataImportList(wrapper, model);
		processTpl(Classes.getPackageName(model.getModelMetaDataClass()), 
			Classes.getShortClassName(model.getModelMetaDataClass()) + ".java", 
			wrapper, tplModelMetaData);

		setJavaModelExampleImportList(wrapper, model);
		processTpl(Classes.getPackageName(model.getModelExampleClass()), 
			Classes.getShortClassName(model.getModelExampleClass()) + ".java", 
			wrapper, tplModelExample, true);

		if (!model.getPropertyList().isEmpty()) {
			processTpl(Classes.getPackageName(model.getModelBeanClass()), 
				Classes.getShortClassName(model.getModelBeanClass()) + "-validation.xml", 
				wrapper, tplModelValidate);
		}
	}

	protected void processModelDao(Module module, Model model) throws Exception {
		Map<String, Object> wrapper = getWrapper(module, model);
		setModelDaoImportList(wrapper, model);
		processTpl(Classes.getPackageName(model.getModelDaoClass()), 
			Classes.getShortClassName(model.getModelDaoClass()) + ".java", 
			wrapper, tplModelDAO);
	}

	protected void setImports(Map<String, Object> wrapper, Object imports) {
		wrapper.put("imports", imports);
	}

}
