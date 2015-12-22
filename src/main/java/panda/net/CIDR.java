package panda.net;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

import panda.lang.Exceptions;
import panda.lang.Objects;

public class CIDR implements Cloneable, Serializable {
	private static final long serialVersionUID = 1L;

	public static final String MATCH_ALL_V4 = "0.0.0.0/0";
	public static final String MATCH_ALL_V4_ALT = "allv4";
	public static final String MATCH_ALL_V6 = "[::]/0";
	public static final String MATCH_ALL_V6_ALT = "allv6";
	public static final String MATCH_ALL = "all";
	public static final String MATCH_NONE = "none";

	private static final int MASK_ZERO = 0;
	private static final int MASK_MAX = 128;
	private static final int MASK_IPV6 = 128;
	private static final int MASK_IPV4 = 32;
	
	private InetAddress addr;
	private int mask;

	public CIDR() {
		setMatchNone();
	}

	public CIDR(String cidr) {
		setCIDR(cidr);
	}

	public CIDR(CIDR cidr) {
		addr = cidr.addr;
		mask = cidr.mask;
	}

	public boolean isV6() {
		return addr instanceof Inet6Address;
	}

	public boolean isV4() {
		return addr instanceof Inet4Address;
	}

	public boolean isMatchNone() {
		return addr == null && mask == MASK_MAX;
	}

	public boolean isMatchAll() {
		return addr == null && mask == MASK_ZERO;
	}

	public void setMatchNone() {
		addr = null;
		mask = MASK_MAX;
	}

	public void setMatchAll() {
		mask = MASK_ZERO;
		addr = null;
	}

	private InetAddress MaskAddress(InetAddress ia, int mask) {
		byte[] bs = ia.getAddress();

		int i = mask / 8;
		if (i < bs.length) {
			byte cmask = (byte)(0xFF << (8 - (mask % 8)));
			bs[i] &= cmask;
			
			for (++i ; i < bs.length; i++) {
				bs[i] = 0;
			}
		
			try {
				return InetAddress.getByAddress(bs);
			}
			catch (UnknownHostException e) {
				throw Exceptions.wrapThrow(e);
			}
		}

		return ia;
	}

	// does this CIDR subnet include the supplied address/subnet
	public boolean include(CIDR cidr) {
		return include(cidr.addr);
	}
	
	public boolean include(InetAddress ia) {
		if (isMatchAll()) {
			return true;
		}
		if (isMatchNone()) {
			return false;
		}
		

		InetAddress ma = MaskAddress(ia, mask);

		return addr.equals(ma);
	}

	/**
	 * set CIDR
	 * @param cidr a CIDR string
	 * @throws IllegalArgumentException if aCIDR is not a correct CIDR format string
	 */
	public void setCIDR(String cidr) {
		if (MATCH_ALL.equalsIgnoreCase(cidr)) {
			setMatchAll();
			return;
		}
		if (MATCH_NONE.equalsIgnoreCase(cidr)) {
			setMatchNone();
			return;
		}
		if (MATCH_ALL_V4_ALT.equalsIgnoreCase(cidr)) {
			setCIDR(MATCH_ALL_V4);
			return;
		}
		if (MATCH_ALL_V6_ALT.equalsIgnoreCase(cidr)) {
			setCIDR(MATCH_ALL_V6);
			return;
		}

		String addr = cidr;
		int mask = -1;
		int sep = cidr.indexOf('/');
		if (sep >= 0) {
			addr = cidr.substring(0, sep);
			try {
				mask = Integer.parseInt(cidr.substring(sep + 1));
			}
			catch (NumberFormatException e) {
				throw new IllegalArgumentException("Illegal CIDR: " + cidr, e);
			}
		}

		InetAddress ia;
		try {
			ia = InetAddress.getByName(addr);
		}
		catch (UnknownHostException e) {
			throw new IllegalArgumentException("Illegal CIDR: " + cidr, e);
		}

		if (Inets.isIpV6(ia)) {
			if (mask < 0 || mask > MASK_IPV6) {
				mask = MASK_IPV6;
			}

			this.mask = mask;
			this.addr = MaskAddress(ia, mask);
			return;
		}

		if (Inets.isIpV4(ia)) {
			if (mask < 0 || mask > MASK_IPV4) {
				mask = MASK_IPV4;
			}

			this.mask = mask;
			this.addr = MaskAddress(ia, mask);
			return;
		}
	}

	public String toString() {
		if (isMatchAll()) {
			return MATCH_ALL;
		}
		if (isMatchNone()) {
			return MATCH_NONE;
		}
		return addr.getHostAddress() + "/" + mask;
	}

	public String toAddress() {
		return addr == null ? null : addr.getHostAddress();
	}

	/** 
	 */
	@Override
	public CIDR clone() {
		return new CIDR(this);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCodes(addr, mask);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		CIDR rhs = (CIDR)obj;
		return Objects.equalsBuilder()
				.append(mask, rhs.mask)
				.append(addr, rhs.addr)
				.isEquals();
	}
}
