package panda.bind.adapter;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

import panda.bind.SourceAdapter;
import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Long<br/>
 * setToTime(false) to use DateFormat to convert Date Type Value 
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class DateAdapter implements SourceAdapter {
	private Format dateFormat;
	private boolean toTime = true;

	/**
	 * Constructor
	 */
	public DateAdapter() {
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = FastDateFormat.getInstance(dateFormat);
	}

	/**
	 * @param toTime the toTime to set
	 */
	public void setToTime(boolean toTime) {
		this.toTime = toTime;
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
		else if (toTime) {
			return value.getTime();
		}
		else {
			return value;
		}
	}
}

