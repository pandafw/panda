package panda.tool.codegen;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import panda.lang.Strings;
import panda.tool.codegen.bean.Action;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.InputUI;
import panda.tool.codegen.bean.ListUI;
import panda.tool.codegen.bean.Module;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 */
public class FreemarkerGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for FreemarkerGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new FreemarkerGenerator();

			cgm.execute(cg, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private static final String[] tpls = {
		"_bulk",
		"_bulk_success",
		"_list",
		"_list_csv",
		"_list_tsv",
		"_list_pdf",
		"_list_popup",
		"_list_print",
		"_delete",
		"_delete_success",
		"_add",
		"_add_confirm",
		"_add_success",
		"_edit",
		"_edit_confirm",
		"_edit_success",
		"_view",
		"_print"
	};
	
	private Map<String, Template> tplMap;

	protected void loadTemplates(Configuration cfg) throws Exception {
		tplMap = new HashMap<String, Template>();
		for (String s : tpls) {
			String ftl = "view/freemarker/" + "action" + s + ".ftl.ftl";
			try {
				tplMap.put(s, cfg.getTemplate(ftl));
			}
			catch (Exception e) {
				throw new RuntimeException("Failed to load " + ftl, e);
			}
		}
	}

	private Template findTpl(String name) {
		Template tpl = tplMap.get(name);
		if (tpl == null) {
			throw new RuntimeException("Unknown template: " + name);
		}
		return tpl;
	}
	
	@Override
	protected void processModule(Module module) throws Exception {
		for (Action action : module.getActionList()) {
			if (Boolean.TRUE.equals(action.getGenerate())) {
				print2("Processing action - " + action.getName());
				
				Entity ae = null;
				for (Entity entity : module.getEntityList()) {
					if (entity.getName().equals(action.getEntity())) {
						ae = entity;
						break;
					}
				}
				
				if (ae == null) {
					throw new Exception("Can not find entity[" + action.getEntity() + "] of action[" + action.getName() + "]");
				}

				Map<String, Object> wrapper = new HashMap<String, Object>();
				
				if ("true".equals(module.getProps().getProperty("source.datetime"))) {
					wrapper.put("date", Calendar.getInstance().getTime());
				}
				wrapper.put("module", module);
				wrapper.put("props", module.getProps());
				wrapper.put("action", action);
				wrapper.put("entity", ae);
				wrapper.put("gen", this);

				String pkg = action.getActionPackage();
				
				checkLicense(module, pkg);
				
				//processTpl(pkg, action.getName() + ".ftl", wrapper, findTpl(""));
				
				for (ListUI lui : action.getSortedListUIList()) {
					if (Strings.isEmpty(lui.getTemplate())) {
						throw new IllegalArgumentException("template of ListUI[" + lui.getName() + "] can not be empty!");
					}
					
					wrapper.put("ui", lui);
					
					String uin = action.getSimpleActionClass() + "_" + lui.getName();

					for (String t : lui.getTemplates()) {
						if ("bdelete".equals(t) || "bupdate".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_bulk"));
							processTpl(pkg, uin + "_execute.ftl", wrapper, findTpl("_bulk_success"));
						}
						else if ("bedit".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_bedit"));
							processTpl(pkg, uin + "_confirm.ftl", wrapper, findTpl("_bedit_confirm"));
							processTpl(pkg, uin + "_execute.ftl", wrapper, findTpl("_bedit_success"));
						}
						else if (t.startsWith("list")) {
							if (!("list_json".equals(t) || "list_xml".equals(t))) {
								Template tpl = findTpl("_" + t);
								processTpl(pkg, uin + ".ftl", wrapper, tpl);
							}
						}
					}
				}

				for (InputUI iui : action.getSortedInputUIList()) {
					if (Strings.isEmpty(iui.getTemplate())) {
						throw new IllegalArgumentException("template of InputUI[" + iui.getName() + "] can not be empty!");
					}

//					System.out.println("INPUT: " + Jsons.toJson(iui, true));

					wrapper.put("ui", iui);

					String uin = action.getSimpleActionClass() + "_" +iui.getName();

					for (String t : iui.getTemplates()) {
						if ("view".equals(t) || "print".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_" + t));
						}
						else if ("delete".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_delete"));
							processTpl(pkg, uin + "_execute.ftl", wrapper, findTpl("_delete_success"));
						}
						else if ("add".equals(t) || "copy".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_add"));
							processTpl(pkg, uin + "_confirm.ftl", wrapper, findTpl("_add_confirm"));
							processTpl(pkg, uin + "_execute.ftl", wrapper, findTpl("_add_success"));
						}
						else if ("edit".equals(t)) {
							processTpl(pkg, uin + ".ftl", wrapper, findTpl("_edit"));
							processTpl(pkg, uin + "_confirm.ftl", wrapper, findTpl("_edit_confirm"));
							processTpl(pkg, uin + "_execute.ftl", wrapper, findTpl("_edit_success"));
						}
					}
				}
			}
		}
	}
	
	public boolean startsWithLetter(String str) {
		if (Strings.isNotEmpty(str)) {
			return Character.isLetter(str.charAt(0));
		}
		return true;
	}
	
	public boolean startsWithMark(String str) {
		if (Strings.isNotEmpty(str)) {
			return !Character.isLetter(str.charAt(0));
		}
		return true;
	}
	
	public String stripStartMark(String str) {
		if (str == null) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == ':' || ch == '/' || Character.isLetter(ch)) {
				return str.substring(i);
			}
		}
		return str;
	}
	
	public String stripStart(String str, String strip) {
		return Strings.stripStart(str, strip);
	}
	
	public String getActionQuery(String uri) {
		return Strings.substringAfter(uri, '?');
	}
	
	public String getActionPath(String uri) {
		return Strings.substringBefore(uri, '?');
	}
	
	public String getActionName(String uri) {
		return Strings.substringAfter(Strings.substringBefore(uri, '?'), '/');
	}
}
