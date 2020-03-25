package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;

import panda.Panda;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.mvc.Mvcs;
import panda.mvc.view.tag.ui.Link;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.RenderingContext;
import panda.net.URLHelper;
import panda.net.http.UserAgent;

public class LinkRenderer extends AbstractEndRenderer<Link> {
	private static final String JQUERY1_VERSION = "1.12.4";
	private static final String JQUERY2_VERSION = "2.2.4";
	private static final String JQUERY3_VERSION = "3.4.1";
	private static final String JQUERY_CDN_BASE = "https://code.jquery.com/jquery-";
	private static final String JQUERY1_CDN_PATH = JQUERY_CDN_BASE + JQUERY1_VERSION;
	private static final String JQUERY2_CDN_PATH = JQUERY_CDN_BASE + JQUERY2_VERSION;
	private static final String JQUERY3_CDN_PATH = JQUERY_CDN_BASE + JQUERY3_VERSION;
	private static final String JQUERY_BIND_BASE = "/jquery/js/jquery-";
	private static final String JQUERY1_BIND_PATH = JQUERY_BIND_BASE + JQUERY1_VERSION;
	private static final String JQUERY2_BIND_PATH = JQUERY_BIND_BASE + JQUERY2_VERSION;
	private static final String JQUERY3_BIND_PATH = JQUERY_BIND_BASE + JQUERY3_VERSION;

	private static final String BOOTSTRAP3_VERSION = "3.4.1";
	private static final String BOOTSTRAP4_VERSION = "4.4.1";
	private static final String BOOTSTRAP_CDN_BASE = "https://netdna.bootstrapcdn.com/bootstrap/";
	private static final String BOOTSTRAP3_CDN_CSS_PATH = BOOTSTRAP_CDN_BASE + BOOTSTRAP3_VERSION + "/css/bootstrap";
	private static final String BOOTSTRAP3_CDN_JS_PATH = BOOTSTRAP_CDN_BASE + BOOTSTRAP3_VERSION + "/js/bootstrap";
	private static final String BOOTSTRAP4_CDN_CSS_PATH = BOOTSTRAP_CDN_BASE + BOOTSTRAP4_VERSION + "/css/bootstrap";
	private static final String BOOTSTRAP4_CDN_JS_PATH = BOOTSTRAP_CDN_BASE + BOOTSTRAP4_VERSION + "/js/bootstrap.bundle";
	private static final String BOOTSTRAP3_BIND_CSS_PATH = "/bootstrap3/css/bootstrap";
	private static final String BOOTSTRAP3_BIND_JS_PATH = "/bootstrap3/js/bootstrap";
	private static final String BOOTSTRAP4_BIND_CSS_PATH = "/bootstrap4/css/bootstrap";
	private static final String BOOTSTRAP4_BIND_JS_PATH = "/bootstrap4/js/bootstrap.bundle";
	
	private static final String FONTAWESOME_CDN_BASE = "https://netdna.bootstrapcdn.com/font-awesome/";
	private static final String FONTAWESOME4_VERSION = "4.7.0";

