package panda.mvc.view.util;

import panda.ioc.annotation.IocBean;
import panda.lang.Chars;

@IocBean(singleton=false)
public class TsvExporter extends CsvExporter {
	public TsvExporter() {
		setSeparator(Chars.TAB);
	}
}
