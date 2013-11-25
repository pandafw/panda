package panda.tool.poi.xls;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFSimpleShape;
import org.apache.poi.hssf.usermodel.HSSFTextbox;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HeaderFooter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import panda.lang.Doms;
import panda.lang.Doms.SimpleDomTraverser;
import panda.lang.Numbers;
import panda.tool.poi.ESummary;

/**
 * 
 */
public class XlsTextReplacer extends XlsTextProcessor {
	private EWorkbook ewb;
	private ESheet esh;
	
	/**
	 * Constructor
	 */
	public XlsTextReplacer() {
	}

	public static class Converter extends SimpleDomTraverser {
		private EWorkbook ewb;
		private ESheet esh;
		private int last;
		
		/**
		 * @param ewb
		 */
		public Converter(EWorkbook ewb) {
			this.ewb = ewb;
		}

		public int handle(Node node, int level) {
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				return TRAVERSE;
			}
			
			Element el = (Element)node;
			if (el.getNodeName().equals("summary")) {
				ESummary es = new ESummary();
				es.copy(el);
				ewb.setSummary(es);
			}
			else if (el.getNodeName().equals("header")) {
				EHeaderFooter eh = new EHeaderFooter();
				eh.setLeft(el.getAttribute("left"));
				eh.setCenter(el.getAttribute("center"));
				eh.setRight(el.getAttribute("right"));
				esh.setHeader(eh);
			}
			else if (el.getNodeName().equals("footer")) {
				EHeaderFooter ef = new EHeaderFooter();
				ef.setLeft(el.getAttribute("left"));
				ef.setCenter(el.getAttribute("center"));
				ef.setRight(el.getAttribute("right"));
				esh.setFooter(ef);
			}
			else if (el.getNodeName().equals("comments")) {
				last = 'c';
			}
			else if (el.getNodeName().equals("strings")) {
				last = 's';
			}
			else if (el.getNodeName().equals("textboxs")) {
				last = 't';
			}
			else if (el.getNodeName().equals("item")) {
				switch (last) {
				case 'c':
					esh.getComments().put(el.getAttribute("key"), el.getTextContent());
					break;
				case 's':
					esh.getStrings().put(el.getAttribute("key"), el.getTextContent());
					break;
				case 't':
					esh.getTextboxs().put(el.getAttribute("key"), el.getTextContent());
					break;
				}
			}
			else if (el.getNodeName().equals("value")) {
				esh = new ESheet();
				esh.setIndex(Numbers.toInt(el.getAttribute("index")));
				esh.setName(el.getAttribute("name"));
				ewb.getSheets().put(esh.getIndex(), esh);
			}
			return TRAVERSE;
		}
	}
	
	private void convert(Document doc) {
		ewb = new EWorkbook();
		Doms.traverse(doc, new Converter(ewb));
	}
	
	public void replace(HSSFWorkbook wb, Document doc) {
		convert(doc);
		process(wb);
	}

	public void replaceFromXml(HSSFWorkbook wb, File file) throws SAXException, IOException, ParserConfigurationException {
		Document doc = Doms.parse(file);

		replace(wb, doc);
	}
	
	@Override
	protected void handleWorkbook(HSSFWorkbook workbook) {
		ESummary es = ewb.getSummary();
		if (es != null) {
			es.set(workbook.getSummaryInformation());
		}
		esh = null;
	}

	@Override
	protected void handleSheet(HSSFWorkbook workbook, HSSFSheet sheet, int index) {
		esh = ewb.getSheets().get(index);

		if (esh == null) {
			return;
		}
		
		HeaderFooter eh = esh.getHeader();
		if (eh != null) {
			sheet.getHeader().setCenter(eh.getCenter());
			sheet.getHeader().setLeft(eh.getLeft());
			sheet.getHeader().setRight(eh.getRight());
		}

		HeaderFooter ef = esh.getFooter();
		if (ef != null) {
			sheet.getFooter().setCenter(ef.getCenter());
			sheet.getFooter().setLeft(ef.getLeft());
			sheet.getFooter().setRight(ef.getRight());
		}
	}

	@Override
	protected void handleCell(String key, HSSFCell cell) {
		String v = esh.getStrings().get(key);
		if (v != null) {
			replaceRichTextString(cell, v);
		}
	}

	@Override
	protected void handleComment(String key, HSSFComment comment) {
		String v = esh.getComments().get(key);
		if (v != null) {
			replaceRichTextString(comment, v);
		}
	}

	@Override
	protected void handleTextbox(String key, HSSFTextbox textbox) {
		String v = esh.getTextboxs().get(key);
		if (v != null) {
			replaceRichTextString(textbox, v);
		}
	}
	
	private void replaceRichTextString(HSSFCell cell, String text) {
		HSSFRichTextString ors = cell.getRichStringCellValue();
		HSSFRichTextString nrs = new HSSFRichTextString(text);
		
		copyFont(ors, nrs);
		
		cell.setCellValue(nrs);
	}

	private void replaceRichTextString(HSSFSimpleShape shape, String text) {
		HSSFRichTextString ors = shape.getString();
		HSSFRichTextString nrs = new HSSFRichTextString(text);

		copyFont(ors, nrs);
		
		shape.setString(nrs);
	}
	
	private void copyFont(HSSFRichTextString ors, HSSFRichTextString nrs) {
		String ot = ors.getString();
		String nt = nrs.getString();

		// TODO: use rate to apply font
		short lastFont = HSSFRichTextString.NO_FONT;
		int lastStart = 0;
		for (int i = 0; i < ot.length() && i < nt.length(); i++) {
			short font = ors.getFontAtIndex(i);
			if (font != lastFont) {
				if (lastFont != HSSFRichTextString.NO_FONT) {
					nrs.applyFont(lastStart, i, lastFont);
				}
				lastFont = font;
				lastStart = i;
			}
		}
		if (lastFont != HSSFRichTextString.NO_FONT 
				&& (lastStart < ot.length() || lastStart < nt.length())) {
			nrs.applyFont(lastStart, nt.length(), lastFont);
		}
	}
}
