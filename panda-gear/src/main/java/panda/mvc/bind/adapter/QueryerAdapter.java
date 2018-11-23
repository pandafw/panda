package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Queryer;

public class QueryerAdapter extends IncludePropertyFilter<Queryer> {
	private static final QueryerAdapter s = new QueryerAdapter(true);
	private static final QueryerAdapter i = new QueryerAdapter(false);

	public static QueryerAdapter s() {
		return s;
	}
	
	public static QueryerAdapter i() {
		return i;
	}
	
	/**
	 * Constructor
	 */
	public QueryerAdapter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public QueryerAdapter(boolean shortName) {
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

