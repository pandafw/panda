package panda.mvc.view.ftl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.Properties;

import javax.servlet.ServletContext;

import freemarker.cache.TemplateLoader;
import freemarker.ext.jsp.TaglibFactory;
import freemarker.template.Configuration;
import freemarker.template.ObjectWrapper;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Charsets;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.view.taglib.TagLibrary;
import panda.mvc.view.taglib.TagLibraryManager;

@IocBean(create="initialize")
public class FreemarkerManager {
	private static final Log LOG = Logs.getLog(FreemarkerManager.class);

	public static final String KEY_JSP_TAGLIBS = "JspTaglibs";
	public static final String KEY_STATIC = "static";

	@IocInject(value=MvcConstants.FREEMARKER_SETTINGS, required=false)
	protected String settings = "freemarker.properties";
	
	@IocInject(required=false)
	protected ServletContext servletContext;
	
	@IocInject
	protected TemplateLoader templateLoader;
	
	@IocInject(value = MvcConstants.FREEMARKER_WRAPPER_ALT_MAP, required = false)
	protected boolean altMapWrapper;

	@IocInject(value = MvcConstants.FREEMARKER_BEANWRAPPER_CACHE, required = false)
	protected boolean cacheBeanWrapper;

	protected String staticBase;
	
	@IocInject
	protected TagLibraryManager taglibManager;
	
	protected Configuration config;
	protected ObjectWrapper wrapper;
	protected TaglibFactory jspTaglibs;

	public Configuration getConfig() {
		return config;
	}

	public ObjectWrapper getWrapper() {
		return wrapper;
	}

	public Configuration getConfiguration() {
		return config;
	}

	public ActionHash buildTemplateModel(ActionContext ac) {
		return buildTemplateModel(ac, wrapper);
	}
	
	public ActionHash buildTemplateModel(ActionContext ac, ObjectWrapper wrapper) {
		ActionHash model = new ActionHash(wrapper, ac);

		if (taglibManager != null) {
			for (Entry<String, TagLibrary> en : taglibManager.getTagLibraries().entrySet()) {
				model.put(en.getKey(), en.getValue().getModels(ac));
			}
		}

		if (jspTaglibs != null) {
			model.put(KEY_JSP_TAGLIBS, jspTaglibs);
		}

		if (staticBase == null) {
			staticBase = Mvcs.getStaticBase(ac, null);
		}
		model.put(KEY_STATIC, staticBase);

		return model;
	}

	public void initialize() throws IOException, TemplateException {
		// Process object_wrapper init-param out of order:
		initObjectWrapper();
		if (LOG.isDebugEnabled()) {
			LOG.debug("Using object wrapper of class " + wrapper.getClass().getName());
		}

		// create configuration
		initConfiguration();

		// jsp taglib support
		if (servletContext != null) {
			jspTaglibs = new TaglibFactory(servletContext);
		}
	}

	/**
	 * Create the instance of the freemarker Configuration object.
	 * <p/>
	 * this implementation
	 * <ul>
	 * <li>obtains the default configuration from Configuration.getDefaultConfiguration()
	 * <li>sets up template loading from a ClassTemplateLoader and a WebappTemplateLoader
	 * <li>sets up the object wrapper to be the BeansWrapper
	 * <li>loads settings from the classpath file /freemarker.properties
	 * </ul>
	 */
	protected void initConfiguration() throws IOException, TemplateException {
		config = new Configuration();

		config.setObjectWrapper(wrapper);
		config.setTemplateLoader(templateLoader);
		config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

		config.setDefaultEncoding(Charsets.UTF_8);
		config.setLocalizedLookup(true);
		config.setWhitespaceStripping(true);

		loadSettings();
	}

	protected void initObjectWrapper() {
		wrapper = new AltBeanWrapper(altMapWrapper);
		((AltBeanWrapper)wrapper).setUseCache(cacheBeanWrapper);
	}

	/**
	 * Load the settings from the /freemarker.properties file on the classpath
	 * 
	 * @see freemarker.template.Configuration#setSettings for the definition of valid settings
	 */
	protected void loadSettings() throws IOException, TemplateException {
		InputStream in = null;

		try {
			in = Streams.getStream(settings);

			if (in != null) {
				Properties p = new Properties();
				p.load(in);

				for (Object o : p.keySet()) {
					String name = (String)o;
					String value = (String)p.get(name);

					if (name == null) {
						throw new IllegalArgumentException(
							"init-param without param-name.  Maybe the " + settings + " is not well-formed?");
					}
					if (value == null) {
						throw new IllegalArgumentException(
							"init-param without param-value.  Maybe the " + settings + " is not well-formed?");
					}
					config.setSetting(name, value);
				}
			}
		}
		catch (FileNotFoundException e) {
			// skip
		}
		finally {
			Streams.safeClose(in);
		}
	}
}
