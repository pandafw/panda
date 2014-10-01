package panda.el;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import panda.lang.Asserts;
import panda.tpl.AbstractTemplate;
import panda.tpl.TemplateException;

public class ElTemplate extends AbstractTemplate {
	private List<Object> segments;

	public ElTemplate(String expression) {
		this(expression, '$');
	}

	public ElTemplate(String expression, char prefix) {
		this(expression, prefix, '{', '}');
	}

	public ElTemplate(String expression, char prefix, char open, char close) {
		Asserts.notNull(expression);
		
		segments = new ArrayList<Object>();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);
			if (c == prefix && i < expression.length() - 1 && expression.charAt(i + 1) == open) {
				String exp = null;
				int j = i + 2;
				for (; j < expression.length(); j++) {
					if (expression.charAt(j) == close) {
						exp = expression.substring(i + 2, j);
						break;
					}
				}
				if (exp == null) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): unexpected end of tag reached.");
				}
				else if (exp.length() < 1) {
					throw new IllegalArgumentException("Illegal statement (" + i + "): the paramenter can not be empty.");
				}
				
				if (sb.length() > 0) {
					segments.add(sb.toString());
					sb.setLength(0);
				}

				El el = new El(exp);
				segments.add(el);

				i = j;
			}
			else {
				sb.append(c);
			}
		}

		if (sb.length() > 0) {
			segments.add(sb.toString());
			sb.setLength(0);
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
			throw new TemplateException(e);
		}
	}
}
