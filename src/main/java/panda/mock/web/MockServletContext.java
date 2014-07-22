package panda.mock.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.activation.FileTypeMap;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import panda.io.Files;
import panda.lang.Asserts;
import panda.lang.ClassLoaders;
import panda.lang.Collections;
import panda.lang.Objects;
import panda.log.Log;
import panda.log.Logs;
import panda.servlet.HttpServlets;


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
 */
public class MockServletContext implements ServletContext {

	private static final String TEMP_DIR_SYSTEM_PROPERTY = "java.io.tmpdir";


	private final Log logger = Logs.getLog(getClass());

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
	 */
	public MockServletContext() {
		this("");
	}

	/**
	 * Create a new MockServletContext, using a DefaultResourceLoader.
	 * @param resourceBasePath the WAR root directory (should not end with a slash)
	 */
	public MockServletContext(String resourceBasePath) {
		this.resourceBasePath = (resourceBasePath != null ? resourceBasePath : "");

		// Use JVM temp dir as ServletContext temp dir.
		String tempDir = System.getProperty(TEMP_DIR_SYSTEM_PROPERTY);
		if (tempDir != null) {
			this.attributes.put(HttpServlets.TEMP_DIR_CONTEXT_ATTRIBUTE, new File(tempDir));
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
		path = (path.endsWith("/") ? path : path + "/");
		String actualPath = getResourceLocation(path);

		File file = new File(actualPath);
		String[] fileList = file.list();
		if (Objects.isEmpty(fileList)) {
			return null;
		}
		
		Set<String> resourcePaths = new LinkedHashSet<String>(fileList.length);
		for (String fileEntry : fileList) {
			String resultPath = actualPath + fileEntry;
			if (Files.isDirectory(resultPath)) {
				resultPath += "/";
			}
			resourcePaths.add(resultPath);
		}
		return resourcePaths;
	}

	public URL getResource(String path) throws MalformedURLException {
		return new URL(getResourceLocation(path));
	}

	public InputStream getResourceAsStream(String path) {
		URL url;
		
		try {
			url = getResource(path);
			if (url == null) {
				return null;
			}
		}
		catch (MalformedURLException ex) {
			logger.warn("Couldn't open InputStream for " + path, ex);
			return null;
		}

		try {
			return url.openStream();
		}
		catch (IOException ex) {
			logger.warn("Couldn't open InputStream for " + url, ex);
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
		File file = new File(getResourceLocation(path));
		return file.getAbsolutePath();
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
		return ClassLoaders.getClassLoader();
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

	//----------------------------------------------------------------------
	// Servlet 3.0
	//
	@Override
	public Dynamic addServlet(String servletName, String className) {
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName, Servlet servlet) {
		return null;
	}

	@Override
	public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
		return null;
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public ServletRegistration getServletRegistration(String servletName) {
		return null;
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
		return null;
	}

	@Override
	public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
		return null;
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public FilterRegistration getFilterRegistration(String filterName) {
		return null;
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		return null;
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		return null;
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		return null;
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		return null;
	}

	@Override
	public void addListener(String className) {
		
	}

	@Override
	public <T extends EventListener> void addListener(T t) {
		
	}

	@Override
	public void addListener(Class<? extends EventListener> listenerClass) {
		
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
		return null;
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		return null;
	}

	@Override
	public String getVirtualServerName() {
		return null;
	}

}
