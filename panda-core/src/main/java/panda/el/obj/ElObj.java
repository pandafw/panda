package panda.el.obj;

import panda.el.ElContext;

public class ElObj {
	private String val;

	public ElObj(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public Object getObj(ElContext ec) {
		if (val == null) {
			return null;
		}
		return ec.get(val);
	}

	public String toString() {
		return val;
	}
}
