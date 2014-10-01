package panda.mvc.validator;


import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.validator.CreditCardNumberValidator;


public class CreditCardNumberFieldValidator extends AbstractStringFieldValidator {
	private boolean normalize = false;
	private char cardType = CreditCardNumberValidator.ALL;

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
	protected boolean validateString(ActionContext ac, Object object, String value) throws ValidationException {
		String cardno = value;
		if (normalize) {
			cardno = normalizeCardNumber(value);
		}
		
		CreditCardNumberValidator ccv = new CreditCardNumberValidator(cardType);
		if (ccv.isValid(cardno)) {
			return true;
		}
		
		addFieldError(ac, getName(), value);
		return false;
	}
}
