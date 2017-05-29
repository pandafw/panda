package panda.cast.castor;

import java.util.Locale;

import panda.cast.CastContext;
import panda.lang.Locales;

/**
 */
public class LocaleCastor extends AnySingleCastor<Locale> {
	public LocaleCastor() {
		super(Locale.class);
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
