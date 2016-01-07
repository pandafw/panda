package panda.mvc.view.tag;

import java.io.Writer;

import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class CSet extends ContextBean {
	private Object value;

	/**
	 * @param value the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}


	/**
	 * @return true
	 */
	public boolean usesBody() {
		// overriding this to true such that EVAL_BODY_BUFFERED is return and
		// bodyContent will be valid hence, text between start & end tag will
		// be honoured as default message (WW-1268)
		return true;
	}

	/**
	 * @param writer the output writer.
	 * @param body the rendered body.
	 * @return true if the body should be evaluated again
	 */
	public boolean end(Writer writer, String body) {
		putInVars(value == null ? body : value);
		return super.end(writer, "");
	}
}
