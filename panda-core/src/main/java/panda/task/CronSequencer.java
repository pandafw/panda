package panda.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

import panda.lang.Objects;
import panda.lang.Strings;


/**
 * Date sequence generator for a <a href="http://www.manpagez.com/man/5/crontab/">Crontab pattern</a>,
 * allowing clients to specify a pattern that the sequence matches.
 *
 * <p>The pattern is a list of six single space-separated fields: representing
 * second, minute, hour, day, month, weekday. Month and weekday names can be
 * given as the first three letters of the English names.
 *
 * <p>Example patterns:
 * <ul>
 * <li>"0 0 * * * *" = the top of every hour of every day.</li>
 * <li>"*&#47;10 * * * * *" = every ten seconds.</li>
 * <li>"0 0 8-10 * * *" = 8, 9 and 10 o'clock of every day.</li>
 * <li>"0 0/30 8-10 * * *" = 8:00, 8:30, 9:00, 9:30 and 10 o'clock every day.</li>
 * <li>"0 0 9-17 * * MON-FRI" = on the hour nine-to-five weekdays</li>
 * <li>"0 0 0 25 12 ?" = every Christmas Day at midnight</li>
 * </ul>
 */
public class CronSequencer {

	private final BitSet seconds = new BitSet(60);

	private final BitSet minutes = new BitSet(60);

	private final BitSet hours = new BitSet(24);

	private final BitSet daysOfWeek = new BitSet(7);

	private final BitSet daysOfMonth = new BitSet(31);

	private final BitSet months = new BitSet(12);

	private final String expression;

	private final TimeZone timeZone;


	/**
	 * Construct a {@link CronSequencer} from the pattern provided,
	 * using the default {@link TimeZone}.
	 * @param expression a space-separated list of time fields
	 * @throws IllegalArgumentException if the pattern cannot be parsed
	 * @see java.util.TimeZone#getDefault()
	 */
	public CronSequencer(String expression) {
		this(expression, TimeZone.getDefault());
	}

	/**
	 * Construct a {@link CronSequencer} from the pattern provided,
	 * using the specified {@link TimeZone}.
	 * @param expression a space-separated list of time fields
	 * @param timeZone the TimeZone to use for generated trigger times
	 * @throws IllegalArgumentException if the pattern cannot be parsed
	 */
	public CronSequencer(String expression, TimeZone timeZone) {
		this.expression = expression;
		this.timeZone = timeZone;
		parse(expression);
	}


	/**
	 * Get the next {@link Date} in the sequence matching the Cron pattern and
	 * after the value provided. The return value will have a whole number of
	 * seconds, and will be after the input value.
	 * @param date a seed value
	 * @return the next value matching the pattern
	 */
	public Date next(Date date) {
		/*
		The plan:

		1 Round up to the next whole second

		2 If seconds match move on, otherwise find the next match:
		2.1 If next match is in the next minute then roll forwards

		3 If minute matches move on, otherwise find the next match
		3.1 If next match is in the next hour then roll forwards
		3.2 Reset the seconds and go to 2

		4 If hour matches move on, otherwise find the next match
		4.1 If next match is in the next day then roll forwards,
		4.2 Reset the minutes and seconds and go to 2

		...
		*/

		Calendar calendar = new GregorianCalendar();
		calendar.setTimeZone(timeZone);
		calendar.setTime(date);

		// First, just reset the milliseconds and try to calculate from there...
		calendar.set(Calendar.MILLISECOND, 0);
		long originalTimestamp = calendar.getTimeInMillis();
		doNext(calendar, calendar.get(Calendar.YEAR));

		if (calendar.getTimeInMillis() == originalTimestamp) {
			// We arrived at the original timestamp - round up to the next whole second and try again...
			calendar.add(Calendar.SECOND, 1);
			doNext(calendar, calendar.get(Calendar.YEAR));
		}

		return calendar.getTime();
	}

