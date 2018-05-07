package panda.io.stream;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 2007 writer.
 */
public class XlsxWriter extends XlsWriter {
	public XlsxWriter(OutputStream os) throws IOException {
		super(os);
	}

	public XlsxWriter(OutputStream os, String sheetname) throws IOException {
		super(os, sheetname);
	}

	/**
	 * create workbook
	 * @return workbook
	 * @throws IOException
	 */
	@Override
	protected Workbook create() throws IOException {
		return new XSSFWorkbook();
	}
}
