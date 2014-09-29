package panda.mvc.ioc.loader;

import panda.ioc.Scope;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.meta.IocObject;
import panda.mvc.annotation.At;

public class MvcAnnotationIocLoader extends AnnotationIocLoader {
	public MvcAnnotationIocLoader(String... packages) {
		super(packages);
	}

	@Override
	protected boolean isBeanAnnotated(Class<?> cls) {
		return super.isBeanAnnotated(cls) || cls.getAnnotation(At.class) != null;
	}

	@Override
	protected IocObject createIocObject(Class<?> clazz) {
		IocObject io = super.createIocObject(clazz);
		if (io != null) {
			return io;
		}
		
		At at = clazz.getAnnotation(At.class);
		if (at == null) {
			return null;
		}

		IocObject iocObject = new IocObject();
		iocObject.setType(clazz);
		iocObject.setSingleton(true);
		iocObject.setScope(Scope.REQUEST);

		setIocInjects(iocObject, clazz);

		return iocObject;
	}
}
