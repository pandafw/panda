package panda.bind.xml;

import java.io.Reader;
import java.lang.reflect.Type;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
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
		
	}
	
	protected XmlException wrapError(Throwable e) {
		if (e instanceof XmlException) {
			return (XmlException)e;
		}
		return new XmlException(e);
	}
}
