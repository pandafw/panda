package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Locale;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.URLHelper;
import panda.net.http.UserAgent;

public class LinkRenderer extends AbstractEndRenderer<Link> {
	private static final String JQUERY1_VERSION = "1.12.4";
	private static final String JQUERY2_VERSION = "2.2.4";
	private static final String JQUERY3_VERSION = "3.3.1";
	private static final String JQUERY_CDN_BASE = "//code.jquery.com/jquery-";
	private static final String JQUERY_BIND_BASE = "/jquery/js/jquery-";
	private static final String JQUERY1_CDN_PATH = JQUERY_CDN_BASE + JQUERY1_VERSION;
	private static final String JQUERY2_CDN_PATH = JQUERY_CDN_BASE + JQUERY2_VERSION;
	private static final String JQUERY3_CDN_PATH = JQUERY_CDN_BASE + JQUERY3_VERSION;
	private static final String JQUERY1_BIND_PATH = JQUERY_BIND_BASE + JQUERY1_VERSION;
	private static final String JQUERY2_BIND_PATH = JQUERY_BIND_BASE + JQUERY2_VERSION;
	private static final String JQUERY3_BIND_PATH = JQUERY_BIND_BASE + JQUERY3_VERSION;

	private static final String BOOTSTRAP3_VERSION = "3.3.7";
	private static final String FONTAWESOME4_VERSION = "4.7.0";
	
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
			writeBootstrap();
			writeExtras();
			writePanda();
			writeStyleSheets();
		}

		js = tag.isJs();
		if (js) {
			css = false;
			writeJquery();
			writeJqueryPlugins();
			writeBootstrap();
			writeExtras();
			writeRespondJs();
			writePanda();
			writeJscripts();
		}
	}

	private void writeJquery() throws IOException {
		if (js) {
			int v = tag.isJquery3() ? 3 : (tag.isJquery2() ? 2 : (tag.isJquery1() ? 1 : 0));
			if (v == 0) {
				UserAgent ua = context.getUserAgent();
				if (ua.isMsie()) {
					int m = ua.getMajorVersion(UserAgent.MSIE);
					v = m < 9 ? 1 : 2;
				}
				else {
					v = 2;
				}
			}

			switch (v) {
			case 3:
				if (tag.getCdn()) {
					writeCdnJs(JQUERY3_CDN_PATH);
				}
				else {
					writeStaticJs(JQUERY3_BIND_PATH);
				}
				break;
			case 2:
				if (tag.getCdn()) {
					writeCdnJs(JQUERY2_CDN_PATH);
				}
				else {
					writeStaticJs(JQUERY2_BIND_PATH);
				}
				break;
			case 1:
				if (tag.getCdn()) {
					writeCdnJs(JQUERY1_CDN_PATH);
				}
				else {
					writeStaticJs(JQUERY1_BIND_PATH);
				}
				break;
			}
		}
	}

	private void writeJqueryPlugins() throws IOException {
		if (tag.isJqplugins()) {
			writeStaticCss("/jquery/css/jquery-plugins");
			writeStaticJs("/jquery/js/jquery-plugins");
		}
	}

	private void writeExtras() throws IOException {
		if (tag.isExtras()) {
			writeExtra("extras", true);

			String la = getBootstrapLang();
			if ("ja".equals(la) || la.startsWith("zh")) {
				writeStaticJs("/extras/i18n/extras." + la);
			}
		}
		
		if (tag.isHammer()) {
			writeExtra("jquery.ui.hammer", false);
		}
		
		if (tag.isLightbox()) {
			writeExtra("jquery.ui.lightbox", true);

			String la = getBootstrapLang();
			if ("ja".equals(la) || la.startsWith("zh")) {
				writeStaticJs("/extras/i18n/jquery.ui.lightbox." + la);
			}
		}

		if (tag.isMeiomask()) {
			writeExtra("jquery.ui.meio.mask", false);

			String la = getBootstrapLang();
			if ("ja".equals(la)) {
				writeStaticJs("/extras/i18n/jquery.ui.meio.mask." + la);
			}
		}

		if (tag.isMousewheel()) {
			writeExtra("jquery.ui.mousewheel", false);
		}

		if (tag.isTablesorter()) {
			writeExtra("jquery.ui.tablesorter", true);
		}
		
		if (tag.isSwitch()) {
			writeExtra("bootstrap-switch", true);
		}
	}
	
	private void writeExtra(String name, boolean css) throws IOException {
		if (css) {
			writeStaticCss("/extras/css/" + name);
		}
		writeStaticJs("/extras/js/" + name);
	}
	
	private void writeBootstrap() throws IOException {
		boolean bs = tag.isBootstrap();
		if (css && bs) {
			if (tag.useCdn()) {
				writeCdnCss("//netdna.bootstrapcdn.com/bootstrap/" 
						+ BOOTSTRAP3_VERSION 
						+ "/css/bootstrap");
				writeCdnCss("//netdna.bootstrapcdn.com/font-awesome/" 
						+ FONTAWESOME4_VERSION 
						+ "/css/font-awesome");
			}
			else {
				writeStaticCss("/bootstrap3/css/bootstrap");
				writeStaticCss("/font-awesome/css/font-awesome");
			}
		}
		if (js && bs) {
			if (tag.useCdn()) {
				writeCdnJs("//netdna.bootstrapcdn.com/bootstrap/" 
						+ BOOTSTRAP3_VERSION 
						+ "/js/bootstrap");
			}
			else {
				writeStaticJs("/bootstrap3/js/bootstrap");
			}
		}
	}

	private String getBootstrapLang() {
		if (Strings.isEmpty(lang)) {
			Locale locale = tag.getLocale();
			lang = locale.getLanguage();
			if ("zh".equals(lang)) {
				String country = Strings.upperCase(locale.getCountry());
				lang += ("TW".equals(country) || "HK".equals(country)) ? "_TW" : "_CN";
			}
		}
		return lang;
	}

	private void writePanda() throws IOException {
		boolean panda = tag.isPanda();
		if (css && panda) {
			writeStaticCss("/panda/css/panda");
		}
		if (js && panda) {
			writeStaticJs("/panda/js/panda");
		}
	}
	
	private void writeRespondJs() throws IOException {
		if (js && tag.isRespondjs()) {
			UserAgent ua = context.getUserAgent();
			if (ua.isMsie() && ua.getMajorVersion(UserAgent.MSIE) < 9) {
				if (tag.useCdn()) {
					writeCdnJs("//cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond");
				}
				else {
					writeStaticJs("/respondjs/respondjs");
				}
			}
		}
	}
	
	private void writeStyleSheets() throws IOException {
		if (js && Collections.isNotEmpty(tag.getStylesheets())) {
			for (String css : tag.getStylesheets()) {
				writeCss(css + vquery());
			}
		}
	}
	
	private void writeJscripts() throws IOException {
		if (js && Collections.isNotEmpty(tag.getJscripts())) {
			for (String js : tag.getJscripts()) {
				writeJs(js + vquery());
			}
		}
	}

	//-------------------------------
	private String debug() {
		return (tag.useDebug() ? "" : ".min");
	}

	protected void writeJscript(String jsc) throws IOException {
		if (js) {
			writeJsc(jsc);
		}
	}

	protected void writeCdnJs(String jsl) throws IOException {
		writeCdnJs(jsl, true);
	}
	
	protected void writeCdnJs(String jsl, boolean debug) throws IOException {
		if (js) {
			writeJs(jsl + (debug ? debug() : "") + ".js");
		}
	}
	
	protected void writeCdnCss(String cssl) throws IOException {
		if (css) {
			writeCss(cssl + debug() + ".css");
		}
	}

	protected void writeStaticJs(String jsl) throws IOException {
		writeStaticJs(jsl, true);
	}
	
	protected void writeStaticJs(String jsl, boolean debug) throws IOException {
		if (js) {
			writeJs(suri(jsl + (debug ? debug() : "") + ".js"));
		}
	}

	protected void writeStaticCss(String cssl) throws IOException {
		if (css) {
			writeCss(suri(cssl + debug() + ".css"));
		}
	}

	private String vquery() {
		if (Strings.isEmpty(tag.getVersion())) {
			return Strings.EMPTY;
		}
		
		return "?v=" + URLHelper.encodeURL(tag.getVersion());
	}

	@Override
	protected String suri(String uri) {
		return super.suri(uri + vquery());
	}
}
