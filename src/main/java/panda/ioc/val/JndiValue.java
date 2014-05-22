package panda.ioc.val;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.lang.Exceptions;

/**
 * 通过JNDI查找相应的对象
 */
public class JndiValue implements ValueProxy {

	private String jndiName;
	private Context cntxt;

	public JndiValue(String jndiName) {
		this.jndiName = jndiName;
	}

	public Object get(IocMaking ing) {
		try {
			if (cntxt == null) {
				cntxt = (Context)new InitialContext().lookup("java:comp/env");
			}
			return cntxt.lookup(jndiName);
		}
		catch (NamingException e) {
			try {
				return new InitialContext().lookup(jndiName);
			}
			catch (NamingException e2) {
				try {
					return ((Context)new InitialContext().lookup("java:/comp/env")).lookup(jndiName);
				}
				catch (NamingException e3) {
					throw Exceptions.wrapThrow(e3);
				}
			}
		}
	}

}
