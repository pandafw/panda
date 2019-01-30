package panda.mvc.validator;

import java.math.BigDecimal;

import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.mvc.ActionContext;

@IocBean(singleton=false)
public class DecimalValidator extends AbstractValidator {
	public static final char TYPE_DB = 'd';
	public static final char TYPE_JAVA = 'j';

	private Integer precision = null;
	private Integer scale = 0;
	private char type = TYPE_DB;

	public DecimalValidator() {
		setMsgId(Validators.MSGID_DECIMAL_PRECISION);
	}

	/**
	 * @return precision
	 */
	public Integer getPrecision() {
		return precision;
	}

	/**
	 * @param precision the precision to set
	 */
	public void setPrecision(Integer precision) {
		this.precision = precision;
	}

	/**
	 * @return scale
	 */
	public Integer getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(Integer scale) {
		this.scale = scale;
	}

	/**
	 * @return type
	 */
	public String getType() {
		return String.valueOf(type);
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		if (Strings.isNotEmpty(type)) {
			this.type = type.toLowerCase().charAt(0);
			if (this.type != TYPE_DB && this.type != TYPE_JAVA) {
				this.type = TYPE_DB; 
			}
		}
	}

	@Override
	protected boolean validateValue(ActionContext ac, Object value) {
		if (precision == null || precision < 0) {
			throw new IllegalArgumentException("the precision of '" + getName() + "' can not be null or nagative");
		}
		if (scale != null) {
			if (scale < 0 || scale >= precision) {
				throw new IllegalArgumentException("the scale of '" + getName() + "' can not nagative or >= precision");
			}
		}

		if (value == null) {
			return true;
		}

		if (!(value instanceof BigDecimal)) {
			throw new IllegalArgumentException("The value of field '" + getName() + "' (" + value.getClass() + ") is not a instance of "
					+ BigDecimal.class);
		}

		BigDecimal bd = (BigDecimal)value;

		if (TYPE_JAVA == type) {
			if (bd.precision() > precision || bd.scale() > scale) {
				addFieldError(ac);
				return false;
			}
		}
		else {
			if (bd.precision() - bd.scale() > precision - scale || bd.scale() > scale) {
				addFieldError(ac);
				return false;
			}
		}
		return true;
	}
	
}
