package panda.bind.adapter;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import panda.bind.SourceAdapter;
import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Milliseconds (Long), 
 * or setDateFormat("yyyy-MM-dd") to use DateFormat to convert Date to formatted string.
 */
public class DateAdapter implements SourceAdapter {
	public static final DateAdapter toMillis = new DateAdapter();
	
	private Format dateFormat;

	/**
	 * Constructor
	 */
	public DateAdapter() {
	}

	/**
	 * @param dateFormat the date format to set
	 */
	public DateAdapter(String dateFormat) {
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
	public Object apply(Object value) {
		if (value == null) {
			return null;
		}

		Date d;
		if (value instanceof Calendar) {
			d = ((Calendar)value).getTime(); 
		}
		else if (value instanceof Date) {
			d = (Date)value;
		}
		else {
			throw new IllegalArgumentException("The value[" + value.getClass() + "] must be Date/Calendar type.");
		}
		
		return processDateValue(d);
	}

	protected Object processDateValue(Date value) {
		if (dateFormat != null){
			return dateFormat.format(value);
		}
		
		return value.getTime();
	}
}

