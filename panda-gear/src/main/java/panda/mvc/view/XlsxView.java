package panda.mvc.view;

import panda.io.MimeTypes;


public class XlsxView extends XlsView {
	public static final XlsxView DEFAULT = new XlsxView("");

	/**
	 * Constructor.
	 */
	public XlsxView() {
		this("");
	}

	/**
	 * Constructor.
	 * @param location the location
	 */
	public XlsxView(String location) {
		super(location);
		setContentType(MimeTypes.APP_XLSX);
	}
}

