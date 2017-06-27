package panda.el;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.lang.Arrays;
import panda.lang.Asserts;
import panda.lang.Chars;
import panda.lang.collection.LRUMap;
import panda.tpl.AbstractTemplate;
import panda.tpl.TemplateException;

public class ElTemplate extends AbstractTemplate {
	private static Map<String, ElTemplate> cache = new LRUMap<String, ElTemplate>(1000);
	
	public static final char[] PREFIXS = { Chars.DOLLAR, Chars.PERCENT };
	
	private List<Object> segments;

	public ElTemplate(String expr) throws TemplateException {
		this(expr, PREFIXS);
	}

	public ElTemplate(String expr, char prefix) throws TemplateException {
		this(expr, prefix, Chars.BRACES_LEFT, Chars.BRACES_RIGHT);
	}

	public ElTemplate(String expr, char[] prefixs) throws TemplateException {
		this(expr, prefixs, Chars.BRACES_LEFT, Chars.BRACES_RIGHT);
	}

	public ElTemplate(String expr, char prefix, char open, char close) throws TemplateException {
		this(expr, new char[] { prefix }, open, close);
	}
	
	public ElTemplate(String expr, char[] prefixs, char open, char close) throws TemplateException {
		Asserts.notNull(expr);
		
		segments = new ArrayList<Object>();
		
		if (Arrays.isEmpty(prefixs)) {
			initWithoutPrefix(expr, open, close);
		}
		else if (prefixs.length == 1) {
			initWithPrefix(expr, prefixs[0], open, close);
		}
		else {
			initWithPrefixs(expr, prefixs, open, close);
		}
	}
	
	private void initWithoutPrefix(String expr, char open, char close) throws TemplateException {
		int x = 0, len = expr.length();
		for (int i = 0; i < len; i++) {
			char c = expr.charAt(i);
			if (c != open) {
				continue;
			}
			
			i = findSegment(expr, x, i, i + 1, close);
			x = i + 1;
		}

		if (x < len) {
			segments.add(expr.substring(x, len));
		}
	}
	
	private void initWithPrefix(String expr, char prefix, char open, char close) throws TemplateException {
		int x = 0, len = expr.length();
		for (int i = 0; i < len; i++) {
			char c = expr.charAt(i);
			if (c != prefix) {
				continue;
			}
			
			if (i < len - 1 && expr.charAt(i + 1) == open) {
				i = findSegment(expr, x, i, i + 2, close);
				x = i + 1;
			}
		}

		if (x < len) {
			segments.add(expr.substring(x, len));
		}
	}

	private void initWithPrefixs(String expr, char[] prefixs, char open, char close) throws TemplateException {
		int x = 0, len = expr.length();
		for (int i = 0; i < len; i++) {
			char c = expr.charAt(i);
			if (!Arrays.contains(prefixs, c)) {
				continue;
			}
			
			if (i < len - 1 && expr.charAt(i + 1) == open) {
				i = findSegment(expr, x, i, i + 2, close);
				x = i + 1;
			}
		}

		if (x < len) {
			segments.add(expr.substring(x, len));
		}
	}

	private int findSegment(String expr, int x, int i, int s, char close) {
		String el = null;

		int len = expr.length();
		int j = s;
		for (; j < len; j++) {
			if (expr.charAt(j) == close) {
				el = expr.substring(s, j);
				break;
			}
		}

		// close char not found
		if (el == null) {
			segments.add(expr.substring(x));
			return len;
		}
		
		// empty expression
		if (el.length() < 1) {
			throw new TemplateException("Illegal statement (" + i + "): the el expression can not be empty.");
		}
		
		if (i > x) {
			segments.add(expr.substring(x, i));
		}

		segments.add(El.get(el));

		return j;
	}
	
	public void evaluate(Appendable out, Object context, boolean strict) throws TemplateException {
		try {
			for (Object o : segments) {
				if (o instanceof El) {
					Object r = ((El)o).eval(new ElContext(context, strict));
					if (r != null) {
						out.append(r.toString());
					}
				}
				else {
					out.append((String)o);
				}
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String evaluate(Object context, boolean strict) throws TemplateException {
		StringBuilder sb = new StringBuilder();
		evaluate(sb, context, strict);
		return sb.toString();
	}
	
	@Override
	public void evaluate(Appendable out, Object context) throws TemplateException {
		evaluate(out, context, false);
	}
	
	//-----------------------------------------------------------------------
	// static methods
	//
	private static ElTemplate get(String expression) throws TemplateException {
		ElTemplate elt = cache.get(expression);
		if (elt != null) {
			return elt;
		}
		synchronized(cache) {
			elt = cache.get(expression);
			if (elt != null) {
				return elt;
			}
			
			elt = new ElTemplate(expression);
			cache.put(expression, elt);
			return elt;
		}
	}

	public static String evaluate(String expression) throws TemplateException {
		try {
			return get(expression).evaluate();
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")", e);
		}
	}
	
	public static String evaluate(String expression, Object context) throws TemplateException {
		try {
			return get(expression).evaluate(context);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")", e);
		}
	}
	
	public static String evaluate(String expression, Object context, boolean strict) throws TemplateException {
		try {
			return get(expression).evaluate(context, strict);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")", e);
		}
	}

	public static void evaluate(String expression, Appendable out) throws TemplateException {
		try {
			get(expression).evaluate(out);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")", e);
		}
	}

	public static void evaluate(String expression, Appendable out, Object context) throws TemplateException {
		try {
			get(expression).evaluate(out, context);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")", e);
		}
	}
}
