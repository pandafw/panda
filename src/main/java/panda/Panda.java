package panda;

import java.security.AccessController;
import java.security.PrivilegedAction;

import panda.aop.ClassDefiner;
import panda.aop.DefaultClassDefiner;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Panda {
	public static final String VERSION = "1.0.0";

	public static ClassDefiner cd() {
		return AccessController.doPrivileged(new PrivilegedAction<DefaultClassDefiner>() {
			public DefaultClassDefiner run() {
				return new DefaultClassDefiner(Panda.class.getClassLoader());
			}
		});
	}
}
