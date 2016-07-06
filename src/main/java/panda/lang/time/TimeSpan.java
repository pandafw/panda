package panda.lang.time;

import java.io.Serializable;

import panda.lang.Numbers;



/**
 * TimeSpan
 * @author yf.frank.wang@gmail.com
 */
public class TimeSpan implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final long MS_SECOND = DateTimes.MS_SECOND;
	public static final long MS_MINUTE = DateTimes.MS_MINUTE;
	public static final long MS_HOUR = DateTimes.MS_HOUR;
	public static final long MS_DAY = DateTimes.MS_DAY;
	public static final long MS_WEEK = DateTimes.MS_WEEK;
	public static final long MS_MONTH = DateTimes.MS_MONTH;
	public static final long MS_YEAR = DateTimes.MS_YEAR;
	
	/**
	 * millisecond
	 */
	private long time;

	/**
	 * Constructor
	 */
	public TimeSpan() {
	}
	
	/**
	 * @param time millisecond
	 */
	public TimeSpan(long time) {
		this.time = time;
	}

	public TimeSpan(int days, int hours, int minutes, int seconds, int milliseconds) {
		this.time = days * MS_DAY + hours * MS_HOUR + minutes * MS_MINUTE + seconds * MS_SECOND + milliseconds;
	}

	public TimeSpan(int days, int hours, int minutes, int seconds) {
		this(days, hours, minutes, seconds, 0);
	}

	public TimeSpan(int days, int hours, int minutes) {
		this(days, hours, minutes, 0, 0);
	}

	/**
	 * Returns a TimeSpan that represents a specified number of days
	 */
	public static TimeSpan fromDays(double days) {
		return new TimeSpan((long)(days * MS_DAY));
	}

	/**
	 * Returns a TimeSpan that represents a specified number of hours
	 */
	public static TimeSpan fromHours(double hours) {
		return new TimeSpan((long)(hours * MS_HOUR));
	}

	/**
	 * Returns a TimeSpan that represents a specified number of minutes
	 */
	public static TimeSpan fromMinutes(double minutes) {
		return new TimeSpan((long)(minutes * MS_MINUTE));
	}

	/**
	 * Returns a TimeSpan that represents a specified number of seconds
	 */
	public static TimeSpan fromSeconds(double seconds) {
		return new TimeSpan((long)(seconds * MS_SECOND));
	}

	/**
	 * Returns a TimeSpan that represents a specified number of milliseconds
	 */
	public static TimeSpan fromMilliseconds(long milliseconds) {
		return new TimeSpan(milliseconds);
	}
	
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

	/**
	 * @return the days
	 */
	public int getDays() {
		return (int)(time / MS_DAY);
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(int days) {
		time += (days - getDays()) * MS_DAY;
	}

	/**
	 * @return the hours
	 */
	public int getHours() {
		long t = time - getDays() * MS_DAY;
		return (int)(t / MS_HOUR);
	}

	/**
	 * @param hours the hours to set
	 */
	public void setHours(int hours) {
		time += (hours - getHours()) * MS_HOUR;
	}

	/**
	 * @return the minutes
	 */
	public int getMinutes() {
		long t = time - time / MS_HOUR * MS_HOUR;
		return (int)(t / MS_MINUTE);
	}

	/**
	 * @param minutes the minutes to set
	 */
	public void setMinutes(int minutes) {
		time += (minutes - getMinutes()) * MS_MINUTE;
	}

	/**
	 * @return the seconds
	 */
	public int getSeconds() {
		long t = time - time / MS_MINUTE * MS_MINUTE;
		return (int)(t / MS_SECOND);
	}

	/**
	 * @param seconds the seconds to set
	 */
	public void setSeconds(int seconds) {
		time += (seconds - getSeconds()) * MS_SECOND;
	}

	/**
	 * @return the milliseconds
	 */
	public int getMilliseconds() {
		long t = time - time / MS_SECOND * MS_SECOND;
		return (int)t;
	}

	/**
	 * @param milliseconds the milliseconds to set
	 */
	public void setMilliseconds(int milliseconds) {
		time += (milliseconds - getMilliseconds());
	}

	public boolean equals(Object ts) {
		if (this == ts) {
			return true;
		}
		if (ts instanceof TimeSpan) {
			return ((TimeSpan)ts).time == time;
		}
		return false;
	}

	public TimeSpan add(long ts) {
		time += ts;
		return this;
	}

	public TimeSpan add(TimeSpan ts) {
		return add(ts.time);
	}

	public TimeSpan subtract(long ts) {
		time -= ts;
		if (time < 0) {
			time = 0;
		}
		return this;
	}

	public TimeSpan subtract(TimeSpan ts) {
		return subtract(ts.time);
	}

	public double totalYears() {
		return (double)time / MS_YEAR;
	}

	public double totalMonths() {
		return (double)time / MS_MONTH;
	}

	public double totalDays() {
		return (double)time / MS_DAY;
	}

	public double totalHours() {
		return (double)time / MS_HOUR;
	}

	public double totalMinutes() {
		return (double)time / MS_MINUTE;
	}

	public double totalSeconds() {
		return (double)time / MS_SECOND;
	}

	public long totalMilliseconds() {
		return time;
	}

	private static String formatSpan(double span) {
		return Numbers.format(span, 2);
	}
	
	public static String toDisplayString(TimeSpan ts) {
		if (ts.time >= MS_YEAR) {
			return formatSpan(ts.totalYears()) + " years";
		}
		if (ts.time >= MS_MONTH) {
			return formatSpan(ts.totalMonths()) + " months";
		}
		if (ts.time >= MS_DAY) {
			return formatSpan(ts.totalDays()) + " days";
		}
		if (ts.time >= MS_HOUR) {
			return formatSpan(ts.totalHours()) + " hours";
		}
		if (ts.time >= MS_MINUTE) {
			return formatSpan(ts.totalMinutes()) + " minutes";
		}
		if (ts.time >= MS_SECOND) {
			return formatSpan(ts.totalSeconds()) + " seconds";
		}
		return ts.time + " ms";
	}

	public static String toDisplayString(long time) {
		return toDisplayString(new TimeSpan(time));
	}

	@Override
	public String toString() {
		return toDisplayString(this);
	}
	
	@Override
	public int hashCode() {
		return new Long(time).hashCode();
	}
}
