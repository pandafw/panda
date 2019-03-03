package panda.io.filter;

import java.io.File;
import java.util.List;

import panda.io.IOCase;

/**
 * Filters file paths for a certain prefix.
 * <p>
 * For example, to print all files and directories in the current directory whose name starts with
 * <code>Test</code>:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(new PrefixPathFilter(&quot;Test&quot;));
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @see FileFilters#prefixPathFilter(String...)
 * @see FileFilters#prefixPathFilter(IOCase, String...)
 */
public class PrefixPathFilter extends AbstractFileFilter {

	/** The filename prefixes to search for */
	private final String[] prefixes;

	/** Whether the comparison is case sensitive. */
	private final IOCase caseSensitivity;

	/**
	 * Constructs a new Prefix file filter for a single prefix.
	 * 
	 * @param prefix the prefix to allow, must not be null
	 * @throws IllegalArgumentException if the prefix is null
	 */
	public PrefixPathFilter(String prefix) {
		this(prefix, IOCase.SENSITIVE);
	}

	/**
	 * Constructs a new Prefix file filter for a single prefix specifying case-sensitivity.
	 * 
	 * @param prefix the prefix to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the prefix is null
	 */
	public PrefixPathFilter(String prefix, IOCase caseSensitivity) {
		if (prefix == null) {
			throw new IllegalArgumentException("The prefix must not be null");
		}
		this.prefixes = new String[] { prefix };
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Constructs a new Prefix file filter for any of an array of prefixes.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param prefixes the prefixes to allow, must not be null
	 * @throws IllegalArgumentException if the prefix array is null
	 */
	public PrefixPathFilter(String[] prefixes) {
		this(prefixes, IOCase.SENSITIVE);
	}

	/**
	 * Constructs a new Prefix file filter for any of an array of prefixes specifying
	 * case-sensitivity.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param prefixes the prefixes to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the prefix is null
	 */
	public PrefixPathFilter(String[] prefixes, IOCase caseSensitivity) {
		if (prefixes == null) {
			throw new IllegalArgumentException("The array of prefixes must not be null");
		}
		this.prefixes = new String[prefixes.length];
		System.arraycopy(prefixes, 0, this.prefixes, 0, prefixes.length);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Constructs a new Prefix file filter for a list of prefixes.
	 * 
	 * @param prefixes the prefixes to allow, must not be null
	 * @throws IllegalArgumentException if the prefix list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public PrefixPathFilter(List<String> prefixes) {
		this(prefixes, IOCase.SENSITIVE);
	}

	/**
	 * Constructs a new Prefix file filter for a list of prefixes specifying case-sensitivity.
	 * 
	 * @param prefixes the prefixes to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the prefix list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public PrefixPathFilter(List<String> prefixes, IOCase caseSensitivity) {
		if (prefixes == null) {
			throw new IllegalArgumentException("The list of prefixes must not be null");
		}
		this.prefixes = prefixes.toArray(new String[prefixes.size()]);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Checks to see if the filename starts with the prefix.
	 * 
	 * @param file the File to check
	 * @return true if the file path starts with one of our prefixes
	 */
	@Override
	public boolean accept(File file) {
		String path = file.getPath();
		for (String prefix : this.prefixes) {
			if (caseSensitivity.checkStartsWith(path, prefix)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Provide a String representaion of this file filter.
	 * 
	 * @return a String representaion
	 */
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(super.toString());
		buffer.append("(");
		if (prefixes != null) {
			for (int i = 0; i < prefixes.length; i++) {
				if (i > 0) {
					buffer.append(",");
				}
				buffer.append(prefixes[i]);
			}
		}
		buffer.append(")");
		return buffer.toString();
	}

}
