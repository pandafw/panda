package panda.ioc.aop.config.impl;

import java.util.regex.Pattern;

import panda.aop.MethodMatcher;
import panda.aop.matcher.MethodMatcherFactory;

public class AopConfigrationItem {

	private String name;

	private String method;

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

	public AopConfigrationItem(String name, String method, String interceptor, boolean singleton) {
		super();
		this.name = name;
		this.method = method;
		this.interceptor = interceptor;
		this.singleton = singleton;
	}

	public String getName() {
		return name;
	}

	public void setName(String className) {
		this.name = className;
		this.classnamePattern = Pattern.compile(className);
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String methodName) {
		this.method = methodName;
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
