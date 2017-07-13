package panda.mvc.view;

import panda.mvc.ActionContext;
import panda.mvc.view.sitemesh.Sitemesher;
import panda.servlet.HttpServletResponseBlocker;

public class SitemeshJspView extends JspView {
	public static final SitemeshJspView DEFAULT = new SitemeshJspView(null);

	public SitemeshJspView(String location) {
		super(location);
	}

	@Override
	protected void forward(ActionContext ac, String path) {
		Sitemesher sm = ac.getIoc().get(Sitemesher.class);
		if (!sm.needMesh()) {
			super.forward(ac, path);
			return;
		}
		
		try {
			HttpServletResponseBlocker res = new HttpServletResponseBlocker(ac.getResponse());
			forward(ac.getRequest(), res, path);

			String body = res.getBodyContent();
			sm.meshup(ac.getResponse().getWriter(), body);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to forward " + path, e);
		}
	}
}


