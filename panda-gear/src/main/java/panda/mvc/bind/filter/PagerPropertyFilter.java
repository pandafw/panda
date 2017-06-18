package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

public class PagerPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public PagerPropertyFilter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public PagerPropertyFilter(boolean shortName) {
		if (shortName) {
			includes.add("s");
			includes.add("l");
			includes.add("p");
			includes.add("t");
		}
		else {
			includes.add("start");
			includes.add("limit");
			includes.add("page");
			includes.add("total");
		}
	}
}

