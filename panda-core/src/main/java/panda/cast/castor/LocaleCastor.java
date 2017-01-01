package panda.cast.castor;

import java.util.Locale;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;
import panda.lang.Locales;

/**
 */
public class LocaleCastor extends AbstractCastor<Object, Locale> {
	public LocaleCastor() {
		super(Object.class, Locale.class);
	}

	@Override
	protected Locale castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			return Locales.parseLocale(s);
		}
		
		return castError(value, context);
	}
}
