package panda.mvc.view;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class TsvView extends CsvView {
	public static final TsvView DEFAULT = new TsvView();

	/**
	 * Constructor.
	 */
	public TsvView() {
		setContentType(MimeTypes.TEXT_TSV);
	}
}

