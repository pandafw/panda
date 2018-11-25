package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.view.sitemesh.Sitemesher;
import panda.servlet.HttpServletResponseCapturer;

@IocBean(singleton=false)
public class SitemeshResourceView extends ResourceView {
	
	public SitemeshResourceView() {
		super();
	}

	public SitemeshResourceView(String location) {
		super(location);
	}

	@Override
	protected void forward(ActionContext ac, Object file) {
		Sitemesher sm = ac.getIoc().get(Sitemesher.class);
		if (!sm.needMesh()) {
			super.forward(ac, file);
			return;
		}
		
		try {
			HttpServletResponseCapturer hsrc = new HttpServletResponseCapturer(ac.getResponse(), true);
			forward(ac.getSettings(), ac.getRequest(), hsrc, file);

			String body = hsrc.getBodyContent();
			sm.meshup(ac.getResponse().getWriter(), body);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to forward " + file, e);
		}
	}
}


