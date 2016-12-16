package panda.tool.codegen;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import panda.dao.query.GenericQuery;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerOx;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.view.tag.ListColumn;
import panda.tool.codegen.bean.Action;
import panda.tool.codegen.bean.ActionProperty;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.InputUI;
import panda.tool.codegen.bean.ListUI;
import panda.tool.codegen.bean.Module;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * action source generator
 */
public class ActionGenerator extends AbstractCodeGenerator {
	/**
	 * Main class for ActionGenerator
	 */
	public static class Main extends AbstractCodeGenerator.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main cgm = new Main();
			
			AbstractCodeGenerator cg = new ActionGenerator();

			cgm.execute(cg, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private Template tplAction;

	private int cntAction = 0;
	
	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		tplAction = cfg.getTemplate("action/Action.java.ftl");
	}

	@Override
	protected void processModule(Module module) throws Exception {
		for (Action action : module.getActionList()) {
			if (Boolean.TRUE.equals(action.getGenerate())) {
				print2("Processing action - " + action.getName() + " / " + action.getEntity());
				
				if (Strings.isEmpty(action.getEntity())) {
					throw new IllegalArgumentException("Missing entity: " + action.getName());
				}

				Entity ae = null;
				for (Entity entity : module.getEntityList()) {
					if (action.getEntity().equals(entity.getName())) {
						ae = entity;
						break;
					}
				}
				
				if (ae == null) {
					throw new IllegalArgumentException("Can not find entity[" + action.getEntity() + "] of action[" + action.getName() + "]");
				}
	
				processJavaAction(module, action, ae);
				
				cntAction++;
			}
		}
	}

	@Override
	protected void postProcess() throws Exception {
		print0(cntModule + " modules processed, " + cntFile + " files of " + cntAction + " actions generated successfully.");
	}

	private void prepareImportList(List<ActionProperty> ps, Set<String> imports) {
		for (ActionProperty p : ps) {
			String type = p.getFullJavaType();
			if (type.endsWith("[]")) {
				type = type.substring(0, type.length() - 2);
			}
			addImportType(imports, type);
		}
	}

	private void processJavaAction(Module module, Action action, Entity entity) throws Exception {
		String pkg = action.getActionPackage();

		checkLicense(module, pkg);
		
		String cls = action.getSimpleActionClass();

		Map<String, Object> wrapper = getWrapper(module, action, entity);

		Set<String> imports = new TreeSet<String>();
		setImports(wrapper, imports);

		prepareImportList(action.getPropertyList(), imports);
		imports.add(entity.getName());
		imports.add(action.getActionBaseClass());
		if (Collections.isNotEmpty(action.getSortedListUIList())) {
			for (ListUI lui : action.getSortedListUIList()) {
				for (String s : lui.getTemplates()) {
					if ("bdelete".equals(s) 
							|| "bupdate".equals(s)
							|| "bedit".equals(s)) {
						imports.add(Map.class.getName());
					}
					else if ("list".equals(s)
							|| "list_popup".equals(s)
							|| "list_print".equals(s)) {
						imports.add(Queryer.class.getName());
						imports.add(Validates.class.getName());
					}
					else if ("list_csv".equals(s)
							|| "list_tsv".equals(s)) {
						imports.add(Validates.class.getName());
						imports.add(QueryerOx.class.getName());
						imports.add(List.class.getName());
						imports.add(ArrayList.class.getName());
						imports.add(ListColumn.class.getName());
					}
				}
			}
		}

		imports.add(At.class.getName());
		imports.add(Ok.class.getName());
		imports.add(Err.class.getName());
		imports.add(Param.class.getName());
		if (Collections.isNotEmpty(action.getSortedInputUIList())) {
			for (InputUI iui : action.getSortedInputUIList()) {
				if (Collections.contains(iui.getTemplates(), "copy") 
						|| Collections.contains(iui.getTemplates(), "edit")
						|| Collections.contains(iui.getTemplates(), "add")) {
					imports.add(Validates.class.getName());
				}
			}
		}
		if (Collections.isNotEmpty(entity.getNotNullList()) && Collections.isNotEmpty(action.getSortedInputUIList())) {
			for (InputUI iui : action.getSortedInputUIList()) {
				if (Collections.contains(iui.getTemplates(), "copy") 
						|| Collections.contains(iui.getTemplates(), "edit")
						|| Collections.contains(iui.getTemplates(), "add")) {
					imports.add(Validates.class.getName());
					if (Collections.isNotEmpty(iui.getRequiredValidateFieldList())) {
						imports.add(Validate.class.getName());
						imports.add(Validators.class.getName());
					}
				}
			}
		}
		imports.add(View.class.getName());
		if (Strings.isNotEmpty(action.getAuth())) {
			imports.add("panda.wing.auth.Auth");
			imports.add("panda.wing.constant.AUTH");
		}
		
		if (Strings.isNotEmpty(action.getAutoJoin())) {
			imports.add(entity.getQueryName());
			imports.add(GenericQuery.class.getName());
		}
		
		processTpl(pkg, cls + ".java", wrapper, tplAction, true);
	}

	private Map<String, Object> getWrapper(Module module, Action action, Entity entity) {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		
		if ("true".equals(module.getProps().getProperty("source.datetime"))) {
			wrapper.put("date", Calendar.getInstance().getTime());
		}
		wrapper.put("module", module);
		wrapper.put("props", module.getProps());
		wrapper.put("action", action);
		wrapper.put("entity", entity);
		wrapper.put("gen", this);
		
		return wrapper;
	}
	
	private void setImports(Map<String, Object> wrapper, Object imports) {
		wrapper.put("imports", imports);
	}


	public String trimAtName(String nm) {
		if ("import".equals(nm)) {
			return "(\"import\")";
		}
		return "";
	}

	public String trimMethodName(String nm) {
		if ("import".equals(nm)) {
			return "import_";
		}
		return nm;
	}

	public String translateToJava(String sv) {
		if (Strings.startsWithChar(sv, '"') && Strings.endsWithChar(sv, '"')) {
			return sv;
		}
		if (Strings.startsWith(sv, "assist.")) {
			return "assist().get" + Strings.capitalize(Strings.substringAfter(sv, "assist.")) + "()";
		}
		if (Strings.startsWith(sv, "consts.")) {
			return "consts().get" + Strings.capitalize(Strings.substringAfter(sv, "consts.")) + "()";
		}
		return "panda.mvc.Mvcs.findValue(\"" + sv + "\")";
	}
}
