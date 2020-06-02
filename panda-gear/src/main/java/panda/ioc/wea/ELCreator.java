package panda.ioc.wea;

import panda.el.EL;
import panda.ioc.Ioc;
import panda.ioc.bean.IocProxy;
import panda.lang.reflect.Creator;

public class ELCreator<T> implements Creator<T> {
	private Ioc ioc;
	private EL el;

	public ELCreator(Ioc ioc, String expr) {
		this.ioc = ioc;
		el = new EL(expr);
	}

	@SuppressWarnings("unchecked")
	public T create(Object... args) {
		return (T)el.eval(new IocProxy(ioc));
	}
}
