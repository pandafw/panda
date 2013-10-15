package panda.exts.freemarker;

import java.io.IOException;
import java.io.Reader;

import freemarker.cache.TemplateLoader;

/**
 * static delegate template loader
 */
public class StaticDelegateTemplateLoader implements TemplateLoader {

	private static TemplateLoader delegate;

	/**
	 * @return the delegate
	 */
	public static TemplateLoader getDelegate() {
		return delegate;
	}

	/**
	 * @param delegate the delegate to set
	 */
	public static void setDelegate(TemplateLoader delegate) {
		StaticDelegateTemplateLoader.delegate = delegate;
	}

	/**
	 * Constructor
	 */
	public StaticDelegateTemplateLoader() {
	}

	/**
	 * Constructor
	 * 
	 * @param delegate delegate template loader
	 */
	public StaticDelegateTemplateLoader(TemplateLoader delegate) {
		StaticDelegateTemplateLoader.delegate = delegate;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	public void closeTemplateSource(Object templateSource) throws IOException {
		if (delegate != null) {
			delegate.closeTemplateSource(templateSource);
		}
	}

	/**
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	public Object findTemplateSource(String name) throws IOException {
		if (delegate == null) {
			return null;
		}
		return delegate.findTemplateSource(name);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	public long getLastModified(Object templateSource) {
		if (delegate == null) {
			return 0;
		}
		return delegate.getLastModified(templateSource);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object,
	 *      java.lang.String)
	 */
	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		if (delegate == null) {
			return null;
		}
		return delegate.getReader(templateSource, encoding);
	}
}
