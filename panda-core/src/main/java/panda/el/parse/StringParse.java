package panda.el.parse;

import panda.el.ElException;
import panda.el.Parse;

/**
 * 字符串转换器
 */
public class StringParse implements Parse {
	public Object fetchItem(CharQueue exp) {
		// @ JKTODO 添加转意字符
		switch (exp.peek()) {
		case '\'':
		case '"':
			StringBuilder sb = new StringBuilder();
			char end = exp.poll();
			while (!exp.isEmpty() && exp.peek() != end) {
				if (exp.peek() == '\\') {
					parseSp(exp, sb);
				}
				else {
					sb.append(exp.poll());
				}
			}
			exp.poll();
			return sb.toString();
		}
		return NULL;
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
		case 'u':
			char[] hex = new char[4];
			for (int i = 0; i < 4; i++)
				hex[i] = exp.poll();
			sb.append((char)Integer.valueOf(new String(hex), 16).intValue());
			break;
		case 'b':
			sb.append(' ');
			break;
		case 'f':
			sb.append('\f');
			break;
		default:
			throw new ElException("Unexpected char");
		}
	}
}
