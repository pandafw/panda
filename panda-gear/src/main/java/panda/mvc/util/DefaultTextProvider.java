package panda.mvc.util;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Set;
import java.util.TreeSet;

import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.io.resource.Resource;
import panda.io.resource.ResourceLoader;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.ClassLoaders;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;

@IocBean(type=TextProvider.class, scope=Scope.REQUEST)
public class DefaultTextProvider implements TextProvider {
	private final static Log log = Logs.getLog(DefaultTextProvider.class);

	private List<String> defaultResources;
	
	@IocInject(required=false)
	private Beans beans;

	@IocInject
	private ActionContext context;

	@IocInject
	private ResourceLoader resourceLoader;

	private ClassLoader resourceClassLoader;

	public DefaultTextProvider() {
		beans = Beans.i();
		resourceClassLoader = ClassLoaders.getClassLoader();
	}

	/**
	 * Add's the bundle to the internal list of default bundles.
	 * <p/>
	 * If the bundle already exists in the list it will be read.
	 * 
	 * @param resourceBundles the names of the bundle to set.
	 */
	@IocInject(value=MvcConstants.DEFAULT_RESOURCE_BUNDLES, required=false)
	public void setDefaultResourceBundle(List<String> resourceBundles) {
		defaultResources = resourceBundles;

		if (log.isDebugEnabled()) {
			log.debug("set default resource bundle [" + Strings.join(defaultResources, ", ") + "]");
		}
	}

