package panda.bind.xmlrpc;

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
public abstract class XmlRpcs extends Binds {
	public static <T> T fromXml(String xml, Type type) {
		XmlRpcDeserializer xd = new XmlRpcDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(Reader xml, Type type) {
		XmlRpcDeserializer xd = new XmlRpcDeserializer();
		return xd.deserialize(xml, type);
	}

	public static <T> T fromXml(InputStream xml, String encoding, Type type) {
		Reader r = Streams.toReader(xml, encoding);
		XmlRpcDeserializer xd = new XmlRpcDeserializer();
		return xd.deserialize(r, type);
	}

	private static XmlRpcSerializer createXmlSerializer() {
		XmlRpcSerializer xs = new XmlRpcSerializer();
		setDefaultSerializerOptions(xs);
		return xs;
	}
	
	public static String toXml(Object value, boolean call) {
		return toXml(value, call, false);
	}

	public static String toXml(Object value, boolean isMethodCall, boolean pretty) {
		XmlRpcSerializer xs = createXmlSerializer();
		xs.setMethodCall(isMethodCall);
		xs.setPrettyPrint(pretty);
		return xs.serialize(value);
	}

	public static String toXml(Object value, boolean isMethodCall, int indent) {
		XmlRpcSerializer xs = createXmlSerializer();
		xs.setMethodCall(isMethodCall);
		xs.setIndentFactor(indent);
		return xs.serialize(value);
	}

	public static void toXml(Object value, Appendable writer) {
		XmlRpcSerializer xs = createXmlSerializer();
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, boolean pretty) {
		XmlRpcSerializer xs = createXmlSerializer();
		xs.setPrettyPrint(pretty);
		xs.serialize(value, writer);
	}

	public static void toXml(Object value, Appendable writer, int indent) {
		XmlRpcSerializer xs = createXmlSerializer();
		xs.setIndentFactor(indent);
		xs.serialize(value, writer);
	}
}
