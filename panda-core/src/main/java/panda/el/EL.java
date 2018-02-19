package panda.el;

import java.util.Map;
import java.util.Queue;
import java.util.WeakHashMap;

import panda.el.arithmetic.RPN;
import panda.el.arithmetic.ShuntingYard;

public class EL {
	private static Map<String, EL> cache = new WeakHashMap<String, EL>();
	
	private RPN rpn;
	private CharSequence expr;

	/**
	 * EL expression constructor
	 * 
	 * @param cs the EL expression
	 */
	public EL(CharSequence cs) {
		try {
			expr = cs;
			ShuntingYard sy = new ShuntingYard();
			Queue<Object> que = sy.parseToRPN(cs);
			rpn = new RPN(que);
		}
		catch (Exception e) {
			throw new ELException("Failed to parse El expression '" + cs + "'", e);
		}
	}

	/**
	 * evaluate the EL expression
	 * @return the evaluated value
	 */
	public Object eval() {
		return rpn.calculate(ELContext.DEFAULT);
	}

	/**
	 * evaluate the EL expression
	 * 
	 * @param context the context object
	 * @return the evaluated value
	 */
	public Object eval(Object context) {
		ELContext ec = new ELContext(context);
		return rpn.calculate(ec);
	}

	/**
	 * evaluate the EL expression
	 * 
	 * @param ec the ElContext object
	 * @return the evaluated value
	 */
	public Object eval(ELContext ec) {
		if (ec == null) {
			ec = ELContext.DEFAULT;
		}
		return rpn.calculate(ec);
	}

	public String toString() {
		return expr.toString();
	}

	//---------------------------------------------------------------
	// static methods
	//
	public static EL get(String expr) {
		EL el = cache.get(expr);
		if (el != null) {
			return el;
		}
		synchronized(cache) {
			el = cache.get(expr);
			if (el != null) {
				return el;
			}
			el = new EL(expr);
			cache.put(expr, el);
			return el;
		}
	}

	public static EL parse(String expr) {
		return new EL(expr);
	}
	
	/**
	 * evaluate the EL expression
	 * 
	 * @param expr the EL expression
	 * @return the evaluated value
	 */
	public static Object eval(String expr) {
		return eval(expr, null);
	}

	/**
	 * evaluate the EL expression
	 * 
	 * @param expr the EL expression
	 * @param context the parameters
	 * @return the evaluated value
	 */
	public static Object eval(String expr, Object context) {
		try {
			return get(expr).eval(context);
		}
		catch (Exception e) {
			throw new ELException("Failed to eval('" + expr + "', " + (context == null ? "null" : context.getClass()) + ")", e);
		}
	}

	/**
	 * evaluate the EL expression
	 * 
	 * @param expr the EL expression
	 * @param context the parameters
	 * @return the evaluated value
	 */
	public static Object eval(String expr, ELContext context) {
		try {
			return get(expr).eval(context);
		}
		catch (Exception e) {
			throw new ELException("Failed to eval('" + expr + "', " + context + ")", e);
		}
	}
}
