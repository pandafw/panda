package panda.tpl;

import java.io.IOException;

import panda.lang.Exceptions;

public abstract class AbstractTemplate implements Template {

	public String evaluate() {
		return evaluate((Object)null);
	}
	
	public String evaluate(Object context) {
		StringBuilder sb = new StringBuilder();
		try {
			evaluate(sb, context);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}

	public void evaluate(Appendable out) throws TemplateException {
		evaluate(out, null);
	}
}
