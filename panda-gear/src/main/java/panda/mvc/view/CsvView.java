package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.io.MimeTypes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.Csv;
import panda.mvc.view.util.CsvExporter;


public class CsvView extends AbstractDataView {
	public static final CsvView DEFAULT = new CsvView("");

	private Object result;
	
	/**
	 * Constructor.
	 */
	public CsvView() {
		this("");
	}

	/**
	 * Constructor.
	 */
	public CsvView(String location) {
		super(location);
		setContentType(MimeTypes.TEXT_CSV);
		setBom(true);
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

			PrintWriter writer = ac.getResponse().getWriter();
			writeResult(writer, result);
			writer.flush();
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
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
		if (result instanceof Csv) {
			Csv csv = (Csv)result;
			csv.start(writer);
			csv.end(writer, Strings.EMPTY);
		}
		else if (result instanceof CsvExporter) {
			CsvExporter csv = (CsvExporter)result;
			csv.export(writer);
		}
	}
}

