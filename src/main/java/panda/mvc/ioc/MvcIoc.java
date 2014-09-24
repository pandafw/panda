package panda.mvc.ioc;

import panda.ioc.IocContext;
import panda.ioc.IocLoader;
import panda.ioc.ObjectMaker;
import panda.ioc.annotation.IocBean;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.impl.DefaultIoc;
import panda.mvc.annotation.At;
import panda.mvc.ioc.provider.AnnotationIocProvider.MvcAnnotationIocLoader;

public class MvcIoc extends DefaultIoc {
	/**
	 * @param loader
	 * @param context
	 * @param defaultScope
	 */
	public MvcIoc(IocLoader loader, IocContext context, String defaultScope) {
		super(loader, context, defaultScope);
	}

	/**
	 * @param loader
	 */
	public MvcIoc(IocLoader loader) {
		super(loader);
	}

	/**
	 * @param maker
	 * @param loader
	 * @param context
	 * @param defaultScope
	 * @param mirrors
	 */
	public MvcIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope, MirrorFactory mirrors) {
		super(maker, loader, context, defaultScope, mirrors);
	}

	/**
	 * @param maker
	 * @param loader
	 * @param context
	 * @param defaultScope
	 */
	public MvcIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope) {
		super(maker, loader, context, defaultScope);
	}

	@Override
	protected String getBeanName(Class<?> type) {
		return MvcAnnotationIocLoader.getBeanName(type, type.getAnnotation(IocBean.class), type.getAnnotation(At.class));
	}
	
}
