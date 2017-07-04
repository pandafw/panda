package panda.mvc.view;

import panda.io.MimeType;


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
	 */
	public XlsxView(String location) {
		super(location);
		setContentType(MimeType.APP_XLSX);
	}
}

