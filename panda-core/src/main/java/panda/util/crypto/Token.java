package panda.util.crypto;

import panda.lang.Objects;
import panda.lang.Randoms;
import panda.lang.Strings;

public class Token {
	public static final int SALT_LENGTH = 8;
	public static final int TIMESTAMP_LENGTH = 16;
	public static final int SECRET_LENGTH = 16;
	public static final String SECRET_CHARS = Strings.BASE64URL;

	private String salt;
	private long timestamp;
	private String secret;
	private String token;

	public static boolean isSameSecret(Token t1, Token t2) {
		if (t1 == null || t2 == null) {
			return false;
		}
		return Strings.equals(t1.getSecret(), t2.getSecret());
	}

	public static Token parse(String token) {
		if (token == null) {
			return null;
		}
		if (token.length() <= SALT_LENGTH + TIMESTAMP_LENGTH) {
			return null;
		}

		Token t = new Token();

		t.token = token;
		t.salt = Strings.left(token, SALT_LENGTH);
		try {
			String s = Strings.mid(token, SALT_LENGTH, TIMESTAMP_LENGTH);
			s = unsalt(s, t.salt);
			t.timestamp = Long.parseLong(s, 16);
		} catch (NumberFormatException e) {
			return null;
		}
		t.secret = unsalt(Strings.substring(token, SALT_LENGTH + TIMESTAMP_LENGTH), t.salt);
		return t;
	}

	public Token() {
		this(null);
	}

	protected Token(String secret) {
		this.secret = Strings.isEmpty(secret) ? newSecret() : secret;
		refresh();
	}

	public void refresh() {
		this.salt = newSalt();
		this.timestamp = System.currentTimeMillis();
		this.token = salt + saltTimestamp() + saltSecret();
	}
	
	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the secret
	 */
	public String getSecret() {
		return secret;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return secret
	 */
	protected String newSecret() {
		return Randoms.randString(SECRET_LENGTH, SECRET_CHARS);
	}

	/**
	 * @return salt
	 */
	protected String newSalt() {
		return Randoms.randString(SALT_LENGTH, SECRET_CHARS);
	}

	/**
	 * @return salted secret
	 */
	protected String saltSecret() {
		return salt(secret, salt);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(salt);
	}

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
		return Strings.equals(token, ((Token)obj).token);
	}

	@Override
	public String toString() {
		return timestamp + ' ' + secret;
	}

	/**
	 * @return salted timestamp
	 */
	protected String saltTimestamp() {
		String t = Strings.leftPad(Long.toHexString(timestamp), TIMESTAMP_LENGTH, '0');
		return salt(t, salt);
	}

	protected static char getChar(String str, int i) {
		int len = str.length();
		if (i < 0) {
			i = (i % len) + len;
		} else if (i >= len) {
			i %= len;
		}
		return str.charAt(i);
	}

	/**
	 * @param str the original string
	 * @param salt the salt
	 * @return salted string
	 */
	public static String salt(String str, String salt) {
		int len = str.length();
		StringBuilder salted = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int x = SECRET_CHARS.indexOf(getChar(str, i));
			int y = SECRET_CHARS.indexOf(getChar(salt, i));
			salted.append(getChar(SECRET_CHARS, x + y));
		}

		return salted.toString();
	}

	/**
	 * @param str the salted string
	 * @param salt the salt
	 * @return unsalted string
	 */
	public static String unsalt(String str, String salt) {
		int len = str.length();
		StringBuilder unsalted = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			int x = SECRET_CHARS.indexOf(getChar(str, i));
			int y = SECRET_CHARS.indexOf(getChar(salt, i));
			unsalted.append(getChar(SECRET_CHARS, x - y));
		}
		return unsalted.toString();
	}
}
