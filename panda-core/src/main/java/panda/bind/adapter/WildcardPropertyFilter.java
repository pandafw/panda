package panda.bind.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.formula.functions.T;

import panda.io.FileNames;
import panda.lang.Collections;

public class WildcardPropertyFilter extends AbstractSerializeAdapter<T> {
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
	@Override
	public String adaptPropertyName(T src, String name) {
		if (filterNames != null) {
			for (String s : filterNames) {
				if (FileNames.wildcardMatch(name, s)) {
					return null;
				}
			}
		}
		
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object adaptPropertyValue(T src, Object value) {
		if (value == null) {
			return null;
		}
		
		if (Collections.isNotEmpty(filterTypes)) {
			for (String s : filterTypes) {
				if (FileNames.wildcardMatch(value.getClass().getName(), s)) {
					return FILTERED;
				}
			}
		}

		return value;
	}
}
