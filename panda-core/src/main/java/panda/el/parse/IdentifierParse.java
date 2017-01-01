package panda.el.parse;

import panda.el.Parse;
import panda.el.obj.ElObj;

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
			if (sb.toString().equals("null")) {
				return new ElObj(null);
			}
			if (sb.toString().equals("true")) {
				return Boolean.TRUE;
			}
			if (sb.toString().equals("false")) {
				return Boolean.FALSE;
			}
			return new ElObj(sb.toString());
		}
		return NULL;
	}

}
