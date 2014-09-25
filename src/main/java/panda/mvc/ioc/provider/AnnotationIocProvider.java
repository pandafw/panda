package panda.mvc.ioc.provider;

import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.meta.IocObject;
import panda.lang.Strings;
import panda.mvc.IocProvider;
import panda.mvc.MvcConfig;
import panda.mvc.annotation.At;
import panda.mvc.ioc.MvcIoc;


public class AnnotationIocProvider implements IocProvider {
	public static class MvcAnnotationIocLoader extends AnnotationIocLoader {
		public MvcAnnotationIocLoader(String... packages) {
			super(packages);
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

		protected String getBeanName(Class<?> cls) {
			return getBeanName(cls, cls.getAnnotation(IocBean.class), cls.getAnnotation(At.class));
		}

		public static String getBeanName(Class<?> cls, IocBean iocBean, At at) {
			if (iocBean != null) {
				return getBeanName(cls, iocBean);
			}
			
			if (at != null) {
				return cls.getName();
			}
			return null;
		}
	}
	
	public Ioc create(MvcConfig config, Object... args) {
		String[] pkgs;
		if (args == null || args.length == 0) {
			pkgs = new String[] { config.getMainModule().getPackage().getName() };
		}
		else {
			pkgs = Strings.toStringArray(args);
		}
		return new MvcIoc(new MvcAnnotationIocLoader(pkgs));
	}

}
