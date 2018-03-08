package panda.mvc.view;

import panda.io.MimeTypes;
import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class TsvView extends CsvView {
	/**
	 * Constructor.
	 */
	public TsvView() {
		setContentType(MimeTypes.TEXT_TSV);
	}
}

