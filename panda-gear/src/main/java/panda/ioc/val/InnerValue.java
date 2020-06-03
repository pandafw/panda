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
	public Object get(IocMaking im) {
		IocMaking iim = im.clone(null);
		ObjectProxy op = im.getObjectMaker().makeDynamic(iim, iobj);
		return op.get(iobj.getType(), iim);
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ": " + iobj;
	}
}
