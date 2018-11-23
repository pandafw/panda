package panda.bind.adapter;

import java.util.ArrayList;
import java.util.List;

public class IncludePropertyFilter<T> extends AbstractSerializeAdapter<T> {

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
	 * {@inheritDoc}
	 */
	@Override
	public String adaptPropertyName(T src, String name) {
		if (includes != null) {
			if (includes.contains(name)) {
				return name;
			}
		}
		return null;
	}
}
