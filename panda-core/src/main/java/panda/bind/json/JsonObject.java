package panda.bind.json;

import java.io.InputStream;
import java.io.Reader;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import panda.io.stream.CharSequenceReader;

/**
 * A JsonObject is an unordered collection of name/value pairs. Its external
 * form is a string wrapped in curly braces with colons between the names and
 * values, and commas between the values and names. The internal form is an
 * object having <code>get</code> and <code>opt</code> methods for accessing
 * the values by name, and <code>put</code> methods for adding or replacing
 * values by name. The values can be any of these types: <code>Boolean</code>,
 * <code>JSONArray</code>, <code>JsonObject</code>, <code>Number</code>,
 * <code>String</code>, or the <code>JsonObject.NULL</code> object. A
 * JsonObject constructor can be used to convert an external form JSON text
 * into an internal form whose values can be retrieved with the
 * <code>get</code> and <code>opt</code> methods, or to convert values into a
 * JSON text using the <code>put</code> and <code>toString</code> methods. A
 * <code>get</code> method returns a value if one can be found, and throws an
 * exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object, which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and type
 * coercion for you. The opt methods differ from the get methods in that they
 * do not throw. Instead, they return a specified value, such as null.
 * <p>
 * The <code>put</code> methods add or replace values in an object. For
 * example,
 *
 * <pre>
 * myString = new JsonObject()
 *         .put(&quot;JSON&quot;, &quot;Hello, World!&quot;).toString();
 * </pre>
 *
 * produces the string <code>{"JSON": "Hello, World"}</code>.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * the JSON syntax rules. The constructors are more forgiving in the texts they
 * will accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing brace.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single
 * quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a
 * quote or single quote, and if they do not contain leading or trailing
 * spaces, and if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>,
 * or <code>null</code>.</li>
 * </ul>
 *
 * @see <a href="http://JSON.org">JSON.org</a>
 */
