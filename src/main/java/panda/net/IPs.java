package panda.net;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import panda.lang.Strings;

/**
 * @author yf.frank.wang@gmail.com
 */
public class IPs {
	private final static int INADDR4SZ = 4;
	private final static int INADDR16SZ = 16;
	private final static int INT16SZ = 2;

	/*
	 * Converts IP address in its textual presentation form into its numeric binary form.
	 * @param src a String representing an IP address in standard format
	 * @return a byte array representing the IP numeric address
	 */
	public static byte[] toNetAddress(String src) {
		if (Strings.isEmpty(src)) {
			return null;
		}
		
		byte[] bs = toIPv4Bytes(src);
		if (bs != null) {
			return bs;
		}
		
		return toIPv6Bytes(src);
	}
	
	/*
	 * Converts IPv4 address in its textual presentation form into its numeric binary form.
	 * @param src a String representing an IPv4 address in standard format
	 * @return a byte array representing the IPv4 numeric address
	 */
	public static byte[] toIPv4Bytes(String src) {
		if (Strings.isEmpty(src)) {
			return null;
		}

		byte[] res = new byte[INADDR4SZ];
		int s = 0, e = 0, i = 0;
		while (i < 3) {
			e = src.indexOf('.', s);
			if (e <= s || e >= src.length()) {
				return null;
			}
			
			int n = getIPv4Number(src.substring(s, e));
			if (n == -1) {
				return null;
			}

			res[i] = (byte)(n & 0xff);
			i++;
			s = e + 1;
		}

		int n = getIPv4Number(src.substring(s));
		if (n == -1) {
			return null;
		}
		res[i] = (byte)(n & 0xff);
		return res;
	}

	/*
	 * Convert IPv6 presentation level address to network order binary form. credit: Converted from
	 * C code from Solaris 8 (inet_pton) Any component of the string following a per-cent % is
	 * ignored.
	 * @param src a String representing an IPv6 address in textual format
	 * @return a byte array representing the IPv6 numeric address
	 */
	public static byte[] toIPv6Bytes(String src) {
		// Shortest valid string is "::", hence at least 2 chars
		if (src == null || src.length() < 2) {
			return null;
		}

		int colonp;
		char ch;
		boolean saw_xdigit;
		int val;
		char[] scs = src.toCharArray();
		byte[] dst = new byte[INADDR16SZ];

		int i = 0, j = 0;
		int len = scs.length;
		if (scs[0] == '[' && scs[len - 1] == ']') {
			i = 1;
			len--;
		}

		int pc = src.indexOf("%");
		if (pc == len - 1) {
			return null;
		}

		if (pc != -1) {
			len = pc;
		}

		colonp = -1;
		/* Leading :: requires some special handling. */
		if (scs[i] == ':')
			if (scs[++i] != ':')
				return null;
		int curtok = i;
		saw_xdigit = false;
		val = 0;
		while (i < len) {
			ch = scs[i++];
			int chval = Character.digit(ch, 16);
			if (chval != -1) {
				val <<= 4;
				val |= chval;
				if (val > 0xffff)
					return null;
				saw_xdigit = true;
				continue;
			}
			if (ch == ':') {
				curtok = i;
				if (!saw_xdigit) {
					if (colonp != -1)
						return null;
					colonp = j;
					continue;
				}
				else if (i == len) {
					return null;
				}
				if (j + INT16SZ > INADDR16SZ)
					return null;
				dst[j++] = (byte)((val >> 8) & 0xff);
				dst[j++] = (byte)(val & 0xff);
				saw_xdigit = false;
				val = 0;
				continue;
			}
			if (ch == '.' && ((j + INADDR4SZ) <= INADDR16SZ)) {
				String ia4 = src.substring(curtok, len);
				/* check this IPv4 address has 3 dots, ie. A.B.C.D */
				int dot_count = 0, index = 0;
				while ((index = ia4.indexOf('.', index)) != -1) {
					dot_count++;
					index++;
				}
				if (dot_count != 3) {
					return null;
				}
				byte[] v4addr = toIPv4Bytes(ia4);
				if (v4addr == null) {
					return null;
				}
				for (int k = 0; k < INADDR4SZ; k++) {
					dst[j++] = v4addr[k];
				}
				saw_xdigit = false;
				break; /* '\0' was seen by inet_pton4(). */
			}
			return null;
		}
		if (saw_xdigit) {
			if (j + INT16SZ > INADDR16SZ)
				return null;
			dst[j++] = (byte)((val >> 8) & 0xff);
			dst[j++] = (byte)(val & 0xff);
		}

		if (colonp != -1) {
			int n = j - colonp;

			if (j == INADDR16SZ)
				return null;
			for (i = 1; i <= n; i++) {
				dst[INADDR16SZ - i] = dst[colonp + n - i];
				dst[colonp + n - i] = 0;
			}
			j = INADDR16SZ;
		}
		if (j != INADDR16SZ)
			return null;
		byte[] newdst = convertFromIPv4MappedAddress(dst);
		if (newdst != null) {
			return newdst;
		}
		else {
			return dst;
		}
	}

