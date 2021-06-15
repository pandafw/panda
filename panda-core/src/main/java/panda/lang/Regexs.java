package panda.lang;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

public class Regexs {
	public static final String REGEX_URL = "(https?://)?([\\w\\-]+\\.)+([a-zA-Z]{2,})(/[\\w\\.\\-\\+&%=\\?]*)*";
	public static final String REGEX_EMAIL = "([\\w\\.\\+\\-]+)@([\\w\\-]+\\.)+([a-zA-Z]{2,})";
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

	public static List<Pattern> compiles(List<String> strings) {
		return compiles(strings, 0);
	}
	
	public static List<Pattern> compiles(List<String> strings, int flags) {
		if (strings == null) {
			return null;
		}

		List<Pattern> ps = new ArrayList<Pattern>();
		if (Collections.isNotEmpty(strings)) {
			for (String s : strings) {
				if (Strings.isNotEmpty(s)) {
					ps.add(Pattern.compile(s, flags));
				}
			}
		}
		return ps;
	}
	
	public static boolean matches(List<Pattern> ps, String str) {
		if (Collections.isNotEmpty(ps)) {
			for (Pattern p : ps) {
				if (p.matcher(str).matches()) {
					return true;
				}
			}
		}
		return false;
	}
}
