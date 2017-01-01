package panda.io.filter;

import java.io.File;
import java.util.regex.Pattern;

import panda.io.IOCase;

/**
 * Filters files using supplied regular expression(s).
 * <p/>
 * See java.util.regex.Pattern for regex matching rules
 * <p/>
 * <p/>
 * e.g.
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * FileFilter fileFilter = new RegexFileFilter(&quot;&circ;.*[tT]est(-\\d+)?\\.java$&quot;);
 * File[] files = dir.listFiles(fileFilter);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 */
public class RegexFileFilter extends AbstractFileFilter {

	/** The regular expression pattern that will be used to match filenames */
	private final Pattern pattern;

	/**
	 * Construct a new regular expression filter.
	 * 
	 * @param pattern regular string expression to match
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public RegexFileFilter(String pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}

		this.pattern = Pattern.compile(pattern);
	}

	/**
	 * Construct a new regular expression filter with the specified flags case sensitivity.
	 * 
	 * @param pattern regular string expression to match
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public RegexFileFilter(String pattern, IOCase caseSensitivity) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}
		int flags = 0;
		if (caseSensitivity != null && !caseSensitivity.isCaseSensitive()) {
			flags = Pattern.CASE_INSENSITIVE;
		}
		this.pattern = Pattern.compile(pattern, flags);
	}

	/**
	 * Construct a new regular expression filter with the specified flags.
	 * 
	 * @param pattern regular string expression to match
	 * @param flags pattern flags - e.g. {@link Pattern#CASE_INSENSITIVE}
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public RegexFileFilter(String pattern, int flags) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}
		this.pattern = Pattern.compile(pattern, flags);
	}

	/**
	 * Construct a new regular expression filter for a compiled regular expression
	 * 
	 * @param pattern regular expression to match
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public RegexFileFilter(Pattern pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("Pattern is missing");
		}

		this.pattern = pattern;
	}

	/**
	 * Checks to see if the filename matches one of the regular expressions.
	 * 
	 * @param dir the file directory (ignored)
	 * @param name the filename
	 * @return true if the filename matches one of the regular expressions
	 */
	@Override
	public boolean accept(File dir, String name) {
		return pattern.matcher(name).matches();
	}

}
