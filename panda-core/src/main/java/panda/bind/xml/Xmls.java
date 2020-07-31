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
		return new XmlDeserializer();
	}

	public static XmlSerializer newXmlSerializer() {
		return new XmlSerializer();
	}

	public static <T> T fromXml(String xml, Type type) {
		return newXmlDeserializer().deserialize(xml, type);
	}

	public static <T> T fromXml(Reader xml, Type type) {
		return newXmlDeserializer().deserialize(xml, type);
	}

	public static <T> T fromXml(InputStream xml, String encoding, Type type) {
		return newXmlDeserializer().deserialize(Streams.toReader(xml, encoding), type);
	}
	
	public static String toXml(Object value) {
		return newXmlSerializer().serialize(value);
	}

	public static String toXml(Object value, String dateFormat) {
		XmlSerializer xs = newXmlSerializer();
		xs.setDateFormat(dateFormat);
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
		newXmlSerializer().serialize(value, writer);
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
