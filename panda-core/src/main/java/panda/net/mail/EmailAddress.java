package panda.net.mail;

import java.util.ArrayList;
import java.util.List;

import panda.lang.Objects;
import panda.lang.Regexs;
import panda.lang.Strings;
import panda.net.Mimes;

public class EmailAddress {

	/**
	 * RFC822 specials
	 */
	public final static String RFC822 = "()<>@,;:\\\".[]";

	private String address;
	private String personal;

	public EmailAddress(String address) throws EmailException {
		this(address, null);
	}

	public EmailAddress(String address, String personal) throws EmailException {
		if (Strings.isEmpty(address)) {
			throw new EmailException("Empty email address");
		}
		
		if (!Regexs.isEmail(address)) {
			throw new EmailException("Invalid email address: " + address);
		}

		if (Strings.containsAny(personal, "\"<>")) {
			throw new EmailException("Invalid email personal: " + personal);
		}
		
		this.address = Strings.lowerCase(address);
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

	/**
	 * @return the address's domain
	 */
	public String getDomain() {
		return Strings.substringAfter(address, '@');
	}

	@Override
	public int hashCode() {
		return Objects.hash(address, personal);
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

	//-----------------------------------------------------
	public static List<EmailAddress> parseList(String rcpts) throws EmailException {
		String[] ss = Strings.split(rcpts, ",;");
		List<EmailAddress> eas = new ArrayList<EmailAddress>(ss.length);
		for (String s : ss) {
			eas.add(parse(s));
		}
		return eas;
	}
	
	public static EmailAddress parse(String rcpt) throws EmailException {
		String address = null;
		String personal = null;
		
		int a = rcpt.indexOf('<');
		if (a >= 0) {
			int b = rcpt.indexOf('>', a);
			if (b < 0) {
				throw new EmailException("Invalid email address: " + rcpt);
			}
			address = Strings.strip(rcpt.substring(a + 1, b));
			personal = Strings.strip(unquote(rcpt.substring(0, a)));
		}
		else {
			address = Strings.strip(rcpt);
		}
		return new EmailAddress(address, personal);
	}
	
	public static String quote(String s) {
		return Mimes.quote(s, RFC822);
	}

	public static String unquote(String s) {
		return Mimes.unquote(s);
	}
}
