package panda.dao.criteria;

import panda.lang.Strings;


/**
 * Constants of logic operator.
 * 
 * @author yf.frank.wang@gmail.com
 */
public enum Operator {
	/**
	 * AND = "AND";
	 */
	AND,
	/**
	 * OR = "OR";
	 */
	OR,
	/**
	 * OPEN_PAREN = "(";
	 */
	BEG_PAREN {
		public String toString() {
			return "(";
		}
	},
	/**
	 * CLOSE_PAREN = ")";
	 */
	END_PAREN {
		public String toString() {
			return ")";
		}
	},
	/**
	 * "IS NULL";
	 */
	IS_NULL {
		public String toString() {
			return "IS NULL";
		}
	},
	/**
	 * "IS NOT NULL";
	 */
	IS_NOT_NULL {
		public String toString() {
			return "IS NOT NULL";
		}
	},
	/**
	 * EQUAL = "=";
	 */
	EQUAL {
		public String toString() {
			return "=";
		}
	},
	/**
	 * NOT_EQUAL = "!=";
	 */
	NOT_EQUAL {
		public String toString() {
			return "!=";
		}
	},
	/**
	 * GREAT_THAN = ">";
	 */
	GREAT_THAN {
		public String toString() {
			return ">";
		}
	},
	/**
	 * GREAT_EQUAL = ">=";
	 */
	GREAT_EQUAL {
		public String toString() {
			return ">=";
		}
	},
	/**
	 * LESS_THAN = "<";
	 */
	LESS_THAN {
		public String toString() {
			return "<";
		}
	},
	/**
	 * LESS_EQUAL = "<=";
	 */
	LESS_EQUAL {
		public String toString() {
			return "<=";
		}
	},
	/**
	 * LIKE = "LIKE";
	 */
	LIKE,
	/**
	 * NOT_LIKE = "NOT LIKE";
	 */
	NOT_LIKE {
		public String toString() {
			return "NOT LIKE";
		}
	},
	/**
	 * IN = "IN";
	 */
	IN,
	/**
	 * NOT_IN = "NOT IN";
	 */
	NOT_IN {
		public String toString() {
			return "NOT IN";
		}
	},
	/**
	 * BETWEEN = "BETWEEN";
	 */
	BETWEEN,
	/**
	 * NOT_BETWEEN = "NOT BETWEEN";
	 */
	NOT_BETWEEN {
		public String toString() {
			return "NOT BETWEEN";
		}
	},
	/**
	 * MATCH = "~~";
	 */
	MATCH {
		public String toString() {
			return "~~";
		}
	},
	/**
	 * LEFT_MATCH = "~=";
	 */
	LEFT_MATCH {
		public String toString() {
			return "~=";
		}
	},
	/**
	 * RIGHT_MATCH = "=~";
	 */
	RIGHT_MATCH {
		public String toString() {
			return "=~";
		}
	};
	
	public static Operator parse(String op) {
		op = Strings.upperCase(op);
		return Operator.valueOf(op);
	}
}

