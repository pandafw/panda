package panda.mvc.view.tag.ui.theme;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.mvc.view.tag.ui.theme.bs3.Bs3Theme;
import panda.mvc.view.tag.ui.theme.bs3.horizontal.Bs3HorizontalTheme;
import panda.mvc.view.tag.ui.theme.bs3.inline.Bs3InlineTheme;
import panda.mvc.view.tag.ui.theme.simple.SimpleTheme;
import panda.mvc.view.tag.ui.theme.xhtml.XhtmlTheme;

/**
 * Template engine that renders tags using java implementations
 */
public class JavaRenderEngine implements RendererEngine {

	private static final Log log = Logs.getLog(JavaRenderEngine.class);

	private Map<String, Theme> themes = new HashMap<String, Theme>();
	
	public JavaRenderEngine() {
		Theme simpleTheme = new SimpleTheme();
		addTheme(simpleTheme);
		addTheme(new Bs3Theme(simpleTheme));
		addTheme(new Bs3HorizontalTheme(simpleTheme));
		addTheme(new Bs3InlineTheme(simpleTheme));
		addTheme(new XhtmlTheme(simpleTheme));
	}

	protected void addTheme(Theme theme) {
		themes.put(theme.getName(), theme);
	}

	protected Theme getTheme(String name) {
		return themes.get(name);
	}
	
	public void start(RenderingContext rctx) throws Exception {
		Theme theme = themes.get(rctx.getTheme());
		if (theme == null) {
			throw new IllegalArgumentException("Cannot render tag [" + rctx.getTag().getClass()
					+ "] because theme [" + rctx.getTheme() + "] was not found.");
		}
		theme.renderStart(rctx);
	}
	
	public void end(RenderingContext rctx) throws Exception {
		Theme theme = themes.get(rctx.getTheme());
		if (theme == null) {
			throw new IllegalArgumentException("Cannot render tag [" + rctx.getTag().getClass()
					+ "] because theme [" + rctx.getTheme() + "] was not found.");
		}
		theme.renderEnd(rctx);
	}

	/**
	 * Allows for providing custom theme classes (implementations of the
	 * org.apache.struts2.views.java.Theme) interface for custom rendering of
	 * tags using the javatemplates engine
	 * 
	 * @param themeClasses a comma delimited list of custom theme class names
	 */
	@IocInject(value = MvcConstants.PANDA_CUSTOM_THEMES, required = false)
	public void setThemeClasses(String themeClasses) throws Exception {
		StringTokenizer customThemes = new StringTokenizer(themeClasses, ",");

		while (customThemes.hasMoreTokens()) {
			String themeClass = customThemes.nextToken().trim();
			try {
				log.info("Registering custom theme '" + themeClass + "' to javatemplates engine");
				addTheme((Theme)Classes.newInstance(themeClass));
			}
			catch (ClassCastException cce) {
				log.error("Invalid java them class '"
						+ themeClass
						+ "'. Class does not implement 'org.apache.struts2.views.java.Theme' interface");
				throw cce;
			}
			catch (ClassNotFoundException cnf) {
				log.error("Invalid java theme class '" + themeClass + "'. Class not found");
				throw cnf;
			}
			catch (Exception e) {
				log.error("Could not find messages file " + themeClass + ".properties. Skipping");
				throw e;
			}
		}
	}
}

