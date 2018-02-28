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
	
	public static View evalView(Ioc ioc, String viewstr) {
		if (Strings.isEmpty(viewstr)) {
			return null;
		}

		View view = getViewMaker(ioc).make(ioc, viewstr);
		if (view != null) {
			return view;
		}

		throw new IllegalArgumentException("Can not create view '" + viewstr + "'");
	}
}
