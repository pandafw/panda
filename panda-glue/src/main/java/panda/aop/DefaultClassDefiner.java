package panda.aop;

import java.security.AccessController;
import java.security.PrivilegedAction;

public class DefaultClassDefiner extends ClassLoader implements ClassDefiner {
	public static ClassDefiner create() {
		return AccessController.doPrivileged(new PrivilegedAction<DefaultClassDefiner>() {
			public DefaultClassDefiner run() {
				return new DefaultClassDefiner(DefaultClassDefiner.class.getClassLoader());
			}
		});
	}

	public DefaultClassDefiner(ClassLoader parent) {
		super(parent);
	}

	public Class<?> define(String className, byte[] bytes) throws ClassFormatError {
		try {
			return load(className);
		}
		catch (ClassNotFoundException e) {
		}
		// If not found ...
		return defineClass(className, bytes, 0, bytes.length);
	}

	public boolean has(String className) {
		try {
			load(className);
			return true;
		}
		catch (ClassNotFoundException e) {
		}
		return false;
	}

	public Class<?> load(String className) throws ClassNotFoundException {
		try {
			ClassLoader cl = Thread.currentThread().getContextClassLoader();
			if (cl != null) {
				return cl.loadClass(className);
			}
		}
		catch (Throwable e) {
			try {
				return ClassLoader.getSystemClassLoader().loadClass(className);
			}
			catch (Throwable e2) {
				try {
					return getParent().loadClass(className);
				}
				catch (Throwable e3) {
				}
			}
		}
		return super.loadClass(className);
	}
}
