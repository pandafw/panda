package panda.io.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.NumberToTextConverter;

import panda.lang.Strings;

/**
 * Excel reader.
 */
public class XlsReader implements ListReader<Object>, Closeable {
	private Workbook workbook;
	private Sheet sheet;
	private int rowidx;
	private boolean numAsText;
	
	public XlsReader(InputStream is) throws IOException {
		workbook = load(is);
		init();
	}

	/**
	 * load workbook
	 * @param is input stream
	 * @return workbook
	 * @throws IOException
	 */
	protected Workbook load(InputStream is) throws IOException {
		return new HSSFWorkbook(is);
	}

	/**
	 * initialize workbook
	 * @throws IOException
	 */
	protected void init() throws IOException {
		setSheet(0);
	}
	
	/**
	 * Closes the underlying reader.
	 * 
	 * @throws IOException if the close fails
	 */
	public void close() throws IOException {
		workbook.close();
	}

	/**
	 * Get the number of spreadsheets in the workbook
	 * 
	 * @return the number of sheets
	 */
	public int getSheetCount() {
		return workbook.getNumberOfSheets();
	}
	
	/**
	 * set current sheet
	 * @param i sheet index
	 */
	public void setSheet(int i) {
		sheet = workbook.getSheetAt(i);
	}

	/**
	 * set current row index
	 * @param i row index
	 */
	public void setRowIndex(int i) {
		rowidx = i;
	}
	
	/**
	 * Returns the name of this sheet
	 * 
	 * @return the name of this sheet
	 */
	public String getSheetName() {
		return sheet.getSheetName();
	}

	/**
	 * @return the row count of this sheet
	 */
	public int getRowCount() {
		return sheet.getLastRowNum() + 1;
	}

	/**
	 * @return the numAsText
	 */
	public boolean isNumAsText() {
		return numAsText;
	}

	/**
	 * @param numAsText the numAsText to set
	 */
	public void setNumAsText(boolean numAsText) {
		this.numAsText = numAsText;
	}

	/**
	 * Reads the entire sheet into a List with each element being a String[] of tokens.
	 * 
	 * @return a List of String[], with each String[] representing a row of the excel file.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<List<Object>> readAll() throws IOException {
		List<List<Object>> all = new ArrayList<List<Object>>();
		int n = getRowCount();
		for (int i = 0; i < n; i++) {
			List<Object> row = readRow(i);
			all.add(row);
		}
		return all;
	}

	protected Object readCell(Cell c, int type) {
		Object v = Strings.EMPTY;
		switch (type) {
		case Cell.CELL_TYPE_BLANK:
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			v = c.getBooleanCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(c)) {
				v = c.getDateCellValue();
			}
			else {
				if (numAsText) {
					v = NumberToTextConverter.toText(c.getNumericCellValue());
				}
				else {
					v = c.getNumericCellValue();
				}
			}
			break;
		case Cell.CELL_TYPE_FORMULA:
			v = readCell(c, c.getCachedFormulaResultType());
			break;
		default: 
			v = c.getStringCellValue();
			break;
		}

		return v;
	}

	/**
	 * Reads the row and converts to a string array.
	 * 
	 * @param r the row index
	 * @return a string array with each comma-separated element as a separate entry.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<Object> readRow(int r) throws IOException {
		Row row = sheet.getRow(r);
		if (row == null) {
			return null;
		}
		
		List<Object> vs = new ArrayList<Object>();
		
		int n = row.getLastCellNum();
		for (int i = 0; i < n; i++) {
			Cell c = row.getCell(i);
			if (c == null) {
				vs.add(null);
				continue;
			}

			Object v = readCell(c, c.getCellType());
			vs.add(v);
		}
		return vs;
	}
	

	/**
	 * Reads the row and converts to a string array.
	 * 
	 * @return a string array with each comma-separated element as a separate entry.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	@Override
	public List<Object> readList() throws IOException {
		if (rowidx >= getRowCount()) {
			return null;
		}
		
		List<Object> row = readRow(rowidx++);
		if (row == null) {
			return new ArrayList<Object>();
		}
		return row;
	}
}