	private void doNext(Calendar calendar, int dot) {
		List<Integer> resets = new ArrayList<Integer>();

		int second = calendar.get(Calendar.SECOND);
		int updateSecond = findNext(seconds, second, calendar, Calendar.SECOND, Calendar.MINUTE, resets);
		if (second == updateSecond) {
			resets.add(Calendar.SECOND);
		}

		int minute = calendar.get(Calendar.MINUTE);
		int updateMinute = findNext(minutes, minute, calendar, Calendar.MINUTE, Calendar.HOUR_OF_DAY, resets);
		if (minute == updateMinute) {
			resets.add(Calendar.MINUTE);
		}
		else {
			doNext(calendar, dot);
		}

		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int updateHour = findNext(hours, hour, calendar, Calendar.HOUR_OF_DAY, Calendar.DAY_OF_WEEK, resets);
		if (hour == updateHour) {
			resets.add(Calendar.HOUR_OF_DAY);
		}
		else {
			doNext(calendar, dot);
		}

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		int updateDayOfMonth = findNextDay(calendar, dayOfMonth, dayOfWeek, resets);
		if (dayOfMonth == updateDayOfMonth) {
			resets.add(Calendar.DAY_OF_MONTH);
		}
		else {
			doNext(calendar, dot);
		}

		int month = calendar.get(Calendar.MONTH);
		int updateMonth = findNext(months, month, calendar, Calendar.MONTH, Calendar.YEAR, resets);
		if (month != updateMonth) {
			if (calendar.get(Calendar.YEAR) - dot > 4) {
				throw new IllegalArgumentException("Invalid cron expression \"" + expression +
						"\" led to runaway search for next trigger");
			}
			doNext(calendar, dot);
		}

	}

	private int findNextDay(Calendar calendar, int dayOfMonth, int dayOfWeek, List<Integer> resets) {
		int count = 0;
		int max = 366;
		// the DAY_OF_WEEK values in java.util.Calendar start with 1 (Sunday),
		// but in the cron pattern, they start with 0, so we subtract 1 here
		while ((!daysOfMonth.get(dayOfMonth) || !daysOfWeek.get(dayOfWeek - 1)) && count++ < max) {
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			reset(calendar, resets);
		}
		if (count >= max) {
			throw new IllegalArgumentException("Overflow in day for expression \"" + expression + "\"");
		}
		return dayOfMonth;
	}

	/**
	 * Search the bits provided for the next set bit after the value provided,
	 * and reset the calendar.
	 * @param bits a {@link BitSet} representing the allowed values of the field
	 * @param value the current value of the field
	 * @param calendar the calendar to increment as we move through the bits
	 * @param field the field to increment in the calendar (@see
	 * {@link Calendar} for the static constants defining valid fields)
	 * @param lowerOrders the Calendar field ids that should be reset (i.e. the
	 * ones of lower significance than the field of interest)
	 * @return the value of the calendar field that is next in the sequence
	 */
	private int findNext(BitSet bits, int value, Calendar calendar, int field, int nextField, List<Integer> lowerOrders) {
		int nextValue = bits.nextSetBit(value);
		// roll over if needed
		if (nextValue == -1) {
			calendar.add(nextField, 1);
			reset(calendar, Arrays.asList(field));
			nextValue = bits.nextSetBit(0);
		}
		if (nextValue != value) {
			calendar.set(field, nextValue);
			reset(calendar, lowerOrders);
		}
		return nextValue;
	}

	/**
	 * Reset the calendar setting all the fields provided to zero.
	 */
	private void reset(Calendar calendar, List<Integer> fields) {
		for (int field : fields) {
			calendar.set(field, field == Calendar.DAY_OF_MONTH ? 1 : 0);
		}
	}


	// Parsing logic invoked by the constructor

	/**
	 * Parse the given pattern expression.
	 */
	private void parse(String expression) throws IllegalArgumentException {
		String[] fields = Strings.split(expression, ' ');
		if (fields.length != 6) {
			throw new IllegalArgumentException(String.format(
					"Cron expression must consist of 6 fields (found %d in \"%s\")", fields.length, expression));
		}
		setNumberHits(seconds, fields[0], 0, 60);
		setNumberHits(minutes, fields[1], 0, 60);
		setNumberHits(hours, fields[2], 0, 24);
		setDaysOfMonth(daysOfMonth, fields[3]);
		setMonths(months, fields[4]);
		setDays(daysOfWeek, replaceOrdinals(fields[5], "SUN,MON,TUE,WED,THU,FRI,SAT"), 8);
		if (daysOfWeek.get(7)) {
			// Sunday can be represented as 0 or 7
			daysOfWeek.set(0);
			daysOfWeek.clear(7);
		}
	}

