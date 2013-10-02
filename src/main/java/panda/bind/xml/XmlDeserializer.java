package panda.bind.xml;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import panda.bind.AbstractDeserializer;
import panda.io.Streams;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class XmlDeserializer extends AbstractDeserializer {
	public XmlDeserializer() {
	}

	/**
	 * Creates a object from a XML, with a specific target class.<br>
	 */
	public <T> T deserialize(Reader xml, Type type) {
		try {
			SAXParserFactory parserFactor = SAXParserFactory.newInstance();
			SAXParser parser = parserFactor.newSAXParser();
			parser.parse(new InputSource(xml), new SAXHandler());
			//TODO
			return null;
		}
		catch (Exception e) {
			throw wrapError(e);
		}
		finally {
			Streams.safeClose(xml);
		}
	}

	private class SAXHandler extends DefaultHandler {
		@Override
		public InputSource resolveEntity(String publicId, String systemId) throws IOException, SAXException {
			return super.resolveEntity(publicId, systemId);
		}

		@Override
		public void notationDecl(String name, String publicId, String systemId) throws SAXException {
		}

		@Override
		public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName)
				throws SAXException {
		}

		@Override
		public void setDocumentLocator(Locator locator) {
		}

		@Override
		public void startDocument() throws SAXException {
		}

		@Override
		public void endDocument() throws SAXException {
		}

		@Override
		public void startPrefixMapping(String prefix, String uri) throws SAXException {
		}

		@Override
		public void endPrefixMapping(String prefix) throws SAXException {
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
		}

		@Override
		public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
		}

		@Override
		public void processingInstruction(String target, String data) throws SAXException {
		}

		@Override
		public void skippedEntity(String name) throws SAXException {
		}

		@Override
		public void warning(SAXParseException e) throws SAXException {
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
		}

		@Override
		public void fatalError(SAXParseException e) throws SAXException {
		}
	}
	
	protected XmlException wrapError(Throwable e) {
		if (e instanceof XmlException) {
			return (XmlException)e;
		}
		return new XmlException(e);
	}
}
