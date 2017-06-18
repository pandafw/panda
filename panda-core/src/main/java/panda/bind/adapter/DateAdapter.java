package panda.bind.adapter;

import java.text.Format;
import java.util.Date;

import panda.bind.SourceAdapter;
import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Milliseconds (Long), 
 * or setDateFormat("yyyy-MM-dd") to use DateFormat to convert Date to formatted string.
 */
public class DateAdapter implements SourceAdapter<Date> {
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
	public Object apply(Date value) {
		if (value == null) {
			return null;
		}

		if (dateFormat != null){
			return dateFormat.format(value);
		}
		
		return value.getTime();
	}
}

