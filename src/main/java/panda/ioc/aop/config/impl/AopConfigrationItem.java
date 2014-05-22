package panda.ioc.aop.config.impl;

import java.util.regex.Pattern;

import panda.aop.MethodMatcher;
import panda.aop.matcher.MethodMatcherFactory;

public class AopConfigrationItem {

	private String className;

	private String methodName;

	private String interceptor;

	private boolean singleton;

	private MethodMatcher methodMatcher;

	private Pattern classnamePattern;

	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

	public boolean matchClassName(String className) {
		return classnamePattern.matcher(className).find();
	}

	public AopConfigrationItem() {
	}

	public AopConfigrationItem(String className, String methodName, String interceptor, boolean singleton) {
		super();
		this.className = className;
		this.methodName = methodName;
		this.interceptor = interceptor;
		this.singleton = singleton;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
		this.classnamePattern = Pattern.compile(className);
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
		this.methodMatcher = MethodMatcherFactory.matcher(methodName);
	}

	public String getInterceptor() {
		return interceptor;
	}

	public void setInterceptor(String interceptor) {
		this.interceptor = interceptor;
	}

	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

}
