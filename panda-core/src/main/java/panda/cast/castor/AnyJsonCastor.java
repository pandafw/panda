package panda.cast.castor;

import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;

import panda.bind.json.Jsons;
import panda.lang.Charsets;

/**
 * 
 * @param <T> target type
 */
public abstract class AnyJsonCastor<T> extends AnyObjectCastor<T> {
	public AnyJsonCastor(Type toType) {
		super(toType);
	}

	@Override
	protected Object prepare(Object value) {
		if (value instanceof CharSequence) {
			return Jsons.fromJson((CharSequence)value, toType);
		}
		if (value instanceof Reader) {
			return Jsons.fromJson((Reader)value, toType);
		}
		if (value instanceof InputStream) {
			return Jsons.fromJson((InputStream)value, Charsets.UTF_8, toType);
		}
		return value;
	}
}
