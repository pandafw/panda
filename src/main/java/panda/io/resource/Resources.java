package panda.io.resource;

import java.util.Locale;

import panda.lang.Strings;

public class Resources {
	/**
	 * Converts the given <code>baseName</code> and <code>locale</code> to the bundle name. 
	 * <p>
	 * This implementation returns the following value:
	 * 
	 * <pre>
	 * baseName + &quot;_&quot; + language + &quot;_&quot; + country + &quot;_&quot; + variant
	 * </pre>
	 * 
	 * where <code>language</code>, <code>country</code> and <code>variant</code> are the language,
	 * country and variant values of <code>locale</code>, respectively. Final component values that
	 * are empty Strings are omitted along with the preceding '_'. If all of the values are empty
	 * strings, then <code>baseName</code> is returned.
	 * <p>
	 * For example, if <code>baseName</code> is <code>"baseName"</code> and <code>locale</code> is
	 * <code>Locale("ja",&nbsp;"",&nbsp;"XX")</code>, then <code>"baseName_ja_&thinsp;_XX"</code> is
	 * returned. If the given locale is <code>Locale("en")</code>, then <code>"baseName_en"</code>
	 * is returned.
	 * <p>
	 * Overriding this method allows applications to use different conventions in the organization
	 * and packaging of localized resources.
	 * 
	 * @param baseName the base name of the resource bundle, a fully qualified class name
	 * @param locale the locale for which a resource bundle should be loaded
	 * @return the bundle name for the resource bundle
	 * @exception NullPointerException if <code>baseName</code> or <code>locale</code> is
	 *                <code>null</code>
	 */
	public static String toBundleName(String baseName, Locale locale) {
		if (locale == Locale.ROOT) {
			return baseName;
		}

		String language = locale.getLanguage();
		String country = locale.getCountry();
		String variant = locale.getVariant();

		if (Strings.isEmpty(language) && Strings.isEmpty(country) && Strings.isEmpty(variant)) {
			return baseName;
		}

		StringBuilder sb = new StringBuilder(baseName);
		sb.append('_');
		if (Strings.isNotEmpty(variant)) {
			sb.append(language).append('_').append(country).append('_').append(variant);
		}
		else if (Strings.isNotEmpty(country)) {
			sb.append(language).append('_').append(country);
		}
		else {
			sb.append(language);
		}
		return sb.toString();
	}

	/**
	 * Converts the given <code>bundleName</code> to the form required by the
	 * {@link ClassLoader#getResource ClassLoader.getResource} method by replacing all occurrences
	 * of <code>'.'</code> in <code>bundleName</code> with <code>'/'</code> and appending a
	 * <code>'.'</code> and the given file <code>suffix</code>. For example, if
	 * <code>bundleName</code> is <code>"foo.bar.MyResources_ja_JP"</code> and <code>suffix</code>
	 * is <code>"properties"</code>, then <code>"foo/bar/MyResources_ja_JP.properties"</code> is
	 * returned.
	 * 
	 * @param bundleName the bundle name
	 * @param suffix the file type suffix
	 * @return the converted resource name
	 * @exception NullPointerException if <code>bundleName</code> or <code>suffix</code> is
	 *                <code>null</code>
	 */
	public static String toResourceName(String bundleName, String suffix) {
		StringBuilder sb = new StringBuilder(bundleName.length() + 1 + suffix.length());
		sb.append(bundleName.replace('.', '/')).append('.').append(suffix);
		return sb.toString();
	}
}
