package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class CompositeQueryPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public CompositeQueryPropertyFilter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public CompositeQueryPropertyFilter(boolean shortName) {
		if (shortName) {
			includes.add("n");
			includes.add("m");
			includes.add("fs");
		}
		else {
			includes.add("name");
			includes.add("method");
			includes.add("filters");
		}
	}
}

