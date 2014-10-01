package panda.tpl;

import java.io.IOException;

public class TemplateException extends IOException {

	private static final long serialVersionUID = 1L;

	public TemplateException() {
		super();
	}

	public TemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TemplateException(String message) {
		super(message);
	}

	public TemplateException(Throwable cause) {
		super(cause);
	}

}
