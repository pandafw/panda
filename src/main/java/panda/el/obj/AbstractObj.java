package panda.el.obj;

import panda.el.ElContext;

/**
 * 对象
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class AbstractObj implements ElObj {
	private String val;

	public AbstractObj(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public Object getObj(ElContext ec) {
		return ec.get(val);
	}

	public String toString() {
		return val;
	}
}
