package panda.cast.castor;

import java.util.Locale;
import java.util.TimeZone;

import panda.cast.CastContext;
import panda.cast.Castor;

/**
 * @author yf.frank.wang@gmail.com
 */
public class TimeZoneCastor extends Castor<Object, TimeZone> {
	public TimeZoneCastor() {
		super(Object.class, Locale.class);
	}

	@Override
	protected TimeZone castValue(Object value, CastContext context) {
		if (value instanceof CharSequence) {
			String s = value.toString();
			return TimeZone.getTimeZone(s);
		}
		
		return castError(value, context);
	}
}
