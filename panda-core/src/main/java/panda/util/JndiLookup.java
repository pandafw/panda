package panda.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiLookup {
	public static Object lookup(String name) throws NamingException {
		Context ctx = new InitialContext();
		try {
			return ((Context)ctx.lookup("java:comp/env")).lookup(name);
		}
		catch (NamingException e) {
			try {
				return ctx.lookup(name);
			}
			catch (NamingException e2) {
				return ((Context)ctx.lookup("java:/comp/env")).lookup(name);
			}
		}
	}
}
