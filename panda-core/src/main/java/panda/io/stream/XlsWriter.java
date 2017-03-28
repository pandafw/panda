package panda.io.stream;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import panda.lang.Arrays;
import panda.lang.Collections;

/**
 * XLS writer
 */
public class XlsWriter implements ListWriter, Closeable, Flushable {
	private OutputStream writer;
	private Workbook workbook;
	private Sheet sheet;
	private int rowidx;

	private boolean allString;
	private CellStyle dateCellStyle;

	public XlsWriter(OutputStream os) throws IOException {
		writer = os;
		workbook = create();
		init();
	}

	/**
	 * @return the allString
	 */
	public boolean isAllString() {
		return allString;
	}

	/**
	 * @param allString the allString to set
	 */
	public void setAllString(boolean allString) {
		this.allString = allString;
	}

	/**
	 * create workbook
	 * @return workbook
	 * @throws IOException
	 */
	protected Workbook create() throws IOException {
		return new HSSFWorkbook();
	}

	/**
	 * initialize workbook
	 * @throws IOException
	 */
	protected void init() throws IOException {
		newSheet();
		setDateFormat("yyyy-MM-dd HH:mm:ss");
	}

	public void setDateFormat(String format) {
		dateCellStyle = workbook.createCellStyle();
		CreationHelper createHelper = workbook.getCreationHelper();
		dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("format"));
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
	 * Returns the name of this sheet
	 * 
	 * @return the name of this sheet
	 */
	public String getSheetName() {
		return sheet.getSheetName();
	}

	public void newSheet() {
		sheet = workbook.createSheet();
		rowidx = 0;
	}

	public void newSheet(String name) {
		sheet = workbook.createSheet(name);
		rowidx = 0;
	}

	/**
	 * set current row index
	 * @param i row index
	 */
	public void setRowIndex(int i) {
		rowidx = i;
	}

	/**
	 * @return the row count of this sheet
	 */
	public int getRowCount() {
		return sheet.getLastRowNum() + 1;
	}

	/**
	 * Writes the entire list to a CSV file. The list is assumed to be a String[]
	 *
	 * @param allLines a List of String[], with each String[] representing a line of the file.
	 * @throws IOException if an IO error occurred
	 */
	public void writeAll(List<?> allLines) throws IOException {
		for (Iterator iter = allLines.iterator(); iter.hasNext();) {
			Object nextLine = iter.next();
			if (nextLine == null){
				writeList(Collections.EMPTY_LIST);
			}
			else if (nextLine instanceof Collection) {
				writeList((Collection)nextLine);
			}
			else if (nextLine instanceof Object[]) {
				writeArray((Object[])nextLine);
			}
			else {
				throw new IllegalArgumentException("the element of list is not a instance of Collection or String[].");
			}
		}
	}

	/**
	 * Writes the list to the file.
	 *
	 * @param list a collection with each comma-separated element as a separate entry.
	 */
	@Override
	public void writeList(Collection<?> list) throws IOException {
		Row row = sheet.createRow(rowidx++);
		
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext();) {
			Cell cell = row.createCell(i++);

			Object o = it.next();
			if (o == null) {
				continue;
			}

			if (allString) {
				cell.setCellType(Cell.CELL_TYPE_STRING);
				cell.setCellValue(o.toString());
			}
			else {
				if (o instanceof Date) {
					cell.setCellValue((Date)o);
					cell.setCellStyle(dateCellStyle);
				}
				else if (o instanceof Calendar) {
					cell.setCellValue((Calendar)o);
				}
				else if (o instanceof Boolean) {
					cell.setCellValue((Boolean)o);
				}
				else if (o instanceof Number) {
					cell.setCellValue(((Number)o).doubleValue());
				}
				else {
					cell.setCellValue(o.toString());
				}
			}
		}
	}

	/**
	 * Writes the next line to the file.
	 *
	 * @param array a string array with each comma-separated element as a separate entry.
	 * @throws IOException if an IO error occurred
	 */
	public void writeArray(Object[] array) throws IOException {
		writeList(Arrays.asList(array));
	}

	/**
	 * Flush underlying stream to writer.
	 *
	 * @throws IOException if bad things happen
	 */
	public void flush() throws IOException {
		writer.flush();
	}

	/**
	 * Close the underlying stream writer flushing any buffered content.
	 *
	 * @throws IOException if bad things happen
	 *
	 */
	public void close() throws IOException {
		writer.close();
	}
}
