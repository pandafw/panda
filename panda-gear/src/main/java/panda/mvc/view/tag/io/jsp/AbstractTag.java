package panda.mvc.view.tag.io.jsp;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;

import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.view.tag.TagBean;

public abstract class AbstractTag extends BodyTagSupport implements DynamicAttributes {
	private static final long serialVersionUID = 1L;

	protected Map<String, Object> parameters;
	protected TagBean component;

	protected TagBean getBean(ActionContext ac, Class<? extends TagBean> cls) {
		return ac.getIoc().get(cls);
	}
	
	protected abstract TagBean getBean(ActionContext ac);
	
	protected ActionContext getActionContext() {
		return Mvcs.getActionContext(pageContext.getRequest());
	}
	
	public TagBean getComponent() {
		return component;
	}

	protected String getBody() {
		if (bodyContent == null) {
			return "";
		}
		return bodyContent.getString().trim();
	}

	public int doEndTag() throws JspException {
		component.end(pageContext.getOut(), getBody());
		component = null;
		return EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		component = getBean(getActionContext());

		populateParams();
		boolean evalBody = component.start(pageContext.getOut());

		if (evalBody) {
			return component.usesBody() ? EVAL_BODY_BUFFERED : EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}

	protected void populateParams() {
		if (Strings.isNotEmpty(getId())) {
			parameters.put("id", getId());
		}
		component.setParameters(parameters);
	}

	@Override
	public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
		if (parameters == null) {
			parameters = new HashMap<String, Object>();
		}
		parameters.put(localName, value);
	}
}

