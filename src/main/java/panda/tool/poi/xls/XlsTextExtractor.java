package panda.tool.poi.xls;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.transform.TransformerException;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import panda.bind.xml.Xmls;
import panda.lang.Charsets;
import panda.tool.poi.ESummary;

/**
 * 
 */
public class XlsTextExtractor extends XlsTextProcessor {
	private boolean extractSummary = false;
	private boolean extractHeader = false;
	private boolean extractFooter = false;
	
	private EWorkbook ewb;
	private ESheet esh;
	
	/**
	 * Constructor
	 */
	public XlsTextExtractor() {
	}

	/**
	 * @return the extractSummary
	 */
	public boolean isExtractSummary() {
		return extractSummary;
	}

	/**
	 * @param extractSummary the extractSummary to set
	 */
	public void setExtractSummary(boolean extractSummary) {
		this.extractSummary = extractSummary;
	}

	/**
	 * @return the extractHeader
	 */
	public boolean isExtractHeader() {
		return extractHeader;
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	public void setExtractHeader(boolean extractHeader) {
		this.extractHeader = extractHeader;
	}

	/**
	 * @return the extractFooter
	 */
	public boolean isExtractFooter() {
		return extractFooter;
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	public void setExtractFooter(boolean extractFooter) {
		this.extractFooter = extractFooter;
	}

	public void extractToXml(HSSFWorkbook wb, OutputStream output) throws TransformerException, UnsupportedEncodingException {
		OutputStreamWriter osw = new OutputStreamWriter(output, Charsets.CS_UTF_8);

		extractToXml(wb, osw);
	}

	public void extractToXml(HSSFWorkbook wb, Writer output) throws TransformerException {
		process(wb);

		Xmls.toXml(ewb, output, true);
	}

	@Override
	protected void handleWorkbook(HSSFWorkbook workbook) {
		ewb = new EWorkbook();
		esh = null;
		if (extractSummary) {
			SummaryInformation si = workbook.getSummaryInformation();
			if (si != null) {
				ESummary es = new ESummary();
				es.copy(si);
				ewb.setSummary(es);
			}
		}
	}

	@Override
	protected void handleSheet(HSSFWorkbook workbook, HSSFSheet sheet, int index) {
		esh = new ESheet();

		ewb.getSheets().put(index, esh);

		esh.setIndex(index);
		esh.setName(workbook.getSheetName(index));
		if (extractHeader) {
			esh.setHeader(sheet.getHeader());
		}
		if (extractFooter) {
			esh.setFooter(sheet.getFooter());
		}
	}

	@Override
	protected void handleCell(String key, HSSFCell cell) {
		String v = cell.getStringCellValue();
		esh.getStrings().put(key, v);
	}

	@Override
	protected void handleComment(String key, HSSFComment comment) {
		String s = comment.getString().getString();
		esh.getComments().put(key, s);
	}

	@Override
	protected void handleTextbox(String key, HSSFTextbox textbox) {
		String s = textbox.getString().getString();
		esh.getTextboxs().put(key, s);
	}
}
