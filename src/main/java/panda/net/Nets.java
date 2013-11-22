package panda.net;

import panda.io.Files;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

/**
 * @author yf.frank.wang@gmail.com
 */
public class Nets {
	public static boolean isIntranetHost(String ipAddr) {
		if (Strings.isEmpty(ipAddr)) {
			return false;
		}
		return ipAddr.startsWith("127.") 
				|| ipAddr.startsWith("10.")
				|| ipAddr.startsWith("0.1.")
				|| ipAddr.startsWith("0:0:0:0:");
	}
	
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
}
