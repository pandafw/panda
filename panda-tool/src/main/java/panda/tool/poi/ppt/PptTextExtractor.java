package panda.tool.poi.ppt;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.transform.TransformerException;

import org.apache.poi.hslf.model.Comment;
import org.apache.poi.hslf.model.HeadersFooters;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import panda.bind.xml.Xmls;
import panda.lang.Charsets;

/**
 * 
 */
public class PptTextExtractor extends PptTextProcessor {
	private boolean extractSummary = false;
	private boolean extractHeader = false;
	private boolean extractFooter = false;
	
	private ESlideShow ess;
	private ESlide es;
	
	/**
	 * Constructor
	 */
	public PptTextExtractor() {
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

	public void extractToXml(SlideShow ss, OutputStream output) throws TransformerException, UnsupportedEncodingException {
		OutputStreamWriter osw = new OutputStreamWriter(output, Charsets.CS_UTF_8);

		extractToXml(ss, osw);
	}

	public void extractToXml(SlideShow ss, Writer output) throws TransformerException {
		process(ss);

		Xmls.toXml(ess, output, true);
	}

	@Override
	protected void handleSlideShow(SlideShow ss) {
		ess = new ESlideShow();
		es = null;
//		if (extractSummary) {
//			SummaryInformation si = ss..getSummaryInformation();
//			if (si != null) {
//				ESummary es = new ESummary();
//				es.copy(si);
//				ess.setSummary(es);
//			}
//		}
		HeadersFooters hf = ss.getSlideHeadersFooters();
		EHeaderFooter ehf = new EHeaderFooter();
		ehf.setHeader(hf.getHeaderText());
		ehf.setFooter(hf.getFooterText());
		ess.setHeaders(ehf);
	}

	@Override
	protected void handleMasterSlide(String key, SlideMaster slide) {
		es = new ESlide();
		ess.getSlides().put(key, es);
	}

	@Override
	protected void handleSlide(String key, Slide slide) {
		es = new ESlide();
		
		HeadersFooters hf = slide.getHeadersFooters();
		EHeaderFooter ehf = new EHeaderFooter();
		ehf.setHeader(hf.getHeaderText());
		ehf.setFooter(hf.getFooterText());
		
		es.setHeader(ehf);
		ess.getSlides().put(key, es);
	}

	@Override
	protected void handleComment(String key, Comment comment) {
		EComment ec = new EComment();
		ec.setAuthor(comment.getAuthor());
		ec.setText(comment.getText());
		es.getComments().put(key, ec);
	}

	@Override
	protected void handleText(String key, TextRun textRun) {
		es.getTexts().put(key, textRun.getText());
	}

	@Override
	protected void handleNote(String key, TextRun textRun) {
		es.getNotes().put(key, textRun.getText());
	}
}
