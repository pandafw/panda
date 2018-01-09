package panda.mvc.view;

import panda.io.MimeTypes;


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
	 * @param location the location
	 */
	public TsvView(String location) {
		super(location);
		setContentType(MimeTypes.TEXT_TSV);
	}
}

