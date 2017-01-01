package panda.net.http;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Cookie {
	// RFC2109 attributes
	public static final String VERSION = "version";
	public static final String PATH = "path";
	public static final String DOMAIN = "domain";
	public static final String MAX_AGE = "max-age";
	public static final String SECURE = "secure";
	public static final String COMMENT = "comment";
	public static final String EXPIRES = "expires";

	// RFC2965 attributes
	public static final String PORT = "port";
	public static final String COMMENTURL = "commenturl";
	public static final String DISCARD = "discard";

	private String name;
	private String value;
	private Map<String, String> attrs;

	public Cookie(String name, String value) {
		this.name = value;
		this.value = value;
	}

	/**
	 * Constructor
	 * @param s cookie string
	 */
	public Cookie(String s) {
		parse(s);
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return get(VERSION);
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		setAttr(VERSION, version);
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return get(PATH);
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		setAttr(PATH, path);
	}

	/**
	 * @return the domain
	 */
	public String getDomain() {
		return get(DOMAIN);
	}

	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		setAttr(DOMAIN, domain);
	}

	/**
	 * @return the expires
	 */
	public String getExpires() {
		return get(EXPIRES);
	}

	/**
	 * @param expires the expires to set
	 */
	public void setExpires(String expires) {
		setAttr(EXPIRES, expires);
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return get(COMMENT);
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		setAttr(COMMENT, comment);
	}

	/**
	 * @return the secure
	 */
	public String getSecure() {
		return get(SECURE);
	}

	/**
	 * @param secure the secure to set
	 */
	public void setSecure(String secure) {
		setAttr(SECURE, secure);
	}

	/**
	 * @return the maxAge
	 */
	public String getMaxAge() {
		return get(MAX_AGE);
	}

	/**
	 * @param maxage the maxage to set
	 */
	public void setMaxAge(String maxage) {
		setAttr(MAX_AGE, maxage);
	}

	/**
	 * @return the port
	 */
	public String getPort() {
		return get(PORT);
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(String port) {
		setAttr(PORT, port);
	}

	/**
	 * @return the commenturl
	 */
	public String getCommenturl() {
		return get(COMMENTURL);
	}

	/**
	 * @param commenturl the commenturl to set
	 */
	public void setCommenturl(String commenturl) {
		setAttr(COMMENTURL, commenturl);
	}

	/**
	 * @return the discard
	 */
	public String getDiscard() {
		return get(DISCARD);
	}

	/**
	 * @param discard the discard to set
	 */
	public void setDiscard(String discard) {
		setAttr(DISCARD, discard);
	}

	public String get(String name) {
		return attrs.get(name);
	}

	public Cookie removeAttr(String name) {
		attrs.remove(name);
		return this;
	}

	public Cookie setAttr(String name, String value) {
		attrs.put(name, value);
		return this;
	}

	private Map<String, String> attrs() {
		if (attrs == null) {
			attrs = new LinkedHashMap<String, String>();
		}
		return attrs;
	}

	public void parse(String str) {
		String[] ss = Strings.split(str, ";");
		for (int i = 0; i < ss.length; i++) {
			String s = Strings.trim(ss[i]);
			int sep = s.indexOf('=');
			String k = s;
			String v = "";
			if (sep > 0) {
				k = s.substring(0, sep);
				v = s.substring(sep + 1);
			}
			if (i == 0) {
				name = k;
				value = v;
			}
			else {
				attrs().put(k, v);
			}
		}
	}

	public boolean isValid() {
		return Strings.isNotEmpty(name) && Strings.isNotEmpty(value);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(name).append('=').append(value);
		for (Entry<String, String> en : attrs.entrySet()) {
			sb.append("; ").append(en.getKey()).append('=').append(en.getValue());
		}
		return sb.toString();
	}

}
