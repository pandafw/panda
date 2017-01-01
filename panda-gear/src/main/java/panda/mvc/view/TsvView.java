package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;

import panda.io.MimeType;
import panda.mvc.ActionContext;
import panda.mvc.view.tag.Csv;


public class TsvView extends CsvView {
	protected Csv csv;

	public static final TsvView DEFAULT = new TsvView("");

	/**
	 * Constructor.
	 */
	public TsvView() {
		this("");
	}

	/**
	 * Constructor.
	 */
	public TsvView(String location) {
		super(location);
		setContentType(MimeType.TEXT_TSV);
	}

	/**
	 * @return the csv
	 */
	public Csv getCsv() {
		return csv;
	}

	/**
	 * @param csv the csv to set
	 */
	public void setCsv(Csv csv) {
		this.csv = csv;
	}

	public void render(ActionContext ac) {
		Csv csv = this.csv;
		if (csv == null) {
			csv = (Csv)ac.getResult();
		}

		writeResult(ac, csv);
	}

	/**
	 * write result
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
		if (result != null) {
			Csv csv = (Csv)result;
			csv.start(writer);
			csv.end(writer, NULL);
		}
	}
}

