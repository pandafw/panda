package panda.ioc.aop.config.impl;

import java.util.regex.Pattern;

import panda.aop.MethodMatcher;
import panda.aop.matcher.MethodMatcherFactory;
import panda.lang.Objects;

public class AopConfigrationItem {

	private String name;

	private String method;

	private String interceptor;

	private boolean singleton;

	private MethodMatcher methodMatcher;

	private Pattern classnamePattern;

	public AopConfigrationItem() {
	}

	public AopConfigrationItem(String name, String method, String interceptor, boolean singleton) {
		setName(name);
		setMethod(method);
		setInterceptor(interceptor);
		setSingleton(singleton);
	}

	public String getName() {
		return name;
	}

	public void setName(String className) {
		this.name = className;
		this.classnamePattern = Pattern.compile(className);
	}

	public boolean matchClassName(String className) {
		return classnamePattern.matcher(className).find();
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String methodName) {
		this.method = methodName;
		this.methodMatcher = MethodMatcherFactory.matcher(methodName);
	}

	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
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

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("name", name)
				.append("method", method)
				.append("interceptor", interceptor)
				.append("singletion", singleton)
				.toString();
	}
}
