package panda.dao.query;

import panda.lang.Strings;


/**
 * Constants of logic operator.
 * 
 */
public enum Operator {
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
	 * GREATER_THAN = ">";
	 */
	GREATER_THAN {
		public String toString() {
			return ">";
		}
	},
	/**
	 * GREATER_EQUAL = ">=";
	 */
	GREATER_EQUAL {
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
	 * MATCH = "~~";
	 */
	MATCH {
		public String toString() {
			return "~~";
		}
	},
	/**
	 * NOT_MATCH = "!~~";
	 */
	NOT_MATCH {
		public String toString() {
			return "!~~";
		}
	},
	/**
	 * LEFT_MATCH = "!~=";
	 */
	LEFT_MATCH {
		public String toString() {
			return "~=";
		}
	},
	/**
	 * NOT_LEFT_MATCH = "!~=";
	 */
	NOT_LEFT_MATCH {
		public String toString() {
			return "!~=";
		}
	},
	/**
	 * RIGHT_MATCH = "=~";
	 */
	RIGHT_MATCH {
		public String toString() {
			return "=~";
		}
	},
	/**
	 * NOT_RIGHT_MATCH = "!=~";
	 */
	NOT_RIGHT_MATCH {
		public String toString() {
			return "!=~";
		}
	};
	
	public static Operator parse(String op) {
		op = Strings.upperCase(op);
		return Operator.valueOf(op);
	}
}

