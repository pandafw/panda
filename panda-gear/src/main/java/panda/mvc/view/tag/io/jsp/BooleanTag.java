package panda.mvc.view.tag.io.jsp;

import panda.mvc.ActionContext;
import panda.mvc.view.tag.CBoolean;
import panda.mvc.view.tag.TagBean;

public class BooleanTag extends AbstractTag {
	private static final long serialVersionUID = 1L;

	protected TagBean getBean(ActionContext context) {
		return getBean(context, CBoolean.class);
	}
}
