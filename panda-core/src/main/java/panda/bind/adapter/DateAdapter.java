package panda.bind.adapter;

import java.text.Format;
import java.util.Date;

import panda.lang.time.FastDateFormat;

/**
 * Convert Date to Milliseconds (Long), 
 * or setDateFormat("yyyy-MM-dd") to use DateFormat to convert Date to formatted string.
 */
public class DateAdapter extends AbstractSerializeAdapter<Date> {
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
	@Override
	public Object adaptSource(Date src) {
		if (src == null) {
			return null;
		}

		if (dateFormat != null){
			return dateFormat.format(src);
		}
		
		return src.getTime();
	}
}

