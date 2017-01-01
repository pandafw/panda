package panda.bind.json;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.bind.Binds;
import panda.io.Streams;
import panda.lang.Chars;
import panda.lang.Charsets;

/**
 * 
 *
 */
public abstract class Jsons extends Binds {
	public static Object fromJson(InputStream json, String encoding) {
		return fromJson(json, encoding, Object.class);
	}

	public static Object fromJson(Reader json) {
		return fromJson(json, Object.class);
	}

	public static Object fromJson(CharSequence json) {
		return fromJson(json, Object.class);
	}

	public static <T> T fromJson(InputStream json, String encoding, Type type) {
		if (json == null) {
			return null;
		}

		Reader r = Streams.toReader(json, encoding);
		JsonDeserializer jd = new JsonDeserializer();
		return jd.deserialize(r, type);
	}

	public static <T> T fromJson(InputStream json, Type type) {
		return fromJson(json, Charsets.UTF_8, type);
	}

	public static <T> T fromJson(Reader json, Type type) {
		if (json == null) {
			return null;
		}
		JsonDeserializer jd = new JsonDeserializer();
		return jd.deserialize(json, type);
	}

	public static <T> T fromJson(CharSequence json, Type type) {
		if (json == null) {
			return null;
		}
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
