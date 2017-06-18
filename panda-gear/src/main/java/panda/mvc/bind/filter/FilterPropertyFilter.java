package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

public class FilterPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public FilterPropertyFilter() {
		this(false);
	}
	
	/**
	 * Constructor
	 * @param shortName shortName
	 * 
	 */
	public FilterPropertyFilter(boolean shortName) {
		if (shortName) {
			includes.add("n");
			includes.add("c");
			includes.add("t");
			includes.add("vs");
		}
		else {
			includes.add("name");
			includes.add("comparator");
			includes.add("type");
			includes.add("values");
		}
	}
}

