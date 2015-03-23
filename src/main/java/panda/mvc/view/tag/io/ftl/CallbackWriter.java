package panda.mvc.view.tag.io.ftl;

import java.io.IOException;
import java.io.Writer;

import freemarker.template.TemplateModelException;
import freemarker.template.TransformControl;

import panda.io.stream.StringBuilderWriter;
import panda.mvc.view.tag.Component;

/**
 */
public class CallbackWriter extends Writer implements TransformControl {
	private Component bean;
	private Writer writer;
	private StringBuilderWriter body;
	private boolean saveBody = false;

	public CallbackWriter(Component bean, Writer writer) {
		this.bean = bean;
		this.writer = writer;

		if (bean.usesBody()) {
			this.body = new StringBuilderWriter();
		}
	}

	public void close() throws IOException {
		if (bean.usesBody()) {
			body.close();
		}
	}

	public void flush() throws IOException {
		writer.flush();

		if (bean.usesBody()) {
			body.flush();
		}
	}

	public void write(char cbuf[], int off, int len) throws IOException {
		if (bean.usesBody() && saveBody) {
			body.write(cbuf, off, len);
		}
		else {
			writer.write(cbuf, off, len);
		}
	}

	public int onStart() throws TemplateModelException, IOException {
		boolean result = bean.start(this);

		saveBody = true;

		return result ? EVALUATE_BODY : SKIP_BODY;
	}

	public int afterBody() throws TemplateModelException, IOException {
		saveBody = false;
		
		boolean result = bean.end(this, bean.usesBody() ? body.toString() : "");

		return result ? REPEAT_EVALUATION : END_EVALUATION;
	}

	public void onError(Throwable throwable) throws Throwable {
		throw throwable;
	}

	public Component getBean() {
		return bean;
	}
}
