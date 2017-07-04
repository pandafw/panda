package panda.mvc.view.util;

import java.io.IOException;
import java.io.OutputStream;

import panda.io.stream.XlsWriter;
import panda.io.stream.XlsxWriter;
import panda.ioc.annotation.IocBean;

@IocBean(singleton=false)
public class XlsxExporter extends XlsExporter {
	/**
	 * create a XlsWriter
	 * @param os output stream
	 * @return XlsWriter
	 * @throws IOException
	 */
	@Override
	protected XlsWriter newXlsWriter(OutputStream os) throws IOException {
		return new XlsxWriter(os);
	}
}
