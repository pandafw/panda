package panda.mvc.view.tag;

import panda.lang.Strings;

/**
 * Base class for control and data tags
 */
public abstract class ContextBean extends TagBean {
	protected String var;

	protected String getVar() {
		return this.var;
	}

	public void setVar(String var) {
		this.var = var;
	}

	protected void putInVars(Object value) {
		if (Strings.isNotEmpty(var)) {
			context.getVars().put(var, value);
		}
	}
}