	/**
	 * Convert IPv4-Mapped address to IPv4 address. Both input and returned value are in network
	 * order binary form.
	 * @param addr a byte array representing an IPv4-Mapped address
	 * @return a byte array representing the IPv4 numeric address
	 */
	public static byte[] convertFromIPv4MappedAddress(byte[] addr) {
		if (isIPv4MappedAddress(addr)) {
			byte[] newAddr = new byte[INADDR4SZ];
			System.arraycopy(addr, 12, newAddr, 0, INADDR4SZ);
			return newAddr;
		}
		return null;
	}

	/**
	 * Utility routine to check if the InetAddress is an IPv4 mapped IPv6 address.
	 *
	 * @return a <code>boolean</code> indicating if the InetAddress is an IPv4 mapped IPv6 address;
	 *         or false if address is IPv4 address.
	 */
	public static boolean isIPv4MappedAddress(byte[] addr) {
		if (addr.length < INADDR16SZ) {
			return false;
		}
		if ((addr[0] == 0x00) && (addr[1] == 0x00) && (addr[2] == 0x00) && (addr[3] == 0x00) && (addr[4] == 0x00)
				&& (addr[5] == 0x00) && (addr[6] == 0x00) && (addr[7] == 0x00) && (addr[8] == 0x00)
				&& (addr[9] == 0x00) && (addr[10] == (byte)0xff) && (addr[11] == (byte)0xff)) {
			return true;
		}
		return false;
	}

	public static int getIPv4Number(String s) {
		try {
			int n = Integer.parseInt(s);
			if (n < 0 || n > 255) {
				return -1;
			}
			return n;
		}
		catch (NumberFormatException ex) {
			return -1;
		}
	}

	public static boolean isIPv4Number(String s) {
		return getIPv4Number(s) != -1;
	}
	
	/**
	 * @param src a String representing an IPv4 address in textual format
	 * @return a boolean indicating whether src is an IPv4 literal address
	 */
	public static boolean isIPv4(String src) {
		int s = 0, e = 0, i = 0;
		while (i < 3) {
			e = src.indexOf('.', s);
			if (e <= s || e >= src.length()) {
				return false;
			}
			
			if (!isIPv4Number(src.substring(s, e))) {
				return false;
			}
			
			i++;
			s = e + 1;
		}

		return isIPv4Number(src.substring(s));
	}

	/**
	 * @param src a String representing an IPv6 address in textual format
	 * @return a boolean indicating whether src is an IPv6 literal address
	 */
	public static boolean isIPv6(String src) {
		return toIPv6Bytes(src) != null;
	}

	public static boolean isIPv6(InetAddress ia) {
		return ia instanceof Inet6Address;
	}
	
	public static boolean isIPv4(InetAddress ia) {
		return ia instanceof Inet4Address;
	}
	
	public static boolean isIntranetAddr(String ipAddr) {
		if (Strings.isEmpty(ipAddr)) {
			return false;
		}
		
		// Class A: 10.0.0.0 ~ 10.255.255.255 (10.0.0.0/8)
		// Class B: 172.16.0.0 ~ 172.31.255.255 (172.16.0.0/12)
		// Class C: 192.168.0.0 ~ 192.168.255.255 (192.168.0.0/16)
		try {
			InetAddress ia = InetAddress.getByName(ipAddr);
			return ia.isLoopbackAddress() || ia.isAnyLocalAddress() || ia.isSiteLocalAddress();
		}
		catch (UnknownHostException e) {
			return false;
		}
	}
}
