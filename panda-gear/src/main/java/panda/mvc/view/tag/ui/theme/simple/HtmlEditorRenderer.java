package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import panda.lang.Arrays;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.HtmlEditor;
import panda.mvc.view.tag.ui.theme.AbstractEndRenderer;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public class HtmlEditorRenderer extends AbstractEndRenderer<HtmlEditor> {
	private static final String SUMMERNOTE_KEY = HtmlEditorRenderer.class.getName() + ".summernote";
	private static final String SUMMERNOTE_VERSION = "0.8.11";
	private static final String SUMMERNOTE_CDN = "http://cdnjs.cloudflare.com/ajax/libs/summernote/" + SUMMERNOTE_VERSION + "/summernote";
	private static final String SUMMERNOTE_ROOT = "/summernote/";
	private static final String SUMMERNOTE_PATH = SUMMERNOTE_ROOT + "summernote";

	private static final Map<String, String> SUMMERNOTE_LANGS = Arrays.toMap(
			"ar",  "AR", 
			"bg",  "BG", 
			"ca",  "ES", 
			"cs",  "CZ", 
			"da",  "DK", 
			"de",  "DE", 
			"el",  "GR", 
			"fa",  "IR", 
			"fi",  "FI", 
			"fr",  "FR", 
			"gl",  "ES", 
			"he",  "IL", 
			"hr",  "HR", 
			"hu",  "HU", 
			"id",  "ID", 
			"it",  "IT", 
			"ja",  "JP", 
			"ko",  "KR", 
			"lt",  "LT", 
			"lt",  "LV", 
			"mn",  "MN", 
			"nb",  "NO", 
			"nl",  "NL", 
			"pl",  "PL", 
			"ro",  "RO", 
			"ru",  "RU", 
			"sk",  "SK", 
			"sl",  "SI", 
			"sr",  "RS", 
			"sv",  "SE", 
			"ta",  "IN", 
			"th",  "TH", 
			"tr",  "TR", 
			"uk",  "UA", 
			"uz",  "UZ", 
			"vi",  "VN"
		);
	private static final String CLEDITOR_KEY = HtmlEditorRenderer.class.getName() + ".cleditor";
	private static final String CLEDITOR_VERSION = "1.4.5";
	private static final String CLEDITOR_PATH = "/cleditor/jquery.cleditor";

	public HtmlEditorRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected void render() throws IOException {
		Attributes attrs = new Attributes();

		attrs.id(tag)
			.name(tag)
			.cols(tag)
			.rows(tag)
			.wrap(tag)
			.disabled(tag)
			.readonly(tag)
			.tabindex(tag)
			.maxlength(tag)
			.tooltip(tag)
			.placeholder(tag)
			.commons(tag)
			.events(tag);

		if ("cleditor".equalsIgnoreCase(tag.getEditor())) {
			attrs.css(this, "p-htmleditor p-cleditor");
			attrs.data("cleditorJs", cleditorJs());
			attrs.dynamics(tag);

			if (!Boolean.TRUE.equals(context.getReq().get(CLEDITOR_KEY))) {
				context.getReq().put(CLEDITOR_KEY, true);
				writeCss(cleditorCss());
			}
		}
		else {
			String lang = getSummerNoteLang();
			attrs.css(this, "p-htmleditor p-summernote");
			attrs.data("summernoteJs", summernoteJs());
			if (Strings.isNotEmpty(lang)) {
				attrs.data("summernoteLang", lang);
				attrs.data("summernoteLangJs", suri(SUMMERNOTE_ROOT + "lang/summernote-" + lang + ".js?v=" + SUMMERNOTE_VERSION));
			}
			attrs.dynamics(tag);

			if (!Boolean.TRUE.equals(context.getReq().get(SUMMERNOTE_KEY))) {
				context.getReq().put(SUMMERNOTE_KEY, true);
				writeCss(summernoteCss());
			}
		}

		stag("textarea", attrs);
		write(formatValue(tag.getValue(), tag.getFormat()));
		etag("textarea");
	}
	
	private String getSummerNoteLang() {
		Locale locale = tag.getLocale();
		String lang = locale.getLanguage();
		String country = Strings.upperCase(locale.getCountry());
		if ("zh".equals(lang)) {
			lang += ("TW".equals(country) || "HK".equals(country)) ? "-TW" : "-CN";
		}
		else if ("es".equals(lang)) {
			lang += "EU".equals(country) ? "-EU" : "-ES";
		}
		else if ("pt".equals(lang)) {
			lang += "BR".equals(country) ? "-BR" : "-ES";
		}
		else {
			country = SUMMERNOTE_LANGS.get(lang);
			lang = country == null ? null : (lang + '-' + country);
		}
		return lang;
	}

	private String summernoteCss() {
		if (tag.useCdn()) {
			return SUMMERNOTE_CDN + ".css";
		}
		return suri(SUMMERNOTE_PATH + ".css?v=" + SUMMERNOTE_VERSION);
	}
	
	private String summernoteJs() {
		if (tag.useCdn()) {
			return SUMMERNOTE_CDN + debug() + ".js";
		}
		return suri(SUMMERNOTE_PATH + debug() + ".js?v=" + SUMMERNOTE_VERSION);
	}

	private String cleditorCss() {
		return suri(CLEDITOR_PATH + ".css?v=" + CLEDITOR_VERSION);
	}
	
	private String cleditorJs() {
		return suri(CLEDITOR_PATH + debug() + ".js?v=" + CLEDITOR_VERSION);
	}

	private String debug() {
		return (tag.useDebug() ? "" : ".min");
	}
}
