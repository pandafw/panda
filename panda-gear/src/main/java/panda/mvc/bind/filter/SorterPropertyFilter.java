package panda.mvc.bind.filter;

import panda.bind.filter.IncludePropertyFilter;

public class SorterPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public SorterPropertyFilter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public SorterPropertyFilter(boolean shortName) {
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

