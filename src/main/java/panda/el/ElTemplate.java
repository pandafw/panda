package panda.el;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import panda.lang.Asserts;
import panda.lang.Marks;
import panda.lang.collection.LRUMap;
import panda.tpl.AbstractTemplate;
import panda.tpl.TemplateException;

public class ElTemplate extends AbstractTemplate {
	private static Map<String, ElTemplate> cache = new LRUMap<String, ElTemplate>(1000);
	
	private List<Object> segments;

	public ElTemplate(String expression) throws TemplateException {
		this(expression, Marks.DOLLAR);
	}

	public ElTemplate(String expression, char prefix) throws TemplateException {
		this(expression, prefix, Marks.BRACES_LEFT, Marks.BRACES_RIGHT);
	}

	public ElTemplate(String expr, char prefix, char open, char close) throws TemplateException {
		Asserts.notNull(expr);
		
		segments = new ArrayList<Object>();
		
		int x = 0, len = expr.length();
		for (int i = 0; i < len; i++) {
			char c = expr.charAt(i);
			if (c == prefix && i < len - 1 && expr.charAt(i + 1) == open) {
				String el = null;
				int j = i + 2;
				for (; j < len; j++) {
					if (expr.charAt(j) == close) {
						el = expr.substring(i + 2, j);
						break;
					}
				}
				if (el == null) {
					throw new TemplateException("Illegal statement (" + i + "): unexpected end of tag reached.");
				}
				else if (el.length() < 1) {
					throw new TemplateException("Illegal statement (" + i + "): the paramenter can not be empty.");
				}
				
				if (i > x) {
					segments.add(expr.substring(x, i));
				}

				segments.add(El.get(el));

				i = j;
				x = i + 1;
			}
		}

		if (x < len) {
			segments.add(expr.substring(x, len));
		}
	}

	public void evaluate(Appendable out, Object context) throws TemplateException {
		try {
			for (Object o : segments) {
				if (o instanceof El) {
					Object r = ((El)o).eval(context);
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
			throw new TemplateException("Failed to evaluate(" + expression + ")");
		}
	}
	
	public static String evaluate(String expression, Object context) throws TemplateException {
		try {
			return get(expression).evaluate(context);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")");
		}
	}

	public static void evaluate(String expression, Appendable out) throws TemplateException {
		try {
			get(expression).evaluate(out);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")");
		}
	}

	public static void evaluate(String expression, Appendable out, Object context) throws TemplateException {
		try {
			get(expression).evaluate(out, context);
		}
		catch (Exception e) {
			throw new TemplateException("Failed to evaluate(" + expression + ")");
		}
	}
}
