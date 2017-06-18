package panda.bind.adapter;

import java.text.Format;
import java.util.Calendar;

import panda.bind.SourceAdapter;
import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Milliseconds (Long), 
 * or setDateFormat("yyyy-MM-dd") to use DateFormat to convert Date to formatted string.
 */
public class CalendarAdapter implements SourceAdapter<Calendar> {
	public static final DateAdapter toMillis = new DateAdapter();

	private Format dateFormat;

	/**
	 * Constructor
	 */
	public CalendarAdapter() {
	}

	/**
	 * @param dateFormat the date format to set
	 */
	public CalendarAdapter(String dateFormat) {
		setDateFormat(dateFormat);
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = FastDateFormat.getInstance(dateFormat);
	}

	/**
	 * {@inheritDoc}
	 */
	public Object apply(Calendar value) {
		if (value == null) {
			return null;
		}

		if (dateFormat != null){
			return dateFormat.format(value);
		}
		
		return value.getTime();
	}
}

