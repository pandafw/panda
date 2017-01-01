package panda.dao.query;

import panda.lang.Strings;


/**
 */
public enum Order {
	/**
	 * ASC = "ASC";
	 */
	ASC,

	/**
	 * DESC = "DESC";
	 */
	DESC;
	
	public static Order parse(String order) {
		if (Strings.isNotEmpty(order)) {
			char c = Character.toLowerCase(order.charAt(0));
			if (c == 'd') {
				return DESC;
			}
		}
		return ASC;
	}
	
	public static Order reverse(Order order) {
		return order == ASC ? DESC : ASC;
	}
}

