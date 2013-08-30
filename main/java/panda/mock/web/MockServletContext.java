package panda.mock.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.activation.FileTypeMap;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import panda.io.resource.DefaultResourceLoader;
import panda.io.resource.Resource;
import panda.io.resource.ResourceLoader;
import panda.lang.Asserts;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServletUtils;


/**
 * Mock implementation of the {@link javax.servlet.ServletContext} interface.
 *
 * <p>Compatible with Servlet 2.5 and partially with Servlet 3.0. Can be configured to
 * expose a specific version through {@link #setMajorVersion}/{@link #setMinorVersion};
 * default is 2.5. Note that Servlet 3.0 support is limited: servlet, filter and listener
 * registration methods are not supported; neither is cookie or JSP configuration.
 * We generally do not recommend to unit-test your ServletContainerInitializers and
 * WebApplicationInitializers which is where those registration methods would be used.
 *
 * <p>Used for testing the Spring web framework; only rarely necessary for testing
 * application controllers. As long as application components don't explicitly
 * access the ServletContext, ClassPathXmlApplicationContext or
 * FileSystemXmlApplicationContext can be used to load the context files for testing,
 * even for DispatcherServlet context definitions.
 *
 * <p>For setting up a full WebApplicationContext in a test environment, you can
 * use XmlWebApplicationContext (or GenericWebApplicationContext), passing in an
 * appropriate MockServletContext instance. You might want to configure your
 * MockServletContext with a FileSystemResourceLoader in that case, to make your
 * resource paths interpreted as relative file system locations.
 *
 * <p>A common setup is to point your JVM working directory to the root of your
 * web application directory, in combination with filesystem-based resource loading.
 * This allows to load the context files as used in the web application, with
 * relative paths getting interpreted correctly. Such a setup will work with both
 * FileSystemXmlApplicationContext (which will load straight from the file system)
 * and XmlWebApplicationContext with an underlying MockServletContext (as long as
 * the MockServletContext has been configured with a FileSystemResourceLoader).
 */
public class MockServletContext implements ServletContext {

	private static final String TEMP_DIR_SYSTEM_PROPERTY = "java.io.tmpdir";


	private final Log logger = Logs.getLog(getClass());

	private final ResourceLoader resourceLoader;

	private final String resourceBasePath;

	private String contextPath = "";

	private int majorVersion = 2;

	private int minorVersion = 5;

	private int effectiveMajorVersion = 2;

	private int effectiveMinorVersion = 5;

	private final Map<String, ServletContext> contexts = new HashMap<String, ServletContext>();

	private final Map<String, String> initParameters = new LinkedHashMap<String, String>();

	private final Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	private String servletContextName = "MockServletContext";

	private final Set<String> declaredRoles = new HashSet<String>();


	/**
	 * Create a new MockServletContext, using no base path and a
	 * DefaultResourceLoader (i.e. the classpath root as WAR root).
	 * @see panda.io.resource.DefaultResourceLoader
	 */
	public MockServletContext() {
		this("", null);
	}

	/**
	 * Create a new MockServletContext, using a DefaultResourceLoader.
	 * @param resourceBasePath the WAR root directory (should not end with a slash)
	 * @see panda.io.resource.DefaultResourceLoader
	 */
	public MockServletContext(String resourceBasePath) {
		this(resourceBasePath, null);
	}

	/**
	 * Create a new MockServletContext, using the specified ResourceLoader
	 * and no base path.
	 * @param resourceLoader the ResourceLoader to use (or null for the default)
	 */
	public MockServletContext(ResourceLoader resourceLoader) {
		this("", resourceLoader);
	}

	/**
	 * Create a new MockServletContext.
	 * @param resourceBasePath the WAR root directory (should not end with a slash)
	 * @param resourceLoader the ResourceLoader to use (or null for the default)
	 */
	public MockServletContext(String resourceBasePath, ResourceLoader resourceLoader) {
		this.resourceLoader = (resourceLoader != null ? resourceLoader : new DefaultResourceLoader());
		this.resourceBasePath = (resourceBasePath != null ? resourceBasePath : "");

		// Use JVM temp dir as ServletContext temp dir.
		String tempDir = System.getProperty(TEMP_DIR_SYSTEM_PROPERTY);
		if (tempDir != null) {
			this.attributes.put(HttpServletUtils.TEMP_DIR_CONTEXT_ATTRIBUTE, new File(tempDir));
		}
	}


	/**
	 * Build a full resource location for the given path,
	 * prepending the resource base path of this MockServletContext.
	 * @param path the path as specified
	 * @return the full resource path
	 */
	protected String getResourceLocation(String path) {
		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return this.resourceBasePath + path;
	}


	public void setContextPath(String contextPath) {
		this.contextPath = (contextPath != null ? contextPath : "");
	}

	/* This is a Servlet API 2.5 method. */
	public String getContextPath() {
		return this.contextPath;
	}

	public void registerContext(String contextPath, ServletContext context) {
		this.contexts.put(contextPath, context);
	}

	public ServletContext getContext(String contextPath) {
		if (this.contextPath.equals(contextPath)) {
			return this;
		}
		return this.contexts.get(contextPath);
	}

	public void setMajorVersion(int majorVersion) {
		this.majorVersion = majorVersion;
	}

	public int getMajorVersion() {
		return this.majorVersion;
	}

	public void setMinorVersion(int minorVersion) {
		this.minorVersion = minorVersion;
	}

	public int getMinorVersion() {
		return this.minorVersion;
	}

