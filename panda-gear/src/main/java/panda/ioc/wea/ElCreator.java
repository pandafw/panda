package panda.ioc.wea;

import panda.el.El;
import panda.ioc.Ioc;
import panda.ioc.bean.IocProxy;
import panda.lang.Creator;

public class ElCreator<T> implements Creator<T> {
	private Ioc ioc;
	private El el;

	public ElCreator(Ioc ioc, String expr) {
		this.ioc = ioc;
		el = new El(expr);
	}

	@SuppressWarnings("unchecked")
	public T create(Object... args) {
		return (T)el.eval(new IocProxy(ioc));
	}
}
