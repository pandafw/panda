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
	public String adaptPropertyName(T src, String name) {
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object adaptPropertyValue(T src, Object value) {
		return value;
	}
}
