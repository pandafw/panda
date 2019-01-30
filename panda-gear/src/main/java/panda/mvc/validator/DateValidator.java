package panda.mvc.validator;

import java.util.Calendar;
import java.util.Date;

import panda.ioc.annotation.IocBean;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class DateValidator extends AbstractValidator {

	private Date max;
	private Date min;

	public DateValidator() {
		setMsgId(Validators.MSGID_DATE_RANGE);
	}

	/**
	 * @return the max
	 */
	public Date getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Date max) {
		this.max = max;
	}

	/**
	 * @return the min
	 */
	public Date getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Date min) {
		this.min = min;
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object val) {
		// if there is no value - don't do comparison
		// if a value is required, a required validator should be added to the field
		if (val == null) {
			return true;
		}

		Date date = null;
		if (val instanceof Calendar) {
			date = ((Calendar)val).getTime();
		}
		else if (val instanceof Date) {
			date = (Date)val;
		}
		else {
			throw new IllegalArgumentException("field [" + getName() + "] (" + val.getClass()
				+ ") is not a instance of Date/Calendar");
		}

		// only check for a minimum value if the min parameter is set
		if ((getMin() != null) && (date.compareTo(getMin()) < 0)) {
			addFieldError(ac);
			return false;
		}
		
		if ((getMax() != null) && (date.compareTo(getMax()) > 0)) {
			addFieldError(ac);
			return false;
		}
		
		return true;
	}
}
