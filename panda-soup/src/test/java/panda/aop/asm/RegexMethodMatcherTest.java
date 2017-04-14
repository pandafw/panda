package panda.aop.asm;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.Test;

import panda.aop.DefaultClassDefiner;
import panda.aop.asm.test.Aop1;
import panda.aop.interceptor.AbstractMethodInterceptor;
import panda.aop.interceptor.LoggingMethodInterceptor;
import panda.aop.matcher.MethodMatcherFactory;
import panda.aop.matcher.RegexMethodMatcher;
import panda.lang.Classes;

public class RegexMethodMatcherTest {

	@Test
	public void testRegexMethodMatcherStringStringInt() throws Throwable {
		AsmClassAgent agent = new AsmClassAgent();
		MyL interceptor = new MyL();
		agent.addInterceptor(new RegexMethodMatcher(null, "nonArgsVoid", 0), interceptor);
		agent.addInterceptor(MethodMatcherFactory.matcher(".*"), new LoggingMethodInterceptor());
		Class<Aop1> cls = agent.define(DefaultClassDefiner.create(), Aop1.class);
		Aop1 aop1 = Classes.born(cls, "AOP");
		aop1.nonArgsVoid();
		assertFalse(interceptor.runned);
		aop1.argsVoid(null);
		assertTrue(interceptor.runned);
	}

}

class MyL extends AbstractMethodInterceptor {

	public boolean runned = false;

	@Override
	public boolean beforeInvoke(Object obj, Method method, Object... args) {
		runned = true;
		return super.beforeInvoke(obj, method, args);
	}
}
