package panda.aop.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

import panda.aop.ClassAgent;
import panda.aop.DefaultClassDefiner;
import panda.aop.asm.test.Aop1;
import panda.aop.asm.test.MyMethodInterceptor;
import panda.aop.matcher.MethodMatcherFactory;
import panda.lang.Classes;
import panda.log.Log;
import panda.log.Logs;

public class Main2 {

	private static final Log log = Logs.getLog(Main2.class);
	
	public static void main(String[] args) throws Throwable {

		ClassAgent agent = new AsmClassAgent();
		agent.addInterceptor(MethodMatcherFactory.matcher(".*"), new MyMethodInterceptor());
		Class<Aop1> classZ = agent.define(DefaultClassDefiner.create(), Aop1.class);
		log.debug(classZ.toString());
		Field[] fields = classZ.getDeclaredFields();
		for (Field field : fields) {
			log.debug("找到一个Field: " + field);
		}
		Method methods[] = classZ.getDeclaredMethods();
		for (Method method : methods) {
			log.debug("找到一个Method: " + method);
		}
		Constructor<?>[] constructors = classZ.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			log.debug("找个一个Constructor: " + constructor);
		}
		Aop1 a1 = Classes.born(classZ, "Wendal");
		a1.nonArgsVoid();
		a1.argsVoid("Wendal is the best!");
		a1.mixObjectsVoid("Arg1", new Object(), 1, null);
		a1.mixArgsVoid("XX", "WendalXXX", 0, 'c', 1L, 9090L);
		a1.mixArgsVoid2("Aop1", Boolean.TRUE, 8888, 'p', 34L, false, 'b', "Gp", null, null, 23L, 90L, 78L);
		String result = (String)a1.mixArgsVoid4("WendalXXX");
		log.debug("返回值: " + result);
		try {
			a1.x();
		}
		catch (Throwable e) {
			// TODO: handle exception
		}
		a1.returnString();
		a1.returnLong();
		a1.returnBoolean();
		a1.returnByte();
		a1.returnChar();
		a1.returnFloat();
		a1.returnShort();
		a1.toString();
		a1.equals(new Object());
		a1.getLog(new StringBuilder("I am OK"));
		try {
			a1.throwError();
		}
		catch (Throwable e) {
			log.debug("抓住你：");
			e.printStackTrace(System.out);
		}
		a1.returnObjectArray();
		a1.returnLongArray();
		a1.returnBooleanArray();
		a1.returnByteArray();
		a1.returnCharArray();
		a1.returnFloatArray();
		a1.returnShortArray();
		{
			// 带异常的构造函数
			Constructor<?> constructor = a1.getClass().getConstructor(new Class<?>[] { Object.class, Object.class });
			log.debug("构造方法:" + constructor + " \n带有的异常:"
					+ Arrays.toString(constructor.getExceptionTypes()));
		}
		a1.getRunnable();
		a1.getEnum();
		log.debug("-Demo Over-");
	}

}
