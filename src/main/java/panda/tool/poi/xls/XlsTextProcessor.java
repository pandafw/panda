package panda.tool.poi.xls;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFShapeContainer;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import panda.lang.Strings;

/**
 * 
 */
public abstract class XlsTextProcessor {
	/**
	 * Constructor
	 */
	public XlsTextProcessor() {
	}

	private void processStrings(HSSFSheet sheet) {
		int firstRow = sheet.getFirstRowNum();
		int lastRow = sheet.getLastRowNum();
		for (int r = firstRow; r <= lastRow; r++) {
			HSSFRow row = sheet.getRow(r);
			if (row == null) { continue; }

			// Check each cell in turn
			int firstCell = row.getFirstCellNum();
			int lastCell = row.getLastCellNum();

			for (int c = firstCell; c < lastCell; c++) {
				HSSFCell cell = row.getCell(c);

				if (cell == null) {
					continue;
				}

				if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
					String v = cell.getStringCellValue();
					if (Strings.isNotEmpty(v)) {
						handleCell(getKey(r, c), cell);
					}
				}
			}
		}
	}

	private String getKey(int x, int y) {
		return String.valueOf(x) + ':' + String.valueOf(y);
	}
	
	private void processObjects(int level, HSSFShapeContainer container) {
		if (container == null) {
			return;
		}

		List<HSSFShape> shapes = container.getChildren();
		if (shapes == null) {
			return;
		}
		
		for (int i = 0; i < shapes.size(); i++) {
			HSSFShape shape = shapes.get(i);
			if (shape instanceof HSSFShapeContainer) {
				processObjects(level + 1, (HSSFShapeContainer)shape);
				continue;
			}
			if (shape instanceof HSSFComment) {
				HSSFComment c = (HSSFComment)shape;
				String s = c.getString().getString();
				if (Strings.isNotEmpty(s)) {
					handleComment(getKey(level, i), c);
				}
			}
			else if (shape instanceof HSSFTextbox) {
				HSSFTextbox t = (HSSFTextbox)shape;
				String s = t.getString().getString();
				if (Strings.isNotEmpty(s)) {
					handleTextbox(getKey(level, i), t);
				}
			}
		}
	}

	protected abstract void handleWorkbook(HSSFWorkbook workbook);
	protected abstract void handleSheet(HSSFWorkbook workbook, HSSFSheet sheet, int index);
	protected abstract void handleCell(String key, HSSFCell cell);
	protected abstract void handleComment(String key, HSSFComment comment);
	protected abstract void handleTextbox(String key, HSSFTextbox textbox);
	
	protected void process(HSSFWorkbook wb) {
		// We don't care about the difference between
		//  null (missing) and blank cells
		wb.setMissingCellPolicy(HSSFRow.RETURN_BLANK_AS_NULL);

		handleWorkbook(wb);
		
		// Process each sheet in turn
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			HSSFSheet sh = wb.getSheetAt(i);
			if (sh == null) { continue; }

			handleSheet(wb, sh, i);

			HSSFShapeContainer container = sh.getDrawingPatriarch();
			processObjects(0, container);
			processStrings(sh);
		}
	}
}
