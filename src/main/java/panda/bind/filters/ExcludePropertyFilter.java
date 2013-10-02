package panda.bind.filters;

import java.util.ArrayList;
import java.util.List;

import panda.bind.PropertyFilter;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class ExcludePropertyFilter implements PropertyFilter {

	protected List<String> excludes;

	/**
	 * Constructor
	 */
	public ExcludePropertyFilter() {
		excludes = new ArrayList<String>();
	}

	/**
	 * @return the excludes
	 */
	public List<String> getExcludes() {
		return excludes;
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(List<String> excludes) {
		this.excludes = excludes;
	}


	/**
	 * @param source the owner of the property
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return adapter type
	 */
	public boolean accept(Object source, String name, Object value) {
		if (excludes != null) {
			if (excludes.contains(name)) {
				return false;
			}
		}
		return true;
	}
}
