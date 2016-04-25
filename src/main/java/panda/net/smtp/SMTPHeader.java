package panda.net.smtp;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import panda.io.Streams;
import panda.lang.Arrays;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.Numbers;
import panda.lang.Randoms;
import panda.lang.time.DateTimes;
import panda.net.InetDates;

/**
 * https://tools.ietf.org/html/rfc4021
 */
public class SMTPHeader implements Map<String, Object>, Cloneable, Serializable {
	private static final long serialVersionUID = 3L;
	
	public static final String CONTENT_DISPOSITION       = "Content-Disposition";
	public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public static final String CONTENT_TYPE              = "Content-Type";
	public static final String DATE                      = "Date";
	public static final String MIME_VERSION              = "MIME-Version";
	public static final String MIME_VERSION_10           = "1.0";
	public static final String MESSAGE_ID                = "Message-ID";

	public static final String FROM                      = "From";
	public static final String TO                        = "To";
	public static final String CC                        = "Cc";
	public static final String SUBJECT                   = "Subject";

	// -------------------------------------------------------------
	public static SMTPHeader create() {
		SMTPHeader header = new SMTPHeader();
		return header;
	}

	// -------------------------------------------------------------
	private Map<String, Object> map = new TreeMap<String, Object>();

	public SMTPHeader() {
		setDate(DATE, DateTimes.getDate());
		set(MESSAGE_ID, Randoms.randUUID());
	}

	public SMTPHeader(String from, String to, String subject) {
		this();
		set(FROM, from);
		set(TO, to);
		set(SUBJECT, subject);
	}

	//-------------------------------------------------
	public int getInt(String key) {
		String str = getString(key);
		return Numbers.toInt(str, -1);
	}
	
	public Date getDate(String key) {
		String str = getString(key);
		return InetDates.safeParse(str);
	}
	
	public String getString(String key) {
		return getString(key, null);
	}
	
	public String getString(String key, String defaultValue) {
		Object value = get(key);
		if (value == null)
			return defaultValue;
		
		if (value instanceof String) {
			return (String)value;
		}
		if (value instanceof List) {
			List vs = (List)value;
			return vs.size() > 0 ? vs.get(0).toString() : defaultValue;
		}

		return defaultValue;
	}

	@SuppressWarnings("unchecked")
	public List<String> getStrings(String key) {
		Object value = get(key);
		if (value == null) {
			return null;
		}
		
		if (value instanceof List) {
			return (List<String>)value;
		}
		return Arrays.toList(value.toString());
	}

	//-------------------------------------------------
	public SMTPHeader setInt(String key, int value) {
		return set(key, value);
	}

	public SMTPHeader setDate(String key, long value) {
		return set(key, new Date(value));
	}

	public SMTPHeader setDate(String key, Date value) {
		return set(key, value);
	}
	
	public SMTPHeader setString(String key, String value) {
		return set(key, value);
	}
	
	public SMTPHeader set(String key, Object value) {
		put(key, value);
		return this;
	}

	public SMTPHeader setAll(Map<? extends String, ? extends Object> m) {
		putAll(m);
		return this;
	}

	//-------------------------------------------------
	public SMTPHeader addInt(String key, int value) {
		return add(key, value);
	}

	public SMTPHeader addDate(String key, long value) {
		return add(key, new Date(value));
	}

	public SMTPHeader addDate(String key, Date value) {
		return add(key, value);
	}
	
	public SMTPHeader addString(String key, String value) {
		add(key, (Object)value);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public SMTPHeader add(String key, Object value) {
		if (key == null || value == null) {
			return this;
		}

		value = convertValue(value);

		Object object = map.get(key);
		if (object == null) {
			map.put(key, value);
			return this;
		}
		
		List vs = null;
		if (object instanceof List) {
			vs = (List)object;
		}
		else {
			vs = new ArrayList<String>();
			vs.add(object);
			map.put(key, vs);
		}
		
		if (value instanceof List) {
			vs.addAll((List)value);
		}
		else {
			vs.add(value);
		}

		return this;
	}
	
	public SMTPHeader addAll(Map<? extends String, ? extends Object> m) {
		for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
			add(en.getKey(), en.getValue());
		}
		return this;
	}

	//-------------------------------------------------
	@SuppressWarnings("unchecked")
	private Object convertValue(Object value) {
		if (value == null) {
			return null;
		}
		
		if (value instanceof Date) {
			return InetDates.format((Date)value);
		}
		if (value instanceof Calendar) {
			return InetDates.format(((Calendar)value).getTime());
		}

		if (Iterators.isIterable(value)) {
			List vs = new ArrayList();
			for (Object v : Iterators.asIterable(value)) {
				v = convertValue(v);
				if (v instanceof List) {
					for (Object c : (List)v) {
						vs.add(c);
					}
				}
				else {
					vs.add(v);
				}
			}
			return vs;
		}

		return value.toString();
	}

	public void clear() {
		map.clear();
	}

	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public Object get(Object key) {
		return map.get(key);
	}

	public boolean isEmpty() {
		return map.isEmpty();
	}

	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Object put(String key, Object value) {
		if (key == null) {
			return null;
		}
		
		if (value == null) {
			return map.remove(key);
		}

		value = convertValue(value);
		return map.put(key, value);
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		for (Entry<? extends String, ? extends Object> en : m.entrySet()) {
			put(en.getKey(), en.getValue());
		}
	}

	public Object remove(Object key) {
		return map.remove(key);
	}

	public int size() {
		return map.size();
	}

	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public boolean equals(Object o) {
		return map.equals(o);
	}

	@Override
	public int hashCode() {
		return map.hashCode();
	}

	@Override
	public Object clone() {
		SMTPHeader hh = new SMTPHeader();
		hh.map.putAll(map);
		return hh;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		try {
			toString(sb);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return sb.toString();
	}

	public void toString(Appendable writer) throws IOException {
		for (Map.Entry<String, Object> en : entrySet()) {
			String key = en.getKey();
			writer.append(key).append(':').append(' ');

			Object val = en.getValue();
			if (val != null) {
				Iterator it = Iterators.asIterator(en.getValue());
				while (it.hasNext()) {
					writer.append(format(it.next()));
					if (it.hasNext()) {
						writer.append(',');
					}
				}
			}
			writer.append(Streams.LINE_SEPARATOR_UNIX);
		}
	}
	
	private String format(Object value) {
		if (value instanceof Calendar) {
			return InetDates.format(((Calendar)value).getTime());
		}
		if (value instanceof Date) {
			return InetDates.format((Date)value);
		}
		return value.toString();
	}
}
