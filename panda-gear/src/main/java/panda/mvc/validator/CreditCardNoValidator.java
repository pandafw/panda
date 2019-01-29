package panda.mvc.validator;


import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.util.validator.CreditCardNumberValidator;


@IocBean(singleton=false)
public class CreditCardNoValidator extends AbstractStringValidator {
	public static final char ALL = CreditCardNumberValidator.ALL;

	private boolean normalize = true;
	
	private char type = ALL;

	public CreditCardNoValidator() {
		setMsgId(Validators.CREDITCARDNO);
	}

	/**
	 * @return the normalize
	 */
	public boolean isNormalize() {
		return normalize;
	}

	/**
	 * @param normalize the normalize to set
	 */
	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	/**
	 * @return the cardType
	 */
	public char getType() {
		return type;
	}

	/**
	 * @param type the cardType to set
	 */
	public void setType(char type) {
		this.type = type;
	}

	/**
	 * normalize card number
	 * @param value card number
	 * @return normalized card number
	 */
	protected String normalizeCardNumber(String value) {
		return Strings.replaceChars(value, "- ", "");
	}
	
	@Override
	protected boolean validateString(String value) {
		String cardno = value;
		if (normalize) {
			cardno = normalizeCardNumber(value);
		}
		
		CreditCardNumberValidator ccv = new CreditCardNumberValidator(type);
		return ccv.isValid(cardno);
	}
}
