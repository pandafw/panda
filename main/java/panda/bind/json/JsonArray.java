package panda.bind.json;

import java.io.StringReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A JsonArray is an ordered sequence of values. Its external text form is a
 * string wrapped in square brackets with commas separating the values. The
 * internal form is an object having <code>get</code> and <code>opt</code>
 * methods for accessing the values by index, and <code>put</code> methods for
 * adding or replacing values. The values can be any of these types:
 * <code>Boolean</code>, <code>JsonArray</code>, <code>JsonObject</code>,
 * <code>Number</code>, <code>String</code>, or the
 * <code>JsonObject.NULL object</code>.
 * <p>
 * The constructor can convert a JSON text into a Java object. The
 * <code>toString</code> method converts to JSON text.
 * <p>
 * A <code>get</code> method returns a value if one can be found, and throws an
 * exception if one cannot be found. An <code>opt</code> method returns a
 * default value instead of throwing an exception, and so is useful for
 * obtaining optional values.
 * <p>
 * The generic <code>get()</code> and <code>opt()</code> methods return an
 * object which you can cast or query for type. There are also typed
 * <code>get</code> and <code>opt</code> methods that do type checking and type
 * coercion for you.
 * <p>
 * The texts produced by the <code>toString</code> methods strictly conform to
 * JSON syntax rules. The constructors are more forgiving in the texts they will
 * accept:
 * <ul>
 * <li>An extra <code>,</code>&nbsp;<small>(comma)</small> may appear just
 * before the closing bracket.</li>
 * <li>The <code>null</code> value will be inserted when there is <code>,</code>
 * &nbsp;<small>(comma)</small> elision.</li>
 * <li>Strings may be quoted with <code>'</code>&nbsp;<small>(single
 * quote)</small>.</li>
 * <li>Strings do not need to be quoted at all if they do not begin with a quote
 * or single quote, and if they do not contain leading or trailing spaces, and
 * if they do not contain any of these characters:
 * <code>{ } [ ] / \ : , #</code> and if they do not look like numbers and
 * if they are not the reserved words <code>true</code>, <code>false</code>, or
 * <code>null</code>.</li>
 * </ul>
 *
 * @original JSON.org
 * @author yf.frank.wang@gmail.com
 */
public class JsonArray extends ArrayList<Object> {
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Construct an empty JsonArray.
	 */
	public JsonArray() {
	}

	/**
	 * @param initialCapacity the initial capacity of the list
	 */
	public JsonArray(int initialCapacity) {
		super(initialCapacity);
	}

	/**
	 * Construct a JsonArray from a Collection.
	 * 
	 * @param collection A Collection.
	 */
	public JsonArray(Collection collection) {
		if (collection != null) {
			Iterator iter = collection.iterator();
			while (iter.hasNext()) {
				this.add(JsonObject.wrap(iter.next()));
			}
		}
	}

	/**
	 * Construct a JsonArray from an array
	 * 
	 * @throws JsonException If not an array.
	 */
	public JsonArray(Object array) throws JsonException {
		if (array.getClass().isArray()) {
			int length = Array.getLength(array);
			for (int i = 0; i < length; i += 1) {
				this.put(JsonObject.wrap(Array.get(array, i)));
			}
		}
		else {
			throw new JsonException("JsonArray initial value should be a string or collection or array.");
		}
	}

	/**
	 * Get the boolean value associated with an index. The string values "true" and "false" are
	 * converted to boolean.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The truth.
	 * @throws JsonException If there is no value for the index or if the value is not convertible
	 *             to boolean.
	 */
	public boolean getBoolean(int index) throws JsonException {
		Object object = this.get(index);
		if (object.equals(Boolean.FALSE) || (object instanceof String && ((String)object).equalsIgnoreCase("false"))) {
			return false;
		}
		else if (object.equals(Boolean.TRUE) || (object instanceof String && ((String)object).equalsIgnoreCase("true"))) {
			return true;
		}
		throw new JsonException("JsonArray[" + index + "] is not a boolean.");
	}

