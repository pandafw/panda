package panda.mvc.view;

import panda.ioc.annotation.IocBean;

/**
 * <p>Jsp Forward View</p>
 * 
 * Example:
 * <ul>
 * <li>'@Ok("jsp")' => path: abc.cbc => /WEB-INF/abc.cbc.jsp
 * <li>'@Ok("jsp:abc.cbc")' => /WEB-INF/abc.cbc.jsp
 * <li>'@Ok("jsp:/abc/cbc")' => /abc/cbc.jsp
 * <li>'@Ok("jsp:/abc/cbc.jsp")' => /abc/cbc.jsp
 * </ul>
 */
@IocBean(singleton=false)
public class JspView extends ForwardView {

	public JspView() {
	}
	
	public JspView(String location) {
		super(location);
	}

	@Override
	protected String getExtension() {
		return ".jsp";
	}
}
