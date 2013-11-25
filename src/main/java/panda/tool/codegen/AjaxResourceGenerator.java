package panda.tool.codegen;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.cli.CommandLine;

import panda.lang.Strings;
import panda.tool.codegen.bean.Action;
import panda.tool.codegen.bean.ListUI;
import panda.tool.codegen.bean.Model;
import panda.tool.codegen.bean.Module;
import panda.tool.codegen.bean.Resource;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * generate ".properties" resource file 
 */
public class AjaxResourceGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for PropertyResourceGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("l", "locale", "Resource locale (e.g: ja zh_CN)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("l")) {
				setParameter("locale", cl.getOptionValue("l").trim());
			}
		}

		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new AjaxResourceGenerator();

			cgm.execute(cg, args);
		}

	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String locale;

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	private static final String[] tpls = {
		"SliduModule_xx"
	};
	
	private Map<String, Template> tplMap;

	protected void loadTemplates(Configuration cfg) throws Exception {
		tplMap = new HashMap<String, Template>();
		for (String s : tpls) {
			tplMap.put(s, cfg.getTemplate("view/ajax/" + s + ".js.ftl"));
		}
	}

	
	@Override
	protected void prepareModule(Module module) throws Exception {
		for (Resource resource : module.getResourceList()) {
			resource.merge(module);
			resource.prepare();
		}			
	}
	
	@Override
	protected void processModule(Module module) throws Exception {
		for (Resource resource : module.getResourceList()) {
			if (locale == null || locale.equals(resource.getLocale())) {
				processLocaleResource(module, resource);
			}
		}
	}
	
	protected void processLocaleResource(Module module, Resource resource) throws Exception {
		String locale = Strings.isEmpty(resource.getLocale()) ? "_" : resource.getLocale();

		for (Action action : resource.getActionList()) {
			if (Boolean.TRUE.equals(action.getGenerate())) {
				print2("Processing text of action - " + action.getName() + locale);
				
				Model am = null;
				for (Model model : resource.getModelList()) {
					if (model.getName().equals(action.getModel())) {
						am = model;
						break;
					}
				}
				
				if (am == null) {
					throw new Exception("Can not find model[" + action.getModel() + "] of action[" + action.getName() + "]");
				}

				Map<String, Object> wrapper = new HashMap<String, Object>();
				
				if ("true".equals(module.getProps().getProperty("source.datetime"))) {
					wrapper.put("date", Calendar.getInstance().getTime());
				}
				wrapper.put("module", module);
				wrapper.put("props", module.getProps());
				wrapper.put("action", action);
				wrapper.put("model", am);
				wrapper.put("locale", locale);

				String pkg = locale + "/" + action.getPackage();
				checkLicense(module, action.getPackage());
				
				for (ListUI lui : action.getListUIList()) {
					if (Boolean.TRUE.equals(lui.getGenerate())) {
						if (Strings.isEmpty(lui.getTemplate())) {
							throw new IllegalArgumentException("template of ListUI[" + lui.getName() + "] can not be empty!");
						}
						
						wrapper.put("ui", lui);
						
						String uin = Strings.capitalize(action.getName());

						for (String t : lui.getTemplates()) {
							if ("list".equals(t)) {
								Template tpl = tplMap.get("SliduModule_xx");
								if (tpl == null) {
									throw new IllegalArgumentException("Illegal template: " + t);
								}

								processTpl(pkg, uin + "Module" + (locale.equals("_") ? "" : locale) + ".js", wrapper, tpl);
							}
						}
					}
				}
			}
		}
	}
}
