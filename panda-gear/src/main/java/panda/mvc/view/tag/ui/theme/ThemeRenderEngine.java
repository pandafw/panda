package panda.mvc.view.tag.ui.theme;

import java.util.HashMap;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.lang.Strings;
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
@IocBean(type=RendererEngine.class)
public class ThemeRenderEngine implements RendererEngine {

	private static final Log log = Logs.getLog(ThemeRenderEngine.class);

	private Map<String, Theme> themes = new HashMap<String, Theme>();
	
	public ThemeRenderEngine() {
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
	 * Allows for providing custom theme classes (implementations of the Theme)
	 * 
	 * @param themes a space delimited list of theme class names
	 * @throws Exception if a custom theme class could not be create
	 */
	@IocInject(value = MvcConstants.TAG_THEMES, required = false)
	public void setThemeClasses(String themes) throws Exception {
		String[] ts = Strings.split(themes);
		for (String t : ts) {
			try {
				log.info("Register theme '" + t + "'.");
				addTheme((Theme)Classes.newInstance(t));
			}
			catch (ClassCastException cce) {
				log.error("Invalid theme " + t + ". Class does not implement " + Theme.class);
				throw cce;
			}
			catch (ClassNotFoundException cnf) {
				log.error("Invalid theme " + t + ". Class not found");
				throw cnf;
			}
		}
	}
}

