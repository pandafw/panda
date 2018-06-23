package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Filter;

public class FilterAdapter extends IncludePropertyFilter<Filter> {
	/**
	 * Constructor
	 */
	public FilterAdapter() {
		this(false);
	}
	
	/**
	 * Constructor
	 * @param shortName shortName
	 * 
	 */
	public FilterAdapter(boolean shortName) {
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

