package panda.aop.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

import panda.aop.AbstractClassAgent;
import panda.aop.ClassDefiner;
import panda.aop.MethodInterceptor;
import panda.asm.Opcodes;
import panda.lang.Systems;
import panda.lang.reflect.Fields;
import panda.log.Log;
import panda.log.Logs;

/**
 * @author wendal(wendal1985@gmail.com)
 */
public class AsmClassAgent extends AbstractClassAgent {
	private static Log log = Logs.getLog(AsmClassAgent.class);
	
	protected static int CLASS_LEVEL = Opcodes.V1_5;

	protected static final String MethodArray_FieldName = "_$$AopMethodArray";
	protected static final String MethodInterceptorList_FieldName = "_$$AopMethodInterceptorList";

	static {
		if (Systems.IS_JAVA_1_6) {
			CLASS_LEVEL = Opcodes.V1_6;
		}
		else if (Systems.IS_JAVA_1_7) {
			CLASS_LEVEL = Opcodes.V1_7;
		}
		log.debug("AsmClassAgent will define class in Version " + CLASS_LEVEL);
	}

	@SuppressWarnings("unchecked")
	protected <T> Class<T> generate(ClassDefiner cd, Pair2[] pair2s, String newName, Class<T> klass,
			Constructor<T>[] constructors) {
		try {
			return (Class<T>)cd.load(newName);
		}
		catch (ClassNotFoundException e) {
		}

		Method[] methodArray = new Method[pair2s.length];
		List<MethodInterceptor>[] methodInterceptorList = new List[pair2s.length];
		for (int i = 0; i < pair2s.length; i++) {
			Pair2 pair2 = pair2s[i];
			methodArray[i] = pair2.method;
			methodInterceptorList[i] = pair2.listeners;
		}
		byte[] bytes = ClassY.enhandClass(klass, newName, methodArray, constructors);

//		try {
//			Files.write(new File(newName), bytes);
//		}
//		catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		Class<T> newClass = (Class<T>)cd.define(newName, bytes);
		
//		System.out.println(Strings.repeat('-', 80));
//		System.out.println(newClass);
//		System.out.println(Strings.repeat('-', 80));
//		for (Field f : Fields.getAllFieldsList(newClass)) {
//			System.out.println(f);
//		}
//		System.out.println(Strings.repeat('-', 80));
//		for (Method m : Methods.getAllMethods(newClass)) {
//			System.out.println(m);
//		}

		try {
			Fields.writeStaticField(newClass, MethodArray_FieldName, methodArray, true);
		}
		catch (Throwable e) {
			log.warn("Failed to set " + MethodArray_FieldName, e);
		}

		try {
			Fields.writeStaticField(newClass, MethodInterceptorList_FieldName, methodInterceptorList, true);
		}
		catch (Throwable e) {
			log.warn("Failed to set " + MethodInterceptorList_FieldName, e);
		}

		return newClass;
	}

}
