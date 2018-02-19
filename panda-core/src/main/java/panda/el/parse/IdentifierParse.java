package panda.el.parse;

import panda.el.Parse;
import panda.el.obj.ELObj;

/**
 * 标识符转换
 */
public class IdentifierParse implements Parse {

	public Object fetchItem(CharQueue exp) {
		StringBuilder sb = new StringBuilder();
		if (Character.isJavaIdentifierStart(exp.peek())) {
			sb.append(exp.poll());
			while (!exp.isEmpty() && Character.isJavaIdentifierPart(exp.peek())) {
				sb.append(exp.poll());
			}
			
			String s = sb.toString();
			if (s.equals("null")) {
				return new ELObj(null);
			}
			if (s.equals("true")) {
				return Boolean.TRUE;
			}
			if (s.equals("false")) {
				return Boolean.FALSE;
			}
			return new ELObj(sb.toString());
		}
		return NULL;
	}

}
