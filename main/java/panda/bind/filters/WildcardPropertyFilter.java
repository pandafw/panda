package panda.bind.filters;

import java.util.ArrayList;
import java.util.List;

import panda.bind.PropertyFilter;
import panda.lang.Texts;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class WildcardPropertyFilter implements PropertyFilter {

	private List<String> filterNames;
	private List<String> filterTypes;
	
	/**
	 * Constructor
	 */
	public WildcardPropertyFilter() {
	}

	/**
	 * @return the filterNames
	 */
	public List<String> getFilterNames() {
		if (filterNames == null) {
			filterNames = new ArrayList<String>();
		}
		return filterNames;
	}

	/**
	 * @param filterNames the filterNames to set
	 */
	public void setFilterNames(List<String> filterNames) {
		this.filterNames = filterNames;
	}

	/**
	 * @return the filterTypes
	 */
	public List<String> getFilterTypes() {
		if (filterTypes == null) {
			filterTypes = new ArrayList<String>();
		}
		return filterTypes;
	}

	/**
	 * @param filterTypes the filterTypes to set
	 */
	public void setFilterTypes(List<String> filterTypes) {
		this.filterTypes = filterTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean accept(Object source, String name, Object value) {
		if (value == null) {
			return false;
		}
		
		if (filterNames != null) {
			for (String s : filterNames) {
				if (Texts.wildcardMatch(name, s)) {
					return false;
				}
			}
		}
		
		if (filterTypes != null) {
			for (String s : filterTypes) {
				if (Texts.wildcardMatch(value.getClass().getName(), s)) {
					return false;
				}
			}
		}

		return true;
	}
	
}
