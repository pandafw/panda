package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Queryer;

public class QueryerAdapter extends IncludePropertyFilter<Queryer> {
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

