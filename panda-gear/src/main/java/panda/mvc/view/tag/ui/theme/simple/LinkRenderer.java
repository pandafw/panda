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
	private static final String CDN_BASE = Mvcs.PANDA_CDN + '/' + Panda.VERSION;
	private static final String JQUERY_VERSION = "1.12.4";
	private static final String BOOTSTRAP_VERSION = "3.3.7";
	private static final String FONTAWESOME_VERSION = "4.7.0";
	
	private boolean js;
	private boolean css;
	private String lang;

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
			writeJqueryExtras();
			writeBootstrap();
			writeBootstrapPlugins();
			writePanda();
		}

		js = tag.isJs();
		if (js) {
			css = false;
			writeJquery();
			writeJqueryPlugins();
			writeJqueryExtras();
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
			writeCdnJs(CDN_BASE + path + (debug ? debug() : "") + ".js");
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
		writeCdnCss(CDN_BASE + path + debug() + ".css");
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
		
		StringBuilder s = new StringBuilder();
		s.append(sbase).append(uri);
		if (Strings.isNotEmpty(version)) {
			s.append("?v=").append(version);
		}

		return s.toString();
	}

	private void writeJquery() throws IOException {
		if (js && tag.isJquery()) {
			if (tag.getCdn()) {
				writeCdnJs("//code.jquery.com/jquery-" + JQUERY_VERSION + debug() + ".js");
			}
			else {
				writeStaticJs("/jquery/js/jquery-" + JQUERY_VERSION);
			}
		}
	}

	private void writeJqueryPlugins() throws IOException {
		if (tag.isJqplugins()) {
			if (tag.useCdn()) {
				writePandaCdnCss("/jquery/css/jquery-plugins");
				writePandaCdnJs("/jquery/js/jquery-plugins");
			}
			else {
				writeStaticCss("/jquery/css/jquery-plugins");
				writeStaticJs("/jquery/js/jquery-plugins");
			}
		}
	}

	private void writeJqueryExtras() throws IOException {
		if (tag.isJqextras()) {
			if (tag.useCdn()) {
				writePandaCdnCss("/jquery/extras/css/jquery-extras");
				writePandaCdnJs("/jquery/extras/js/jquery-extras");
			}
			else {
				writeStaticCss("/jquery/extras/css/jquery-extras");
				writeStaticJs("/jquery/extras/js/jquery-extras");
			}

			String la = this.getBootstrapLang();
			if ("ja".equals(la) || la.startsWith("zh")) {
				writeStaticJs("/jquery/extras/locales/jquery-extras-" + jsstr(la));
			}
		}
		
		if (tag.isHammer()) {
			writeJqueryExtra("hammer", false);
		}
		
		if (tag.isLightbox()) {
			writeJqueryExtra("lightbox", true);

			String la = this.getBootstrapLang();
			if ("ja".equals(la) || la.startsWith("zh")) {
				writeStaticJs("/jquery/extras/locales/jquery.ui.lightbox." + jsstr(la));
			}
		}

		if (tag.isMeiomask()) {
			writeJqueryExtra("meio.mask", false);
			String la = this.getBootstrapLang();
			if ("ja".equals(la)) {
				writeStaticJs("/jquery/extras/locales/jquery.ui.meio.mask." + jsstr(la));
			}
		}

		if (tag.isMousewheel()) {
			writeJqueryExtra("mousewheel", false);
		}

		if (tag.isTablesorter()) {
			writeJqueryExtra("tablesorter", true);
		}
	}
	
	private void writeJqueryExtra(String name, boolean css) throws IOException {
		String path = "/jquery/extras/css/jquery.ui." + name;
		if (tag.useCdn()) {
			writePandaCdnCss(path);
			writePandaCdnJs(path);
		}
		else {
			writeStaticCss(path);
			writeStaticJs(path);
		}
	}
	
	private void writeBootstrap() throws IOException {
		boolean bs = tag.isBootstrap();
		if (css && bs) {
			if (tag.useCdn()) {
				writeCdnCss("//netdna.bootstrapcdn.com/bootstrap/" 
						+ BOOTSTRAP_VERSION 
						+ "/css/bootstrap"
						+ debug() + ".css");
				writeCdnCss("//netdna.bootstrapcdn.com/font-awesome/" 
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
			if (tag.useCdn()) {
				writeCdnJs("//netdna.bootstrapcdn.com/bootstrap/" 
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
		boolean bsp = tag.isBsplugins();
		if (css && bsp) {
			if (tag.useCdn()) {
				writePandaCdnCss("/bootstrap3/css/bootstrap-plugins");
			}
			else {
				writeStaticCss("/bootstrap3/css/bootstrap-plugins");
			}
		}
		
		if (js && bsp) {
			if (tag.useCdn()) {
				writePandaCdnJs("/bootstrap3/js/bootstrap-plugins");
			}
			else {
				writeStaticJs("/bootstrap3/js/bootstrap-plugins");
			}
			
			String la = getBootstrapLang();
			if (tag.isI18n()) {
				if (tag.useCdn()) {
					writePandaCdnJs("/bootstrap3/js/bootstrap-plugins-i18n");
				}
				else {
					writeStaticJs("/bootstrap3/js/bootstrap-plugins-i18n");
				}
			}
			else if (!"en".equals(la)) {
				if (tag.useCdn()) {
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
		if (Strings.isEmpty(lang)) {
			Locale locale = tag.getLocale();
			lang = locale.getLanguage();
			if ("fa".equals(lang)) {
				lang += "-IR";
			}
			else if ("pt".equals(lang)) {
				lang += "-BR";
			}
			else if ("zh".equals(lang)) {
				if ("TW".equalsIgnoreCase(locale.getCountry())
						|| "HK".equalsIgnoreCase(locale.getCountry())) {
					lang += "-TW";
				}
				else {
					lang += "-CN";
				}
			}
		}
		return lang;
	}

	private void writePanda() throws IOException {
		boolean panda = tag.isPanda();
		if (css && panda) {
			if (tag.useCdn()) {
				writePandaCdnCss("/panda/css/panda");
			}
			else {
				writeStaticCss("/panda/css/panda");
			}
		}
		if (js && panda) {
			if (tag.useCdn()) {
				writePandaCdnJs("/panda/js/panda");
			}
			else {
				writeStaticJs("/panda/js/panda");
			}
		}
	}
	
	private String debug() {
		 return (tag.useDebug() ? "" : ".min");
	}
}
