package panda.mvc.view.util;

import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class TsvExporter extends CsvExporter {
	public TsvExporter() {
		setSeparator('\t');
	}
}
