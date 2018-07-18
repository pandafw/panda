package panda.ioc.val;

import panda.ioc.IocMaking;
import panda.ioc.ObjectProxy;
import panda.ioc.ValueProxy;
import panda.ioc.meta.IocObject;

public class InnerValue implements ValueProxy {

	private IocObject iobj;

	public InnerValue(IocObject iobj) {
		this.iobj = iobj;
	}

	@Override
	public Object get(IocMaking ing) {
		IocMaking innering = ing.clone(null);
		ObjectProxy op = ing.getMaker().make(innering, iobj);
		return op.get(iobj.getType(), innering);
	}

	@Override
	public String toString() {
		return String.valueOf(iobj);
	}
}
