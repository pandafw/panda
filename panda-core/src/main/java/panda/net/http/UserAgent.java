package panda.net.http;

import panda.lang.Numbers;
import panda.lang.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 */
public class UserAgent {

	public static class Version {
		public String version;
		public int major;
		public int minor;
		public Version(String version, int major, int minor) {
			this.version = version;
			this.major = major;
			this.minor = minor;
		}

		@Override
		public String toString() {
			return version;
		}
	}
	
	private static final Version DUMMY = new Version("", 0, 0);

	private String userAgent;
	private Map<String, Version> browsers;

	public static final String CHROME = "Chrome";
	public static final String FIREFOX = "Firefox";
	public static final String EDGE = "Edge";
	public static final String MSIE = "MSIE";
	public static final String MSIE11 = "Trident";
	public static final String NETSCAPE = "Netscape";
	public static final String OPERA = "Opera";
	public static final String SAFARI = "Safari";
	public static final String IPHONE = "iPhone";
	public static final String IPAD = "iPad";
	public static final String IPOD = "iPod";
	public static final String IOS = "ios";
	public static final String ANDROID = "Android";
	public static final String WINDOWS = "Windows";
	public static final String MACINTOSH = "Macintosh";
	public static final String LINUX = "Linux";
	public static final String UBUNTU = "Ubuntu";
	public static final String MINT = "Mint";
	public static final String FEDORA = "Fedora";
	public static final String GENTOO = "Gentoo";
	
	private static final String[] ROBOTS = { 
		"robot", "Googlebot", "bingbot", "yahoo", "baidu", 
		"msnbot", "ask.", "naver.", "DotBot", "Yandex", "goo.ne.jp"
	};

	public static final String MOBILE = "mobile";
	public static final String ROBOT = "robot";

	/**
	 * @param userAgent user agent
	 */
	public UserAgent(String userAgent) {
		super();
		this.userAgent = userAgent;
		init();
	}

	/**
	 * @return true if user agent is Chrome
	 */
	public boolean isChrome() {
		return browsers.containsKey(CHROME);
	}

	/**
	 * @return true if user agent is EDGE
	 */
	public boolean isEdge() {
		return browsers.containsKey(EDGE);
	}

	/**
	 * @return true if user agent is MSIE
	 */
	public boolean isMsie() {
		return browsers.containsKey(MSIE);
	}

	/**
	 * @return true if user agent is Netscape
	 */
	public boolean isNetscape() {
		return browsers.containsKey(NETSCAPE);
	}

	/**
	 * @return true if user agent is Firefox
	 */
	public boolean isFirefox() {
		return browsers.containsKey(FIREFOX);
	}

	/**
	 * @return true if user agent is Safari
	 */
	public boolean isSafari() {
		return browsers.containsKey(SAFARI);
	}

	/**
	 * @return true if user agent is Opera
	 */
	public boolean isOpera() {
		return browsers.containsKey(OPERA);
	}

	/**
	 * @return true if user agent is Mobile
	 */
	public boolean isMobile() {
		return browsers.containsKey(MOBILE);
	}

	/**
	 * @return true if user agent is Robot
	 */
	public boolean isRobot() {
		return browsers.containsKey(ROBOT);
	}

	/**
	 * @return true if user agent is Android
	 */
	public boolean isAndroid() {
		return browsers.containsKey(ANDROID);
	}

	/**
	 * @return true if user agent is iPad
	 */
	public boolean isIpad() {
		return browsers.containsKey(IPAD);
	}

	/**
	 * @return true if user agent is iPhone
	 */
	public boolean isIphone() {
		return browsers.containsKey(IPHONE);
	}

	/**
	 * @return true if user agent is iPod
	 */
	public boolean isIpod() {
		return browsers.containsKey(IPOD);
	}

	/**
	 * @return true if user agent is ios
	 */
	public boolean isIos() {
		return browsers.containsKey(IOS);
	}

	/**
	 * @return true if user agent is windows
	 */
	public boolean isWindows() {
		return browsers.containsKey(WINDOWS);
	}

