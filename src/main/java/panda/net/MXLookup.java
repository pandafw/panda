package panda.net;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import panda.lang.Collections;
import panda.lang.Strings;
import panda.lang.collection.ExpireMap;
import panda.lang.collection.LRUMap;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;

/**
 * A utility class for mx records lookup.
 */
public class MXLookup {
	private static final Log log = Logs.getLog(MXLookup.class);
	
	/**
	 * default expire time: 1h
	 */
	public static final int DEFAULT_CACHE_MAXAGE = DateTimes.SEC_HOUR;
	
	/**
	 * default cache limit: 100
	 */
	public static final int DEFAULT_CACHE_LIMIT = 100;
	
	protected static Map<String, List<String>> cache;

	/**
	 * init cache
	 * @param limit limit count
	 * @param maxAge max age (sec)
	 */
	public static synchronized void initCache(int limit, int maxAge) {
		ExpireMap<String, List<String>> em = new ExpireMap<String, List<String>>(new LRUMap<String, List<String>>(limit), maxAge);
		cache = Collections.synchronizedMap(em);
	}

	private static synchronized List<String> getCachedHosts(String hostname) {
		return cache == null ? null : cache.get(hostname);
	}

	private static synchronized void putHostsToCache(String hostname, List<String> hosts) {
		if (cache == null) {
			initCache(DEFAULT_CACHE_LIMIT, DEFAULT_CACHE_MAXAGE);
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
		
		if (log.isDebugEnabled()) {
			log.debug("MX Lookup for " + hostname);
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

		hosts = new ArrayList<String>(attr.size());
		for (int i = 0; i < attr.size(); i++) {
			String[] ss = Strings.split((String)attr.get(i), ' ');
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
