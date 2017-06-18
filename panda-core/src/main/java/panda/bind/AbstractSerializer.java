package panda.bind;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bind.adapter.CalendarAdapter;
import panda.bind.adapter.DateAdapter;
import panda.lang.Arrays;
import panda.lang.Chars;
import panda.lang.Collections;
import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;

@SuppressWarnings("rawtypes")
public abstract class AbstractSerializer extends AbstractBinder implements Serializer {
	public static final int PRETTY_INDENT_FACTOR = 1;
	
	public static final int DEFAULT_PRETTY_INDENT = 1;

	//-------------- settings --------------------- 
	protected int cycleDetectStrategy = CycleDetectStrategy.CYCLE_DETECT_NOPROP;

	/** indent character */
	protected char indentChar = Chars.TAB;

	/** The number of spaces to add to each level of indentation. */
	protected int indentFactor = 0;

	/** source object adapters */
	protected Map<Class, SourceAdapter> sourceAdapters = new HashMap<Class, SourceAdapter>();

	/** convert Date or Calendar object to milliseconds */
	protected boolean dateToMillis = false;
	
	//-------------- internal --------------------- 
	protected CycleDetector cycleDetector = new CycleDetector();

	protected Appendable writer;

	/**
	 * 
	 */
	public AbstractSerializer() {
		getExcludePropertyNames().addAll(Arrays.asList(Beans.RESERVED_PROPERTY_NAMES));
	}

	/**
	 * @return the cycleDetectStrategy
	 */
	public int getCycleDetectStrategy() {
		return cycleDetectStrategy;
	}

	/**
	 * @param cycleDetectStrategy the cycleDetectStrategy to set
	 */
	public void setCycleDetectStrategy(int cycleDetectStrategy) {
		this.cycleDetectStrategy = cycleDetectStrategy;
	}

	/**
	 * @return the indentChar
	 */
	public char getIndentChar() {
		return indentChar;
	}

	/**
	 * @param indentChar the indentChar to set
	 */
	public void setIndentChar(char indentChar) {
		this.indentChar = indentChar;
	}

	/**
	 * @return the indentFactor
	 */
	public int getIndentFactor() {
		return indentFactor;
	}

	/**
	 * @param indentFactor the indentFactor to set
	 */
	public void setIndentFactor(int indentFactor) {
		this.indentFactor = indentFactor;
	}

	/**
	 * @return the prettyPrint
	 */
	public boolean isPrettyPrint() {
		return indentFactor > 0;
	}
	
	/**
	 * @param prettyPrint the prettyPrint to set
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		indentFactor = prettyPrint ? PRETTY_INDENT_FACTOR : 0;
	}

	/**
	 * @return the dateToMillis
	 */
	public boolean isDateToMillis() {
		return dateToMillis;
	}

	/**
	 * @param dateToMillis the dateToMillis to set
	 */
	public void setDateToMillis(boolean dateToMillis) {
		this.dateToMillis = dateToMillis;
	}

	//----------------------------------------------------------
	public Map<Class, SourceAdapter> getSourceAdapters() {
		return sourceAdapters;
	}

	@SuppressWarnings("unchecked")
	public <T> SourceAdapter<T> getSourceAdapter(Class<T> type) {
		if (dateToMillis) {
			if (Date.class.isAssignableFrom(type)) {
				return (SourceAdapter<T>)DateAdapter.toMillis;
			}
			if (Calendar.class.isAssignableFrom(type)) {
				return (SourceAdapter<T>)CalendarAdapter.toMillis;
			}
		}
		
		if (Collections.isEmpty(sourceAdapters)) {
			return null;
		}
		
		SourceAdapter sa = sourceAdapters.get(type);
		if (sa != null) {
			return sa;
		}
		
		for (Entry<Class, SourceAdapter> en : sourceAdapters.entrySet()) {
			if (en.getKey().isAssignableFrom(type)) {
				return en.getValue();
			}
		}
		return null;
	}
	
