package panda.el.parse;

import panda.el.ELException;

/**
 * Number parse
 */
public class NumberParse implements Parse {

	@Override
	public Object fetchItem(CharQueue exp) {
		StringBuilder sb = new StringBuilder();
		switch (exp.peek()) {
		case '.':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			boolean dot = exp.peek() == '.';
			sb.append(exp.poll());
			while (!exp.isEmpty()) {
				switch (exp.peek()) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					sb.append(exp.poll());
					break;
				case '.':
					if (dot) {
						throw new ELException("Invalid number expression, multiple '.'!");
					}
					dot = true;
					sb.append(exp.poll());
					break;
				case 'l':
				case 'L':
					sb.append(exp.poll());
					return Long.parseLong(sb.toString());
				case 'f':
				case 'F':
					sb.append(exp.poll());
					return Float.parseFloat(sb.toString());
				case 'd':
				case 'D':
					sb.append(exp.poll());
					return Double.parseDouble(sb.toString());
				default:
					if (dot) {
						return Double.parseDouble(sb.toString());
					}
					return Integer.parseInt(sb.toString());
				}
			}
			if (dot) {
				return Double.parseDouble(sb.toString());
			}
			return Integer.parseInt(sb.toString());
		}
		return null;
	}

}
