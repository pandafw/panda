package panda.cast.castor;

import java.util.TimeZone;

import panda.cast.AbstractCastor;
import panda.cast.CastContext;

/**
 */
public class TimeZoneCastor extends AbstractCastor<Object, TimeZone> {
	public TimeZoneCastor() {
		super(Object.class, TimeZone.class);
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
