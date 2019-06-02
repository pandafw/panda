package panda.app.action.tool;

import java.util.ArrayList;
import java.util.List;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.MVC;
import panda.app.util.AppFreemarkerTemplateLoader;
import panda.app.util.AppResourceBundleLoader;
import panda.ioc.annotation.IocInject;
import panda.lang.Collections;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.view.Views;


@At("${!!super_path|||'/super'}/loadtask")
@Auth(AUTH.SUPER)
@To(Views.SFTL)
public class LoadTaskAction extends AbstractAction {
	protected final static String CKEY_RESOURCE = "resource";
	
	protected final static String CKEY_TEMPLATE = "template";
	
	@IocInject(value=MVC.TASK_LOAD_KEYS, required=false)
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