package panda.el.parse;

import panda.el.ELException;

/**
 * String parse
 */
public class StringParse implements Parse {

	@Override
	public Object fetchItem(CharQueue exp) {
		switch (exp.peek()) {
		case '\'':
		case '"':
			StringBuilder sb = new StringBuilder();
			char end = exp.poll();
			while (!exp.isEmpty() && exp.peek() != end) {
				if (exp.peek() == '\\') {
					exp.poll();
					parseSp(exp, sb);
				}
				else {
					sb.append(exp.poll());
				}
			}
			exp.poll();
			return sb.toString();
		}
		return null;
	}

	private void parseSp(CharQueue exp, StringBuilder sb) {
		switch (exp.poll()) {
		case 'n':
			sb.append('\n');
			break;
		case 'r':
			sb.append('\r');
			break;
		case 't':
			sb.append('\t');
			break;
		case '\\':
			sb.append('\\');
			break;
		case '\'':
			sb.append('\'');
			break;
		case '\"':
			sb.append('\"');
			break;
		case 'x':
			sb.append(hex2char(exp, 2));
			break;
		case 'u':
			sb.append(hex2char(exp, 4));
			break;
		case 'b':
			sb.append(' ');
			break;
		case 'f':
			sb.append('\f');
			break;
		default:
			throw new ELException("Unexpected char after \\");
		}
	}
	
	private char hex2char(CharQueue exp, int len) {
		char[] hex = new char[len];
		for (int i = 0; i < len; i++) {
			hex[i] = exp.poll();
		}
		return (char)Integer.valueOf(new String(hex), 16).intValue();
	}
}
