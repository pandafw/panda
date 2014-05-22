package panda.ioc.aop.impl;

import java.util.List;

import panda.aop.ClassAgent;
import panda.aop.ClassDefiner;
import panda.aop.DefaultClassDefiner;
import panda.aop.MethodInterceptor;
import panda.aop.asm.AsmClassAgent;
import panda.ioc.Ioc;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.aop.config.AopConfigration;
import panda.ioc.aop.config.InterceptorPair;
import panda.ioc.aop.config.impl.AnnotationAopConfigration;
import panda.log.Log;
import panda.log.Logs;

/**
 * 通过AopConfigration来识别需要拦截的方法,并根据需要生成新的类
 * 
 * @author zozoh(zozohtnt@gmail.com)
 * @author Wendal(wendal1985@gmail.com)
 */
public class DefaultMirrorFactory implements MirrorFactory {

	private static final Log log = Logs.getLog(DefaultMirrorFactory.class);

	private Ioc ioc;

	private ClassDefiner cd;

	private AopConfigration aopConfigration;

	private static final Object lock = new Object();

	public DefaultMirrorFactory(Ioc ioc) {
		this.ioc = ioc;
	}

	public <T> Class<T> getMirror(Class<T> type, String name) {
		if (MethodInterceptor.class.isAssignableFrom(type)
			|| type.getName().endsWith(ClassAgent.CLASSNAME_SUFFIX)
			|| AopConfigration.IOCNAME.equals(name)
			|| AopConfigration.class.isAssignableFrom(type)) {
			return type;
		}

		if (aopConfigration == null) {
			if (ioc.has(AopConfigration.IOCNAME)) {
				aopConfigration = ioc.get(AopConfigration.class, AopConfigration.IOCNAME);
			}
			else {
				aopConfigration = new AnnotationAopConfigration();
			}
		}
		
		List<InterceptorPair> interceptorPairs = aopConfigration.getInterceptorPairList(ioc, type);
		if (interceptorPairs == null || interceptorPairs.size() < 1) {
			if (log.isDebugEnabled()) {
				log.debugf("%s , no config to enable AOP.", type);
			}
			return type;
		}

		synchronized (lock) {
			// 这段代码的由来:
			// 当用户把nutz.jar放到java.ext.dirs下面时,DefaultMirrorFactory的classloader将无法获取用户的类
			if (cd == null) {
				ClassLoader classLoader = type.getClassLoader();
				if (classLoader == null) {
					classLoader = Thread.currentThread().getContextClassLoader();
					if (classLoader == null)
						classLoader = getClass().getClassLoader();
				}
				log.info("Use as AOP ClassLoader parent : " + classLoader);
				DefaultClassDefiner.init(classLoader);
				cd = DefaultClassDefiner.defaultOne();
			}
			ClassAgent agent = new AsmClassAgent();
			for (InterceptorPair interceptorPair : interceptorPairs)
				agent.addInterceptor(interceptorPair.getMethodMatcher(),
									 interceptorPair.getMethodInterceptor());
			return agent.define(cd, type);
		}

	}

	public void setAopConfigration(AopConfigration aopConfigration) {
		this.aopConfigration = aopConfigration;
	}
}
