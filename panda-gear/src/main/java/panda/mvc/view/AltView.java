package panda.mvc.view;

import java.lang.reflect.Method;

import panda.lang.Strings;
import panda.mvc.ActionConfig;
import panda.mvc.ActionContext;
import panda.mvc.ActionMapping;
import panda.mvc.View;
import panda.mvc.ViewMaker;

public class AltView extends AbstractView {

	private ViewMaker maker;
	
	public AltView(ViewMaker maker, String location) {
		super(location);
		this.maker = maker;
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
		
		View view = null;

		Class cls = ac.getAction().getClass();
		for (Method m : cls.getMethods()) {
			if (!m.getName().equals(name)) {
				continue;
			}
			
			ActionMapping am = ac.getIoc().get(ActionMapping.class);
			ActionConfig acfg = am.getActionConfig(cls, m);
			if (acfg == null) {
				continue;
			}

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
			
			view = maker.make(ac.getIoc(), sv);
			break;
		}
		
		if (view == null) {
			throw new IllegalArgumentException("Failed to find altview: " + location 
				+ " / " + ac.getConfig().getActionMethod().getName() + '@' + ac.getConfig().getActionType());
		}

		view.render(ac);
	}

}
