package panda.tool.poi.doc;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import panda.bean.Beans;
import panda.lang.Doms;
import panda.lang.Doms.SimpleDomTraverser;
import panda.lang.Numbers;
import panda.tool.poi.ESummary;

/**
 * 
 */
public class DocTextReplacer extends DocTextProcessor {
	//private static final Log log = LogFactory.getLog(DocTextProcessor.class);

	private EDocument edoc;
	private Map<Integer, EParagraph> erange;
	private EParagraph eparagraph;
	
	/**
	 * Constructor
	 */
	public DocTextReplacer() {
	}

	public static class Converter extends SimpleDomTraverser {
		private EDocument edoc;
		private Map<Integer, EParagraph> erange;
		private EParagraph eparagraph;
		private ECharRun ecrun;
		
		/**
		 * @param ewb
		 */
		public Converter(EDocument edoc) {
			this.edoc = edoc;
		}

		public int handle(Node node, int level) {
			if (node.getNodeType() != Node.ELEMENT_NODE) {
				return TRAVERSE;
			}
			
			Element el = (Element)node;
			
			if (level == 2) {
				if ("summary".equals(el.getNodeName())) {
					ESummary es = new ESummary();
					es.copy(el);
					edoc.setSummary(es);
				}
				else {
					erange = new LinkedHashMap<Integer, EParagraph>();
					Beans.setProperty(edoc, el.getNodeName(), erange);
				}
			}
			else if (level == 3) {
				Integer key = Numbers.toInt(el.getAttribute("key"), -1);
				eparagraph = new EParagraph();
				erange.put(key, eparagraph);
			}
			else if (level == 6) {
				Integer key = Numbers.toInt(el.getAttribute("key"), -1);
				ecrun = new ECharRun();
				eparagraph.getCruns().put(key, ecrun);
			}
			else if (level == 7) {
				ecrun.setType(el.getAttribute("type"));
				ecrun.setTitle(el.getAttribute("title"));
				ecrun.setText(el.getTextContent());
			}

			return TRAVERSE;
		}
	}
	
	private void convert(Document doc) {
		edoc = new EDocument();
		Doms.traverse(doc, new Converter(edoc));
	}
	
	public void replace(HWPFDocument doc, Document dom) {
		convert(dom);
		process(doc);
	}

	public void replaceFromXml(HWPFDocument doc, File file) throws SAXException, IOException, ParserConfigurationException {
		Document dom = Doms.parse(file);

		replace(doc, dom);
	}
	
	@Override
	protected void handleDocument(HWPFDocument doc) {
		ESummary es = edoc.getSummary();
		if (es != null) {
			es.set(doc.getSummaryInformation());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	protected boolean handleRange(String name, HWPFDocument doc, Range range) {
		erange = (Map<Integer, EParagraph>)Beans.getProperty(edoc, name);
		if (erange == null) {
			return false;
		}
		return true;
	}

	@Override
	protected boolean handleParagraph(Range range, Paragraph paragraph, int index) {
		eparagraph = erange.get(index);
		return eparagraph != null;
	}

	@Override
	protected boolean handleText(Range range, Paragraph paragraph, CharacterRun crun, int index, ECharRun ecrun) {
		if (ECharRun.ECR_TEXT.equals(ecrun.getType())) {
			ECharRun ecrun2 = eparagraph.getCruns().get(index);
			if (ecrun2 != null) {
				crun.replaceText(ecrun2.getText(), true);
				return true;
			}
		}
		return false;
	}
}
