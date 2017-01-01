package panda.lang.time;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import panda.lang.TimeZones;

public class RollingCalendar extends GregorianCalendar {
	private static final long serialVersionUID = 1L;

	// The code assumes that the following constants are in a increasing
	// sequence.
	public static final int TOP_OF_TROUBLE = -1;
	public static final int TOP_OF_MINUTE = 0;
	public static final int TOP_OF_HOUR = 1;
	public static final int TOP_OF_DAY = 2;
	public static final int TOP_OF_WEEK = 3;
	public static final int TOP_OF_MONTH = 4;

	private int rollingType = TOP_OF_TROUBLE;

	public RollingCalendar() {
		super();
	}

	public RollingCalendar(TimeZone tz, Locale locale) {
		super(tz, locale);
	}


	/**
	 * @return the rollingType
	 */
	public int getRollingType() {
		return rollingType;
	}

	/**
	 * @param rollingType the rollingType to set
	 */
	public void setRollingType(int rollingType) {
		this.rollingType = rollingType;
	}

	public long getNextCheckMillis(long now) {
		return getNextCheckDate(new Date(now)).getTime();
	}

	public long getNextCheckMillis(long now, int amount) {
		return getNextCheckDate(new Date(now), amount).getTime();
	}

	public long getNextCheckMillis(Date now) {
		return getNextCheckDate(now).getTime();
	}

	public long getNextCheckMillis(Date now, int amount) {
		return getNextCheckDate(now, amount).getTime();
	}
	
	public Date getNextCheckDate(Date now) {
		return getNextCheckDate(now, 1);
	}
	
	public Date getNextCheckDate(Date now, int amount) {
		this.setTime(now);

		switch (rollingType) {
		case TOP_OF_MINUTE:
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MINUTE, amount);
			break;
		case TOP_OF_HOUR:
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.HOUR_OF_DAY, amount);
			break;
		case TOP_OF_DAY:
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.DATE, amount);
			break;
		case TOP_OF_WEEK:
			this.set(Calendar.DAY_OF_WEEK, getFirstDayOfWeek());
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.WEEK_OF_YEAR, amount);
			break;
		case TOP_OF_MONTH:
			this.set(Calendar.DATE, 1);
			this.set(Calendar.HOUR_OF_DAY, 0);
			this.set(Calendar.MINUTE, 0);
			this.set(Calendar.SECOND, 0);
			this.set(Calendar.MILLISECOND, 0);
			this.add(Calendar.MONTH, amount);
			break;
		default:
			throw new IllegalStateException("Unknown periodicity type.");
		}
		return getTime();
	}
	
	// This method computes the roll over period by looping over the
	// periods, starting with the shortest, and stopping when the r0 is
	// different from from r1, where r0 is the epoch formatted according
	// the datePattern (supplied by the user) and r1 is the
	// epoch+nextMillis(i) formatted according to datePattern. All date
	// formatting is done in GMT and not local format because the test
	// logic is based on comparisons relative to 1970-01-01 00:00:00
	// GMT (the epoch).
	public void setRollingPattern(String datePattern) {
		RollingCalendar rc = new RollingCalendar(TimeZones.GMT, Locale.getDefault());

		// set sate to 1970-01-01 00:00:00 GMT
		Date epoch = new Date(0);
		if (datePattern != null) {
			FastDateFormat fdf = FastDateFormat.getInstance(datePattern, TimeZones.GMT);
			for (int i = TOP_OF_MINUTE; i <= TOP_OF_MONTH; i++) {
				String r0 = fdf.format(epoch);
				
				rc.setRollingType(i);
				Date next = new Date(rc.getNextCheckMillis(epoch));
				String r1 = fdf.format(next);

				// System.out.println("Type = "+i+", r0 = "+r0+", r1 = "+r1);
				if (r0 != null && r1 != null && !r0.equals(r1)) {
					rollingType = i;
					return;
				}
			}
		}
		return; // Deliberately head for trouble...
	}
	
}