	/**
	 * Replace the values in the commaSeparatedList (case insensitive) with
	 * their index in the list.
	 * @return a new string with the values from the list replaced
	 */
	private String replaceOrdinals(String value, String commaSeparatedList) {
		value = value.toUpperCase();
		String[] list = Strings.split(commaSeparatedList, ',');
		for (int i = 0; i < list.length; i++) {
			String item = list[i].toUpperCase();
			value = Strings.replace(value, item, String.valueOf(i));
		}
		return value;
	}

	private void setDaysOfMonth(BitSet bits, String field) {
		int max = 31;
		// Days of month start with 1 (in Cron and Calendar) so add one
		setDays(bits, field, max + 1);
		// ... and remove it from the front
		bits.clear(0);
	}

	private void setDays(BitSet bits, String field, int max) {
		if (field.contains("?")) {
			field = "*";
		}
		setNumberHits(bits, field, 0, max);
	}

	private void setMonths(BitSet bits, String value) {
		int max = 12;
		value = replaceOrdinals(value, "FOO,JAN,FEB,MAR,APR,MAY,JUN,JUL,AUG,SEP,OCT,NOV,DEC");
		BitSet months = new BitSet(13);
		// Months start with 1 in Cron and 0 in Calendar, so push the values first into a longer bit set
		setNumberHits(months, value, 1, max + 1);
		// ... and then rotate it to the front of the months
		for (int i = 1; i <= max; i++) {
			if (months.get(i)) {
				bits.set(i - 1);
			}
		}
	}

	private void setNumberHits(BitSet bits, String value, int min, int max) {
		String[] fields = Strings.split(value, ',');
		for (String field : fields) {
			if (!field.contains("/")) {
				// Not an incrementer so it must be a range (possibly empty)
				int[] range = getRange(field, min, max);
				bits.set(range[0], range[1] + 1);
			}
			else {
				String[] split = Strings.split(field, '/');
				if (split.length != 2) {
					throw new IllegalArgumentException("Incrementer has more than two fields: '" +
							field + "' in expression \"" + expression + "\"");
				}
				int[] range = getRange(split[0], min, max);
				if (!split[0].contains("-")) {
					range[1] = max - 1;
				}
				int delta = Integer.valueOf(split[1]);
				for (int i = range[0]; i <= range[1]; i += delta) {
					bits.set(i);
				}
			}
		}
	}

	private int[] getRange(String field, int min, int max) {
		int[] result = new int[2];
		if (field.contains("*")) {
			result[0] = min;
			result[1] = max - 1;
			return result;
		}
		if (!field.contains("-")) {
			result[0] = result[1] = Integer.valueOf(field);
		}
		else {
			String[] split = Strings.split(field, '-');
			if (split.length > 2) {
				throw new IllegalArgumentException("Range has more than two fields: '" +
						field + "' in expression \"" + expression + "\"");
			}
			result[0] = Integer.valueOf(split[0]);
			result[1] = Integer.valueOf(split[1]);
		}
		if (result[0] >= max || result[1] >= max) {
			throw new IllegalArgumentException("Range exceeds maximum (" + max + "): '" +
					field + "' in expression \"" + expression + "\"");
		}
		if (result[0] < min || result[1] < min) {
			throw new IllegalArgumentException("Range less than minimum (" + min + "): '" +
					field + "' in expression \"" + expression + "\"");
		}
		return result;
	}

	String getExpression() {
		return expression;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof CronSequencer)) {
			return false;
		}
		
		CronSequencer cron = (CronSequencer) obj;
		return cron.months.equals(months) && cron.daysOfMonth.equals(daysOfMonth)
				&& cron.daysOfWeek.equals(daysOfWeek) && cron.hours.equals(hours)
				&& cron.minutes.equals(minutes) && cron.seconds.equals(seconds);
	}

	@Override
	public int hashCode() {
		return 37 + 17 * months.hashCode() + 29 * daysOfMonth.hashCode() + 37 * daysOfWeek.hashCode()
				+ 41 * hours.hashCode() + 53 * minutes.hashCode() + 61 * seconds.hashCode();
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append(expression)
				.append(timeZone)
				.toString();
	}

}
