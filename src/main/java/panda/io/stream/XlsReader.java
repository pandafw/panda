package panda.io.stream;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import panda.lang.Strings;

/**
 * Excel reader.
 */
public class XlsReader implements ListReader, Closeable {
	private Workbook workbook;
	private Sheet sheet;
	private int rowidx;
	
	public XlsReader(InputStream is) throws IOException {
		workbook = init(is);
		setSheet(0);
	}

	/**
	 * initialize workbook
	 * @param is input stream
	 * @return workbook
	 * @throws IOException
	 */
	protected Workbook init(InputStream is) throws IOException {
		return new HSSFWorkbook(is);
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
	 * Reads the entire sheet into a List with each element being a String[] of tokens.
	 * 
	 * @return a List of String[], with each String[] representing a row of the excel file.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<List<String>> readAll() throws IOException {
		List<List<String>> all = new ArrayList<List<String>>();
		int n = getRowCount();
		for (int i = 0; i < n; i++) {
			List<String> row = readRow(i);
			all.add(row);
		}
		return all;
	}

	/**
	 * Reads the row and converts to a string array.
	 * 
	 * @return a string array with each comma-separated element as a separate entry.
	 * 
	 * @throws IOException if bad things happen during the read
	 */
	public List<String> readRow(int r) throws IOException {
		Row row = sheet.getRow(r);
		if (row == null) {
			return null;
		}
		
		List<String> vs = new ArrayList<String>();
		
		int n = row.getLastCellNum();
		for (int i = 0; i < n; i++) {
			Cell c = row.getCell(i);
			if (c == null) {
				vs.add(null);
				continue;
			}

			String v = null;
			switch (c.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				v = c.getBooleanCellValue() ? "true" : "false";
				break;
			case Cell.CELL_TYPE_NUMERIC:
				v = String.valueOf(c.getNumericCellValue());
				if (Strings.contains(v, '.')) {
					v = Strings.stripEnd(v, "0");
					v = Strings.stripEnd(v, ".");
				}
				break;
			case Cell.CELL_TYPE_FORMULA:
				try {
					v = String.valueOf(c.getNumericCellValue());
				}
				catch (Exception e) {
					v = c.getStringCellValue();
				}
				break;
			default: 
				v = c.getStringCellValue();
				break;
			}
			
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
	public List<String> readList() throws IOException {
		if (rowidx >= getRowCount()) {
			return null;
		}
		List<String> row = readRow(rowidx++);
		if (row == null) {
			return new ArrayList<String>();
		}
		return row;
	}
}
