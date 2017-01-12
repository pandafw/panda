package panda.lang;

import java.util.Collection;
import java.util.regex.Pattern;

public class Regexs {
	public static final String REGEX_URL = "(https?://)?([\\w\\.\\-]+)\\.([a-z\\.]{2,6})(/[\\w\\.\\-\\+&%=\\?]*)*";
	public static final String REGEX_EMAIL = "([\\w\\.\\+\\-]+)@([\\w\\.\\-]+)\\.([a-zA-Z\\.]{2,6})";
	public static final String REGEX_FILENAME = "[^\\\\/:*?\"<>|]*";

	public static final Pattern PATTERN_URL = Pattern.compile(REGEX_URL, Pattern.CASE_INSENSITIVE);
	public static final Pattern PATTERN_EMAIL = Pattern.compile(REGEX_EMAIL);
	public static final Pattern PATTERN_FILENAME = Pattern.compile(REGEX_FILENAME);

	public static boolean matches(Collection<Pattern> patterns, CharSequence s) {
		if (Collections.isNotEmpty(patterns)) {
			for (Pattern p : patterns) {
				if (p.matcher(s).matches()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public static boolean isURL(String value) {
		return PATTERN_URL.matcher(value).matches();
	}

	public static boolean isEmail(String value) {
		return PATTERN_EMAIL.matcher(value).matches();
	}

	public static boolean isFileName(String value) {
		return PATTERN_FILENAME.matcher(value).matches();
	}

}
