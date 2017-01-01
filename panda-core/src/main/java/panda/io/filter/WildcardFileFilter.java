package panda.io.filter;

import java.io.File;
import java.util.List;

import panda.io.FileNames;
import panda.io.IOCase;

/**
 * Filters files using the supplied wildcards.
 * <p>
 * This filter selects files and directories based on one or more wildcards. Testing is
 * case-sensitive by default, but this can be configured.
 * <p>
 * The wildcard matcher uses the characters '?' and '*' to represent a single or multiple wildcard
 * characters. This is the same as often found on Dos/Unix command lines. The extension check is
 * case-sensitive by . See {@link FileNames#wildcardMatchOnSystem} for more information.
 * <p>
 * For example:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * FileFilter fileFilter = new WildcardFileFilter(&quot;*test*.java&tilde;*&tilde;&quot;);
 * File[] files = dir.listFiles(fileFilter);
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 */
public class WildcardFileFilter extends AbstractFileFilter {

	/** The wildcards that will be used to match filenames. */
	private final String[] wildcards;
	/** Whether the comparison is case sensitive. */
	private final IOCase caseSensitivity;

	/**
	 * Construct a new case-sensitive wildcard filter for a single wildcard.
	 * 
	 * @param wildcard the wildcard to match
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public WildcardFileFilter(String wildcard) {
		this(wildcard, null);
	}

	/**
	 * Construct a new wildcard filter for a single wildcard specifying case-sensitivity.
	 * 
	 * @param wildcard the wildcard to match, not null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the pattern is null
	 */
	public WildcardFileFilter(String wildcard, IOCase caseSensitivity) {
		if (wildcard == null) {
			throw new IllegalArgumentException("The wildcard must not be null");
		}
		this.wildcards = new String[] { wildcard };
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Construct a new case-sensitive wildcard filter for an array of wildcards.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param wildcards the array of wildcards to match
	 * @throws IllegalArgumentException if the pattern array is null
	 */
	public WildcardFileFilter(String[] wildcards) {
		this(wildcards, null);
	}

	/**
	 * Construct a new wildcard filter for an array of wildcards specifying case-sensitivity.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param wildcards the array of wildcards to match, not null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the pattern array is null
	 */
	public WildcardFileFilter(String[] wildcards, IOCase caseSensitivity) {
		if (wildcards == null) {
			throw new IllegalArgumentException("The wildcard array must not be null");
		}
		this.wildcards = new String[wildcards.length];
		System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Construct a new case-sensitive wildcard filter for a list of wildcards.
	 * 
	 * @param wildcards the list of wildcards to match, not null
	 * @throws IllegalArgumentException if the pattern list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public WildcardFileFilter(List<String> wildcards) {
		this(wildcards, null);
	}

	/**
	 * Construct a new wildcard filter for a list of wildcards specifying case-sensitivity.
	 * 
	 * @param wildcards the list of wildcards to match, not null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the pattern list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public WildcardFileFilter(List<String> wildcards, IOCase caseSensitivity) {
		if (wildcards == null) {
			throw new IllegalArgumentException("The wildcard list must not be null");
		}
		this.wildcards = wildcards.toArray(new String[wildcards.size()]);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	// -----------------------------------------------------------------------
	/**
	 * Checks to see if the filename matches one of the wildcards.
	 * 
	 * @param dir the file directory (ignored)
	 * @param name the filename
	 * @return true if the filename matches one of the wildcards
	 */
	@Override
	public boolean accept(File dir, String name) {
		for (String wildcard : wildcards) {
			if (FileNames.wildcardMatch(name, wildcard, caseSensitivity)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the filename matches one of the wildcards.
	 * 
	 * @param file the file to check
	 * @return true if the filename matches one of the wildcards
	 */
	@Override
	public boolean accept(File file) {
		String name = file.getName();
		for (String wildcard : wildcards) {
			if (FileNames.wildcardMatch(name, wildcard, caseSensitivity)) {
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
		if (wildcards != null) {
			for (int i = 0; i < wildcards.length; i++) {
				if (i > 0) {
					buffer.append(",");
				}
				buffer.append(wildcards[i]);
			}
		}
		buffer.append(")");
		return buffer.toString();
	}

}
