package panda.tool.codegen;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import panda.lang.Strings;
import panda.tool.codegen.bean.Action;
import panda.tool.codegen.bean.ListUI;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.Module;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 */
public class AjaxGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for FreemarkerGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new AjaxGenerator();

			cgm.execute(cg, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private static final String[] tpls = {
		"SliduModule"
	};
	
	private Map<String, Template> tplMap;

	protected void loadTemplates(Configuration cfg) throws Exception {
		tplMap = new HashMap<String, Template>();
		for (String s : tpls) {
			tplMap.put(s, cfg.getTemplate("view/ajax/" + s + ".js.ftl"));
		}
	}

	@Override
	protected void processModule(Module module) throws Exception {
		for (Action action : module.getActionList()) {
			if (Boolean.TRUE.equals(action.getGenerate())) {
				print2("Processing action - " + action.getName());
				
				Entity am = null;
				for (Entity model : module.getEntityList()) {
					if (model.getName().equals(action.getEntity())) {
						am = model;
						break;
					}
				}
				
				if (am == null) {
					throw new Exception("Can not find model[" + action.getEntity() + "] of action[" + action.getName() + "]");
				}

				Map<String, Object> wrapper = new HashMap<String, Object>();
				
				if ("true".equals(module.getProps().getProperty("source.datetime"))) {
					wrapper.put("date", Calendar.getInstance().getTime());
				}
				wrapper.put("module", module);
				wrapper.put("props", module.getProps());
				wrapper.put("action", action);
				wrapper.put("model", am);

				String pkg = action.getActionPackage();
				checkLicense(module, pkg);
				
				for (ListUI lui : action.getListUIList()) {
					if (Boolean.TRUE.equals(lui.getGenerate())) {
						if (Strings.isEmpty(lui.getTemplate())) {
							throw new IllegalArgumentException("template of ListUI[" + lui.getName() + "] can not be empty!");
						}
						
						wrapper.put("ui", lui);
						
						String uin = Strings.capitalize(action.getName());

						for (String t : lui.getTemplates()) {
							if ("list".equals(t)) {
								Template tpl = tplMap.get("SliduModule");
								if (tpl == null) {
									throw new IllegalArgumentException("Illegal template: " + t);
								}
								processTpl(pkg, uin + "Module.js", wrapper, tpl);
							}
						}
					}
				}
			}
		}
	}
}
