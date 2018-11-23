package panda.mvc.bind.adapter;

import panda.bind.adapter.IncludePropertyFilter;
import panda.mvc.bean.Sorter;

public class SorterAdapter extends IncludePropertyFilter<Sorter> {
	private static final SorterAdapter s = new SorterAdapter(true);
	private static final SorterAdapter i = new SorterAdapter(false);

	public static SorterAdapter s() {
		return s;
	}
	
	public static SorterAdapter i() {
		return i;
	}
	
	/**
	 * Constructor
	 */
	public SorterAdapter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public SorterAdapter(boolean shortName) {
		if (shortName) {
			includes.add("c");
			includes.add("d");
		}
		else {
			includes.add("column");
			includes.add("direction");
		}
	}
}