	private static final String FLAGICONCSS_CDN_BASE = "https://cdnjs.cloudflare.com/ajax/libs/flag-icon-css/";
	private static final String FLAGICONCSS_VERSION = "3.4.3";
	
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
			writeFontawesome();
			writeFlagIconCss();
			writePanda();
			writeStyleSheets();
		}

		js = tag.isJs();
		if (js) {
			css = false;
			writeJquery();
			writeJqueryPlugins();
			writeBootstrap();
			writeRespondJs();
			writeNotifyJs();
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
					v = ua.getBrowser().getMajor() < 9 ? 1 : 2;
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
			if (css) {
				if (tag.useCdn()) {
					writeCdnCss(Mvcs.PANDA_CDN + '/' + Panda.VERSION + "/jquery/css/jquery-plugins");
				}
				else {
					writeStaticCss("/jquery/css/jquery-plugins");
				}
			}
			if (js) {
				if (tag.useCdn()) {
					writeCdnJs(Mvcs.PANDA_CDN + '/' + Panda.VERSION + "/jquery/js/jquery-plugins");
				}
				else {
					writeStaticJs("/jquery/js/jquery-plugins");
				}
			}
		}
	}

	private void writeBootstrap() throws IOException {
		if (tag.isBootstrap()) {
			int v = tag.isBootstrap4() ? 4 : 3;

			// TODO: default use bs4
//			int v = tag.isBootstrap4() ? 4 : (tag.isBootstrap3() ? 3 : 0);
//			if (v == 0) {
//				UserAgent ua = context.getUserAgent();
//				if (ua.isMsie()) {
//					v = ua.getBrowser().getMajor() < 10 ? 3 : 4;
//				}
//				else {
//					v = 4;
//				}
//			}

			if (css) {
				switch (v) {
				case 4:
					if (tag.getCdn()) {
						writeCdnCss(BOOTSTRAP4_CDN_CSS_PATH);
					}
					else {
						writeStaticCss(BOOTSTRAP4_BIND_CSS_PATH);
					}
					break;
				case 3:
					if (tag.getCdn()) {
						writeCdnCss(BOOTSTRAP3_CDN_CSS_PATH);
					}
					else {
						writeStaticCss(BOOTSTRAP3_BIND_CSS_PATH);
					}
					break;
				}
			}
			if (js) {
				switch (v) {
				case 4:
					if (tag.getCdn()) {
						writeCdnJs(BOOTSTRAP4_CDN_JS_PATH);
					}
					else {
						writeStaticJs(BOOTSTRAP4_BIND_JS_PATH);
					}
					break;
				case 3:
					if (tag.getCdn()) {
						writeCdnJs(BOOTSTRAP3_CDN_JS_PATH);
					}
					else {
						writeStaticJs(BOOTSTRAP3_BIND_JS_PATH);
					}
					break;
				}
			}
		}
	}

	private void writeFontawesome() throws IOException {
		if (tag.isFontawesome()) {
			if (css) {
				if (tag.useCdn()) {
					writeCdnCss(FONTAWESOME_CDN_BASE + FONTAWESOME4_VERSION + "/css/font-awesome");
				}
				else {
					writeStaticCss("/font-awesome/css/font-awesome");
				}
			}
		}
	}

	private void writeFlagIconCss() throws IOException {
		if (tag.isFlagiconcss()) {
			if (css) {
				if (tag.useCdn()) {
					writeCdnCss(FLAGICONCSS_CDN_BASE + FLAGICONCSS_VERSION + "/css/flag-icon");
				}
				else {
					writeStaticCss("/flag-icon-css/css/flag-icon");
				}
			}
		}
	}

	private void writePanda() throws IOException {
		if (tag.isPanda()) {
			if (css) {
				if (tag.useCdn()) {
					writeCdnCss(Mvcs.PANDA_CDN + '/' + Panda.VERSION + "/panda/css/panda");
				}
				else {
					writeStaticCss("/panda/css/panda");
				}
			}
			if (js) {
				if (tag.useCdn()) {
					writeCdnJs(Mvcs.PANDA_CDN + '/' + Panda.VERSION + "/panda/js/panda");
				}
				else {
					writeStaticJs("/panda/js/panda");
				}
			}
		}
	}
	
	private void writeNotifyJs() throws IOException {
		if (js && tag.isNotifyjs()) {
			if (tag.useCdn()) {
				writeCdnJs(Mvcs.PANDA_CDN + '/' + Panda.VERSION + "/notifyjs/jquery.ui.notify");
			}
			else {
				writeStaticJs("/notifyjs/jquery.ui.notify");
			}
		}
	}
	
	private void writeRespondJs() throws IOException {
		if (js && tag.isRespondjs()) {
			UserAgent ua = context.getUserAgent();
			if (ua.isMsie() && ua.getBrowser().getMajor() < 9) {
				if (tag.useCdn()) {
					writeCdnJs("https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond");
				}
				else {
					writeStaticJs("/respondjs/respond");
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
		if (js) {
			writeJs(jsl + debug() + ".js");
		}
	}
	
	protected void writeCdnCss(String cssl) throws IOException {
		if (css) {
			writeCss(cssl + debug() + ".css");
		}
	}

	protected void writeStaticJs(String jsl) throws IOException {
		if (js) {
			writeJs(suri(jsl + debug() + ".js"));
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
