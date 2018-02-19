package panda.el.opt.logic;

import panda.lang.Objects;

public class Logics {

	public static boolean isTrue(Object o) {
		if (o == null) {
			return false;
		}
		if (o instanceof Boolean) {
			return ((Boolean)o).booleanValue();
		}
		if (o instanceof Number) {
			return ((Number)o).intValue() != 0;
		}
		return Objects.isNotEmpty(o);
	}

	public static boolean isFalse(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof Boolean) {
			return !((Boolean)o).booleanValue();
		}
		if (o instanceof Number) {
			return ((Number)o).intValue() == 0;
		}
		return Objects.isEmpty(o);
	}
}