	public void setEffectiveMajorVersion(int effectiveMajorVersion) {
		this.effectiveMajorVersion = effectiveMajorVersion;
	}

	public int getEffectiveMajorVersion() {
		return this.effectiveMajorVersion;
	}

	public void setEffectiveMinorVersion(int effectiveMinorVersion) {
		this.effectiveMinorVersion = effectiveMinorVersion;
	}

	public int getEffectiveMinorVersion() {
		return this.effectiveMinorVersion;
	}

	public String getMimeType(String filePath) {
		return MimeTypeResolver.getMimeType(filePath);
	}

	public Set<String> getResourcePaths(String path) {
		String actualPath = (path.endsWith("/") ? path : path + "/");
		Resource resource = this.resourceLoader.getResource(getResourceLocation(actualPath));
		try {
			File file = resource.getFile();
			String[] fileList = file.list();
			if (Objects.isEmpty(fileList)) {
				return null;
			}
			Set<String> resourcePaths = new LinkedHashSet<String>(fileList.length);
			for (String fileEntry : fileList) {
				String resultPath = actualPath + fileEntry;
				if (resource.createRelative(fileEntry).getFile().isDirectory()) {
					resultPath += "/";
				}
				resourcePaths.add(resultPath);
			}
			return resourcePaths;
		}
		catch (IOException ex) {
			logger.warn("Couldn't get resource paths for " + resource, ex);
			return null;
		}
	}

	public URL getResource(String path) throws MalformedURLException {
		Resource resource = this.resourceLoader.getResource(getResourceLocation(path));
		if (!resource.exists()) {
			return null;
		}
		try {
			return resource.getURL();
		}
		catch (MalformedURLException ex) {
			throw ex;
		}
		catch (IOException ex) {
			logger.warn("Couldn't get URL for " + resource, ex);
			return null;
		}
	}

	public InputStream getResourceAsStream(String path) {
		Resource resource = this.resourceLoader.getResource(getResourceLocation(path));
		if (!resource.exists()) {
			return null;
		}
		try {
			return resource.getInputStream();
		}
		catch (IOException ex) {
			logger.warn("Couldn't open InputStream for " + resource, ex);
			return null;
		}
	}

	public RequestDispatcher getRequestDispatcher(String path) {
		if (!path.startsWith("/")) {
			throw new IllegalArgumentException("RequestDispatcher path at ServletContext level must start with '/'");
		}
		return new MockRequestDispatcher(path);
	}

	public RequestDispatcher getNamedDispatcher(String path) {
		return null;
	}

	public Servlet getServlet(String name) {
		return null;
	}

	public Enumeration<Servlet> getServlets() {
		return Collections.enumeration(new HashSet<Servlet>());
	}

	public Enumeration<String> getServletNames() {
		return Collections.enumeration(new HashSet<String>());
	}

	public void log(String message) {
		logger.info(message);
	}

	public void log(Exception ex, String message) {
		logger.info(message, ex);
	}

	public void log(String message, Throwable ex) {
		logger.info(message, ex);
	}

	public String getRealPath(String path) {
		Resource resource = this.resourceLoader.getResource(getResourceLocation(path));
		try {
			return resource.getFile().getAbsolutePath();
		}
		catch (IOException ex) {
			logger.warn("Couldn't determine real path of resource " + resource, ex);
			return null;
		}
	}

	public String getServerInfo() {
		return "MockServletContext";
	}

	public String getInitParameter(String name) {
		Asserts.notNull(name, "Parameter name must not be null");
		return this.initParameters.get(name);
	}

	public Enumeration<String> getInitParameterNames() {
		return Collections.enumeration(this.initParameters.keySet());
	}

	public boolean setInitParameter(String name, String value) {
		Asserts.notNull(name, "Parameter name must not be null");
		if (this.initParameters.containsKey(name)) {
			return false;
		}
		this.initParameters.put(name, value);
		return true;
	}

	public void addInitParameter(String name, String value) {
		Asserts.notNull(name, "Parameter name must not be null");
		this.initParameters.put(name, value);
	}

	public Object getAttribute(String name) {
		Asserts.notNull(name, "Attribute name must not be null");
		return this.attributes.get(name);
	}

	public Enumeration<String> getAttributeNames() {
		return new Vector<String>(this.attributes.keySet()).elements();
	}

	public void setAttribute(String name, Object value) {
		Asserts.notNull(name, "Attribute name must not be null");
		if (value != null) {
			this.attributes.put(name, value);
		}
		else {
			this.attributes.remove(name);
		}
	}

	public void removeAttribute(String name) {
		Asserts.notNull(name, "Attribute name must not be null");
		this.attributes.remove(name);
	}

	public void setServletContextName(String servletContextName) {
		this.servletContextName = servletContextName;
	}

	public String getServletContextName() {
		return this.servletContextName;
	}

	public ClassLoader getClassLoader() {
		return Classes.getClassLoader();
	}

	public void declareRoles(String... roleNames) {
		Asserts.notNull(roleNames, "Role names array must not be null");
		for (String roleName : roleNames) {
			Asserts.notEmpty(roleName, "Role name must not be empty");
			this.declaredRoles.add(roleName);
		}
	}

	public Set<String> getDeclaredRoles() {
		return Collections.unmodifiableSet(this.declaredRoles);
	}


	/**
	 * Inner factory class used to just introduce a Java Activation Framework
	 * dependency when actually asked to resolve a MIME type.
	 */
	private static class MimeTypeResolver {

		public static String getMimeType(String filePath) {
			return FileTypeMap.getDefaultFileTypeMap().getContentType(filePath);
		}
	}

}
