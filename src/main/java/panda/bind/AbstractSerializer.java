package panda.bind;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.lang.Arrays;
import panda.lang.Chars;
import panda.lang.Classes;
import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public abstract class AbstractSerializer extends AbstractBinder implements Serializer {
	public static final int PRETTY_INDENT_FACTOR = 1;
	
	public static final int DEFAULT_PRETTY_INDENT = 1;

	//-------------- settings --------------------- 
	private char indentChar = Chars.TAB;
	private int cycleDetectStrategy = CycleDetectStrategy.CYCLE_DETECT_NOPROP;

	/** The number of spaces to add to each level of indentation. */
	protected int indentFactor = 0;

	private Map<Type, SourceAdapter> sourceAdapters = new HashMap<Type, SourceAdapter>();
	
	//-------------- internal --------------------- 
	private CycleDetector cycleDetector = new CycleDetector();

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

	public Map<Type, SourceAdapter> getSourceAdapters() {
		return sourceAdapters;
	}
	@SuppressWarnings("unchecked")
	public <T> SourceAdapter<T> getSourceAdapter(Type type) {
		return sourceAdapters.get(type);
	}
	public void registerSourceAdapter(Type type, SourceAdapter sourceAdapter) {
		sourceAdapters.put(type, sourceAdapter);
	}
	public void removeSourceAdapter(Type type) {
		sourceAdapters.remove(type);
	}
	public void clearSourceAdapters() {
		sourceAdapters.clear();
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
		
		this.startDocument(src);

		if (src == null) {
			if (!isIgnoreNullProperty()) {
				this.writeNull();
			}
		}
		else {
			serializeSource(Strings.EMPTY, src);
		}
		
		this.endDocument(src);
	}

	@SuppressWarnings("unchecked")
	protected void serializeSource(String name, Object src) {
		if (src == null) {
			this.writeNull();
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
			Class type = src.getClass();
			if (type.isArray()) {
				this.startArray(src);
				int len = Array.getLength(src);
				for (int i = 0; i < len; i++) {
					serializeArrayElement(src, Array.get(src, i), i);
				}
				this.endArray(src, len);
			}
			else if (src instanceof Map) {
				Map m = (Map)src;
				PropertyFilter pf = getPropertyFilter(type);

				int len = 0;
				this.startObject(m);
				for (Object o : m.entrySet()) {
					Entry en = (Entry)o;
					String key = en.getKey().toString();
					Object val = en.getValue();
					if (serializeObjectProperty(src, key, val, len, pf)) {
						len++;
					}
				}
				this.endObject(src, len);
			}
			else if (src instanceof Iterable) {
				this.startArray(src);
				Iterator it = ((Iterable)src).iterator();
				int i = 0;
				while (it.hasNext()) {
					serializeArrayElement(src, it.next(), i++);
				}
				this.endArray(src, i);
			}
			else if (src instanceof Enumeration) {
				this.startArray(src);
				Enumeration en = (Enumeration)src;
				int i = 0;
				while (en.hasMoreElements()) {
					serializeArrayElement(src, en.nextElement(), i++);
				}
				this.endArray(src, i);
			}
			else if (src instanceof Boolean) {
				this.writeBoolean((Boolean)src);
			}
			else if (src instanceof Number) {
				this.writeNumber((Number)src);
			}
			else if (src instanceof CharSequence) {
				this.writeString(src.toString());
			}
			else if (isImmutableType(type)) {
				String s = convertValue(src, String.class);
				this.writeString(s);
			}
			else {
				BeanHandler bh = getBeanHandler(type);
				PropertyFilter pf = getPropertyFilter(type);

				int len = 0;
				this.startObject(src);
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
				this.endObject(src, len);
			}
		}
		finally {
			cycleDetector.popup();
		}
	}

	private void serializeArrayElement(Object src, Object val, int idx) {
		this.startArrayElement(val, idx);

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
		this.endArrayElement(val, idx);
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

				this.startObjectProperty(key, val, idx);
				serializeSource(key, val);
				this.endObjectProperty(key, val, idx);
				return true;
			}
		}
		return false;
	}

	protected boolean isImmutableType(Class type) {
		return Classes.isImmutable(type);
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
	// abstract methods
	//-------------------------------------------------------------
	protected abstract void startDocument(Object src);
	
	protected abstract void endDocument(Object src);

	protected abstract void startArray(Object src);

	protected abstract void endArray(Object src, int len);

	protected abstract void startArrayElement(Object src, int index);
	
	protected abstract void endArrayElement(Object src, int index);

	protected abstract void startObject(Object src);
	
	protected abstract void endObject(Object src, int len);

	protected abstract void startObjectProperty(String key, Object val, int index);
	
	protected abstract void endObjectProperty(String key, Object val, int index);
	
	protected abstract void writeNull();
	
	protected abstract void writeString(String str);

	protected abstract void writeNumber(Number num);
	
	protected abstract void writeBoolean(Boolean boo);
}
