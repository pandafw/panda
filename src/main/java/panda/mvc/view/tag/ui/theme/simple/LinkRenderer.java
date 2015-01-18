package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Locale;

import panda.Panda;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class LinkRenderer extends AbstractEndRenderer<Link> {
	private static final String JQUERY_VERSION = "1.11.2";
	private static final String BOOTSTRAP_VERSION = "3.3.1";
	private static final String FONTAWESOME_VERSION = "4.2.0";
	
	private boolean js;
	private boolean css;

	public LinkRenderer(RenderingContext rc) {
		super(rc);
	}
	
	@Override
	protected void render() throws IOException {
		// output css first for client performance
		css = tag.isCss();
		if (css) {
			js = false;
			writeJquery();
			writeJqueryPlugins();
			writeBootstrap();
			writeBootstrapPlugins();
			writePanda();
		}

		js = tag.isJs();
		if (js) {
			css = false;
			writeJquery();
			writeJqueryPlugins();
			writeBootstrap();
			writeBootstrapPlugins();
			writePanda();
		}
	}

	protected void writeJscript(String jsc) throws IOException {
		if (js) {
			writeJsc(jsc);
		}
	}
	protected void writePandaCdnJs(String path) throws IOException {
		writePandaCdnJs(path, true);
	}
	
	protected void writePandaCdnJs(String path, boolean debug) throws IOException {
		if (js) {
			writeCdnJs("http://foolite.github.io/panda-web/" + Panda.VERSION + path + (debug ? debug() : "") + ".js");
		}
	}
	protected void writeCdnJs(String jsl) throws IOException {
		if (js) {
			writeJs(jsl);
		}
	}
	
	protected void writeStaticJs(String jsl) throws IOException {
		writeStaticJs(jsl, true);
	}
	
	protected void writeStaticJs(String jsl, boolean debug) throws IOException {
		if (js) {
			writeJs(suri(jsl + (debug ? debug() : "") + ".js", tag.getVersion()));
		}
	}
	
	protected void writePandaCdnCss(String path) throws IOException {
		writeCdnCss("http://foolite.github.io/panda-web/" + Panda.VERSION + path + debug() + ".css");
	}
	protected void writeCdnCss(String cssl) throws IOException {
		if (css) {
			writeCss(cssl);
		}
	}
	protected void writeStaticCss(String cssl) throws IOException {
		if (css) {
			writeCss(suri(cssl + debug() + ".css", tag.getVersion()));
		}
	}

	protected void writeStaticCss(String cssl, String cls) throws IOException {
		if (css) {
			writeCss(suri(cssl + debug() + ".css", tag.getVersion()), cls);
		}
	}
	
	private String sbase;
	protected String suri(String uri, String version) {
		if (sbase == null) {
			sbase = Mvcs.getStaticBase(context, ((Link)tag).getStatics());
		}
		if (Strings.isNotEmpty(version)) {
			uri += "?v=" + version;
		}
		return sbase + uri;
	}

	private void writeJquery() throws IOException {
		if (js && tag.isJquery()) {
			if (tag.isCdn()) {
				writeCdnJs("http://code.jquery.com/jquery-" + JQUERY_VERSION + debug() + ".js");
			}
			else {
				writeStaticJs("/jquery/js/jquery-" + JQUERY_VERSION);
			}
		}
	}

	private void writeJqueryPlugins() throws IOException {
		if (tag.isJqueryPlugins()) {
			if (tag.isCdn()) {
				writePandaCdnCss("/jquery/css/jquery-plugins");
				writePandaCdnJs("/jquery/js/jquery-plugins");
			}
			else {
				writeStaticCss("/jquery/css/jquery-plugins");
				writeStaticJs("/jquery/js/jquery-plugins");
			}
//
//			String la = getJqueryLang();
//			if ("ja".equals(la) || la.startsWith("zh")) {
//				writeStaticJs("/jquery/plugins/i18n/jquery-plugins-" + jsstr(la));
//			}
		}
	}
	
	private void writeBootstrap() throws IOException {
		boolean bs = tag.isBootstrap();
		if (css && bs) {
			if (tag.isCdn()) {
				writeCdnCss("http://netdna.bootstrapcdn.com/bootstrap/" 
						+ BOOTSTRAP_VERSION 
						+ "/css/bootstrap"
						+ debug() + ".css");
				writeCdnCss("http://netdna.bootstrapcdn.com/font-awesome/" 
						+ FONTAWESOME_VERSION 
						+ "/css/font-awesome"
						+ debug() + ".css");
			}
			else {
				writeStaticCss("/bootstrap3/css/bootstrap");
				writeStaticCss("/font-awesome/css/font-awesome");
			}
		}
		if (js && bs) {
			if (tag.isCdn()) {
				writeCdnJs("http://netdna.bootstrapcdn.com/bootstrap/" 
						+ BOOTSTRAP_VERSION 
						+ "/js/bootstrap" 
						+ debug() + ".js");
			}
			else {
				writeStaticJs("/bootstrap3/js/bootstrap");
			}
		}
	}

	private void writeBootstrapPlugins() throws IOException {
		boolean bsp = tag.isBootstrapPlugins();
		if (css && bsp) {
			if (tag.isCdn()) {
				writePandaCdnCss("/bootstrap3/css/bootstrap-plugins");
			}
			else {
				writeStaticCss("/bootstrap3/css/bootstrap-plugins");
			}
		}
		
		if (js && bsp) {
			if (tag.isCdn()) {
				writePandaCdnJs("/bootstrap3/js/bootstrap-plugins");
			}
			else {
				writeStaticJs("/bootstrap3/js/bootstrap-plugins");
			}
			
			String la = getBootstrapLang();
			if (tag.isI18n()) {
				if (tag.isCdn()) {
					writePandaCdnJs("/bootstrap3/js/bootstrap-plugins-i18n");
				}
				else {
					writeStaticJs("/bootstrap3/js/bootstrap-plugins-i18n");
				}
			}
			else {
				if (tag.isCdn()) {
					writePandaCdnJs("/bootstrap3/js/locales/bootstrap-datetimepicker." + jsstr(la), false);
				}
				else {
					writeStaticJs("/bootstrap3/js/locales/bootstrap-datetimepicker." + jsstr(la), false);
				}
			}
			writeJscript("$(function() { $.fn.datetimepicker.defaults.language = '" + jsstr(la) + "'; });");
		}
	}

	private String getBootstrapLang() {
		Locale locale = tag.getLocale();
		String la = locale.getLanguage();
		if ("fa".equals(la)) {
			la += "-IR";
		}
		else if ("pt".equals(la)) {
			la += "-BR";
		}
		else if ("zh".equals(la)) {
			if ("TW".equalsIgnoreCase(locale.getCountry())
					|| "HK".equalsIgnoreCase(locale.getCountry())) {
				la += "-TW";
			}
			else {
				la += "-CN";
			}
		}
		return la;
	}

	private void writePanda() throws IOException {
		boolean panda = tag.isPanda();
		if (css && panda) {
			if (tag.isCdn()) {
				writePandaCdnCss("/panda/css/panda");
			}
			else {
				writeStaticCss("/panda/css/panda");
			}
		}
		if (js && panda) {
			if (tag.isCdn()) {
				writePandaCdnJs("/panda/js/panda");
			}
			else {
				writeStaticJs("/panda/js/panda");
			}
		}
	}
	
	private String debug() {
		 return (tag.isDebug() ? "" : ".min");
	}
}
