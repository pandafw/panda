package panda.bind.filters;

import java.util.ArrayList;
import java.util.List;

import panda.bind.PropertyFilter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class IncludePropertyFilter implements PropertyFilter {

	protected List<String> includes;

	/**
	 * Constructor
	 */
	public IncludePropertyFilter() {
		includes = new ArrayList<String>();
	}

	/**
	 * @return the includes
	 */
	public List<String> getIncludes() {
		return includes;
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(List<String> includes) {
		this.includes = includes;
	}

	/**
	 * @param source the owner of the property
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return adapter type
	 */
	public boolean accept(Object source, String name, Object value) {
		if (includes != null) {
			if (includes.contains(name)) {
				return true;
			}
		}
		return false;
	}
}
