package panda.net;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class NetUtils {
	public static boolean isIntranetHost(String ipAddr) {
		if (Strings.isEmpty(ipAddr)) {
			return false;
		}
		return ipAddr.startsWith("127.") 
				|| ipAddr.startsWith("10.")
				|| ipAddr.startsWith("0.1.")
				|| ipAddr.startsWith("0:0:0:0:");
	}
}
