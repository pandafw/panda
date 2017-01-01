package panda.tpl.ftl;

import java.io.IOException;
import java.io.Reader;

import freemarker.cache.TemplateLoader;

/**
 * delegate template loader
 */
public class DelegateTemplateLoader implements TemplateLoader {

	private TemplateLoader delegate;

	/**
	 * @return the delegate
	 */
	public TemplateLoader getDelegate() {
		return delegate;
	}

	/**
	 * @param delegate the delegate to set
	 */
	public void setDelegate(TemplateLoader delegate) {
		this.delegate = delegate;
	}

	/**
	 * Constructor
	 */
	public DelegateTemplateLoader() {
	}

	/**
	 * Constructor
	 * 
	 * @param delegate delegate template loader
	 */
	public DelegateTemplateLoader(TemplateLoader delegate) {
		this.delegate = delegate;
	}

	/**
	 * @see freemarker.cache.TemplateLoader#closeTemplateSource(java.lang.Object)
	 */
	public void closeTemplateSource(Object templateSource) throws IOException {
		delegate.closeTemplateSource(templateSource);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#findTemplateSource(java.lang.String)
	 */
	public Object findTemplateSource(String name) throws IOException {
		return delegate.findTemplateSource(name);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getLastModified(java.lang.Object)
	 */
	public long getLastModified(Object templateSource) {
		return delegate.getLastModified(templateSource);
	}

	/**
	 * @see freemarker.cache.TemplateLoader#getReader(java.lang.Object, java.lang.String)
	 */
	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return delegate.getReader(templateSource, encoding);
	}
}
