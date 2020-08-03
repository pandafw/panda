package panda.mvc.view.tag;

import java.io.Writer;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.view.util.Escapes;

/**
 * <!-- START SNIPPET: javadoc --> Render a I18n text message.
 * <p/>
 * The message must be in a resource bundle with the same name as the action that it is associated
 * with. In practice this means that you should create a properties file in the same package as your
 * Java class with the same name as your class, but with .properties extension.
 * <p/>
 * If the named message is not found in a property file, then the body of the tag will be used as
 * default message. If no body is used, then the stack will be searched, and if a value is returned,
 * it will written to the output. If no value is found on the stack, the key of the message will be
 * written out. <!-- END SNIPPET: javadoc --> <!-- START SNIPPET: params -->
 * <ul>
 * <li>name* (String) - the i18n message key</li>
 * </ul>
 * <!-- END SNIPPET: params -->
 * <p/>
 * Example:
 * 
 */
@IocBean(singleton=false)
public class Text extends ContextBean {
	protected String name;
	protected String escape = Escapes.ESCAPE_HTML;

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param escape the escape to set
	 */
	public void setEscape(String escape) {
		this.escape = escape;
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
		String defaultMessage;
		if (Strings.isNotEmpty(body)) {
			defaultMessage = body;
		}
		else {
			defaultMessage = name;
		}

		String txt = context.getText().getText(name, defaultMessage, getParameters());
		String msg = Escapes.escape(txt, escape);
		writeOrSetVar(writer, msg);

		return super.end(writer, "");
	}
}
