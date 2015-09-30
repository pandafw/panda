package panda.tool.codegen2;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import freemarker.template.Configuration;
import freemarker.template.Template;

import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validates;
import panda.tool.codegen.bean.Action;
import panda.tool.codegen.bean.ActionProperty;
import panda.tool.codegen.bean.Entity;
import panda.tool.codegen.bean.Module;

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
	private Template tplListAction;

	private int cntAction = 0;
	
	@Override
	protected void loadTemplates(Configuration cfg) throws Exception {
		tplListAction = cfg.getTemplate("action/ListAction.java.ftl");
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
		imports.add(Queryer.class.getName());
		imports.add(At.class.getName());
		imports.add(Ok.class.getName());
		imports.add(Err.class.getName());
		imports.add(Param.class.getName());
		imports.add(Validates.class.getName());
		imports.add(View.class.getName());
		
		processTpl(pkg, cls + ".java", wrapper, tplListAction, true);
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


}