	/**
	 * @return true if user agent is Macintosh
	 */
	public boolean isMacintosh() {
		return browsers.containsKey(MACINTOSH);
	}

	/**
	 * @return true if user agent is Linux
	 */
	public boolean isLinux() {
		return browsers.containsKey(LINUX);
	}

	/**
	 * @return true if user agent is Ubuntu
	 */
	public boolean isUbuntu() {
		return browsers.containsKey(UBUNTU);
	}

	/**
	 * @return true if user agent is Fedora
	 */
	public boolean isFedora() {
		return browsers.containsKey(FEDORA);
	}

	/**
	 * @return true if user agent is Mint
	 */
	public boolean isMint() {
		return browsers.containsKey(MINT);
	}

	/**
	 * @return true if user agent is Gentoo
	 */
	public boolean isGentoo() {
		return browsers.containsKey(GENTOO);
	}
	
	private void init() {
		browsers = new HashMap<String, Version>();
		
		if (Strings.isEmpty(userAgent)) {
			return;
		}
		
		if (parse(EDGE) 
				|| parse(CHROME)
				|| parse(FIREFOX)
				|| parse(MSIE)
				|| parse(MSIE11, MSIE, 11)
				|| parse(OPERA)
				|| parse(SAFARI)
				|| parse(NETSCAPE)) {
		}

		if (parse(IPHONE) || parse(IPAD) || parse(IPOD)
				|| parse(ANDROID) || parse(WINDOWS) || parse(MACINTOSH)) {
		}
		else if (parse(LINUX)) {
			if (parse(UBUNTU) || parse(MINT) || parse(FEDORA) || parse(GENTOO)) {}
		}
		
		if (checkRobot()) {
			browsers.put(ROBOT, DUMMY);
		}
		
		if (isAndroid()) {
			browsers.put(MOBILE, DUMMY);
		}
		if (isIphone() || isIpad() || isIpod()) {
			browsers.put(MOBILE, DUMMY);
			browsers.put(IOS, DUMMY);
		}
	}

	private boolean parse(String client) {
		return parse(client, client, 0);
	}
	
	private boolean parse(String client, String alias, int major) {
		int i = userAgent.indexOf(client);
		if (i < 0) {
			return false;
		}

		for (i += client.length(); i < userAgent.length(); i++) {
			char c = userAgent.charAt(i);
			if (Character.isDigit(c)) {
				break;
			}
		}
		
		int j = i;
		for (; j < userAgent.length(); j++) {
			char c = userAgent.charAt(j);
			if (!Character.isDigit(c) && c != '.') {
				break;
			}
		}
		
		String ver = "";
		if (i < userAgent.length()) {
			ver = userAgent.substring(i, j);
		}

		if (major == 0) {
			major = this.parseMajorVersion(ver);
		}
		browsers.put(alias, new Version(ver, major, 0));
		return true;
	}
	
	private boolean checkRobot() {
		for (String s : ROBOTS) {
			if (userAgent.indexOf(s) >= 0) {
				return true;
			}
		}
		return false;
	}
	
	private int parseMajorVersion(String ver) {
		if (ver != null) {
			int i = ver.indexOf('.');
			if (i > 0) {
				return Numbers.toInt(ver.substring(0, i), 0);
			}
		}
		return 0;
	}

	/**
	 * @param client client 
	 * @return major version of the client
	 */
	public int getMajorVersion(String client) {
		Version v = browsers.get(client);
		return (v == null ? 0 : v.major);
	}

	/**
	 * @return simple string with major version
	 */
	public String toMajorString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Version> en : browsers.entrySet()) {
			String b = en.getKey().toLowerCase();
			sb.append(b).append(' ');
			Version v = en.getValue();
			if (v.major > 0) {
				sb.append(b).append(v.major).append(' ');
			}
		}
		return sb.toString();
	}

	/**
	 * @return simple string
	 */
	public String toSimpleString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Version> en : browsers.entrySet()) {
			String b = en.getKey().toLowerCase();
			sb.append(b).append(' ');
		}
		return sb.toString();
	}
	
	/**
	 * @return user agent string
	 */
	@Override
	public String toString() {
		return userAgent;
	}
}
