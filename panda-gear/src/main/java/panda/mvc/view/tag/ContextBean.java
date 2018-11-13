package panda.mvc.view.tag;

import java.io.IOException;
import java.io.Writer;

import panda.lang.Strings;
import panda.mvc.MvcException;

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

	protected void writeOrSetVar(Writer writer, String value) {
		if (value == null) {
			return;
		}

		if (Strings.isEmpty(getVar())) {
			try {
				writer.write(value);
			}
			catch (IOException e) {
				throw new MvcException("Failed to write out " + getClass().getSimpleName(), e);
			}
		}
		else {
			putInVars(value);
		}
	}

	protected void putInVars(Object value) {
		if (Strings.isNotEmpty(var)) {
			context.getVars().put(var, value);
		}
	}
}
