package panda.util.bind.filters;

import panda.bind.filters.IncludePropertyFilter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
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

