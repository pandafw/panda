package panda.tool.sql;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

import panda.dao.sql.SqlExecutor;
import panda.dao.sql.SqlManager;
import panda.lang.Strings;

/**
 * Import data from xls to database
 */
public class XlsDataImportor extends AbstractDataImportor {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new XlsDataImportor().execute(args);
	}
	
	/**
	 * Constructor
	 */
	public XlsDataImportor() {
		includes = new String[] { "**/*.xls" };
	}

	@Override
	protected void importFile(FileInputStream fis) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook(fis);
		if (truncate) {
			for (int i = wb.getNumberOfSheets() - 1; i >= 0; i--) {
				HSSFSheet sheet = wb.getSheetAt(i);
				String tableName = sheet.getSheetName(); 
				println2("Truncating table: " + tableName);
				truncateTable(tableName);
			}
		}
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			HSSFSheet sheet = wb.getSheetAt(i);
			impXlsSheetData(sheet);
		}
	}
	
	private void impXlsSheetData(HSSFSheet sheet) throws Exception {
		String tableName = sheet.getSheetName();
		
		List<String> columns = getHeadValues(sheet, 0);
		if (columns.isEmpty()) {
			throw new Exception("[" + tableName + "] - the table column is empty!");
		}
		
		List<String> row2 = getHeadValues(sheet, 1);
		if (row2.size() != columns.size()) {
			throw new Exception("[" + tableName + "] - the column types is incorrect!");
		}
		
		List<DataType> types = new ArrayList<DataType>();
		for (String v : row2) {
			types.add(new DataType(v));
		}
		
		println2("Importing table: " + tableName);

		String insertSql = getInsertSql(tableName, columns, types);

		int line = 2;
		Map<String, Object> values = null;
		try {
			SqlExecutor executor = SqlManager.i().getExecutor(connection); 

			int cnt = 0;
			for (; ; line++) {
				values = getRowValues(sheet, line, columns, types);
				if (values == null) {
					break;
				}
				cntRecord += executor.update(insertSql, values);
				cnt++;
				if (commit > 0 && cnt >= commit) {
					connection.commit();
				}
			}

			if (cnt > 0) {
				connection.commit();
			}
		}
		catch (Exception e) {
			rollback();
			throw new Exception("Failed to import sheet [" + tableName + "]:" 
				+ line + " - " + values, e);
		}
	}	
	
	private List<String> getHeadValues(HSSFSheet sheet, int r) throws Exception {
		List<String> values = new ArrayList<String>();

		HSSFRow row = sheet.getRow(r);
		if (row != null) {
			for (int c = 0; ; c++) {
				HSSFCell cell = row.getCell(c);
				if (cell == null) {
					break;
				}
				String v = null;
				try {
					v = cell.getStringCellValue();
					if (Strings.isBlank(v)) {
						break;
					}
					values.add(v);
				}
				catch (Exception e) {
					throw new Exception("[" + sheet.getSheetName() + "] - head value is incorrect: (" + r + "," + c + ") - " + v, e);
				}
			}
		}
		
		return values;
	}
	
	private Map<String, Object> getRowValues(HSSFSheet sheet, int r, List<String> columns, List<DataType> types) throws Exception {
		HSSFRow row = sheet.getRow(r);
		if (row == null) {
			return null;
		}

		boolean empty = true;
		
		Map<String, Object> values = new HashMap<String, Object>(columns.size());
		for (int c = 0; c < columns.size(); c++) {
			HSSFCell cell = row.getCell(c);
			if (cell == null) {
				continue;
			}

			String v = null;
			try {
				switch (cell.getCellType()) {
				case Cell.CELL_TYPE_NUMERIC:
					v = String.valueOf(cell.getNumericCellValue());
					if (Strings.contains(v, '.')) {
						v = Strings.stripEnd(v, "0");
						v = Strings.stripEnd(v, ".");
					}
					break;
				case Cell.CELL_TYPE_FORMULA:
					try {
						v = String.valueOf(cell.getNumericCellValue());
					}
					catch (Exception e) {
						v = cell.getStringCellValue();
					}
					break;
				default: 
					v = cell.getStringCellValue();
					break;
				}

				Object cv;
				if (Strings.isBlank(v)) {
					cv = null;
				}
				else {
					empty = false;

					String type = types.get(c).type;
					String format = types.get(c).format;
					if ("date".equalsIgnoreCase(type)) {
						try {
							cv = cell.getDateCellValue();
						}
						catch (Exception e) {
							cv = parseDate(v, format);
						}
					}
					else {
						cv = getCellValue(v, c, types);
					}
				}
				values.put(columns.get(c), cv);
			}
			catch (Exception e) {
				throw new Exception("[" + sheet.getSheetName()
						+ "] - cell value is incorrect: (" 
						+ (r + 1) + "," + (c + 1)
						+ ") - " + v, e);
			}
		}
		
		return empty ? null : values;
	}
}
