package panda.bind.adapter;

import panda.bind.SerializeAdapter;

public class AbstractSerializeAdapter<T> implements SerializeAdapter<T> {
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object adaptSource(T src) {
		return src;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String acceptProperty(T src, String name) {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object filterProperty(T src, Object value) {
		return value;
	}
}
