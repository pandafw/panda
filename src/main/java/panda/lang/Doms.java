package panda.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import panda.io.stream.StringBuilderWriter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class Doms {
	public static Document parse(File file) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
	
		return builder.parse(file);
	}
	
	public static Document parse(InputStream input) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
	
		return builder.parse(input);
	}
	
	public static Document parse(String str) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
	
		return builder.parse(str);
	}
	
	public static interface DomTraverser {
		public static final int TRAVERSE = 0;
		public static final int CONTINUE = 1;
		public static final int RETURN = 2;
		
		/**
		 * handle the node when traverse into it 
		 * @param node the node to be handled
		 * @return false if stop the traverse
		 */
		int handle(Node node, int level);

		/**
		 * handle the node when traverse out of it
		 * @param node the node
		 */
		void handleOut(Node node, int level);
	}

	public static abstract class SimpleDomTraverser implements DomTraverser {
		public void handleOut(Node node, int level) {
		}
	}
	
	private static int _traverse(Node node, int level, DomTraverser traverser) {
		int r = traverser.handle(node, level);

		if (r == DomTraverser.TRAVERSE) {
			NodeList childs = node.getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				Node n = childs.item(i);
				if (n != null) {
					r = _traverse(n, level + 1, traverser);
					if (r == DomTraverser.RETURN) {
						return r;
					}
				}
			}
		}

		traverser.handleOut(node, level);

		return r;
	}
	
	public static void traverse(Node node, DomTraverser traverser) {
		if (node == null) {
			throw new IllegalArgumentException("node can not be null!");
		}
		if (traverser == null) {
			throw new IllegalArgumentException("traverser can not be null!");
		}

		_traverse(node, 0, traverser);
	}

	public static int removeNodes(Node node, final String name) {
		final List<Node> rns = new ArrayList<Node>();

		traverse(node, new SimpleDomTraverser() {
			public int handle(Node node, int level) {
				if (name.equals(node.getNodeName())) {
					rns.add(node);
				}
				return TRAVERSE;
			}
		});

		for (Node rn : rns) {
			if (rn.getParentNode() != null) {
				rn.getParentNode().removeChild(rn);
			}
		}
		
		return rns.size();
	}

	public static int removeNodes(Node node, final Collection<String> names) {
		final List<Node> rns = new ArrayList<Node>();

		traverse(node, new SimpleDomTraverser() {
			public int handle(Node node, int level) {
				if (names.contains(node.getNodeName())) {
					rns.add(node);
				}
				return TRAVERSE;
			}
		});

		for (Node rn : rns) {
			if (rn.getParentNode() != null) {
				rn.getParentNode().removeChild(rn);
			}
		}
		
		return rns.size();
	}

	public static Element findElementByAttibute(Node node, final String name, final String value) {
		final List<Element> targets = new ArrayList<Element>();

		traverse(node, new SimpleDomTraverser() {
			public int handle(Node node, int level) {
				if (node.getNodeType() == Node.ELEMENT_NODE 
						&& node.hasAttributes()) {
					NamedNodeMap nnm = node.getAttributes();
					for (int i = 0; i < nnm.getLength(); i++) {
						Node an = nnm.item(i);

						if (an.getNodeType() == Node.ATTRIBUTE_NODE) {
							Attr a = (Attr)an;
							
							if (name.equals(a.getName())
									&& (value == null || value.equals(a.getValue()))) {
								targets.add((Element)node);
							}
						}
					}
				}
				return TRAVERSE;
			}
		});

		return targets.isEmpty() ? null : targets.get(0);
	}

	public static void transform(Result result, Node node, boolean noXmlDeclare) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();

		Transformer transformer = tFactory.newTransformer();
		if (noXmlDeclare) {
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		}
		
		DOMSource source = new DOMSource(node);
		
		transformer.transform(source, result);
	}

	public static void transform(OutputStream output, Node node, boolean noXmlDeclare) throws TransformerException {
		transform(new StreamResult(output), node, noXmlDeclare);
	}
	
	public static void transform(File output, Node node, boolean noXmlDeclare) throws TransformerException {
		transform(new StreamResult(output), node, noXmlDeclare);
	}
	
	public static void transform(Writer output, Node node, boolean noXmlDeclare) throws TransformerException {
		transform(new StreamResult(output), node, noXmlDeclare);
	}
	
	public static void transform(OutputStream output, Node node) throws TransformerException {
		transform(new StreamResult(output), node, true);
	}
	
	public static void transform(File output, Node node) throws TransformerException {
		transform(new StreamResult(output), node, true);
	}
	
	public static void transform(Writer output, Node node) throws TransformerException {
		transform(new StreamResult(output), node, true);
	}
	
	public static void transform(OutputStream output, Document doc) throws TransformerException {
		transform(new StreamResult(output), doc, false);
	}
	
	public static void transform(File output, Document doc) throws TransformerException {
		transform(new StreamResult(output), doc, false);
	}
	
	public static void transform(Writer output, Document doc) throws TransformerException {
		transform(new StreamResult(output), doc, false);
	}
	
	public static String toString(Node node, boolean noXmlDeclare) throws TransformerException {
		StringBuilderWriter sw = new StringBuilderWriter();
		
		transform(sw, node, noXmlDeclare);
		
		return sw.toString();
	}
	
	public static String toString(Node node) throws TransformerException {
		return toString(node, true);
	}
	
	public static String toString(Document node) throws TransformerException {
		return toString(node, false);
	}
}
