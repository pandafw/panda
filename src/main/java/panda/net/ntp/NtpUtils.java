package panda.net.ntp;

/***
 * Common NtpUtils Helper class.
 */
public final class NtpUtils {

	/***
	 * Returns 32-bit integer address to IPv4 address string "%d.%d.%d.%d" format.
	 * 
	 * @param address the 32-bit address
	 * @return the raw IP address in a string format.
	 */
	public static String getHostAddress(int address) {
		return ((address >>> 24) & 0xFF) + "." + ((address >>> 16) & 0xFF) + "." + ((address >>> 8) & 0xFF) + "."
				+ ((address >>> 0) & 0xFF);
	}

	/***
	 * Returns NTP packet reference identifier as IP address.
	 * 
	 * @param packet NTP packet
	 * @return the packet reference id (as IP address) in "%d.%d.%d.%d" format.
	 */
	public static String getRefAddress(NtpV3Packet packet) {
		int address = (packet == null) ? 0 : packet.getReferenceId();
		return getHostAddress(address);
	}

	/***
	 * Get refId as reference clock string (e.g. GPS, WWV, LCL). If string is invalid (non-ASCII
	 * character) then returns empty string "". For details refer to the <A
	 * HREF="http://www.eecis.udel.edu/~mills/ntp/html/refclock.html#list">Comprehensive List of
	 * Clock Drivers</A>.
	 * 
	 * @param message the message to check
	 * @return reference clock string if primary NTP server
	 */
	public static String getReferenceClock(NtpV3Packet message) {
		if (message == null) {
			return "";
		}
		int refId = message.getReferenceId();
		if (refId == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder(4);
		// start at highest-order byte (0x4c434c00 -> LCL)
		for (int shiftBits = 24; shiftBits >= 0; shiftBits -= 8) {
			char c = (char)((refId >>> shiftBits) & 0xff);
			if (c == 0) { // 0-terminated ASCII string
				break;
			}
			if (!Character.isLetterOrDigit(c)) {
				return "";
			}
			buf.append(c);
		}
		return buf.toString();
	}

	/***
	 * Return human-readable name of message mode type (RFC 1305).
	 * 
	 * @param mode the mode type
	 * @return mode name
	 */
	public static String getModeName(int mode) {
		switch (mode) {
		case NtpV3Packet.MODE_RESERVED:
			return "Reserved";
		case NtpV3Packet.MODE_SYMMETRIC_ACTIVE:
			return "Symmetric Active";
		case NtpV3Packet.MODE_SYMMETRIC_PASSIVE:
			return "Symmetric Passive";
		case NtpV3Packet.MODE_CLIENT:
			return "Client";
		case NtpV3Packet.MODE_SERVER:
			return "Server";
		case NtpV3Packet.MODE_BROADCAST:
			return "Broadcast";
		case NtpV3Packet.MODE_CONTROL_MESSAGE:
			return "Control";
		case NtpV3Packet.MODE_PRIVATE:
			return "Private";
		default:
			return "Unknown";
		}
	}

}
