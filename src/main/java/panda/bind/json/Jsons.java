package panda.bind.json;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.bind.Binds;
import panda.io.Streams;
import panda.lang.Chars;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class Jsons extends Binds {
	public static <T> T fromJson(InputStream is, String encoding, Type type) {
		Reader json = Streams.toReader(is, encoding);
		JsonDeserializer jd = new JsonDeserializer();
		return jd.deserialize(json, type);
	}

	public static <T> T fromJson(Reader json, Type type) {
		JsonDeserializer jd = new JsonDeserializer();
		return jd.deserialize(json, type);
	}

	public static <T> T fromJson(String json, Type type) {
		JsonDeserializer jd = new JsonDeserializer();
		return jd.deserialize(json, type);
	}

	private static JsonSerializer createJsonSerializer() {
		JsonSerializer js = new JsonSerializer();
		setDefaultSerializerOptions(js);
		return js;
	}
	
	public static String toJson(Object value) {
		JsonSerializer js = createJsonSerializer();
		return js.serialize(value);
	}

	public static String toJson(Object value, boolean pretty) {
		JsonSerializer js = createJsonSerializer();
		js.setPrettyPrint(pretty);
		return js.serialize(value);
	}

	public static String toJson(Object value, int indent) {
		JsonSerializer js = createJsonSerializer();
		js.setIndentChar(Chars.SPACE);
		js.setIndentFactor(indent);
		return js.serialize(value);
	}
	
	public static void toJson(Object value, Appendable writer) {
		JsonSerializer js = createJsonSerializer();
		js.serialize(value, writer);
	}

	public static void toJson(Object value, Appendable writer, boolean pretty) {
		JsonSerializer js = createJsonSerializer();
		js.setPrettyPrint(pretty);
		js.serialize(value, writer);
	}

	public static void toJson(Object value, Appendable writer, int indent) {
		JsonSerializer js = createJsonSerializer();
		js.setIndentChar(Chars.SPACE);
		js.setIndentFactor(indent);
		js.serialize(value, writer);
	}
}
