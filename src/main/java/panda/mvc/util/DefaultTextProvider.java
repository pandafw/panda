package panda.mvc.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import panda.bean.Beans;
import panda.bind.json.JsonArray;
import panda.bind.json.JsonObject;
import panda.io.resource.ResourceBundleLoader;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.ClassLoaders;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.lang.collection.MultiKey;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;

@IocBean(type=TextProvider.class, scope=Scope.REQUEST)
public class DefaultTextProvider implements TextProvider {
	private final static Log log = Logs.getLog(DefaultTextProvider.class);

	private static class EmptyResourceBundle extends ResourceBundle {
		@Override
		public Enumeration<String> getKeys() {
			return null; // dummy
		}

		@Override
		protected Object handleGetObject(String key) {
			return null; // dummy
		}
	}

	private static final List<String> DEFAULT_RESOURCE_BUNDLES = new CopyOnWriteArrayList<String>();
	private static final ResourceBundle EMPTY_BUNDLE = new EmptyResourceBundle();
	protected static final ConcurrentMap<String, ResourceBundle> bundlesMap = new ConcurrentHashMap<String, ResourceBundle>();
	protected static final ConcurrentMap<MultiKey, MessageFormat> messageFormats = new ConcurrentHashMap<MultiKey, MessageFormat>();

	@IocInject(required=false)
	private Beans beans;

	@IocInject
	private ActionContext context;

	@IocInject
	private ResourceBundleLoader resourceBundleLoader;

	private ClassLoader resourceClassLoader;

	public DefaultTextProvider() {
		beans = Beans.i();
		resourceClassLoader = ClassLoaders.getClassLoader();
	}

	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the context
	 */
	public ActionContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(ActionContext context) {
		this.context = context;
	}

	/**
	 * @return the resource ClassLoader
	 */
	public ClassLoader getResourceClassLoader() {
		return resourceClassLoader;
	}

	/**
	 * @return the resourceBundleLoader
	 */
	public ResourceBundleLoader getResourceBundleLoader() {
		return resourceBundleLoader;
	}

	/**
	 * @param resourceBundleLoader the resourceBundleLoader to set
	 */
	public void setResourceBundleLoader(ResourceBundleLoader resourceBundleLoader) {
		this.resourceBundleLoader = resourceBundleLoader;
	}

