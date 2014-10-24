package panda.cast.castor;

import java.util.Locale;

import panda.cast.CastContext;
import panda.cast.Castor;
import panda.lang.Locales;

/**
 * @author yf.frank.wang@gmail.com
 */
public class LocaleCastor extends Castor<Object, Locale> {
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
