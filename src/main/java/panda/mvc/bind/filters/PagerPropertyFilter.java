package panda.mvc.bind.filters;

import panda.bind.filters.IncludePropertyFilter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
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