	/**
	 * @param resourceClassLoader the resourceClassLoader to set
	 */
	public void setResourceClassLoader(ClassLoader resourceClassLoader) {
		this.resourceClassLoader = resourceClassLoader;
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
		return getText(key, key, Collections.emptyList());
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param defaultValue the default value which will be returned if no text is found
	 * @return value of named text or the provided defaultValue if no value is found
	 */
	public String getText(String key, String defaultValue) {
		return getText(key, defaultValue, Collections.emptyList());
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param defaultValue the default value which will be returned if no text is found
	 * @return value of named text or the provided defaultValue if no value is found
	 */
	public String getText(String key, String defaultValue, String arg) {
		List<Object> args = new ArrayList<Object>();
		args.add(arg);
		return getText(key, defaultValue, args);
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param args a List of args to be used in a MessageFormat message
	 * @return value of named text or the provided key if no value is found
	 */
	public String getText(String key, List<?> args) {
		return getText(key, key, args);
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param args an array of args to be used in a MessageFormat message
	 * @return value of named text or the provided key if no value is found
	 */
	public String getText(String key, String[] args) {
		return getText(key, key, args);
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param defaultValue the default value which will be returned if no text is found
	 * @param args a List of args to be used in a MessageFormat message
	 * @return value of named text or the provided defaultValue if no value is found
	 */
	public String getText(String key, String defaultValue, List<?> args) {
		Object[] argsArray = ((args != null && !args.equals(Collections.emptyList())) ? args.toArray() : null);
		return findText(key, defaultValue, argsArray);
	}

	/**
	 * Get a text from the resource bundles associated with this action. The resource bundles are
	 * searched, starting with the one associated with this particular action, and testing all its
	 * superclasses' bundles. It will stop once a bundle is found that contains the given text. This
	 * gives a cascading style that allow global texts to be defined for an application base class.
	 * If no text is found for this text name, the default value is returned.
	 * 
	 * @param key name of text to be found
	 * @param defaultValue the default value which will be returned if no text is found
	 * @param args an array of args to be used in a MessageFormat message
	 * @return value of named text or the provided defaultValue if no value is found
	 */
	public String getText(String key, String defaultValue, String[] args) {
		return findText(key, defaultValue, args);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param name resource name
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String name) {
		return getTextAsBoolean(name, null);
	}

	/**
	 * getTextAsBoolean
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return boolean value
	 */
	public Boolean getTextAsBoolean(String name, Boolean defaultValue) {
		String s = getText(name, (String)null);
		return s == null ? defaultValue : Boolean.valueOf(s);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param name resource name
	 * @return integer value
	 */
	public Integer getTextAsInt(String name) {
		return getTextAsInt(name, null);
	}

	/**
	 * getTextAsInt
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return integer value
	 */
	public Integer getTextAsInt(String name, Integer defaultValue) {
		String s = getText(name, (String)null);
		return s == null ? defaultValue : Integer.valueOf(s);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param name resource name
	 * @return long value
	 */
	public Long getTextAsLong(String name) {
		return getTextAsLong(name, null);
	}

	/**
	 * getTextAsLong
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return long value
	 */
	public Long getTextAsLong(String name, Long defaultValue) {
		String s = getText(name, (String)null);
		return s == null ? defaultValue : Long.valueOf(s);
	}

	/**
	 * getTextAsList
	 * 
	 * @param name resource name
	 * @return List value
	 */
	public List getTextAsList(String name) {
		return getTextAsList(name, null);
	}

	/**
	 * getTextAsList
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return list value
	 */
	public List getTextAsList(String name, List defaultValue) {
		String expr = getText(name, (String)null);
		return parseTextAsList(expr, defaultValue);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param name resource name
	 * @return map value
	 */
	public Map getTextAsMap(String name) {
		return getTextAsMap(name, null);
	}

	/**
	 * getTextAsMap
	 * 
	 * @param name resource name
	 * @param defaultValue default value
	 * @return map value
	 */
	public Map getTextAsMap(String name, Map defaultValue) {
		String expr = getText(name, (String)null);
		return parseTextAsMap(expr, defaultValue);
	}

	// ----------------------------------------------------------
	protected List parseTextAsList(String text, List defaultValue) {
		List list = defaultValue;

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

	protected Map parseTextAsMap(String text, Map defaultValue) {
		Map map = defaultValue;

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
	protected String findText(String aTextName, String defaultMessage, Object[] args) {
		return findText(context.getAction().getClass(), aTextName, context.getLocale(), defaultMessage, args, context);
	}

	protected Object findValue(Object parent, String property) {
		return beans.getBeanValue(parent, property);
	}

	protected String evalValue(String text, Object model) {
		// TODO: use EL
		return Texts.translate(text, model);
	}

	static class GetDefaultMessageReturnArg {
		String message;
		boolean foundInBundle;

		public GetDefaultMessageReturnArg(String message, boolean foundInBundle) {
			this.message = message;
			this.foundInBundle = foundInBundle;
		}
	}

	/**
	 * Returns a localized message for the specified key, aTextName. Neither the key nor the message
	 * is evaluated.
	 * 
	 * @param aTextName the message key
	 * @param locale the locale the message should be for
	 * @return a localized message based on the specified key, or null if no localized message can
	 *         be found for it
	 */
	protected String findDefaultText(String aTextName, Locale locale) {
		List<String> localList = DEFAULT_RESOURCE_BUNDLES;

		for (String bundleName : localList) {
			ResourceBundle bundle = findResourceBundle(bundleName, locale);
			if (bundle != null) {
				reloadBundles();
				try {
					return bundle.getString(aTextName);
				}
				catch (MissingResourceException e) {
					// ignore and try others
				}
			}
		}

		return null;
	}

	/**
	 * Gets the default message.
	 */
	protected GetDefaultMessageReturnArg getDefaultMessage(String key, Locale locale, Object valueStack, Object[] args,
			String defaultMessage) {
		GetDefaultMessageReturnArg result = null;
		boolean found = true;

		if (key != null) {
			String message = findDefaultText(key, locale);

			if (message == null) {
				message = defaultMessage;
				found = false; // not found in bundles
			}

			// defaultMessage may be null
			if (message != null) {
				MessageFormat mf = buildMessageFormat(evalValue(message, valueStack), locale);

				String msg = formatWithNullDetection(mf, args);
				result = new GetDefaultMessageReturnArg(msg, found);
			}
		}

		return result;
	}

	/**
	 * Determines if we found the text in the bundles.
	 * 
	 * @param result the result so far
	 * @return <tt>true</tt> if we could <b>not</b> find the text, <tt>false</tt> if the text was
	 *         found (=success).
	 */
	protected boolean unableToFindTextForKey(GetDefaultMessageReturnArg result) {
		if (result == null || result.message == null) {
			return true;
		}

		// did we find it in the bundle, then no problem?
		if (result.foundInBundle) {
			return false;
		}

		// not found in bundle
		return true;
	}

	protected String findText(Class aClass, String aTextName, Locale locale, String defaultMessage, Object[] args,
			Object valueStack) {
		if (aTextName == null) {
			if (log.isWarnEnabled()) {
				log.warn("Trying to find text with null key!");
			}
			aTextName = "";
		}

		// search up class hierarchy
		String msg = findMessage(aClass, aTextName, locale, args, null, valueStack);

		if (msg != null) {
			return msg;
		}

		// nothing still? Aright, search the package hierarchy now
		for (Class clazz = aClass; (clazz != null) && !clazz.equals(Object.class); clazz = clazz.getSuperclass()) {
			String basePackageName = clazz.getName();
			while (basePackageName.lastIndexOf('.') != -1) {
				basePackageName = basePackageName.substring(0, basePackageName.lastIndexOf('.'));
				String packageName = basePackageName + ".package";
				msg = getMessage(packageName, locale, aTextName, valueStack, args);

				if (msg != null) {
					return msg;
				}
			}
		}

		// see if it's a child property
		int idx = aTextName.indexOf(".");

		if (idx != -1) {
			String newKey = null;
			String prop = null;

			prop = aTextName.substring(0, idx);
			newKey = aTextName.substring(idx + 1);

			if (prop != null) {
				Object obj = findValue(valueStack, prop);
				try {
					Class clazz = null;
					if (obj != null) {
						clazz = obj.getClass();
					}

					if (clazz != null) {
						msg = findText(clazz, newKey, locale, null, args, obj);

						if (msg != null) {
							return msg;
						}
					}
				}
				catch (Exception e) {
					log.debug("unable to find property " + prop, e);
				}
			}
		}

		// get default
		GetDefaultMessageReturnArg result = null;
		result = getDefaultMessage(aTextName, locale, valueStack, args, defaultMessage);

		// could we find the text, if not log a warn
		if (unableToFindTextForKey(result) && log.isDebugEnabled()) {
			String warn = "Unable to find text for key '" + aTextName + "' ";
			warn += "in class '" + aClass.getName() + "' and locale '" + locale + "'";
			log.debug(warn);
		}

		return result != null ? result.message : null;
	}

	/**
	 * Traverse up class hierarchy looking for message. Looks at class, then implemented interface,
	 * before going up hierarchy.
	 */
	protected String findMessage(Class clazz, String key, Locale locale, Object[] args, Set<String> checked,
			Object valueStack) {
		if (checked == null) {
			checked = new TreeSet<String>();
		}
		else if (checked.contains(clazz.getName())) {
			return null;
		}

		// look in properties of this class
		String msg = getMessage(clazz.getName(), locale, key, valueStack, args);

		if (msg != null) {
			return msg;
		}

		// look in properties of implemented interfaces
		Class[] interfaces = clazz.getInterfaces();

		for (Class anInterface : interfaces) {
			msg = getMessage(anInterface.getName(), locale, key, valueStack, args);
			if (msg != null) {
				return msg;
			}
		}

		// traverse up hierarchy
		if (clazz.isInterface()) {
			interfaces = clazz.getInterfaces();

			for (Class anInterface : interfaces) {
				msg = findMessage(anInterface, key, locale, args, checked, valueStack);

				if (msg != null) {
					return msg;
				}
			}
		}
		else {
			if (!clazz.equals(Object.class) && !clazz.isPrimitive()) {
				return findMessage(clazz.getSuperclass(), key, locale, args, checked, valueStack);
			}
		}

		return null;
	}

	protected MessageFormat buildMessageFormat(String pattern, Locale locale) {
		MultiKey key = new MultiKey(pattern, locale);
		MessageFormat format = messageFormats.get(key);
		if (format == null) {
			format = new MessageFormat(pattern);
			format.setLocale(locale);
			format.applyPattern(pattern);
			messageFormats.put(key, format);
		}

		return format;
	}

	protected String formatWithNullDetection(MessageFormat mf, Object[] args) {
		String message = mf.format(args);
		if ("null".equals(message)) {
			return null;
		}

		return message;
	}

	protected void reloadBundles() {
	}

	/**
	 * Gets the message from the named resource bundle.
	 */
	protected String getMessage(String bundleName, Locale locale, String key, Object valueStack, Object[] args) {
		ResourceBundle bundle = findResourceBundle(bundleName, locale);
		if (bundle == null) {
			return null;
		}

		reloadBundles();

		try {
			String message = evalValue(bundle.getString(key), valueStack);
			MessageFormat mf = buildMessageFormat(message, locale);
			return formatWithNullDetection(mf, args);
		}
		catch (MissingResourceException e) {
			return null;
		}
	}

	/**
	 * Creates a key to used for lookup/storing in the bundle misses cache.
	 * 
	 * @param aBundleName the name of the bundle (usually it's FQN classname).
	 * @param locale the locale.
	 * @return the key to use for lookup/storing in the bundle misses cache.
	 */
	protected String createMissesKey(String aBundleName, Locale locale) {
		return aBundleName + "_" + locale.toString();
	}

	/**
	 * Finds the given resorce bundle by it's name.
	 * <p/>
	 * Will use <code>Thread.currentThread().getContextClassLoader()</code> as the classloader. If
	 * {@link #resourceClassLoader} is defined and the bundle cannot be found the current
	 * classloader it will delegate to that.
	 * 
	 * @param aBundleName the name of the bundle (usually it's FQN classname).
	 * @param locale the locale.
	 * @return the bundle, <tt>null</tt> if not found.
	 */
	public ResourceBundle findResourceBundle(String aBundleName, Locale locale) {
		String key = createMissesKey(aBundleName, locale);

		ResourceBundle bundle = bundlesMap.get(key);
		if (bundle == null) {
			try {
				bundle = resourceBundleLoader.getBundle(aBundleName, locale, resourceClassLoader);
			}
			catch (MissingResourceException ex) {
			}
		}
		if (bundle == null) {
			bundle = EMPTY_BUNDLE;
		}

		bundlesMap.putIfAbsent(key, bundle);
		return (bundle == EMPTY_BUNDLE) ? null : bundle;
	}

}
