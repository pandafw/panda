package panda.validator;

/**
 * Credit Card Number Validator
 * @author yf.frank.wang@gmail.com
 */
public class CreditCardNumberValidator {
	/**
	 * ALL = '*';
	 */
	public static final char ALL = '*';

	/**
	 * AMEX = 'A';
	 */
	public static final char AMEX = 'A';

	/**
	 * VISA = 'V';
	 */
	public static final char VISA = 'V';

	/**
	 * MASTERCARD = 'M';
	 */
	public static final char MASTERCARD = 'M';

	/**
	 * DISCOVER = 'D';
	 */
	public static final char DISCOVER = 'D';

	/**
	 * DINERS = 'I';
	 */
	public static final char DINERS = 'I';

	/**
	 * JCB = 'J';
	 */
	public static final char JCB = 'J';

	private static final String[] PREFIX_AMEX_0 = { "34", "37" };
	private static final String[] PREFIX_VISA_0 = { "4" };
	private static final String[] PREFIX_MASTERCARD_0 = { "51", "52", "53", "54", "55" };
	private static final String[] PREFIX_DISCOVER_0 = { "6011", "65" };
	private static final String[] PREFIX_DINERS_0 = { "36", "38", "300", "301", "302", "303",
			"304", "305" };
	private static final String[] PREFIX_JCB_0 = { "2131", "1800" };
	private static final String[] PREFIX_JCB_1 = { "3" };

	private static final int[] LENGTH_AMEX_0 = { 15 };
	private static final int[] LENGTH_VISA_0 = { 13, 16 };
	private static final int[] LENGTH_MASTERCARD_0 = { 16 };
	private static final int[] LENGTH_DISCOVER_0 = { 16 };
	private static final int[] LENGTH_DINERS_0 = { 14 };
	private static final int[] LENGTH_JCB_0 = { 15 };
	private static final int[] LENGTH_JCB_1 = { 16 };

	private char cardType = ALL;

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
	 * Constructor
	 */
	public CreditCardNumberValidator() {
	}

	/**
	 * Constructor
	 * 
	 * @param cardType card type
	 */
	public CreditCardNumberValidator(char cardType) {
		setCardType(cardType);
	}

	/**
	 * Checks if the field is a valid credit card number.
	 * 
	 * @param card The card number to validate.
	 * @return Whether the card number is valid.
	 */
	public boolean isValid(String card) {
		if ((card == null) || (card.length() < 13) || (card.length() > 19)) {
			return false;
		}

		if (!luhnCheck(card)) {
			return false;
		}

		switch (cardType) {
		case AMEX:
			return isValidAmexCard(card);
		case VISA:
			return isValidVisaCard(card);
		case MASTERCARD:
			return isValidMasterCard(card);
		case DISCOVER:
			return isValidDiscoverCard(card);
		case DINERS:
			return isValidDinersCard(card);
		case JCB:
			return isValidJcbCard(card);
		case ALL:
			return isValidAmexCard(card) || isValidVisaCard(card) || isValidMasterCard(card)
					|| isValidDiscoverCard(card) || isValidJcbCard(card);
		default:
			return false;
		}
	}

	/**
	 * Checks for a valid credit card number.
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number passes the luhnCheck.
	 */
	protected boolean luhnCheck(String cardNumber) {
		int digits = cardNumber.length();
		int oddOrEven = digits & 1;
		long sum = 0;
		for (int count = 0; count < digits; count++) {
			int digit = 0;
			try {
				digit = Integer.parseInt(cardNumber.charAt(count) + "");
			}
			catch (NumberFormatException e) {
				return false;
			}
			if (((count & 1) ^ oddOrEven) == 0) {
				digit *= 2;
				if (digit > 9) {
					digit -= 9;
				}
			}
			sum += digit;
		}
		return (sum == 0) ? false : (sum % 10 == 0);
	}

	/**
	 * is valid Amex card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidAmexCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_AMEX_0, LENGTH_AMEX_0);
	}

	/**
	 * is valid Visa card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidVisaCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_VISA_0, LENGTH_VISA_0);
	}

	/**
	 * is valid master card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidMasterCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_MASTERCARD_0, LENGTH_MASTERCARD_0);
	}

	/**
	 * is valid Discover card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidDiscoverCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_DISCOVER_0, LENGTH_DISCOVER_0);
	}

	/**
	 * is valid Diners card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidDinersCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_DINERS_0, LENGTH_DINERS_0);
	}

	/**
	 * is valid Jcb card number
	 * 
	 * @param cardNumber Credit Card Number.
	 * @return Whether the card number is valid.
	 */
	public static boolean isValidJcbCard(String cardNumber) {
		return checkCardNumber(cardNumber, PREFIX_JCB_0, LENGTH_JCB_0)
				|| checkCardNumber(cardNumber, PREFIX_JCB_1, LENGTH_JCB_1);
	}

	/**
	 * chech card number
	 * @param cardNumber Credit Card Number.
	 * @param prefixArray prefix array
	 * @param lengthArray length array
	 * @return Whether the card number is valid.
	 */
	protected static boolean checkCardNumber(String cardNumber, String[] prefixArray, int[] lengthArray) {
		for (int length : lengthArray) {
			if (length == cardNumber.length()) {
				for (String prefix : prefixArray) {
					if (prefix.equals(cardNumber.substring(0, prefix.length()))) {
						return true;
					}
				}
			}
		}
		return false;
	}

}
