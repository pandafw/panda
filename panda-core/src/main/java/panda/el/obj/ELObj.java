package panda.el.obj;

import panda.el.ELContext;

public class ELObj {
	private String val;

	public ELObj(String val) {
		this.val = val;
	}

	public String getVal() {
		return val;
	}

	public Object getObj(ELContext ec) {
		if (val == null) {
			return null;
		}
		return ec.get(val);
	}

	public String toString() {
		return val;
	}
}
