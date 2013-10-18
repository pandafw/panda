package panda.io.filter;

import java.io.File;
import java.util.List;

import panda.io.IOCase;

/**
 * Filters filenames for a certain name.
 * <p>
 * For example, to print all files and directories in the current directory whose name is
 * <code>Test</code>:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(new NameFileFilter(&quot;Test&quot;));
 * for (int i = 0; i &lt; files.length; i++) {
 * 	System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @see FileFilters#nameFileFilter(String)
 * @see FileFilters#nameFileFilter(String, IOCase)
 */
public class NameFileFilter extends AbstractFileFilter {

	/** The filenames to search for */
	private final String[] names;
	/** Whether the comparison is case sensitive. */
	private final IOCase caseSensitivity;

	/**
	 * Constructs a new case-sensitive name file filter for a single name.
	 * 
	 * @param name the name to allow, must not be null
	 * @throws IllegalArgumentException if the name is null
	 */
	public NameFileFilter(String name) {
		this(name, null);
	}

	/**
	 * Construct a new name file filter specifying case-sensitivity.
	 * 
	 * @param name the name to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the name is null
	 */
	public NameFileFilter(String name, IOCase caseSensitivity) {
		if (name == null) {
			throw new IllegalArgumentException("The wildcard must not be null");
		}
		this.names = new String[] { name };
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Constructs a new case-sensitive name file filter for an array of names.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param names the names to allow, must not be null
	 * @throws IllegalArgumentException if the names array is null
	 */
	public NameFileFilter(String[] names) {
		this(names, null);
	}

	/**
	 * Constructs a new name file filter for an array of names specifying case-sensitivity.
	 * <p>
	 * The array is not cloned, so could be changed after constructing the instance. This would be
	 * inadvisable however.
	 * 
	 * @param names the names to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the names array is null
	 */
	public NameFileFilter(String[] names, IOCase caseSensitivity) {
		if (names == null) {
			throw new IllegalArgumentException("The array of names must not be null");
		}
		this.names = new String[names.length];
		System.arraycopy(names, 0, this.names, 0, names.length);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	/**
	 * Constructs a new case-sensitive name file filter for a list of names.
	 * 
	 * @param names the names to allow, must not be null
	 * @throws IllegalArgumentException if the name list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public NameFileFilter(List<String> names) {
		this(names, null);
	}

	/**
	 * Constructs a new name file filter for a list of names specifying case-sensitivity.
	 * 
	 * @param names the names to allow, must not be null
	 * @param caseSensitivity how to handle case sensitivity, null means case-sensitive
	 * @throws IllegalArgumentException if the name list is null
	 * @throws ClassCastException if the list does not contain Strings
	 */
	public NameFileFilter(List<String> names, IOCase caseSensitivity) {
		if (names == null) {
			throw new IllegalArgumentException("The list of names must not be null");
		}
		this.names = names.toArray(new String[names.size()]);
		this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE : caseSensitivity;
	}

	// -----------------------------------------------------------------------
	/**
	 * Checks to see if the filename matches.
	 * 
	 * @param file the File to check
	 * @return true if the filename matches
	 */
	@Override
	public boolean accept(File file) {
		String name = file.getName();
		for (String name2 : this.names) {
			if (caseSensitivity.checkEquals(name, name2)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks to see if the filename matches.
	 * 
	 * @param dir the File directory (ignored)
	 * @param name the filename
	 * @return true if the filename matches
	 */
	@Override
	public boolean accept(File dir, String name) {
		for (String name2 : names) {
			if (caseSensitivity.checkEquals(name, name2)) {
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
		if (names != null) {
			for (int i = 0; i < names.length; i++) {
				if (i > 0) {
					buffer.append(",");
				}
				buffer.append(names[i]);
			}
		}
		buffer.append(")");
		return buffer.toString();
	}

}
