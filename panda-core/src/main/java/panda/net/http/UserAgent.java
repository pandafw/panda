package panda.net.http;

import panda.lang.Numbers;
import panda.lang.Strings;


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

	public static final int FLAG_IPHONE = 0x0001;
	public static final int FLAG_IPAD = 0x0002;
	public static final int FLAG_IPOD = 0x0004;
	public static final int FLAG_IOS = FLAG_IPHONE | FLAG_IPAD | FLAG_IPOD;
	public static final int FLAG_ANDROID = 0x0010;
	public static final int FLAG_WINDOWS = 0x0020;
	public static final int FLAG_LINUX = 0x0040;
	public static final int FLAG_MACINTOSH = 0x0080;
	public static final int FLAG_MOBILE = 0x0100;
	public static final int FLAG_DESKTOP = 0x0200;
	public static final int FLAG_TABLET = 0x0400;
	public static final int FLAG_GAME = 0x0800;
	public static final int FLAG_32BIT = 0x1000;
	public static final int FLAG_64BIT = 0x2000;

	private static class Detect {
		private String name;
		private String key;
		private boolean nocase;
		private String verp;
		private int flags;
		
		public Detect(String name, String key) {
			this.name = name;
			this.key = key;
		}
		
		public Detect(String name, int flags) {
			this.name = name;
			this.key = name;
			this.flags = flags;
		}

		public Detect(String name, String key, boolean nocase) {
			this.name = name;
			this.key = key;
			this.nocase = nocase;
		}

		public Detect(String name, String key, int flags) {
			this.name = name;
			this.key = key;
			this.flags = flags;
		}

		public Detect(String name, String key, String verp) {
			this.name = name;
			this.key = key;
			this.verp = verp;
		}
	}
	
	public static class Agent {
		private String name;
		private String arch;
		private String version;
		private int major;
		private int minor;
		private int flags;

		public Agent(String name) {
			this.name = name;
		}

		private void setVersion(String version) {
			this.version = version;

			if (Strings.isEmpty(version)) {
				return;
			}

			int i = version.indexOf('.');
			if (i > 0) {
				major =  Numbers.toInt(version.substring(0, i), 0);
				if (i < version.length() - 1) {
					String v2 = version.substring(i + 1);
					int j = v2.indexOf('.');
					if (j > 0) {
						minor = Numbers.toInt(v2.substring(0, j), 0);
					}
					else {
						minor = Numbers.toInt(v2, 0);
					}
				}
			}
			else {
				major = Numbers.toInt(version);
			}
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the version
		 */
		public String getVersion() {
			return version;
		}

		/**
		 * @return the major number
		 */
		public int getMajor() {
			return major;
		}

		/**
		 * @return the minor number
		 */
		public int getMinor() {
			return minor;
		}

		/**
		 * @return the arch
		 */
		public String getArch() {
			return arch;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(name);
			
			if (Strings.isNotEmpty(version)) {
				sb.append(' ').append(name).append(major);
			}
			if (Strings.isNotEmpty(arch)) {
				sb.append(' ').append(arch);
			}
			return sb.toString();
		}
	}
	
	private String value;
	private Agent browser = UNKNOWN;
	private Agent platform = UNKNOWN;
	private Agent device = UNKNOWN;

	private static final Object[] BROWSERS = {
			"Firefox",
			"Edge",
			"MSIE",
			new Detect("MSIE", "Trident", "rv:"),
			"Netscape",
			"Opera",
			new Detect("Opera", "OPR/"),
			"UCBrowser",
			"PhantomJS",
			"Otter",
			"QQBrowser",
			"Electron",
			new Detect("Weibo", "__weibo__", true),
			new Detect("Alipay", "AlipayClient", true),
			new Detect("Fackbook", "FBAV"),
			"Chrome",
			"Safari"
	};

	private static final Object[] DEVICES = {
			new Detect("iPhone", FLAG_IPHONE | FLAG_MOBILE),
			new Detect("iPad", FLAG_IPAD | FLAG_MOBILE | FLAG_TABLET),
			new Detect("iPod", FLAG_IPOD | FLAG_MOBILE),
			new Detect("Macintosh", FLAG_MACINTOSH | FLAG_DESKTOP),
			new Detect("Windows CE", FLAG_WINDOWS | FLAG_MOBILE),
			new Detect("Windows Phone", FLAG_WINDOWS | FLAG_MOBILE),
			new Detect("Windows", FLAG_WINDOWS | FLAG_DESKTOP),
			new Detect("Kindle Fire", FLAG_TABLET | FLAG_MOBILE),
			new Detect("Kindle Fire", "KFTT", FLAG_TABLET | FLAG_MOBILE),
			new Detect("Kindle", FLAG_TABLET | FLAG_MOBILE),
			new Detect("Android", FLAG_ANDROID | FLAG_MOBILE),
			new Detect("PLAYSTATION 3", FLAG_GAME),
			new Detect("PLAYSTATION 4", FLAG_GAME),
			new Detect("PSP", "PlayStation Portable", FLAG_MOBILE | FLAG_GAME),
			new Detect("PSV", "PlayStation Vita", FLAG_MOBILE | FLAG_GAME),
			new Detect("Nintendo WiiU", FLAG_GAME),
			new Detect("Nintendo Wii", FLAG_GAME),
			new Detect("Nintendo Switch", FLAG_MOBILE | FLAG_GAME),
			new Detect("Nintendo 3DS", FLAG_MOBILE | FLAG_GAME),
			new Detect("Xbox One", FLAG_GAME),
			new Detect("Xbox", FLAG_GAME),
			new Detect("BlackBerry", FLAG_MOBILE),
			new Detect("BlackBerry", "BB10", FLAG_MOBILE)
	};
	
	private static final Object[] PLATFORMS = {
			new Detect("Linux", FLAG_LINUX | FLAG_DESKTOP),
			new Detect("Chrome OS", "CrOS", FLAG_DESKTOP),
			new Detect("FreeBSD", FLAG_DESKTOP),
			new Detect("OpenBSD", FLAG_DESKTOP),
			new Detect("NetBSD", FLAG_DESKTOP)
	};
	
	private static final Object[][] ARCHS = {
			{ "i386", FLAG_32BIT },
			{ "i686", FLAG_32BIT },
			{ "x86_64", FLAG_64BIT },
			{ "x64", FLAG_64BIT },
			{ "amd64", FLAG_64BIT },
			{ "WOW64", FLAG_64BIT },
			{ "Win64", FLAG_64BIT }
	};

	private static final Agent UNKNOWN = new Agent("UNKNOWN");
	
	/**
	 * @param value user agent
	 */
	public UserAgent(String value) {
		this.value = value;
		init();
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @return the browser
	 */
	public Agent getBrowser() {
		return browser;
	}

	/**
	 * @return the platform
	 */
	public Agent getPlatform() {
		return platform;
	}

	/**
	 * @return the device
	 */
	public Agent getDevice() {
		return device;
	}

	/**
	 * @return the flags
	 */
	public int getFlags() {
		return device.flags | device.flags | platform.flags;
	}

	/**
	 * @return true if user agent is Chrome
	 */
	public boolean isChrome() {
		return "Chrome".equals(browser.name);
	}

	/**
	 * @return true if user agent is EDGE
	 */
	public boolean isEdge() {
		return "Edge".equals(browser.name);
	}

	/**
	 * @return true if user agent is MSIE
	 */
	public boolean isMsie() {
		return "MSIE".equals(browser.name);
	}

	/**
	 * @return true if user agent is Netscape
	 */
	public boolean isNetscape() {
		return "Netscape".equals(browser.name);
	}

	/**
	 * @return true if user agent is Firefox
	 */
	public boolean isFirefox() {
		return "Firefox".equals(browser.name);
	}

	/**
	 * @return true if user agent is Safari
	 */
	public boolean isSafari() {
		return "Safari".equals(browser.name);
	}

	/**
	 * @return true if user agent is Opera
	 */
	public boolean isOpera() {
		return "Opera".equals(browser.name);
	}

	/**
	 * @return true if user agent is Mobile
	 */
	public boolean isMobile() {
		return (getFlags() & FLAG_MOBILE) != 0;
	}

	/**
	 * @return true if user agent is Tablet
	 */
	public boolean isTablet() {
		return (getFlags() & FLAG_TABLET) != 0;
	}

	/**
	 * @return true if user agent is Desktop
	 */
	public boolean isDesktop() {
		return (getFlags() & FLAG_DESKTOP) != 0;
	}

	/**
	 * @return true if user agent is Game
	 */
	public boolean isGame() {
		return (getFlags() & FLAG_GAME) != 0;
	}

	/**
	 * @return true if user agent is Android
	 */
	public boolean isAndroid() {
		return (getFlags() & FLAG_ANDROID) != 0;
	}

	/**
	 * @return true if user agent is iPad
	 */
	public boolean isIpad() {
		return (getFlags() & FLAG_IPAD) != 0;
	}

	/**
	 * @return true if user agent is iPhone
	 */
	public boolean isIphone() {
		return (getFlags() & FLAG_IPHONE) != 0;
	}

	/**
	 * @return true if user agent is iPod
	 */
	public boolean isIpod() {
		return (getFlags() & FLAG_IPOD) != 0;
	}

	/**
	 * @return true if user agent is ios
	 */
	public boolean isIos() {
		return (getFlags() & FLAG_IOS) != 0;
	}

	/**
	 * @return true if user agent is windows
	 */
	public boolean isWindows() {
		return (getFlags() & FLAG_WINDOWS) != 0;
	}

	/**
	 * @return true if user agent is Macintosh
	 */
	public boolean isMacintosh() {
		return (getFlags() & FLAG_MACINTOSH) != 0;
	}

	/**
	 * @return true if user agent is Linux
	 */
	public boolean isLinux() {
		return (getFlags() & FLAG_LINUX) != 0;
	}

	private void init() {
		if (Strings.isEmpty(value)) {
			return;
		}

		browser = detect(BROWSERS);

		device = detect(DEVICES);
		if ((device.flags & FLAG_ANDROID) != 0) {
			platform = device;
			if (!value.contains("Mobile")) {
				device.flags |= FLAG_TABLET;
			}
		}
		else if ((device.flags & (FLAG_IOS | FLAG_MACINTOSH | FLAG_WINDOWS)) != 0) {
			platform = device;
		}
		else {
			platform = detect(PLATFORMS);
		}

		if (platform != UNKNOWN) {
			for (Object[] ss : ARCHS) {
				if (value.contains((String)ss[0])) {
					platform.arch = (String)ss[0];
					platform.flags |= (int)ss[1];
					break;
				}
			}
		}
	}

	private Agent detect(Object[] agents) {
		Agent a = null;
		for (Object o : agents) {
			if (o instanceof Detect) {
				Detect d = (Detect)o;
				a = parse(d.name, d.key, d.nocase, d.verp);
				if (a != null) {
					a.flags = d.flags;
					break;
				}
			}
			else {
				a = parse((String)o);
				if (a != null) {
					break;
				}
			}
		}
		return a == null ? UNKNOWN : a;
	}

	private Agent parse(String key) {
		return parse(key, key, false, null);
	}
	
	private Agent parse(String name, String key, boolean nocase, String verp) {
		int i = nocase ? Strings.indexOfIgnoreCase(value, key) : value.indexOf(key);
		if (i < 0) {
			return null;
		}

		int len = value.length();

		i += key.length();
		if (Strings.isNotEmpty(verp)) {
			i = value.indexOf(verp, i);
			if (i > 0) {
				i += verp.length();
			}
		}
		
		String ver = "";
		if (i > 0) {
			// find version start
			for (; i < len; i++) {
				char c = value.charAt(i);
				if (Character.isDigit(c)) {
					break;
				}
			}
			
			// find version end
			int j = i;
			for (; j < len; j++) {
				char c = value.charAt(j);
				if (!Character.isDigit(c) && c != '.' && c != '_') {
					break;
				}
			}

			if (i < len) {
				ver = value.substring(i, j).replace('_', '.');
			}
		}
		
		Agent a = new Agent(name);
		a.setVersion(ver);
		return a;
	}
	
	/**
	 * @return user agent string
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (browser != UNKNOWN) {
			sb.append(browser);
		}
		if (device != UNKNOWN) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(device);
		}
		if (platform != UNKNOWN && platform != device) {
			if (sb.length() > 0) {
				sb.append(' ');
			}
			sb.append(platform);
		}
		return sb.toString();
	}
}
