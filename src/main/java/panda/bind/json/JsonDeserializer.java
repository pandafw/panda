package panda.bind.json;

import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import panda.bean.BeanHandler;
import panda.bind.AbstractDeserializer;
import panda.bind.PropertyFilter;
import panda.lang.Numbers;
import panda.lang.reflect.Types;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class JsonDeserializer extends AbstractDeserializer {
	private Class<?> defaultJsonObjectType = JsonObject.class;
	private Class<?> defaultJsonArrayType = JsonArray.class;
	
	private JsonTokener tokener;

	public JsonDeserializer() {
	}

	/**
	 * @return the defaultJsonObjectType
	 */
	public Class<?> getDefaultJsonObjectType() {
		return defaultJsonObjectType;
	}

	/**
	 * @param defaultJsonObjectType the defaultJsonObjectType to set
	 */
	public void setDefaultJsonObjectType(Class<?> defaultJsonObjectType) {
		this.defaultJsonObjectType = defaultJsonObjectType;
	}

	/**
	 * @return the defaultJsonArrayType
	 */
	public Class<?> getDefaultJsonArrayType() {
		return defaultJsonArrayType;
	}

	/**
	 * @param defaultJsonArrayType the defaultJsonArrayType to set
	 */
	public void setDefaultJsonArrayType(Class<?> defaultJsonArrayType) {
		this.defaultJsonArrayType = defaultJsonArrayType;
	}

	/**
	 * Creates a object from a JSON, with a specific target class.<br>
	 */
	public <T> T deserialize(Reader json, Type type) {
		tokener = new JsonTokener(json);
		
		char c = tokener.nextClean();
		if (c == '[') {
			if (isArrayType(type)) {
				tokener.back();
				return parseJsonArray(type);
			}
			throw syntaxError("A json array can not be serialized to the " + Types.typeToString(type));
		}
		else if (c == '{') {
			if (!isArrayType(type)) {
				tokener.back();
				return parseJsonObject(type);
			}
			throw syntaxError("A json object can not be serialized to the " + Types.typeToString(type));
		}
		else {
			throw syntaxError("Invalid json character: " + c);
		}
	}

	/**
	 * Get the next value. The value can be a Boolean, Double, Integer, JSONArray, JSONObject, Long,
	 * or String, or the JSONObject.NULL object.
	 * 
	 * @throws JsonException If syntax error.
	 * @return An object.
	 */
	private <T> T nextValue(Type type) {
		char c = tokener.nextClean();
		String s;

		switch (c) {
		case '"':
		case '\'':
			return convertValue(tokener.nextString(c), type);
		case '{':
			tokener.back();
			return parseJsonObject(type);
		case '[':
			tokener.back();
			return parseJsonArray(type);
		default:
			// empty
		}

		/*
		 * Handle unquoted text. This could be the values true, false, or null, or it can be a
		 * number. An implementation (such as this one) is allowed to also accept non-standard
		 * forms. Accumulate characters until we reach the end of the text or a formatting
		 * character.
		 */
		StringBuilder sb = new StringBuilder();
		char b = c;
		while (c >= ' ' && c <= 0x7F && ",:]}/\\\"[{;=#".indexOf(c) < 0) {
			sb.append(c);
			c = tokener.next();
		}
		tokener.back();

		/*
		 * If it is true, false, or null, return the proper value.
		 */
		s = sb.toString().trim();
		if (s.isEmpty()) {
			throw syntaxError("Missing value.");
		}
		if (s.equalsIgnoreCase("true")) {
			return convertValue(Boolean.TRUE, type);
		}
		if (s.equalsIgnoreCase("false")) {
			return convertValue(Boolean.FALSE, type);
		}
		if (s.equals("null") || s.equals("undefined")) {
			return null;
		}

		/*
		 * If it might be a number, try converting it. We support the 0- and 0x- conventions. If a
		 * number cannot be produced, then the value will just be a string. Note that the 0-, 0x-,
		 * plus, and implied string conventions are non-standard. A JSON parser is free to accept
		 * non-JSON forms as long as it accepts all correct JSON forms.
		 */

		if ((b >= '0' && b <= '9') || b == '.' || b == '-' || b == '+') {
			if (b == '0') {
				if (s.length() > 2 && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
					Integer i = null;
					try {
						i = new Integer(Integer.parseInt(s.substring(2), 16));
					}
					catch (Exception e) {
						/* Ignore the error */
					}
					if (i != null) {
						convertValue(i, type);
					}
				}
				else {
					Integer i = null;
					try {
						i = new Integer(Integer.parseInt(s, 8));
					}
					catch (Exception e) {
						/* Ignore the error */
					}
					if (i != null) {
						convertValue(i, type);
					}
				}
			}

			Number i = null;
			try {
				i = Numbers.createNumber(s);
			}
			catch (Exception e) {
			}
			if (i != null) {
				return convertValue(i, type);
			}
			return convertValue(s, type);
		}

		switch (tokener.peek()) {
		case ',':
		case '}':
		case '{':
		case '[':
		case ']':
			throw new JsonException("Unquotted string '" + s + "'");
		}

		return convertValue(s, type);
	}

	private JsonException syntaxError(String message) {
		return tokener.syntaxError(message);
	}

	private <T> T parseJsonObject(Type type) {
		if (Object.class.equals(type)) {
			type = defaultJsonObjectType;
		}

		char c = tokener.nextClean();
		if (c != '{') {
			throw syntaxError("A json object text must begin with '{'");
		}
		if (type != null && isArrayType(type)) {
			throw syntaxError("A json object can not be serialized to the " + Types.typeToString(type));
		}
		
		T obj = null;
		BeanHandler<T> bh = null;
		PropertyFilter<T> pf = null;
		if (type != null) {
			bh = getBeanHandler(type);
			obj = bh.createObject();
			pf = getPropertyFilter(type);
		}

		String key;
		while (true) {
			c = tokener.nextClean();
			switch (c) {
			case 0:
				throw syntaxError("A JSONObject text must end with '}'");
			case '}':
				return obj;
			case '"':
			case '\'':
				key = tokener.nextString(c);
				break;
			default:
				if (Character.isJavaIdentifierStart(c)) {
					tokener.back();
					key = tokener.nextId();
				}
				else {
					throw syntaxError("Invalid json character: " + c);
				}
			}

			/*
			 * The key is followed by ':'. We will also tolerate '=' or '=>'.
			 */
			c = tokener.nextClean();
			if (c == '=') {
				if (tokener.next() != '>') {
					tokener.back();
				}
			}
			else if (c != ':') {
				throw syntaxError("Expected a ':' after a key");
			}

			if (type == null || isExcludeProperty(key)) {
				nextValue(null);
			}
			else {
				if (bh.canWriteProperty(key)) {
					Type pt = bh.getPropertyType(key);
					if (isExcludeProperty(Types.getRawType(pt))) {
						nextValue(null);
					}
					else {
						Object val = nextValue(pt);
						if (pf == null || pf.accept(obj, key, val)) {
							bh.setPropertyValue(obj, key, val);
						}
					}
				}
				else if (bh.canReadProperty(key)) {
					if (isIgnoreReadonlyProperty()) {
						nextValue(null);
					}
					else {
						throw syntaxError("readonly property: " + key);
					}
				}
				else {
					if (isIgnoreMissingProperty()) {
						nextValue(null);
					}
					else {
						throw syntaxError("missing property: " + key);
					}
				}
			}

			/*
			 * Pairs are separated by ','. We will also tolerate ';'.
			 */
			switch (tokener.nextClean()) {
			case ';':
			case ',':
				if (tokener.nextClean() == '}') {
					return obj;
				}
				tokener.back();
				break;
			case '}':
				return obj;
			default:
				throw syntaxError("Expected a ',' or '}'");
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T parseJsonArray(Type type) {
		if (Object.class.equals(type)) {
			type = defaultJsonArrayType;
		}

		if (tokener.nextClean() != '[') {
			throw syntaxError("A json array text must start with '['");
		}
		if (type != null && !isArrayType(type)) {
			throw syntaxError("A json array can not be serialized to the " + Types.typeToString(type));
		}

		List list = null;
		Type etype = null;

		if (type != null) {
			list = new ArrayList();
			etype = getArrayElementType(type);
		}

		if (tokener.nextClean() == ']') {
			return (T)(list == null ? null : convertValue(list, type));
		}

		tokener.back();
		for (;;) {
			if (tokener.nextClean() == ',') {
				tokener.back();
				if (list != null) {
					list.add(null);
				}
			}
			else {
				tokener.back();
				Object v = nextValue(etype);
				if (list != null) {
					list.add(v);
				}
			}

			switch (tokener.nextClean()) {
			case ';':
			case ',':
				if (tokener.nextClean() == ']') {
					return (T)(list == null ? null : convertValue(list, type));
				}
				tokener.back();
				break;
			case ']':
				return (T)(list == null ? null : convertValue(list, type));
			default:
				throw syntaxError("Expected a ',' or ']'");
			}
		}
	}
}
