package panda.mvc.view;

import java.io.IOException;
import java.io.OutputStream;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.mvc.view.util.XlsExporter;


@IocBean(singleton=false)
public class XlsView extends AbstractDataView {
	public static final XlsView DEFAULT = new XlsView();

	private Object result;
	
	/**
	 * Constructor.
	 */
	public XlsView() {
		setContentType(MimeTypes.APP_XLS);
	}

	/**
	 * @return the result
	 */
	public Object getResult() {
		return result;
	}

	/**
	 * @param result the result to set
	 */
	public void setResult(Object result) {
		this.result = result;
	}

	@Override
	public void render(ActionContext ac) {
		if (result == null) {
			writeResult(ac, ac.getResult());
		}
		else {
			writeResult(ac, result);
		}
	}

	/**
	 * write result
	 * @param ac action context
	 * @param result result object
	 */
	protected void writeResult(ActionContext ac, Object result) {
		try {
			writeHeader(ac);

			OutputStream os = ac.getResponse().getOutputStream();
			writeResult(os, result);
			os.flush();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * write result
	 * @param os output stream
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	protected void writeResult(OutputStream os, Object result) throws IOException {
		if (result instanceof XlsExporter) {
			XlsExporter xls = (XlsExporter)result;
			xls.export(os);
		}
	}
}

