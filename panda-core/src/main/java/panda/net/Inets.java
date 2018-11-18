package panda.net;

import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;

/**
 */
public class Inets {
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
		return Numbers.formatSize(speed) + "/s";
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
