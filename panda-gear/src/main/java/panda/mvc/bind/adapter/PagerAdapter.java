package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Pager;

public class PagerAdapter extends IncludePropertyFilter<Pager> {
	/**
	 * Constructor
	 */
	public PagerAdapter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public PagerAdapter(boolean shortName) {
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

