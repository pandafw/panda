package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

/**
 */
public class FileItemPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 * @param shortName shortName
	 * 
	 */
	public FileItemPropertyFilter() {
		includes.add("id");
		includes.add("name");
		includes.add("size");
		includes.add("date");
		includes.add("contentType");
		includes.add("exists");
		includes.add("temporary");
	}
}

