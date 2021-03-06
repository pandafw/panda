package panda.io.stream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Excel 2007 reader.
 */
public class XlsxReader extends XlsReader {
	public XlsxReader(InputStream is) throws IOException {
		super(is);
	}

	/**
	 * load workbook
	 * @param is input stream
	 * @return workbook
	 * @throws IOException
	 */
	@Override
	protected Workbook load(InputStream is) throws IOException {
		return new XSSFWorkbook(is);
	}
}
