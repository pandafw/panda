package panda.mvc.view.tag.ui.theme.simple;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import panda.Panda;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.mvc.view.tag.ui.DatePicker;
import panda.mvc.view.tag.ui.theme.Attributes;
import panda.mvc.view.tag.ui.theme.RenderingContext;

public abstract class AbstractDatePickerRenderer<T extends DatePicker> extends AbstractTextFieldRenderer<T> {
	private static final String DTP_CSS_KEY = AbstractDatePickerRenderer.class.getName() + ".css";
	private static final String DTP_ROOT = "/datetimepicker/";
	private static final String DTP_PATH = DTP_ROOT + "bootstrap-datetimepicker";
	private static final String DTP_VERSION = Panda.VERSION;
	
	private static final Set<String> LANGS =
			Arrays.toSet("bg", "cs", "da", "de", "es", "fa", "fi", "fo", "fr", "hr", "hu", "id", "is", "it", "ja", "kr",
				"lt", "lv", "ms", "nb", "nl", "pl", "pt", "ro", "rs", "ru", "sk", "sl", "sv", "th", "tr");

	public AbstractDatePickerRenderer(RenderingContext context) {
		super(context);
	}

	@Override
	protected Map<String, String> getDatas() {
		return Arrays.toMap("format", tag.getPattern());
	}

	protected String getDatetimePickerLang() {
		Locale locale = tag.getLocale();
		String lang = locale.getLanguage();

		if ("zh".equals(lang)) {
			String country = Strings.upperCase(locale.getCountry());
			lang += ("TW".equals(country) || "HK".equals(country)) ? "-TW" : "-CN";
		}
		else {
			lang = LANGS.contains(lang) ? lang : null;
		}
		return lang;
	}

	@Override
	protected void renderHeader() throws IOException {
		if (!Boolean.TRUE.equals(context.getReq().get(DTP_CSS_KEY))) {
			context.getReq().put(DTP_CSS_KEY, true);
			writeCss(datetimepickerCss());
		}

		Attributes attrs = new Attributes();
		
		attrs.cssClass("p-" + getName(), "input-group");

		attrs.data("datetimepickerJs", datetimepickerJs());

		addDatetimepickerOptions(attrs);

		String lang = getDatetimePickerLang();
		if (Strings.isNotEmpty(lang)) {
			attrs.data("language", lang);
			attrs.data("datetimepickerLangJs", suri(DTP_ROOT + "lang/bootstrap-datetimepicker." + lang + ".js?v=" + DTP_VERSION));
		}
		stag("div", attrs);
	}

	private String debug() {
		return (tag.useDebug() ? "" : ".min");
	}

	private String datetimepickerCss() {
		return suri(DTP_PATH + ".css?v=" + DTP_VERSION);
	}
	
	private String datetimepickerJs() {
		return suri(DTP_PATH + debug() + ".js?v=" + DTP_VERSION);
	}

	protected void addDatetimepickerOptions(Attributes attrs) {
	}
}
