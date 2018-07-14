package panda.mvc;

import panda.mvc.ActionContext;

/**
 * View
 */
public interface View {
	void setArgument(String arg);
	
	void render(ActionContext ac);
}
