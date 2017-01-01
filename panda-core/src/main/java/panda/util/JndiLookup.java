package panda.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JndiLookup {
	public static Object lookup(String name) throws NamingException {
		try {
			return ((Context)new InitialContext().lookup("java:comp/env")).lookup(name);
		}
		catch (NamingException e) {
			try {
				return new InitialContext().lookup(name);
			}
			catch (NamingException e2) {
				return ((Context)new InitialContext().lookup("java:/comp/env")).lookup(name);
			}
		}
	}
}
