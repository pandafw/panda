package panda.mvc.view;

import panda.ioc.Ioc;
import panda.lang.Strings;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.impl.DefaultViewMaker;

public class Views {
	public static ViewMaker getViewMaker(Ioc ioc) {
		ViewMaker maker = ioc.getIfExists(ViewMaker.class);
		if (maker == null) {
			maker = new DefaultViewMaker();
		}
		return maker;
	}
	
	public static View evalView(Ioc ioc, String viewType) {
		if (Strings.isEmpty(viewType)) {
			return null;
		}

		String str = viewType;
		int pos = str.indexOf(':');
		
		String type, value;
		if (pos > 0) {
			type = Strings.trim(str.substring(0, pos).toLowerCase());
			value = Strings.trim(pos >= (str.length() - 1) ? null : str.substring(pos + 1));
		}
		else {
			type = str;
			value = null;
		}

		View view = getViewMaker(ioc).make(ioc, type, value);
		if (view != null) {
			return view;
		}

		throw new IllegalArgumentException("Can not create view '" + viewType + "'");
	}
}
