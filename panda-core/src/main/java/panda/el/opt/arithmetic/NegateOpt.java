package panda.el.opt.arithmetic;

import java.math.BigDecimal;
import java.math.BigInteger;

import panda.el.ELContext;
import panda.el.opt.AbstractSingleOpt;

/**
 * Negate: '-'
 */
public class NegateOpt extends AbstractSingleOpt {
	public int getPriority() {
		return 2;
	}

	public Object calculate(ELContext ec) {
		Object rval = calculateItem(ec, right);
		if (isReturnNull(ec, rval)) {
			return null;
		}
		if (rval instanceof BigDecimal) {
			return ((BigDecimal)rval).negate();
		}
		if (rval instanceof BigInteger) {
			return ((BigInteger)rval).negate();
		}
		if (rval instanceof Double) {
			return 0 - (Double)rval;
		}
		if (rval instanceof Float) {
			return 0 - (Float)rval;
		}
		if (rval instanceof Long) {
			return 0 - (Long)rval;
		}
		if (rval instanceof Integer) {
			return 0 - (Integer)rval;
		}
		if (rval instanceof Short) {
			return 0 - (Short)rval;
		}
		if (rval instanceof Byte) {
			return 0 - (Byte)rval;
		}
		if (rval instanceof Character) {
			return 0 - ((Character)rval).charValue();
		}
		return 0 - (Integer)rval;
	}

	public String operator() {
		return "-";
	}
}
