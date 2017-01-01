package panda.wing.action.tool;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.wing.AppConstants;
import panda.wing.action.AbstractAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;


@At("${super_context}/loadtask")
@Auth(AUTH.SUPER)
@Ok(View.SFTL)
public class LoadTaskAction extends AbstractAction {
	protected final static String CKEY_RESOURCE = "resource";
	
	protected final static String CKEY_TEMPLATE = "template";
	
	@IocInject(value=AppConstants.PANDA_TASK_LOAD_KEYS, required=false)
	protected List<String> tasks;
	
	@IocInject
	protected AppResourceBundleLoader arbLoader;

	@IocInject
	protected AppFreemarkerTemplateLoader aftLoader;

	/**
	 * @return key list
	 */
	@At("")
	public List<String> input() {
		List<String> ckl = new ArrayList<String>();

		if (arbLoader.getDatabaseResourceLoader() != null) {
			ckl.add(CKEY_RESOURCE);
		}

		if (aftLoader.getDatabaseTemplateLoader() != null) {
			ckl.add(CKEY_TEMPLATE);
		}
		
		if (Collections.isNotEmpty(tasks)) {
			ckl.addAll(tasks);
		}
		return ckl;
	}
}