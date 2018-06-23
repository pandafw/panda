package panda.bind.adapter;

import java.text.Format;
import java.util.Calendar;

import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Milliseconds (Long), 
 * or setDateFormat("yyyy-MM-dd") to use DateFormat to convert Date to formatted string.
 */
public class CalendarAdapter extends AbstractSerializeAdapter<Calendar> {
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
	@Override
	public Object adaptSource(Calendar src) {
		if (src == null) {
			return null;
		}

		if (dateFormat != null){
			return dateFormat.format(src);
		}
		
		return src.getTime();
	}
}

