package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Filter;

public class FilterAdapter extends IncludePropertyFilter<Filter> {
	private static final FilterAdapter s = new FilterAdapter(true);
	private static final FilterAdapter i = new FilterAdapter(false);

	public static FilterAdapter s() {
		return s;
	}
	
	public static FilterAdapter i() {
		return i;
	}
	
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

