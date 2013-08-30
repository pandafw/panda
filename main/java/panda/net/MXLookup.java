package panda.net;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import panda.lang.Collections;
import panda.lang.collection.ExpireMap;

/**
 * A utility class for mx records lookup.
 * @author yf.frank.wang@gmail.com
 */
public class MXLookup {
	/**
	 * default expire time 5m
	 */
	public static final int DEFAULT_EXPIRE = 5 * 60;
	
	protected static Map<String, List<String>> cache;

	/**
	 * init cache
	 * @param expire expire time (s)
	 */
	public static void initCache(int expire) {
		ExpireMap<String, List<String>> em = new ExpireMap<String, List<String>>(new WeakHashMap<String, List<String>>(), expire * 1000);
		cache = Collections.synchronizedMap(em);
	}

	private static List<String> getCachedHosts(String hostname) {
		return cache == null ? null : cache.get(hostname);
	}

	private static void putHostsToCache(String hostname, List<String> hosts) {
		if (cache == null) {
			initCache(DEFAULT_EXPIRE);
		}
		cache.put(hostname, hosts);
	}

	/**
	 * lookup mx records by hostname
	 * @param hostname host name
	 * @return server list
	 * @throws NamingException if a naming error occurs
	 */
	public static List<String> lookup(String hostname) throws NamingException {
		List<String> hosts = getCachedHosts(hostname);
		if (Collections.isNotEmpty(hosts)) {
			return hosts;
		}
		

		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");

		DirContext ictx = new InitialDirContext(env);

		Attributes attrs = ictx.getAttributes(hostname, new String[] { "MX" });
		Attribute attr = attrs.get("MX");

		// if we don't have an MX record, try the machine itself
		if ((attr == null) || (attr.size() == 0)) {
			attrs = ictx.getAttributes(hostname, new String[] { "A" });
			attr = attrs.get("A");
			if (attr == null) {
				throw new NamingException("No match for name '" + hostname + "'");
			}
		}

		hosts = new ArrayList<String>();
		for (int i = 0; i < attr.size(); i++) {
			String[] ss = ((String)attr.get(i)).split(" ");
			if (ss.length == 1) {
				hosts.add(ss[0]);
			}
			else if (ss[1].endsWith(".")) {
				hosts.add(ss[1].substring(0, (ss[1].length() - 1)));
			}
			else {
				hosts.add(ss[1]);
			}
		}

		putHostsToCache(hostname, hosts);
		return hosts;
	}
}
