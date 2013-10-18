package panda.io.resource;

import panda.io.FileNames;
import panda.lang.Asserts;

/**
 * {@link ResourceLoader} implementation that interprets plain resource paths
 * as relative to a given <code>java.lang.Class</code>.
 * 
 * @author yf.frank.wang@gmail.com
 */
public class ClassRelativeResourceLoader extends DefaultResourceLoader {

	private final Class clazz;


	/**
	 * Create a new ClassRelativeResourceLoader for the given class.
	 * @param clazz the class to load resources through
	 */
	public ClassRelativeResourceLoader(Class clazz) {
		Asserts.notNull(clazz, "Class must not be null");
		this.clazz = clazz;
		setClassLoader(clazz.getClassLoader());
	}

	protected Resource getResourceByPath(String path) {
		return new ClassRelativeContextResource(path, this.clazz);
	}


	/**
	 * ClassPathResource that explicitly expresses a context-relative path
	 * through implementing the ContextResource interface.
	 */
	private static class ClassRelativeContextResource extends ClassPathResource implements ContextResource {

		private final Class clazz;

		public ClassRelativeContextResource(String path, Class clazz) {
			super(path, clazz);
			this.clazz = clazz;
		}

		public String getPathWithinContext() {
			return getPath();
		}

		@Override
		public Resource createRelative(String relativePath) {
			String pathToUse = FileNames.concat(getPath(), relativePath);
			return new ClassRelativeContextResource(pathToUse, this.clazz);
		}
	}

}
