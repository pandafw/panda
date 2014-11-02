package panda.tpl.ftl;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.cache.StatefulTemplateLoader;
import freemarker.cache.TemplateLoader;

public class MultiTemplateLoader implements StatefulTemplateLoader {
	private final List<TemplateLoader> loaders = new ArrayList<TemplateLoader>();
	private final Map<String, TemplateLoader> cache = new HashMap<String, TemplateLoader>();

	/**
	 * Creates a new multi template Loader that will use the specified loaders.
	 * 
	 * @param loaders the loaders that are used to load templates.
	 */
	public MultiTemplateLoader(TemplateLoader... loaders) {
		if (loaders != null) {
			this.loaders.addAll(Arrays.asList(loaders));
		}
	}

	public void addTemplateLoader(TemplateLoader loader) {
		synchronized (loaders) {
			loaders.remove(loader);
			loaders.add(loader);
		}
	}
	
	public Object findTemplateSource(String name) throws IOException {
		// Use soft affinity - give the loader that last found this
		// resource a chance to find it again first.
		TemplateLoader lastLoader = cache.get(name);
		if (lastLoader == null) {
			synchronized(cache) {
				lastLoader = cache.get(name);
				if (lastLoader == null) {
					return findSource(name);
				}
			}
		}

		Object source = lastLoader.findTemplateSource(name);
		if (source != null) {
			return new MultiSource(source, lastLoader);
		}

		// Resource not found
		return null;
	}

	private MultiSource findSource(String name) throws IOException {
		// If there is no affine loader, or it could not find the resource
		// again, try all loaders in order of appearance. If any manages
		// to find the resource, then associate it as the new affine loader
		// for this resource.
		synchronized (loaders) {
			for (TemplateLoader loader : loaders) {
				Object source = loader.findTemplateSource(name);
				if (source != null) {
					cache.put(name, loader);
					return new MultiSource(source, loader);
				}
			}
		}
		
		return null;
	}

	public long getLastModified(Object templateSource) {
		return ((MultiSource)templateSource).getLastModified();
	}

	public Reader getReader(Object templateSource, String encoding) throws IOException {
		return ((MultiSource)templateSource).getReader(encoding);
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
		((MultiSource)templateSource).close();
	}

	public void resetState() {
		synchronized (cache) {
			cache.clear();
		}

		synchronized (loaders) {
			for (TemplateLoader loader : loaders) {
				if (loader instanceof StatefulTemplateLoader) {
					((StatefulTemplateLoader)loader).resetState();
				}
			}
		}
	}

	/**
	 * Represents a template source bound to a specific template loader. It serves as the complete
	 * template source descriptor used by the MultiTemplateLoader class.
	 */
	private static final class MultiSource {
		private final Object source;
		private final TemplateLoader loader;

		MultiSource(Object source, TemplateLoader loader) {
			this.source = source;
			this.loader = loader;
		}

		long getLastModified() {
			return loader.getLastModified(source);
		}

		Reader getReader(String encoding) throws IOException {
			return loader.getReader(source, encoding);
		}

		void close() throws IOException {
			loader.closeTemplateSource(source);
		}

		public boolean equals(Object o) {
			if (o instanceof MultiSource) {
				MultiSource m = (MultiSource)o;
				return m.loader.equals(loader) && m.source.equals(source);
			}
			return false;
		}

		public int hashCode() {
			return loader.hashCode() + 31 * source.hashCode();
		}

		public String toString() {
			return source.toString();
		}
	}
}