	/**
	 * Checks if a key is available in the resource bundles associated with this action. The
	 * resource bundles are searched, starting with the one associated with this particular action,
	 * and testing all its superclasses' bundles. It will stop once a bundle is found that contains
	 * the given text. This gives a cascading style that allow global texts to be defined for an
	 * application base class.
	 */
	public boolean hasKey(String key) {
		String message = findText(key, null, new Object[0]);
		return message != null;
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * 
	 * @param key name of text to be found
	 * @return value of named text or the provided key if no value is found
	 */
	public String getText(String key) {
		return getText(key, key, Objects.NULL);
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param def the default value which will be returned if no text is found
	 * @return value of named text or the provided def if no value is found
	 */
	public String getText(String key, String def) {
		return getText(key, def, Objects.NULL);
	}

	/**
	 * Gets a message based on a name using the supplied arg, or, if the message is not found, a
	 * supplied default value is returned.
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def the default value which will be returned if no message is found
	 * @param arg object to be used in a EL expression such as "${top}"
	 * @return the message as found in the resource bundle, or def if none is found
	 */
	public String getText(String key, String def, Object arg) {
		return findText(key, def, arg);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String key) {
		return getTextAsBoolean(key, null);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String key, Boolean def) {
		String s = getText(key, null);
		return s == null ? def : Boolean.valueOf(s);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return integer value
	 */
	public Integer getTextAsInt(String key) {
		return getTextAsInt(key, null);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return integer value
	 */
	public Integer getTextAsInt(String key, Integer def) {
		String s = getText(key, null);
		return s == null ? def : Integer.valueOf(s);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return long value
	 */
	public Long getTextAsLong(String key) {
		return getTextAsLong(key, null);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return long value
	 */
	public Long getTextAsLong(String key, Long def) {
		String s = getText(key, null);
		return s == null ? def : Long.valueOf(s);
	}

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return List value
	 */
	public List getTextAsList(String key) {
		return getTextAsList(key, null);
	}

	/**
	 * getTextAsList
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return list value
	 */
	public List getTextAsList(String key, List def) {
		String expr = getText(key, null);
		return parseTextAsList(expr, def);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @return map value
	 */
	public Map getTextAsMap(String key) {
		return getTextAsMap(key, null);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param key the resource bundle key that is to be searched for
	 * @param def default value
	 * @return map value
	 */
	public Map getTextAsMap(String key, Map def) {
		String expr = getText(key, null);
		return parseTextAsMap(expr, def);
	}

	// ----------------------------------------------------------
	protected List parseTextAsList(String text, List def) {
		List list = def;

		if (Strings.isNotEmpty(text)) {
			text = Strings.strip(text);
			if (!text.startsWith("[")) {
				text = "[" + text + "]";
			}

			list = JsonArray.fromJson(text);
			if (list == null) {
				throw new RuntimeException("Incorrect JSON list expression: " + text);
			}
		}

		return list;
	}

	protected Map parseTextAsMap(String text, Map def) {
		Map map = def;

		if (Strings.isNotEmpty(text)) {
			text = Strings.strip(text);
			if (!text.startsWith("{")) {
				text = "{" + text + "}";
			}

			map = JsonObject.fromJson(text);
			if (map == null) {
				throw new RuntimeException("Incorrect JSON map expression: " + text);
			}
		}

		return map;
	}

	// ----------------------------------------------------------
	private String findText(String key, String def, Object arg) {
		return findText(context.getLocale(), context.getAction().getClass(), context, key, def, arg);
	}

	private Object findValue(Object parent, String property) {
		return beans.getBeanValue(parent, property);
	}

	private String evalMessage(String text, Object arg) {
		return Mvcs.translate(context, text, arg);
	}

	/**
	 * Returns a localized message for the specified key, aTextName. Neither the key nor the message
	 * is evaluated.
	 * 
	 * @param key the message key
	 * @param locale the locale the message should be for
	 * @return a localized message based on the specified key, or null if no localized message can
	 *         be found for it
	 */
	private String findDefaultText(Locale locale, String key) {
		if (Collections.isNotEmpty(defaultResources)) {
			for (String src : defaultResources) {
				Resource res = findResourceBundle(src, locale);
				if (res != null) {
					String txt = res.getString(key);
					if (txt != null) {
						return txt;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Gets the default message.
	 */
	private String getDefaultMessage(Locale locale, String key, String def, Object arg) {
		String txt = findDefaultText(locale, key);
		if (txt == null) {
			return null;
		}

		String msg = evalMessage(txt, arg);
		return msg;
	}

	private String findText(Locale locale, Class clazz, Object base, String key, String def, Object arg) {
		if (Strings.isEmpty(key)) {
			if (log.isWarnEnabled()) {
				log.warn("Trying to find text with empty key!");
			}
			return def;
		}

		// search up class hierarchy
		String msg = findMessage(locale, clazz, key, arg, null);
		if (msg != null) {
			return msg;
		}

		// see if it's a child property
		int idx = key.indexOf(".");
		if (idx != -1) {
			String kn = null;
			String pv = null;

			pv = key.substring(0, idx);
			kn = key.substring(idx + 1);

			if (pv != null) {
				Object obj = findValue(base, pv);
				try {
					Class cls = null;
					if (obj != null) {
						cls = obj instanceof Class ? (Class)obj : obj.getClass();
					}

					if (cls != null) {
						msg = findText(locale, cls, obj, kn, null, arg);
						if (msg != null) {
							return msg;
						}
					}
				}
				catch (Exception e) {
					log.debug("unable to find property " + pv, e);
				}
			}
		}

		// get default
		msg = getDefaultMessage(locale, key, def, arg);
		if (msg == null) {
			if (log.isDebugEnabled()) {
				log.debug("Unable to find text for key '" + key + "' in class '" + clazz.getName() + "' and locale '" + locale + "'");
			}
			return def;
		}
		
		return msg;
	}

	/**
	 * Traverse up class hierarchy looking for message. Looks at class, then implemented interface,
	 * before going up hierarchy.
	 */
	private String findMessage(Locale locale, Class clazz, String key, Object arg, Set<String> checked) {
		if (checked == null) {
			checked = new TreeSet<String>();
		}
		else if (checked.contains(clazz.getName())) {
			return null;
		}

		// add to checked
		checked.add(clazz.getName());

		// look in properties of this class
		String msg = getMessage(locale, clazz.getName(), key, arg);
		if (msg != null) {
			return msg;
		}

		// look in properties of implemented interfaces
		Class[] ifs = clazz.getInterfaces();
		for (Class i : ifs) {
			msg = getMessage(locale, i.getName(), key, arg);
			if (msg != null) {
				return msg;
			}
		}

		// look in properties of this class's package
		String pn = clazz.getPackage().getName() + ".package";
		if (!checked.contains(pn)) {
			checked.add(pn);
			
			msg = getMessage(locale, pn, key, arg);
			if (msg != null) {
				return msg;
			}
		}

		// traverse up hierarchy
		if (clazz.isInterface()) {
			for (Class i : ifs) {
				msg = findMessage(locale, i, key, arg, checked);
				if (msg != null) {
					return msg;
				}
			}
		}
		else {
			if (!clazz.equals(Object.class) && !clazz.isPrimitive()) {
				return findMessage(locale, clazz.getSuperclass(), key, arg, checked);
			}
		}

		return null;
	}

	/**
	 * Gets the message from the named resource bundle.
	 */
	private String getMessage(Locale locale, String src, String key, Object arg) {
		Resource bundle = findResourceBundle(src, locale);
		if (bundle == null) {
			return null;
		}

//		reloadBundles();
		try {
			String txt = bundle.getString(key);
			if (txt == null) {
				return null;
			}
			String msg = evalMessage(txt, arg);
			return msg;
		}
		catch (MissingResourceException e) {
			return null;
		}
	}

	/**
	 * Finds the given resorce bundle by it's name.
	 * <p/>
	 * Will use <code>Thread.currentThread().getContextClassLoader()</code> as the classloader. If
	 * {@link #resourceClassLoader} is defined and the bundle cannot be found the current
	 * classloader it will delegate to that.
	 * 
	 * @param src the name of the bundle (usually it's FQN classname).
	 * @param locale the locale.
	 * @return the bundle, <tt>null</tt> if not found.
	 */
	private Resource findResourceBundle(String src, Locale locale) {
		return resourceLoader.getResource(src, locale, resourceClassLoader);
	}

	public void clearResourceBundlesCache() {
		resourceLoader.clear();
	}

}
