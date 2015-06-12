package panda.cast;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import panda.lang.Charsets;
import panda.lang.Collections;
import panda.lang.CycleDetectStrategy;
import panda.lang.CycleDetector;
import panda.lang.Strings;

public class CastContext extends CycleDetector implements CycleDetectStrategy {
	private String encoding = Charsets.UTF_8;
	private String format;
	private Locale locale;
	
	private String prefix;
	private boolean skipCastError = false;
	private int cycleDetectStrategy = CYCLE_DETECT_NOPROP;

	private Castors castors;
	private Map<String, Object> context;
	private Map<String, Object> errors;
	
	/**
	 * @param castors
	 */
	public CastContext(Castors castors) {
		this.castors = castors;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	/**
	 * @return the format
	 */
	public String getFormat() {
		return format;
	}

	/**
	 * @param format the format to set
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * @return the prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * @param prefix the prefix to set
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * @return the skipCastError
	 */
	public boolean isSkipCastError() {
		return skipCastError;
	}

	/**
	 * @param skipCastError the skipCastError to set
	 */
	public void setSkipCastError(boolean skipCastError) {
		this.skipCastError = skipCastError;
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
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @return the errors
	 */
	public Map<String, Object> getErrors() {
		return errors;
	}

	/**
	 * @param errors the errors to set
	 */
	public void setErrors(Map<String, Object> errors) {
		this.errors = errors;
	}
	
	/**
	 * add error value
	 */
	public void addError(String name, Object value) {
		if (errors == null) {
			errors = new HashMap<String, Object>();
		}
		
		String key = Strings.isEmpty(prefix) ? name : prefix + name;
		errors.put(key, value);
	}

	/**
	 * @return has error
	 */
	public boolean hasError() {
		return Collections.isNotEmpty(errors);
	}
	
	//----------------------------------------------------
	public Object get(String key) {
		if (context == null) {
			return null;
		}
		return context.get(key);
	}
	
	public Object set(String key, Object obj) {
		if (context == null) {
			context = new HashMap<String, Object>();
		}
		return context.put(key, obj);
	}

	//------------------------------------------------------
	public String toName() {
		return Strings.join(names, '.');
	}

	public String toName(String name) {
		return toName() + '.' + name;
	}
	
}
