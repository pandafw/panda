package panda.bind.json;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.io.Streams;
import panda.lang.Charsets;

public abstract class Jsons {
	public static JsonDeserializer newJsonDeserializer() {
		return new JsonDeserializer();
	}

	public static JsonSerializer newJsonSerializer() {
		return new JsonSerializer();
	}
	
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

		return newJsonDeserializer().deserialize(Streams.toReader(json, encoding), type);
	}

	public static <T> T fromJson(InputStream json, Type type) {
		return fromJson(json, Charsets.UTF_8, type);
	}

	public static <T> T fromJson(Reader json, Type type) {
		if (json == null) {
			return null;
		}
		return newJsonDeserializer().deserialize(json, type);
	}

	public static <T> T fromJson(CharSequence json, Type type) {
		if (json == null) {
			return null;
		}
		return newJsonDeserializer().deserialize(json, type);
	}

	public static String toJson(Object value) {
		return newJsonSerializer().serialize(value);
	}

	public static String toJson(Object value, String dateFormat) {
		JsonSerializer js = newJsonSerializer();
		js.setDateFormat(dateFormat);
		return js.serialize(value);
	}

	public static String toJson(Object value, boolean pretty) {
		JsonSerializer js = newJsonSerializer();
		js.setPrettyPrint(pretty);
		return js.serialize(value);
	}

	public static String toJson(Object value, int indent) {
		JsonSerializer js = newJsonSerializer();
		js.setIndentChar(' ');
		js.setIndentFactor(indent);
		return js.serialize(value);
	}
	
	public static void toJson(Object value, Appendable writer) {
		newJsonSerializer().serialize(value, writer);
	}

	public static void toJson(Object value, Appendable writer, boolean pretty) {
		JsonSerializer js = newJsonSerializer();
		js.setPrettyPrint(pretty);
		js.serialize(value, writer);
	}

	public static void toJson(Object value, Appendable writer, int indent) {
		JsonSerializer js = newJsonSerializer();
		js.setIndentChar(' ');
		js.setIndentFactor(indent);
		js.serialize(value, writer);
	}
}
