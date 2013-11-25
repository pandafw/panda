package panda.tool.codegen;

import java.util.Map;
import java.util.Set;

import panda.lang.Strings;
import panda.tool.codegen.bean.Model;
import panda.tool.codegen.bean.Module;
import freemarker.template.Configuration;

/**
 * model source generator
 */
public class GaeModelGenerator extends ModelGenerator {
	/**
	 * Main class for ModelGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new GaeModelGenerator();

			cgm.execute(cg, args);
		}
	}

	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		String type = "gae";
		tplModelBean = loadTemplate(cfg, type, "ModelBean.java.ftl");
		tplModelMetaData = loadTemplate(cfg, type, "ModelMetaData.java.ftl");
		tplModelExample = loadTemplate(cfg, type, "ModelExample.java.ftl");
		tplModelValidate = loadTemplate(cfg, type, "Model-validation.xml.ftl");
		tplModelDAO = loadTemplate(cfg, type, "ModelDAO.java.ftl");
	}

	@Override
	protected void prepareModule(Module module) throws Exception {
		module.getProps().put("sql.joinable", "false");
		super.prepareModule(module);
	}

	@Override
	protected void processModule(Module module) throws Exception {
		for (Model model : module.getModelList()) {
			if (Boolean.TRUE.equals(model.getGenerate())) {
				print2("Processing model - " + model.getName());

				if (model.getPrimaryKeyList().size() < 1) {
					throw new Exception("missing primary key");
				}

				if (model.getPrimaryKeyList().size() > 1) {
					throw new Exception("GAE does not support multi-field primary key");
				}
				if (Strings.isEmpty(model.getIdentity())) {
					model.setIdentity(model.getPrimaryKeyList().get(0).getName());
				}
				
				if (Strings.isEmpty(model.getBaseDaoClass())) {
					model.setBaseDaoClass(GaeModelDAO.class.getName());
				}
				if (Strings.isEmpty(model.getBaseExampleClass())) {
					model.setBaseExampleClass(GaeQueryParameter.class.getName());
				}
				if (Strings.isEmpty(model.getBaseMetaDataClass())) {
					model.setBaseMetaDataClass(GaeModelMetaData.class.getName());
				}
				processJavaModel(module, model);
				processModelDao(module, model);

				cntModel++;
			}
		}
	}

	@Override
	protected Set<String> setJavaModelExampleImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = super.setJavaModelExampleImportList(wrapper, model);

		imports.add(com.google.appengine.api.datastore.Query.class.getName());
		
		return imports;
	}

	protected Set<String> setModelDaoImportList(Map<String, Object> wrapper, Model model) {
		Set<String> imports = super.setModelDaoImportList(wrapper, model);

		imports.add(model.getModelMetaDataClass());
		
		return imports;
	}
}
