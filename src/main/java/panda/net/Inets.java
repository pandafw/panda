package panda.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import panda.io.Files;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Inets {
	public static boolean isIpV6(InetAddress ia) {
		return ia instanceof Inet6Address;
	}
	
	public static boolean isIpV4(InetAddress ia) {
		return ia instanceof Inet4Address;
	}
	
	public static boolean isIntranetAddr(String ipAddr) {
		if (Strings.isEmpty(ipAddr)) {
			return false;
		}
		
		// Class A: 10.0.0.0 ~ 10.255.255.255 （10.0.0.0/8）
		// Class B: 172.16.0.0 ~ 172.31.255.255 （172.16.0.0/12）
		// Class C: 192.168.0.0 ~ 192.168.255.255 （192.168.0.0/16）
		try {
			InetAddress ia = InetAddress.getByName(ipAddr);
			return ia.isLoopbackAddress() || ia.isAnyLocalAddress() || ia.isSiteLocalAddress();
		}
		catch (UnknownHostException e) {
			return false;
		}
	}

	/**
	 * @param size size
	 * @param time ms time
	 * @return display speed string
	 */
	public static String toSpeedString(long size, long time) {
		double sec = (double)time / DateTimes.MS_SECOND; 
		if (sec <= 0) {
			return toSpeedString(size * 1000);
		}
		return toSpeedString(size / sec);
	}
	
	/**
	 * @param speed  speed per second
	 * @return speed string
	 */
	public static String toSpeedString(double speed) {
		return Files.toDisplaySize(speed) + "/s";
	}

	/**
	 * get sub domain
	 * @param domain domain
	 * @return sub domain
	 */
	public static String getSubdomain(String domain) {
		if (Strings.isEmpty(domain)) {
			return domain;
		}
		
		int i = domain.lastIndexOf('.');
		if (i > 0) {
			int j = domain.lastIndexOf('.', i - 1);
			if (j > 0) {
				return domain.substring(0, j);
			}
		}
		return Strings.EMPTY;
	}
}
