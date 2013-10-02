package panda.bind.filters;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class QueryPropertyFilter extends IncludePropertyFilter {
	/**
	 * Constructor
	 */
	public QueryPropertyFilter() {
		this(false);
	}

	/**
	 * Constructor
	 * @param shortName shortName
	 */
	public QueryPropertyFilter(boolean shortName) {
		if (shortName) {
			includes.add("n");
			includes.add("m");
			includes.add("fs");
		}
		else {
			includes.add("name");
			includes.add("method");
			includes.add("filters");
		}
	}
}

