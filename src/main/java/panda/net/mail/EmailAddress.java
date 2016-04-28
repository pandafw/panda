package panda.net.mail;

import panda.lang.Objects;
import panda.lang.Strings;

public class EmailAddress {

	/**
	 * RFC822 specials
	 */
	public final static String RFC822 = "()<>@,;:\\\".[]";

	private String address;
	private String personal;

	public EmailAddress(String address) {
		super();
		this.address = address;
	}

	public EmailAddress(String address, String personal) {
		super();
		this.address = address;
		this.personal = personal;
	}

	/**
	 * @return the personal
	 */
	public String getPersonal() {
		return personal;
	}

	/**
	 * @param personal the personal to set
	 */
	public void setPersonal(String personal) {
		this.personal = personal;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		return Objects.hashCodes(address, personal);
	}


	/**
	 * @return <code>true</code> if this object is the same as the obj argument; <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		
		EmailAddress rhs = (EmailAddress)obj;
		return Objects.equalsBuilder()
				.append(address, rhs.address)
				.isEquals();
	}

	@Override
	public String toString() {
		return Strings.defaultString(quote(personal)) + "<" + address + ">";
	}

	public static String quote(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}
		
		int len = s.length();
		boolean needQuoting = false;

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c == '"' || c == '\\') {
				// need to escape them and then quote the whole string
				StringBuilder sb = new StringBuilder(len + 3);
				sb.append('"');
				for (int j = 0; j < len; j++) {
					char cc = s.charAt(j);
					if (cc == '"' || cc == '\\') {
						// Escape the character
						sb.append('\\');
					}
					sb.append(cc);
				}
				sb.append('"');
				return sb.toString();
			}
			else if ((c < 040 && c != '\r' && c != '\n' && c != '\t') || c >= 0177 || RFC822.indexOf(c) >= 0) {
				// These characters cause the string to be quoted
				needQuoting = true;
			}
		}

		if (needQuoting) {
			StringBuilder sb = new StringBuilder(len + 2);
			sb.append('"').append(s).append('"');
			return sb.toString();
		}
		else
			return s;
	}

	public static String unquote(String s) {
		if (Strings.isEmpty(s)) {
			return s;
		}
		
		if (s.startsWith("\"") && s.endsWith("\"")) {
			s = s.substring(1, s.length() - 1);
			// check for any escaped characters
			if (s.indexOf('\\') >= 0) {
				StringBuilder sb = new StringBuilder(s.length()); // approx
				for (int i = 0; i < s.length(); i++) {
					char c = s.charAt(i);
					if (c == '\\' && i < s.length() - 1)
						c = s.charAt(++i);
					sb.append(c);
				}
				s = sb.toString();
			}
		}
		return s;
	}
}
