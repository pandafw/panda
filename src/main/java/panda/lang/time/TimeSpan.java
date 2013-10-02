package panda.lang.time;

import java.io.Serializable;



/**
 * TimeSpan
 * @author yf.frank.wang@gmail.com
 */
public class TimeSpan implements Serializable {
	public static final long MS_SECOND = 1000;
	public static final long MS_MINUTE = MS_SECOND * 60;
	public static final long MS_HOUR = MS_MINUTE * 60;
	public static final long MS_DAY = MS_HOUR * 24;
	public static final long MS_WEEK = MS_DAY * 7;
	public static final long MS_MONTH = MS_DAY * 30;
	public static final long MS_YEAR = MS_DAY * 365;
	
	private static final long serialVersionUID = 8986342425776326722L;

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

	@Override
	public String toString() {
		return DurationFormatUtils.formatDurationHMS(time);
	}
	
	public int hashCode() {
		return new Long(time).hashCode();
	}
}
