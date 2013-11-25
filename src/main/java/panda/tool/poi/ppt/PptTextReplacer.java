package panda.tool.poi.ppt;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hslf.model.Comment;
import org.apache.poi.hslf.model.HeadersFooters;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import panda.lang.Doms;
import panda.lang.Doms.SimpleDomTraverser;
import panda.lang.Strings;
import panda.tool.poi.ESummary;

/**
 * 
 */
public class PptTextReplacer extends PptTextProcessor {
	private ESlideShow ess;
	private ESlide esd;
	
	/**
	 * Constructor
	 */
	public PptTextReplacer() {
	}

	public static class Converter extends SimpleDomTraverser {
		private ESlideShow ess;
		private ESlide esd;
		
		/**
		 * @param ewb
		 */
		public Converter(ESlideShow ess) {
			this.ess = ess;
		}

		public int handle(Node node, int level) {
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				return TRAVERSE;
			}
			
			Element el = (Element)node;
			if (el.getNodeName().equals("summary")) {
				ESummary es = new ESummary();
				es.copy(el);
				ess.setSummary(es);
			}
			else if (el.getNodeName().equals("headers")) {
				EHeaderFooter ehf = new EHeaderFooter();
				ehf.setHeader(el.getAttribute("header"));
				ehf.setFooter(el.getAttribute("footer"));
				ess.setHeaders(ehf);
			}
			else if (el.getNodeName().equals("header")) {
				EHeaderFooter ehf = new EHeaderFooter();
				ehf.setHeader(el.getAttribute("header"));
				ehf.setFooter(el.getAttribute("footer"));
				esd.setHeader(ehf);
			}
			//TODO: parse
			return TRAVERSE;
		}
	}
	
	private void convert(Document doc) {
		ess = new ESlideShow();
		Doms.traverse(doc, new Converter(ess));
	}
	
	public void replace(SlideShow ss, Document doc) {
		convert(doc);
		process(ss);
	}

	public void replaceFromXml(SlideShow ss, File file) throws SAXException, IOException, ParserConfigurationException {
		Document doc = Doms.parse(file);

		replace(ss, doc);
	}
	
	@Override
	protected void handleSlideShow(SlideShow ss) {
		EHeaderFooter ehf = ess.getHeaders();
		if (ehf != null) {
			HeadersFooters hf = ss.getSlideHeadersFooters();
			if (hf != null) {
				hf.setHeaderText(ehf.getHeader());
				hf.setFootersText(ehf.getFooter());
			}
		}
		ess = null;
	}

	@Override
	protected void handleMasterSlide(String key, SlideMaster slide) {
	}

	@Override
	protected void handleSlide(String key, Slide slide) {
		esd = ess.getSlides().get(key);
		if (esd != null) {
			EHeaderFooter ehf = esd.getHeader();
			if (ehf != null) {
				HeadersFooters hf = slide.getHeadersFooters();
				if (hf != null) {
					hf.setHeaderText(ehf.getHeader());
					hf.setFootersText(ehf.getFooter());
				}
			}
		}
	}

	@Override
	protected void handleComment(String key, Comment comment) {
		EComment ec = esd.getComments().get(key);
		if (ec != null) {
			comment.setAuthor(ec.getAuthor());
			comment.setText(ec.getText());
		}
	}

	@Override
	protected void handleText(String key, TextRun textRun) {
		String s = esd.getTexts().get(key);
		if (Strings.isNotEmpty(s)) {
			textRun.setText(s);
		}
	}

	@Override
	protected void handleNote(String key, TextRun textRun) {
		String s = esd.getNotes().get(key);
		if (Strings.isNotEmpty(s)) {
			textRun.setText(s);
		}
	}
}
