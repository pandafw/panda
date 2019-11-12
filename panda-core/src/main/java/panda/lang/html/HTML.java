package panda.lang.html;

import java.util.HashSet;
import java.util.Set;

import panda.lang.Arrays;
import panda.net.Scheme;

/**
 * HTML utility class.
 */
public abstract class HTML {
	/** Set of valid HTML tags. */
	private final static Set<String> HTML_ELEMENTS = toSet(HTMLElement.values());
	/** Set of unsafe HTML tags. */
	private final static Set<String> HTML_UNSAFE = toSet(HTMLElement.applet, HTMLElement.head, HTMLElement.html,
		HTMLElement.body, HTMLElement.frame, HTMLElement.frameset, HTMLElement.iframe, HTMLElement.script,
		HTMLElement.object);
	
	/** Set of HTML block level tags. */
	private final static Set<String> HTML_BLOCK_ELEMENTS = toSet(HTMLElement.address, HTMLElement.blockquote,
		HTMLElement.del, HTMLElement.div, HTMLElement.dl, HTMLElement.fieldset, HTMLElement.form, HTMLElement.h1,
		HTMLElement.h2, HTMLElement.h3, HTMLElement.h4, HTMLElement.h5, HTMLElement.h6, HTMLElement.hr,
		HTMLElement.ins, HTMLElement.noscript, HTMLElement.ol, HTMLElement.p, HTMLElement.pre, HTMLElement.table,
		HTMLElement.ul);
	
	/** Set of valid markdown link prefixes. */
	private final static Set<String> LINK_PREFIX = Arrays.toSet(Scheme.HTTP, Scheme.HTTPS, Scheme.FTP, Scheme.FTPS);

	private static Set<String> toSet(HTMLElement ... a) {
		Set<String> set = new HashSet<String>(a.length);
		for (HTMLElement e : a) {
			set.add(e.toString());
		}
		return set;
	}
	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is a link prefix.
	 */
	public final static boolean isLinkPrefix(final String value) {
		return LINK_PREFIX.contains(value);
	}

	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is an entity.
	 */
	public final static boolean isEntity(final String value) {
		return HTMLEntities.HTML4_UNESCAPE.containsKey(value);
	}

	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is a HTML tag.
	 */
	public final static boolean isElement(final String value) {
		return HTML_ELEMENTS.contains(value);
	}

	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is a HTML block level tag.
	 */
	public final static boolean isBlockElement(final String value) {
		return HTML_BLOCK_ELEMENTS.contains(value);
	}

	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is a HTML inline level tag.
	 */
	public final static boolean isInlineElement(final String value) {
		return !isBlockElement(value);
	}

	/**
	 * @param value String to check.
	 * @return Returns <code>true</code> if the given String is an unsafe HTML tag.
	 */
	public final static boolean isUnsafeElement(final String value) {
		return HTML_UNSAFE.contains(value);
	}
}