	public <T> void registerSourceAdapter(Class<T> type, SourceAdapter<T> sourceAdapter) {
		sourceAdapters.put(type, sourceAdapter);
	}
	
	public void removeSourceAdapter(Class type) {
		sourceAdapters.remove(type);
	}
	
	public void clearSourceAdapters() {
		sourceAdapters.clear();
	}

	//----------------------------------------------------------
	public String serialize(Object src) {
		StringBuilder sb = new StringBuilder();
		serialize(src, sb);
		return sb.toString();
	}

	/**
	 * This method serializes the specified object into its equivalent representation. This
	 * method should be used when the specified object is not a generic type. This method uses
	 * {@link Class#getClass()} to get the type for the specified object, but the {@code getClass()}
	 * loses the generic type information because of the Type Erasure feature of Java. Note that
	 * this method works fine if the any of the object fields are of generic type, just the object
	 * itself should not be of a generic type. 
	 * 
	 * @param src the object for which representation is to be created
	 * @param writer Writer to which the representation needs to be written
	 */
	public void serialize(Object src, Appendable writer) {
		this.writer = writer;
		
		startDocument(src);

		if (src == null) {
			if (!isIgnoreNullProperty()) {
				writeNull();
			}
		}
		else {
			serializeSource(Strings.EMPTY, src);
		}
		
		endDocument(src);
	}

	@SuppressWarnings("unchecked")
	protected void serializeSource(String name, Object src) {
		if (src == null) {
			writeNull();
			return;
		}

		SourceAdapter sourceAdaptor = getSourceAdapter(src.getClass());
		if (sourceAdaptor != null) {
			src = sourceAdaptor.apply(src);
			serializeSource(name, src);
			return;
		}

		cycleDetector.push(name, src);
		try {
			Class<?> type = src.getClass();
			if (type == byte[].class) {
				serializeByteArray(name, (byte[])src);
			}
			else if (type == char[].class) {
				serializeCharArray(name, (char[])src);
			}
			else if (type.isArray()) {
				serializeArray(name, type, src);
			}
			else if (src instanceof Map) {
				serializeMap(name, type, (Map)src);
			}
			else if (src instanceof Iterable) {
				serializeIterable(name, type, (Iterable)src);
			}
			else if (src instanceof Enumeration) {
				serializeEnumeration(name, type, (Enumeration)src);
			}
			else if (src instanceof Date) {
				writeDate((Date)src);
			}
			else if (src instanceof Calendar) {
				writeCalendar((Calendar)src);
			}
			else if (src instanceof Boolean) {
				writeBoolean((Boolean)src);
			}
			else if (src instanceof Number) {
				writeNumber((Number)src);
			}
			else if (src instanceof CharSequence) {
				writeString(src.toString());
			}
			else if (isImmutableType(type)) {
				writeImmutable(src);
			}
			else {
				serializeBean(name, type, src);
			}
		}
		finally {
			cycleDetector.popup();
		}
	}

	private void serializeArrayElement(String name, Object src, Object val, int idx) {
		startArrayElement(name, val, idx);

		if (cycleDetector.isCycled(val)) {
			switch (cycleDetectStrategy) {
			// always has array element
			case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
			case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
				val = null;
				break;
			default:
				throw new BindException("Cycle object detected: " + Objects.identityToString(val));
			}
		}
		if (val != null && isExcludeProperty(val.getClass())) {
			val = null;
		}

		serializeSource(String.valueOf(idx), val);
		endArrayElement(name, val, idx);
	}

	private boolean serializeObjectProperty(Object src, String key, Object val, int idx, PropertyFilter pf) {
		if (val != null || !isIgnoreNullProperty()) {
			if (pf == null || pf.accept(src, key, val)) {
				if (cycleDetector.isCycled(val)) {
					switch (cycleDetectStrategy) {
					case CycleDetectStrategy.CYCLE_DETECT_NOPROP:
						return false;
					case CycleDetectStrategy.CYCLE_DETECT_LENIENT:
						val = null;
						break;
					default:
						throw new BindException("Cycle object detected: " + Objects.identityToString(val));
					}
				}

				if (val != null && isExcludeProperty(val.getClass())) {
					return false;
				}

				startObjectProperty(key, val, idx);
				serializeSource(key, val);
				endObjectProperty(key, val, idx);
				return true;
			}
		}
		return false;
	}

