package panda.mvc.view;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import panda.lang.Strings;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.View;
import panda.mvc.ViewMaker;

public class AltView extends AbstractView {

	private static final String STACK_KEY = AltView.class.getName() + ".stack";
	
	public AltView(String location) {
		super(location);
	}

	@Override
	public void render(ActionContext ac) {
		String name, type;

		int pos = location.indexOf(':');
		if (pos > 0) {
			name = Strings.stripToNull(location.substring(0, pos));
			type = Strings.stripToNull(location.substring(pos + 1));
		}
		else {
			name = location;
			type = null;
		}
		
		if (Strings.isEmpty(name)) {
			throw new IllegalArgumentException("Invalid altview: " + location 
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
					throw new IllegalArgumentException("Invalid altview: " + location 
						+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
				}
			}

			ViewMaker maker = Views.getViewMaker(ac.getIoc());
			View view = maker.make(ac.getIoc(), sv);
			if (view != null) {
				view.render(ac);
				return;
			}
		}
		
		throw new IllegalArgumentException("Failed to find altview: " + location 
			+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
	}

	protected void cycleDetect(ActionContext ac) {
		String key = ac.getAction().getClass().getName() + '.' + location;

		@SuppressWarnings("unchecked")
		Set<String> stack = (Set<String>)ac.getReq().get(STACK_KEY);
		if (stack == null) {
			stack = new HashSet<String>();
			ac.getReq().put(STACK_KEY, stack);
		}
		
		if (!stack.add(key)) {
			throw new IllegalArgumentException("Cycle altview detected: " + location 
				+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
		}
	}
}
