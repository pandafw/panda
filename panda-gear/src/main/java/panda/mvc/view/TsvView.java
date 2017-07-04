package panda.mvc.view;

import panda.io.MimeType;


public class TsvView extends CsvView {
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
}

