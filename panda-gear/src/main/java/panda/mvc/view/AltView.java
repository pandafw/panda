package panda.mvc.view;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.ViewCreator;

@IocBean(singleton=false)
public class AltView extends AbstractView {

	private static final String STACK_KEY = AltView.class.getName() + ".stack";
	
	@Override
	public void render(ActionContext ac) {
		String name, type;

		int pos = description.indexOf(':');
		if (pos > 0) {
			name = Strings.stripToNull(description.substring(0, pos));
			type = Strings.stripToNull(description.substring(pos + 1));
		}
		else {
			name = description;
			type = null;
		}
		
		if (Strings.isEmpty(name)) {
			throw new IllegalArgumentException("Invalid altview: " + description 
				+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
		}

		// cycle detect
		cycleDetect(ac);

		// find action config
		ActionConfig acfg = null;
		if (ac.getMethodName().equals(name)) {
			acfg = ac.getConfig();
		}
		else {
			// find alternative method ActionConfig
			ActionMapping am = ac.getIoc().get(ActionMapping.class);

			Class cls = ac.getConfig().getActionType();
			for (Method m : cls.getMethods()) {
				if (!m.getName().equals(name)) {
					continue;
				}
				
				acfg = am.getActionConfig(cls, m);
				if (acfg != null) {
					break;
				}
			}
		}

		if (acfg != null) {
			String sv = acfg.getOkView();
			if (Strings.isNotEmpty(type)) {
				if ("ok".equals(type)) {
				}
				else if ("error".equals(type)) {
					sv = acfg.getErrorView();
				}
				else if ("fatal".equals(type)) {
					sv = acfg.getFatalView();
				}
				else {
					throw new IllegalArgumentException("Invalid altview: " + description 
						+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
				}
			}

			ViewCreator vc = Mvcs.getViewCreator(ac.getIoc());
			View view = vc.create(ac, sv);
			if (view != null) {
				view.render(ac);
				return;
			}
		}
		
		throw new IllegalArgumentException("Failed to find altview: " + description 
			+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
	}

	protected void cycleDetect(ActionContext ac) {
		String key = ac.getAction().getClass().getName() + '.' + description;

		@SuppressWarnings("unchecked")
		Set<String> stack = (Set<String>)ac.getReq().get(STACK_KEY);
		if (stack == null) {
			stack = new HashSet<String>();
			ac.getReq().put(STACK_KEY, stack);
		}
		
		if (!stack.add(key)) {
			throw new IllegalArgumentException("Cycle altview detected: " + description
				+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
		}
	}
}