public class JsonObject extends LinkedHashMap<String, Object> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct an empty JsonObject.
	 */
	public JsonObject() {
		super();
	}

	/**
	 * @param initialCapacity the initial capacity
	 * @param loadFactor the load factor
	 */
	public JsonObject(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}

	/**
	 * @param initialCapacity the initial capacity
	 */
	public JsonObject(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Construct a JsonObject from a subset of another JsonObject. An array of strings is used to
	 * identify the keys that should be copied. Missing keys are ignored.
	 * 
	 * @param jo A JsonObject.
	 * @param names An array of strings.
	 * @exception JsonException If a value is a non-finite number or if a name is duplicated.
	 */
	public JsonObject(JsonObject jo, String[] names) {
		for (int i = 0; i < names.length; i += 1) {
			this.set(names[i], jo.opt(names[i]));
		}
	}

	/**
	 * Construct a JsonObject from a Map.
	 * 
	 * @param map A map object that can be used to initialize the contents of the JsonObject.
	 */
	public JsonObject(Map<?,?> map) {
		if (map != null) {
			for (java.util.Map.Entry<?, ?> en : map.entrySet()) {
				this.set(en.getKey().toString(), en.getValue());
			}
		}
	}

	/**
	 * Accumulate values under a key. It is similar to the put method except that if there is
	 * already an object stored under the key then a JSONArray is stored under the key to hold all
	 * of the accumulated values. If there is already a JSONArray, then the new value is appended to
	 * it. In contrast, the put method replaces the previous value. If only one value is accumulated
	 * that is not a JSONArray, then the result will be the same as using put. But if multiple
	 * values are accumulated, then the result will be like append.
	 * 
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JsonException If the value is an invalid number or if the key is null.
	 */
	public JsonObject accumulate(String key, Object value) throws JsonException {
		Object object = this.opt(key);
		if (object == null) {
			this.set(key, value);
		}
		else if (object instanceof JsonArray) {
			((JsonArray)object).put(value);
		}
		else {
			this.put(key, new JsonArray().put(object).put(value));
		}
		return this;
	}

	public JsonObject accumulateAll(Map<String, Object> map) throws JsonException {
		for (java.util.Map.Entry<String, Object> en : map.entrySet()) {
			accumulate(en.getKey(), en.getValue());
		}
		return this;
	}

	/**
	 * Append values to the array under a key. If the key does not exist in the JsonObject, then the
	 * key is put in the JsonObject with its value being a JSONArray containing the value parameter.
	 * If the key was already associated with a JSONArray, then the value parameter is appended to
	 * it.
	 * 
	 * @param key A key string.
	 * @param value An object to be accumulated under the key.
	 * @return this.
	 * @throws JsonException If the key is null or if the current value associated with the key is
	 *             not a JSONArray.
	 */
	public JsonObject append(String key, Object value) throws JsonException {
		Object object = this.opt(key);
		if (object == null) {
			this.put(key, new JsonArray().put(value));
		}
		else if (object instanceof JsonArray) {
			((JsonArray)object).put(value);
		}
		else {
			throw new JsonException("JsonObject[" + key + "] is not a JSONArray.");
		}
		return this;
	}

	/**
	 * Get the value object associated with a key.
	 * 
	 * @param key A key string.
	 * @return The object associated with the key.
	 * @throws JsonException if the key is not found.
	 */
	public Object get(String key) throws JsonException {
		if (key == null) {
			throw new JsonException("Null key.");
		}
		
		Object object = super.get(key);
		if (object == null) {
			throw new JsonException("JsonObject[" + key + "] not found.");
		}
		return object;
	}

	/**
	 * Get the boolean value associated with a key.
	 * 
	 * @param key A key string.
	 * @return The truth.
	 * @throws JsonException if the value is not a Boolean.
	 */
	public boolean getBoolean(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof Boolean) {
			return ((Boolean)object).booleanValue();
		}
		throw new JsonException("JsonObject[" + key + "] is not a Boolean.");
	}

	/**
	 * Get the double value associated with a key.
	 * 
	 * @param key A key string.
	 * @return The numeric value.
	 * @throws JsonException if the key is not found or if the value is not a Number object
	 */
	public double getDouble(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof Number) {
			return ((Number)object).doubleValue();
		}
		throw new JsonException("JsonObject[" + key + "] is not a number.");
	}

	/**
	 * Get the float value associated with a key.
	 * 
	 * @param key A key string.
	 * @return The numeric value.
	 * @throws JsonException if the key is not found or if the value is not a Number object
	 */
	public float getFloat(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof Number) {
			return ((Number)object).floatValue();
		}
		throw new JsonException("JsonObject[" + key + "] is not a number.");
	}

	/**
	 * Get the int value associated with a key.
	 * 
	 * @param key A key string.
	 * @return The integer value.
	 * @throws JsonException if the key is not found or if the value is not a Number object
	 */
	public int getInt(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof Number) {
			return ((Number)object).intValue();
		}
		throw new JsonException("JsonObject[" + key + "] is not a number.");
	}

	/**
	 * Get the JSONArray value associated with a key.
	 * 
	 * @param key A key string.
	 * @return A JSONArray which is the value.
	 * @throws JsonException if the key is not found or if the value is not a JSONArray.
	 */
	public JsonArray getJsonArray(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof JsonArray) {
			return (JsonArray)object;
		}
		throw new JsonException("JsonObject[" + key + "] is not a JsonArray.");
	}

	/**
	 * Get the JsonObject value associated with a key.
	 * 
	 * @param key A key string.
	 * @return A JsonObject which is the value.
	 * @throws JsonException if the key is not found or if the value is not a JsonObject.
	 */
	public JsonObject getJsonObject(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof JsonObject) {
			return (JsonObject)object;
		}
		throw new JsonException("JsonObject[" + key + "] is not a JsonObject.");
	}

	/**
	 * Get the long value associated with a key.
	 * 
	 * @param key A key string.
	 * @return The long value.
	 * @throws JsonException if the key is not found or if the value is not a Number object
	 */
	public long getLong(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof Number) {
			return ((Number)object).longValue();
		}
		throw new JsonException("JsonObject[" + key + "] is not a number.");
	}

	/**
	 * Get the string associated with a key.
	 * 
	 * @param key A key string.
	 * @return A string which is the value.
	 * @throws JsonException if there is no string value for the key.
	 */
	public String getString(String key) throws JsonException {
		Object object = this.get(key);
		if (object instanceof String) {
			return (String)object;
		}
		throw new JsonException("JsonObject[" + key + "] not a string.");
	}

	/**
	 * Determine if the JsonObject contains a specific key.
	 * 
	 * @param key A key string.
	 * @return true if the key exists in the JsonObject.
	 */
	public boolean has(String key) {
		return this.containsKey(key);
	}

	/**
	 * Increment a property of a JsonObject. If there is no such property, create one with a value
	 * of 1. If there is such a property, and if it is an Integer, Long, Double, or Float, then add
	 * one to it.
	 * 
	 * @param key A key string.
	 * @return this.
	 * @throws JsonException If there is already a property with this name that is not an Integer,
	 *             Long, Double, or Float.
	 */
	public JsonObject increment(String key) throws JsonException {
		Object value = this.opt(key);
		if (value == null) {
			this.set(key, 1);
		}
		else if (value instanceof Short) {
			this.set(key, ((Short)value).shortValue() + 1);
		}
		else if (value instanceof Integer) {
			this.set(key, ((Integer)value).intValue() + 1);
		}
		else if (value instanceof Long) {
			this.set(key, ((Long)value).longValue() + 1);
		}
		else if (value instanceof Double) {
			this.set(key, ((Double)value).doubleValue() + 1);
		}
		else if (value instanceof Float) {
			this.set(key, ((Float)value).floatValue() + 1);
		}
		else {
			throw new JsonException("Unable to increment [" + key + "].");
		}
		return this;
	}

	/**
	 * Determine if the value associated with the key is null or if there is no value.
	 * 
	 * @param key A key string.
	 * @return true if there is no value associated with the key or if the value is null.
	 */
	public boolean isNull(String key) {
		return null == this.opt(key);
	}

	/**
	 * Get an enumeration of the keys of the JsonObject.
	 * 
	 * @return An iterator of the keys.
	 */
	public Iterator<String> keys() {
		return this.keySet().iterator();
	}

	/**
	 * Get the number of keys stored in the JsonObject.
	 * 
	 * @return The number of keys in the JsonObject.
	 */
	public int length() {
		return this.size();
	}

	/**
	 * Produce a JSONArray containing the names of the elements of this JsonObject.
	 * 
	 * @return A JSONArray containing the key strings, or null if the JsonObject is empty.
	 */
	public JsonArray names() {
		JsonArray ja = new JsonArray();
		Iterator<String> keys = this.keys();
		while (keys.hasNext()) {
			ja.put(keys.next());
		}
		return ja.length() == 0 ? null : ja;
	}

	/**
	 * Get an optional value associated with a key.
	 * 
	 * @param key A key string.
	 * @return An object which is the value, or null if there is no value.
	 */
	public Object opt(String key) {
		if (key == null) {
			return null;
		}
		return super.get(key);
	}

	/**
	 * Get an optional boolean associated with a key. It returns false if there is no such key, or
	 * if the value is not Boolean.TRUE.
	 * 
	 * @param key A key string.
	 * @return The truth.
	 */
	public boolean optBoolean(String key) {
		return this.optBoolean(key, false);
	}

	/**
	 * Get an optional boolean associated with a key. It returns the defaultValue if there is no
	 * such key, or if it is not a Boolean.
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return The truth.
	 */
	public boolean optBoolean(String key, boolean defaultValue) {
		Object object = this.opt(key);
		if (object instanceof Boolean) {
			return ((Boolean)object).booleanValue();
		}
		return defaultValue;
	}

	/**
	 * Get an optional double associated with a key, or NaN if there is no such key or if its value
	 * is not a number. 
	 * 
	 * @param key A string which is the key.
	 * @return An object which is the value.
	 */
	public double optDouble(String key) {
		return this.optDouble(key, Double.NaN);
	}

	/**
	 * Get an optional double associated with a key, or the defaultValue if there is no such key or
	 * if its value is not a number. 
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public double optDouble(String key, double defaultValue) {
		Object object = this.opt(key);
		if (object instanceof Number) {
			return ((Number)object).doubleValue();
		}
		return defaultValue;
	}

	/**
	 * Get an optional float associated with a key, or NaN if there is no such key or if its value
	 * is not a number.
	 * 
	 * @param key A string which is the key.
	 * @return An object which is the value.
	 */
	public float optFloat(String key) {
		return this.optFloat(key, Float.NaN);
	}

	/**
	 * Get an optional float associated with a key, or the defaultValue if there is no such key or
	 * if its value is not a number.
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public float optFloat(String key, float defaultValue) {
		Object object = this.opt(key);
		if (object instanceof Number) {
			return ((Number)object).floatValue();
		}
		return defaultValue;
	}

	/**
	 * Get an optional int value associated with a key, or zero if there is no such key or if the
	 * value is not a number.
	 * 
	 * @param key A key string.
	 * @return An object which is the value.
	 */
	public int optInt(String key) {
		return this.optInt(key, 0);
	}

	/**
	 * Get an optional int value associated with a key, or the default if there is no such key or if
	 * the value is not a number.
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public int optInt(String key, int defaultValue) {
		Object object = this.opt(key);
		if (object instanceof Number) {
			return ((Number)object).intValue();
		}
		return defaultValue;
	}

	/**
	 * Get an optional JSONArray associated with a key. It returns null if there is no such key, or
	 * if its value is not a JSONArray.
	 * 
	 * @param key A key string.
	 * @return A JSONArray which is the value.
	 */
	public JsonArray optJsonArray(String key) {
		Object o = this.opt(key);
		return o instanceof JsonArray ? (JsonArray)o : null;
	}

	/**
	 * Get an optional JsonObject associated with a key. It returns null if there is no such key, or
	 * if its value is not a JsonObject.
	 * 
	 * @param key A key string.
	 * @return A JsonObject which is the value.
	 */
	public JsonObject optJsonObject(String key) {
		Object object = this.opt(key);
		return object instanceof JsonObject ? (JsonObject)object : null;
	}

	/**
	 * Get an optional long value associated with a key, or zero if there is no such key or if the
	 * value is not a number.
	 * 
	 * @param key A key string.
	 * @return An object which is the value.
	 */
	public long optLong(String key) {
		return this.optLong(key, 0);
	}

	/**
	 * Get an optional long value associated with a key, or the default if there is no such key or
	 * if the value is not a number.
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return An object which is the value.
	 */
	public long optLong(String key, long defaultValue) {
		Object object = this.opt(key);
		if (object instanceof Number) {
			return ((Number)object).longValue();
		}
		return defaultValue;
	}

	/**
	 * Get an optional string associated with a key. It returns null if there is no such
	 * key or if the value is not a string.
	 * 
	 * @param key A key string.
	 * @return A string which is the value.
	 */
	public String optString(String key) {
		return optString(key, null);
	}

	/**
	 * Get an optional string associated with a key. It returns the defaultValue if there is no such
	 * key or if the value is not a string.
	 * 
	 * @param key A key string.
	 * @param defaultValue The default.
	 * @return A string which is the value.
	 */
	public String optString(String key, String defaultValue) {
		Object object = this.opt(key);
		if (object instanceof String) {
			return (String)object;
		}
		return defaultValue;
	}

	/**
	 * Put a key/boolean pair in the JsonObject.
	 * 
	 * @param key A key string.
	 * @param value A boolean which is the value.
	 * @return this.
	 * @throws JsonException If the key is null.
	 */
	public JsonObject set(String key, boolean value) throws JsonException {
		this.put(key, value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a key/value pair in the JsonObject, where the value will be a JSONArray which is produced
	 * from a Collection.
	 * 
	 * @param key A key string.
	 * @param value A Collection value.
	 * @return this.
	 * @throws JsonException if the value is invalid
	 */
	public JsonObject set(String key, Collection<?> value) throws JsonException {
		this.put(key, new JsonArray(value));
		return this;
	}

	/**
	 * Put a key/double pair in the JsonObject.
	 * 
	 * @param key A key string.
	 * @param value A double which is the value.
	 * @return this.
	 * @throws JsonException If the key is null or if the number is invalid.
	 */
	public JsonObject set(String key, double value) throws JsonException {
		this.put(key, new Double(value));
		return this;
	}

	/**
	 * Put a key/float pair in the JsonObject.
	 * 
	 * @param key A key string.
	 * @param value A float which is the value.
	 * @return this.
	 * @throws JsonException If the key is null or if the number is invalid.
	 */
	public JsonObject set(String key, float value) throws JsonException {
		this.put(key, new Float(value));
		return this;
	}

	/**
	 * Put a key/int pair in the JsonObject.
	 * 
	 * @param key A key string.
	 * @param value An int which is the value.
	 * @return this.
	 * @throws JsonException If the key is null.
	 */
	public JsonObject set(String key, int value) throws JsonException {
		this.put(key, new Integer(value));
		return this;
	}

	/**
	 * Put a key/long pair in the JsonObject.
	 * 
	 * @param key A key string.
	 * @param value A long which is the value.
	 * @return this.
	 * @throws JsonException If the key is null.
	 */
	public JsonObject set(String key, long value) throws JsonException {
		this.put(key, new Long(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JsonObject, where the value will be a JsonObject which is
	 * produced from a Map.
	 * 
	 * @param key A key string.
	 * @param value A Map value.
	 * @return this.
	 * @throws JsonException if the value is invalid
	 */
	public JsonObject set(String key, Map<?, ?> value) throws JsonException {
		this.put(key, new JsonObject(value));
		return this;
	}

	/**
	 * Put a key/value pair in the JsonObject. If the value is null, then the key will be removed
	 * from the JsonObject if it is present.
	 * 
	 * @param key A key string.
	 * @param value An object which is the value. It should be of one of these types: Boolean,
	 *            Double, Integer, JSONArray, JsonObject, Long, String, or the JsonObject.NULL
	 *            object.
	 * @return this.
	 * @throws JsonException If the value is non-finite number or if the key is null.
	 */
	public JsonObject set(String key, Object value) throws JsonException {
		this.put(key, value);
		return this;
	}

	/**
	 * Put a key/value pair in the JsonObject, but only if the key and the value are both non-null.
	 * 
	 * @param key A key string.
	 * @param value An object which is the value. It should be of one of these types: Boolean,
	 *            Double, Integer, JSONArray, JsonObject, Long, String, or the JsonObject.NULL
	 *            object.
	 * @return this.
	 * @throws JsonException If the value is a non-finite number.
	 */
	public JsonObject setOpt(String key, Object value) throws JsonException {
		if (key != null) {
			this.set(key, value);
		}
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object put(String key, Object value) {
		if (key == null) {
			throw new NullPointerException("Null key.");
		}
		return super.put(key, wrap(value));
	}

	/**
	 * Wrap an object, if necessary. If the object is null, return the NULL object. If it is an
	 * array or collection, wrap it in a JSONArray. If it is a map, wrap it in a JsonObject. If it
	 * is a standard property (Double, String, et al) then it is already wrapped. Otherwise, if it
	 * comes from one of the java packages, turn it into a string. And if it doesn't, try to wrap it
	 * in a JsonObject. If the wrapping fails, then null is returned.
	 * 
	 * @param object The object to wrap
	 * @return The wrapped value
	 */
	protected static Object wrap(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof JsonObject || object instanceof JsonArray
				|| object instanceof String || object instanceof Character
				|| object instanceof Boolean || object instanceof Number) {
			return object;
		}

		if (object instanceof Collection) {
			return new JsonArray((Collection<?>)object);
		}
		if (object.getClass().isArray()) {
			return new JsonArray(object);
		}
		if (object instanceof Map) {
			return new JsonObject((Map<?, ?>)object);
		}
		
		throw new JsonException("Invalid argument: " + object.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 */
	public String toString() {
		return Jsons.toJson(this);
	}
	
	public String toString(boolean pretty) {
		return Jsons.toJson(this, pretty);
	}
	
	public String toString(int indent) {
		return Jsons.toJson(this, indent);
	}
	
	public static JsonObject fromJson(InputStream json) {
		return Jsons.fromJson(json, JsonObject.class);
	}
	
	public static JsonObject fromJson(InputStream json, String encoding) {
		return Jsons.fromJson(json, encoding, JsonObject.class);
	}
	
	public static JsonObject fromJson(Reader json) {
		return Jsons.fromJson(json, JsonObject.class);
	}
	
	public static JsonObject fromJson(CharSequence json) {
		if (json == null) {
			return null;
		}
		
		JsonTokener jt = new JsonTokener(new CharSequenceReader(json));
		char c = jt.nextClean();
		if (c != '{') {
			json = "{" + json + "}";
		}
		return Jsons.fromJson(json, JsonObject.class);
	}
}
