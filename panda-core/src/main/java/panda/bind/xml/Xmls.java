package panda.bind.xml;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.io.Streams;

/**
 * 
 *
 */
public abstract class Xmls {

	public static XmlDeserializer newXmlDeserializer() {
		XmlDeserializer xd = new XmlDeserializer();
		return xd;
	}

	public static XmlSerializer newXmlSerializer() {
		XmlSerializer xs = new XmlSerializer();
		return xs;
	}

	public static <T> T fromXml(String xml, Type type) {
		XmlDeserializer xd = newXmlDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(Reader xml, Type type) {
		XmlDeserializer xd = newXmlDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(InputStream xml, String encoding, Type type) {
		Reader r = Streams.toReader(xml, encoding);
		XmlDeserializer xd = newXmlDeserializer();
		return xd.deserialize(r, type);
	}
	
	public static String toXml(Object value) {
		XmlSerializer xs = newXmlSerializer();
		return xs.serialize(value);
	}

	public static String toXml(Object value, boolean pretty) {
		XmlSerializer xs = newXmlSerializer();
		xs.setPrettyPrint(pretty);
		return xs.serialize(value);
	}

	public static String toXml(Object value, int indent) {
		XmlSerializer xs = newXmlSerializer();
		xs.setIndentFactor(indent);
		return xs.serialize(value);
	}

	public static void toXml(Object value, Appendable writer) {
		XmlSerializer xs = newXmlSerializer();
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, boolean pretty) {
		XmlSerializer xs = newXmlSerializer();
		xs.setPrettyPrint(pretty);
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, int indent) {
		XmlSerializer xs = newXmlSerializer();
		xs.setIndentFactor(indent);
		xs.serialize(value, writer);
	}
}
