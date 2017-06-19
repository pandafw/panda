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
 */
public class DefaultMirrorFactory extends MirrorFactory {

	private static final Log log = Logs.getLog(DefaultMirrorFactory.class);

	private ClassDefiner cd;

	private AopConfigration aopConfigration;

	@Override
	public <T> Class<T> getMirror(Ioc ioc, Class<T> type, String name) {
		if (MethodInterceptor.class.isAssignableFrom(type)
			|| type.getName().endsWith(ClassAgent.CLASSNAME_SUFFIX)
			|| AopConfigration.IOCNAME.equals(name)
			|| AopConfigration.class.isAssignableFrom(type)) {
			return type;
		}

		if (aopConfigration == null) {
			aopConfigration = ioc.getIfExists(AopConfigration.class, AopConfigration.IOCNAME);
		}
		if (aopConfigration == null) {
			aopConfigration = new AnnotationAopConfigration();
		}
		
		List<InterceptorPair> interceptorPairs = aopConfigration.getInterceptorPairList(ioc, type);
		if (interceptorPairs == null || interceptorPairs.size() < 1) {
			if (log.isDebugEnabled()) {
				log.debugf("%s , no config to enable AOP.", type);
			}
			return type;
		}

		cd = DefaultClassDefiner.create();

		ClassAgent agent = new AsmClassAgent();
		for (InterceptorPair interceptorPair : interceptorPairs) {
			agent.addInterceptor(interceptorPair.getMethodMatcher(), interceptorPair.getMethodInterceptor());
		}
		return agent.define(cd, type);
	}

	public void setAopConfigration(AopConfigration aopConfigration) {
		this.aopConfigration = aopConfigration;
	}
}