	/**
	 * Get the double value associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 * @throws JsonException If the key is not found or if the value cannot be converted to a
	 *             number.
	 */
	public double getDouble(int index) throws JsonException {
		Object object = this.get(index);
		try {
			return object instanceof Number ? ((Number)object).doubleValue() : Double.parseDouble((String)object);
		}
		catch (Exception e) {
			throw new JsonException("JsonArray[" + index + "] is not a number.");
		}
	}

	/**
	 * Get the int value associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 * @throws JsonException If the key is not found or if the value is not a number.
	 */
	public int getInt(int index) throws JsonException {
		Object object = this.get(index);
		try {
			return object instanceof Number ? ((Number)object).intValue() : Integer.parseInt((String)object);
		}
		catch (Exception e) {
			throw new JsonException("JsonArray[" + index + "] is not a number.");
		}
	}

	/**
	 * Get the JsonArray associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return A JsonArray value.
	 * @throws JsonException If there is no value for the index. or if the value is not a JsonArray
	 */
	public JsonArray getJsonArray(int index) throws JsonException {
		Object object = this.get(index);
		if (object instanceof JsonArray) {
			return (JsonArray)object;
		}
		throw new JsonException("JsonArray[" + index + "] is not a JsonArray.");
	}

	/**
	 * Get the JsonObject associated with an index.
	 * 
	 * @param index subscript
	 * @return A JsonObject value.
	 * @throws JsonException If there is no value for the index or if the value is not a JsonObject
	 */
	public JsonObject getJsonObject(int index) throws JsonException {
		Object object = this.get(index);
		if (object instanceof JsonObject) {
			return (JsonObject)object;
		}
		throw new JsonException("JsonArray[" + index + "] is not a JsonObject.");
	}

	/**
	 * Get the long value associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 * @throws JsonException If the key is not found or if the value cannot be converted to a
	 *             number.
	 */
	public long getLong(int index) throws JsonException {
		Object object = this.get(index);
		try {
			return object instanceof Number ? ((Number)object).longValue() : Long.parseLong((String)object);
		}
		catch (Exception e) {
			throw new JsonException("JsonArray[" + index + "] is not a number.");
		}
	}

	/**
	 * Get the string associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return A string value.
	 * @throws JsonException If there is no string value for the index.
	 */
	public String getString(int index) throws JsonException {
		Object object = this.get(index);
		if (object instanceof String) {
			return (String)object;
		}
		throw new JsonException("JsonArray[" + index + "] not a string.");
	}

	/**
	 * Determine if the value is null.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return true if the value at the index is null, or if there is no value.
	 */
	public boolean isNull(int index) {
		return null == this.opt(index);
	}

	/**
	 * Get the number of elements in the JsonArray, included nulls.
	 * 
	 * @return The length (or size).
	 */
	public int length() {
		return this.size();
	}

	/**
	 * Get the optional object value associated with an index.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return An object value, or null if there is no object at that index.
	 */
	public Object opt(int index) {
		return (index < 0 || index >= this.length()) ? null : this.get(index);
	}

	/**
	 * Get the optional boolean value associated with an index. It returns false if there is no
	 * value at that index, or if the value is not Boolean.TRUE or the String "true".
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The truth.
	 */
	public boolean optBoolean(int index) {
		return this.optBoolean(index, false);
	}

