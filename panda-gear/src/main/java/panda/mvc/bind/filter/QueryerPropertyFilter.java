package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

/**
 * 
 *
 */
public class QueryerPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public QueryerPropertyFilter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public QueryerPropertyFilter(boolean shortName) {
		if (shortName) {
			includes.add("n");
			includes.add("m");
			includes.add("p");
			includes.add("s");
			includes.add("fs");
		}
		else {
			includes.add("name");
			includes.add("method");
			includes.add("pager");
			includes.add("sorter");
			includes.add("filters");
		}
	}
}

