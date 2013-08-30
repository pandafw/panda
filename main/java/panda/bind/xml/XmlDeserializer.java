package panda.bind.xml;

import java.io.Reader;
import java.lang.reflect.Type;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

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
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputSource is = new InputSource(xml);
			Document doc = builder.parse(is);
			//TODO
			return convertValue(doc, type);
		}
		catch (Exception e) {
			throw wrapError(e);
		}
		finally {
			Streams.safeClose(xml);
		}
	}
	
	protected XmlException wrapError(Throwable e) {
		if (e instanceof XmlException) {
			return (XmlException)e;
		}
		return new XmlException(e);
	}
}