	protected void writeIndent(int indent) throws IOException {
		if (indentFactor > 0) {
			writer.append(Chars.LF);
			for (int i = 0; i < indent; i++) {
				writer.append(indentChar);
			}
		}
	}
	
	//-------------------------------------------------------------
	// serialize methods
	//-------------------------------------------------------------
	protected void serializeByteArray(String name, byte[] src) {
		String b64 = Base64.encodeBase64String(src);
		writeString(b64);
//		startArray(name, src);
//		int len = src.length;
//		for (int i = 0; i < len; i++) {
//			serializeArrayElement(name, src, src[i], i);
//		}
//		endArray(name, src, len);
	}
	
	protected void serializeCharArray(String name, char[] src) {
		writeString(new String(src));
	}
	
	protected void serializeArray(String name, Class<?> type, Object src) {
		startArray(name, src);
		int len = Array.getLength(src);
		for (int i = 0; i < len; i++) {
			serializeArrayElement(name, src, Array.get(src, i), i);
		}
		endArray(name, src, len);
	}

	protected void serializeMap(String name, Class<?> type, Map src) {
		PropertyFilter pf = getPropertyFilter(type);

		int len = 0;
		startObject(name, src);
		for (Object o : src.entrySet()) {
			Entry en = (Entry)o;
			String key = en.getKey().toString();
			Object val = en.getValue();
			if (serializeObjectProperty(src, key, val, len, pf)) {
				len++;
			}
		}
		endObject(name, src, len);
	}
	
	protected void serializeIterable(String name, Class<?> type, Iterable src) {
		startArray(name, src);
		Iterator it = src.iterator();
		int i = 0;
		while (it.hasNext()) {
			serializeArrayElement(name, src, it.next(), i++);
		}
		endArray(name, src, i);
	}

	protected void serializeEnumeration(String name, Class<?> type, Enumeration src) {
		startArray(name, src);
		int i = 0;
		while (src.hasMoreElements()) {
			serializeArrayElement(name, src, src.nextElement(), i++);
		}
		endArray(name, src, i);
	}
	
	@SuppressWarnings("unchecked")
	protected void serializeBean(String name, Class<?> type, Object src) {
		BeanHandler bh = getBeanHandler(type);
		PropertyFilter pf = getPropertyFilter(type);

		int len = 0;
		startObject(name, src);
		String[] pns = bh.getReadPropertyNames(src);
		for (String key : pns) {
			if (isExcludeProperty(key)) {
				continue;
			}
			
			Object val = bh.getPropertyValue(src, key);
			if (serializeObjectProperty(src, key, val, len, pf)) {
				len++;
			}
		}
		endObject(name, src, len);
	}

	protected void writeCalendar(Calendar src) {
		writeImmutable(src);
	}

	protected void writeDate(Date src) {
		writeImmutable(src);
	}
	
	protected void writeImmutable(Object src) {
		String s = convertValue(src, String.class);
		writeString(s);
	}
	
	//-------------------------------------------------------------
	// abstract methods
	//-------------------------------------------------------------
	protected abstract void startDocument(Object src);
	
	protected abstract void endDocument(Object src);

	protected abstract void startArray(String name, Object src);

	protected abstract void endArray(String name, Object src, int len);

	protected abstract void startArrayElement(String name, Object src, int index);
	
	protected abstract void endArrayElement(String name, Object src, int index);

	protected abstract void startObject(String name, Object src);
	
	protected abstract void endObject(String name, Object src, int len);

	protected abstract void startObjectProperty(String key, Object val, int index);
	
	protected abstract void endObjectProperty(String key, Object val, int index);
	
	protected abstract void writeNull();

	protected abstract void writeString(String str);

	protected abstract void writeNumber(Number num);
	
	protected abstract void writeBoolean(Boolean boo);
}
