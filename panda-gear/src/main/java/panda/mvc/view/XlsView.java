package panda.mvc.view;

import java.io.IOException;
import java.io.OutputStream;

import panda.io.MimeType;
import panda.lang.Exceptions;
import panda.mvc.ActionContext;
import panda.mvc.view.util.XlsExporter;


public class XlsView extends AbstractDataView {
	public static final XlsView DEFAULT = new XlsView("");

	private Object result;
	
	/**
	 * Constructor.
	 */
	public XlsView() {
		this("");
	}

	/**
	 * Constructor.
	 */
	public XlsView(String location) {
		super(location);
		setContentType(MimeType.APP_XLS);
		setBom(false);
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

