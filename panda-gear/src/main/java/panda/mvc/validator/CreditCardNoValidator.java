package panda.mvc.validator;


import panda.ioc.annotation.IocBean;
import panda.lang.Strings;
import panda.util.validator.CreditCardNumberValidator;


@IocBean(singleton=false)
public class CreditCardNoValidator extends AbstractStringValidator {
	private boolean normalize = true;
	private char cardType = CreditCardNumberValidator.ALL;

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
	public char getCardType() {
		return cardType;
	}

	/**
	 * @param cardType the cardType to set
	 */
	public void setCardType(char cardType) {
		this.cardType = cardType;
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
		
		CreditCardNumberValidator ccv = new CreditCardNumberValidator(cardType);
		return ccv.isValid(cardno);
	}
}
