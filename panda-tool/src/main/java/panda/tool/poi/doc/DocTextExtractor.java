package panda.tool.poi.doc;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.transform.TransformerException;

import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;

import panda.bean.Beans;
import panda.bind.xml.Xmls;
import panda.lang.Charsets;
import panda.tool.poi.ESummary;

/**
 * 
 */
public class DocTextExtractor extends DocTextProcessor {
	private boolean extractSummary = false;
	private boolean extractHeader = false;
	private boolean extractFooter = false;
	
	private EDocument edoc;
	private Map<Integer, EParagraph> erange;
	private EParagraph eparagraph;
	
	/**
	 * Constructor
	 */
	public DocTextExtractor() {
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

	public void extractToXml(HWPFDocument doc, OutputStream output) throws TransformerException, UnsupportedEncodingException {
		OutputStreamWriter osw = new OutputStreamWriter(output, Charsets.CS_UTF_8);

		extractToXml(doc, osw);
	}

	public void extractToXml(HWPFDocument doc, Writer output) throws TransformerException {
		process(doc);

		Xmls.toXml(edoc, output, true);
	}

	@Override
	protected void handleDocument(HWPFDocument doc) {
		edoc = new EDocument();
		erange = null;
		eparagraph = null;
		if (extractSummary) {
			final SummaryInformation si = doc.getSummaryInformation();
			if (si != null) {
				ESummary es = new ESummary();
				es.copy(si);
				edoc.setSummary(es);
			}
		}
	}

	@Override
	protected boolean handleRange(String name, HWPFDocument doc, Range range) {
		if (extractHeader || !name.equals("header")) {
			erange = new LinkedHashMap<Integer, EParagraph>();
			Beans.setProperty(edoc, name, erange);
			return true;
		}
		return false;
	}

	@Override
	protected boolean handleParagraph(Range range, Paragraph paragraph, int index) {
		eparagraph = new EParagraph();
		erange.put(index, eparagraph);
		return true;
	}

	@Override
	protected boolean handleText(Range range, Paragraph paragraph, CharacterRun crun, int index, ECharRun ecrun) {
		eparagraph.getCruns().put(index, ecrun);
		return false;
	}
}