	/**
	 * Get the optional boolean value associated with an index. It returns the defaultValue if there
	 * is no value at that index or if it is not a Boolean or the String "true" or "false" (case
	 * insensitive).
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @param defaultValue A boolean default.
	 * @return The truth.
	 */
	public boolean optBoolean(int index, boolean defaultValue) {
		try {
			return this.getBoolean(index);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get the optional double value associated with an index. NaN is returned if there is no value
	 * for the index, or if the value is not a number and cannot be converted to a number.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 */
	public double optDouble(int index) {
		return this.optDouble(index, Double.NaN);
	}

	/**
	 * Get the optional double value associated with an index. The defaultValue is returned if there
	 * is no value for the index, or if the value is not a number and cannot be converted to a
	 * number.
	 * 
	 * @param index subscript
	 * @param defaultValue The default value.
	 * @return The value.
	 */
	public double optDouble(int index, double defaultValue) {
		try {
			return this.getDouble(index);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get the optional int value associated with an index. Zero is returned if there is no value
	 * for the index, or if the value is not a number and cannot be converted to a number.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 */
	public int optInt(int index) {
		return this.optInt(index, 0);
	}

	/**
	 * Get the optional int value associated with an index. The defaultValue is returned if there is
	 * no value for the index, or if the value is not a number and cannot be converted to a number.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @param defaultValue The default value.
	 * @return The value.
	 */
	public int optInt(int index, int defaultValue) {
		try {
			return this.getInt(index);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get the optional JsonArray associated with an index.
	 * 
	 * @param index subscript
	 * @return A JsonArray value, or null if the index has no value, or if the value is not a
	 *         JsonArray.
	 */
	public JsonArray optJsonArray(int index) {
		Object o = this.opt(index);
		return o instanceof JsonArray ? (JsonArray)o : null;
	}

	/**
	 * Get the optional JsonObject associated with an index. Null is returned if the key is not
	 * found, or null if the index has no value, or if the value is not a JsonObject.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return A JsonObject value.
	 */
	public JsonObject optJsonObject(int index) {
		Object o = this.opt(index);
		return o instanceof JsonObject ? (JsonObject)o : null;
	}

	/**
	 * Get the optional long value associated with an index. Zero is returned if there is no value
	 * for the index, or if the value is not a number and cannot be converted to a number.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return The value.
	 */
	public long optLong(int index) {
		return this.optLong(index, 0);
	}

	/**
	 * Get the optional long value associated with an index. The defaultValue is returned if there
	 * is no value for the index, or if the value is not a number and cannot be converted to a
	 * number.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @param defaultValue The default value.
	 * @return The value.
	 */
	public long optLong(int index, long defaultValue) {
		try {
			return this.getLong(index);
		}
		catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Get the optional string value associated with an index. It returns an empty string if there
	 * is no value at that index. If the value is not a string and is not null, then it is coverted
	 * to a string.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @return A String value.
	 */
	public String optString(int index) {
		return this.optString(index, "");
	}

	/**
	 * Get the optional string associated with an index. The defaultValue is returned if the key is
	 * not found.
	 * 
	 * @param index The index must be between 0 and length() - 1.
	 * @param defaultValue The default value.
	 * @return A String value.
	 */
	public String optString(int index, String defaultValue) {
		Object object = this.opt(index);
		return null == object ? defaultValue : object.toString();
	}

	/**
	 * Append a boolean value. This increases the array's length by one.
	 * 
	 * @param value A boolean value.
	 * @return this.
	 */
	public JsonArray put(boolean value) {
		this.put(value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a value in the JsonArray, where the value will be a JsonArray which is produced from a
	 * Collection.
	 * 
	 * @param value A Collection value.
	 * @return this.
	 */
	public JsonArray put(Collection value) {
		this.put(new JsonArray(value));
		return this;
	}

	/**
	 * Append a double value. This increases the array's length by one.
	 * 
	 * @param value A double value.
	 * @throws JsonException if the value is not finite.
	 * @return this.
	 */
	public JsonArray put(double value) throws JsonException {
		Double d = new Double(value);
		this.put(d);
		return this;
	}

	/**
	 * Append a float value. This increases the array's length by one.
	 * 
	 * @param value A float value.
	 * @throws JsonException if the value is not finite.
	 * @return this.
	 */
	public JsonArray put(float value) throws JsonException {
		Float d = new Float(value);
		this.put(d);
		return this;
	}

	/**
	 * Append an int value. This increases the array's length by one.
	 * 
	 * @param value An int value.
	 * @return this.
	 */
	public JsonArray put(int value) {
		this.put(new Integer(value));
		return this;
	}

	/**
	 * Append an long value. This increases the array's length by one.
	 * 
	 * @param value A long value.
	 * @return this.
	 */
	public JsonArray put(long value) {
		this.put(new Long(value));
		return this;
	}

	/**
	 * Put a value in the JsonArray, where the value will be a JsonObject which is produced from a
	 * Map.
	 * 
	 * @param value A Map value.
	 * @return this.
	 */
	public JsonArray put(Map value) {
		this.put(new JsonObject(value));
		return this;
	}

	/**
	 * Append an object value. This increases the array's length by one.
	 * 
	 * @param value An object value. The value should be a Boolean, Double, Integer, JsonArray,
	 *            JsonObject, Long, or String, or the JsonObject.NULL object.
	 * @return this.
	 */
	public JsonArray put(Object value) {
		this.add(value);
		return this;
	}

	/**
	 * Put or replace a boolean value in the JsonArray. If the index is greater than the length of
	 * the JsonArray, then null elements will be added as necessary to pad it out.
	 * 
	 * @param index The subscript.
	 * @param value A boolean value.
	 * @return this.
	 * @throws JsonException If the index is negative.
	 */
	public JsonArray put(int index, boolean value) throws JsonException {
		this.put(index, value ? Boolean.TRUE : Boolean.FALSE);
		return this;
	}

	/**
	 * Put a value in the JsonArray, where the value will be a JsonArray which is produced from a
	 * Collection.
	 * 
	 * @param index The subscript.
	 * @param value A Collection value.
	 * @return this.
	 * @throws JsonException If the index is negative or if the value is not finite.
	 */
	public JsonArray put(int index, Collection value) throws JsonException {
		this.put(index, new JsonArray(value));
		return this;
	}

	/**
	 * Put or replace a double value. If the index is greater than the length of the JsonArray, then
	 * null elements will be added as necessary to pad it out.
	 * 
	 * @param index The subscript.
	 * @param value A double value.
	 * @return this.
	 * @throws JsonException If the index is negative or if the value is not finite.
	 */
	public JsonArray put(int index, double value) throws JsonException {
		this.put(index, new Double(value));
		return this;
	}

	/**
	 * Put or replace an int value. If the index is greater than the length of the JsonArray, then
	 * null elements will be added as necessary to pad it out.
	 * 
	 * @param index The subscript.
	 * @param value An int value.
	 * @return this.
	 * @throws JsonException If the index is negative.
	 */
	public JsonArray put(int index, int value) throws JsonException {
		this.put(index, new Integer(value));
		return this;
	}

	/**
	 * Put or replace a long value. If the index is greater than the length of the JsonArray, then
	 * null elements will be added as necessary to pad it out.
	 * 
	 * @param index The subscript.
	 * @param value A long value.
	 * @return this.
	 * @throws JsonException If the index is negative.
	 */
	public JsonArray put(int index, long value) throws JsonException {
		this.put(index, new Long(value));
		return this;
	}

	/**
	 * Put a value in the JsonArray, where the value will be a JsonObject that is produced from a
	 * Map.
	 * 
	 * @param index The subscript.
	 * @param value The Map value.
	 * @return this.
	 * @throws JsonException If the index is negative or if the the value is an invalid number.
	 */
	public JsonArray put(int index, Map value) throws JsonException {
		this.put(index, new JsonObject(value));
		return this;
	}

	/**
	 * Put or replace an object value in the JsonArray. If the index is greater than the length of
	 * the JsonArray, then null elements will be added as necessary to pad it out.
	 * 
	 * @param index The subscript.
	 * @param value The value to put into the array. The value should be a Boolean, Double, Integer,
	 *            JsonArray, JsonObject, Long, or String, or the JsonObject.NULL object.
	 * @return this.
	 * @throws JsonException If the index is negative or if the the value is an invalid number.
	 */
	public JsonArray put(int index, Object value) throws JsonException {
		if (index < 0) {
			throw new JsonException("JsonArray[" + index + "] not found.");
		}
		if (index < this.length()) {
			this.set(index, value);
		}
		else {
			while (index != this.length()) {
				this.add(null);
			}
			this.put(value);
		}
		return this;
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
	
	public static JsonArray fromJson(String json) {
		if (json == null) {
			return null;
		}
		
		JsonTokener jt = new JsonTokener(new StringReader(json));
		char c = jt.nextClean();
		if (c != '[') {
			json = "[" + json + "]";
		}
		return Jsons.fromJson(json, JsonArray.class);
	}
}
