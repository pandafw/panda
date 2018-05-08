package panda.net.http;

import panda.lang.Numbers;
import panda.lang.Strings;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;


/**
 * HTTP user-agent
 */
public class UserAgent {
	public static final String UA_WINDOWS_IE11 = "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; rv:11.0) like Gecko";
	public static final String UA_WINDOWS_EDGE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299";
	public static final String UA_WINDOWS_CHROME = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36";
	public static final String UA_WINDOWS_FIREFOX = "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:59.0) Gecko/20100101 Firefox/59.0";
	public static final String UA_ANDROID_CHROME = "Mozilla/5.0 (Linux; Android 7.0; SM-G920F Build/NRD90M) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3202.84 Mobile Safari/537.36";
	public static final String UA_IPHONE_SAFARI = "Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_1 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) Version/10.0 Mobile/14E304 Safari/602.1";
	public static final String UA_IPAD_SAFARI = "Mozilla/5.0 (iPad; CPU OS 10_3_3 like Mac OS X) AppleWebKit/603.3.8 (KHTML, like Gecko) Version/10.0 Mobile/14G60 Safari/602.1";

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
	private static final String IPHONE = "iPhone";
	private static final String IPAD = "iPad";
	private static final String IPOD = "iPod";
	private static final String IOS = "ios";
	private static final String ANDROID = "Android";
	private static final String WINDOWS = "Windows";
	private static final String MACINTOSH = "Macintosh";
	private static final String LINUX = "Linux";
	private static final String UBUNTU = "Ubuntu";
	private static final String MINT = "Mint";
	private static final String FEDORA = "Fedora";
	private static final String GENTOO = "Gentoo";
	
	private static final String[] ROBOTS = { 
		"robot", "Googlebot", "bingbot", "yahoo", "baidu", 
		"msnbot", "ask.", "naver.", "DotBot", "Yandex", "goo.ne.jp"
	};

	private static final String MOBILE = "mobile";
	private static final String ROBOT = "robot";

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
