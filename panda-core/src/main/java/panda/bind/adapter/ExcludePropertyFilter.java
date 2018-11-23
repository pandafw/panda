package panda.bind.adapter;

import java.util.ArrayList;
import java.util.List;

public class ExcludePropertyFilter<T> extends AbstractSerializeAdapter<T> {

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
	 * {@inheritDoc}
	 */
	@Override
	public String adaptPropertyName(T src, String name) {
		if (excludes != null) {
			if (excludes.contains(name)) {
				return null;
			}
		}
		return name;
	}
}
