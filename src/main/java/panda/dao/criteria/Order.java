package panda.dao.criteria;

import panda.lang.Strings;


/**
 * @author yf.frank.wang@gmail.com
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
}

