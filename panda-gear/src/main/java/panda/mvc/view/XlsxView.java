package panda.mvc.view;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;


@IocBean(singleton=false)
public class XlsxView extends XlsView {
	public static final XlsxView DEFAULT = new XlsxView();

	/**
	 * Constructor.
	 */
	public XlsxView() {
		setContentType(MimeTypes.APP_XLSX);
	}
}

