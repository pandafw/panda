package panda.bind.xml;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.bind.Binds;
import panda.io.Streams;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class Xmls extends Binds {
	public static <T> T fromXml(String xml, Type type) {
		XmlDeserializer xd = new XmlDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(Reader xml, Type type) {
		XmlDeserializer xd = new XmlDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(InputStream xml, String encoding, Type type) {
		Reader r = Streams.toReader(xml, encoding);
		XmlDeserializer xd = new XmlDeserializer();
		return xd.deserialize(r, type);
	}

	private static XmlSerializer createXmlSerializer() {
		XmlSerializer xs = new XmlSerializer();
		setDefaultSerializerOptions(xs);
		return xs;
	}
	
	public static String toXml(Object value) {
		XmlSerializer xs = createXmlSerializer();
		return xs.serialize(value);
	}

	public static String toXml(Object value, boolean pretty) {
		XmlSerializer xs = createXmlSerializer();
		xs.setPrettyPrint(pretty);
		return xs.serialize(value);
	}

	public static String toXml(Object value, int indent) {
		XmlSerializer xs = createXmlSerializer();
		xs.setIndentFactor(indent);
		return xs.serialize(value);
	}

	public static void toXml(Object value, Appendable writer) {
		XmlSerializer xs = createXmlSerializer();
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, boolean pretty) {
		XmlSerializer xs = createXmlSerializer();
		xs.setPrettyPrint(pretty);
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, int indent) {
		XmlSerializer xs = createXmlSerializer();
		xs.setIndentFactor(indent);
		xs.serialize(value, writer);
	}
}
