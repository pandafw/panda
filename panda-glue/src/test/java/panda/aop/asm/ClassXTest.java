package panda.aop.asm;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import panda.aop.ClassAgent;
import panda.aop.ClassDefiner;
import panda.aop.DefaultClassDefiner;
import panda.aop.asm.test.Aop1;
import panda.aop.asm.test.Aop7;
import panda.aop.asm.test.MyMethodInterceptor;
import panda.aop.asm.test.ZZZ;
import panda.aop.interceptor.AbstractMethodInterceptor;
import panda.aop.interceptor.LoggingMethodInterceptor;
import panda.aop.matcher.MethodMatcherFactory;
import panda.aop.matcher.RegexMethodMatcher;
import panda.lang.Classes;
import panda.lang.reflect.Types;

public class ClassXTest {

	ClassAgent classAgent;

	@Before
	public void setUp() {
		classAgent = new AsmClassAgent();
		classAgent.addInterceptor(new RegexMethodMatcher(".*"), new AbstractMethodInterceptor() {
		});
		classAgent.addInterceptor(new RegexMethodMatcher(".*"), new MyMethodInterceptor());
		classAgent.addInterceptor(new RegexMethodMatcher(".*"), new MyMethodInterceptor());
		classAgent.addInterceptor(new RegexMethodMatcher(".*"), new AbstractMethodInterceptor() {
		});
		classAgent.addInterceptor(MethodMatcherFactory.matcher(".*"), new LoggingMethodInterceptor());
	}

	@Test
	public void testCreat() {
		classAgent.define(DefaultClassDefiner.create(), Object.class);
		classAgent.define(DefaultClassDefiner.create(), getClass());
	}

	@Test(expected = RuntimeException.class)
	public void testInterface() {
		classAgent.define(DefaultClassDefiner.create(), Runnable.class);
	}

	@Test
	public void testDupAop() {
		Class<Aop1> klass = Aop1.class;
		for (int i = 0; i < 10000; i++) {
			klass = classAgent.define(DefaultClassDefiner.create(), klass);
		}
		Assert.assertFalse(Aop1.class == klass);
	}

	@Test
	public void testBorn() throws Exception {
		Class<Aop1> klass = classAgent.define(DefaultClassDefiner.create(), Aop1.class);
		Aop1 a1 = Classes.newInstance(klass, "Nut", String.class);
		a1.returnObjectArray();
	}

	@Test
	public void testCreate2() throws Throwable {
		ClassDefiner cd = DefaultClassDefiner.create();

		Class<?> obj = classAgent.define(cd, Aop1.class);
		Class<?> obj2 = classAgent.define(cd, Aop1.class);
		Assert.assertEquals(obj, obj2);
	}

	@Test
	public void testConstructors() {
		getNewInstance(Aop1.class);
	}

	@Test
	public void testConstructor2() {
		Class<Aop1> newClass = classAgent.define(DefaultClassDefiner.create(), Aop1.class);
		Assert.assertTrue(newClass.getDeclaredConstructors().length > 0);
	}

	@Test
	public void testReturnPrimitive() throws Throwable {
		Aop1 a1 = classAgent.define(DefaultClassDefiner.create(), Aop1.class).getConstructor(String.class).newInstance("AoP");
		a1.returnLong();
		a1.returnBoolean();
		a1.returnByte();
		a1.returnChar();
		a1.returnFloat();
		a1.returnShort();
		a1.returnDouble();
	}

	@Test
	public void testReturnPrimitiveArray() {
		Aop1 a1 = getNewInstance(Aop1.class);
		a1.returnIntArray();
		a1.returnLongArray();
		a1.returnBooleanArray();
		a1.returnByteArray();
		a1.returnCharArray();
		a1.returnFloatArray();
		a1.returnShortArray();
		a1.returnDoubleArray();
	}

	@Test
	public void testReturnObject() throws Throwable {
		Aop1 a1 = getNewInstance(Aop1.class);
		a1.returnString();
		a1.returnObjectArray();
		a1.getRunnable();
		a1.getEnum();
	}

	@Test(expected = RuntimeException.class)
	public void testThrowError() throws Throwable {
		Aop1 a1 = getNewInstance(Aop1.class);
		a1.throwError();
	}

	@Test(expected = Exception.class)
	public void testThrowException() throws Throwable {
		Aop1 a1 = getNewInstance(Aop1.class);
		a1.throwException();
	}

	@Test
	public void testArgs() throws Throwable {
		Aop1 a1 = getNewInstance(Aop1.class);
		a1.nonArgsVoid();
		a1.argsVoid("Wendal is the best!");
		a1.mixObjectsVoid("Arg1", new Object(), 1, null);
		a1.mixArgsVoid("XX", "WendalXXX", 0, 'c', 1L, 9090L);
		a1.mixArgsVoid2("Aop1", Boolean.TRUE, 8888, 'p', 34L, false, 'b', "Gp", null, null, 23L, 90L, 78L);
		String result = String.valueOf(a1.mixArgsVoid4("WendalXXX"));
		Assert.assertEquals("WendalXXX", result);
	}

	private <T> T getNewInstance(Class<T> klass) {
		Class<T> newClass = classAgent.define(DefaultClassDefiner.create(), klass);
		T obj = Classes.born(newClass, "AoP", String.class);
		System.out.println(obj.getClass().getSuperclass());
		return obj;
	}

	@Test
	public void test_annotation() {
		ZZZ z = getNewInstance(ZZZ.class);
		z.p(null);
	}

	@Test
	public void test_signature() throws Throwable {
		Class<?> clazz = classAgent.define(DefaultClassDefiner.create(), Aop7.class);
		System.out.println(clazz.newInstance());
		Assert.assertTrue(Aop1.class.equals(Types.getRawType(Types.getDeclaredGenericTypeParam(clazz, 0))));
	}
}
