package panda.mvc.view;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;
import panda.mvc.view.sitemesh.Sitemesher;
import panda.servlet.HttpServletResponseCapturer;

@IocBean(singleton=false)
public class SitemeshForwardView extends ForwardView {
	
	public SitemeshForwardView() {
		super();
	}

	public SitemeshForwardView(String location) {
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
			HttpServletResponseCapturer hsrc = new HttpServletResponseCapturer(ac.getResponse(), true);
			forward(ac.getRequest(), hsrc, path);

			String body = hsrc.getBodyContent();
			sm.meshup(ac.getResponse().getWriter(), body);
		}
		catch (Exception e) {
			throw new RuntimeException("Failed to forward " + path, e);
		}
	}
}


