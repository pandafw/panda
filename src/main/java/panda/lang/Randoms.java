package panda.lang;

import java.util.Random;
import java.util.UUID;

/**
 * Utility class for Random.
 * @author yf.frank.wang@gmail.com
 */
public class Randoms {
	public static final int UUID_LENGTH = 36;
	public static final int UUID32_LENGTH = 32;
	
	private static final Random rand = new Random();
	private static final char[] CHARS = (Strings.DIGITS + Strings.LOWER_LETTERS + Strings.UPPER_LETTERS + Strings.SYMBOLS).toCharArray();
	
	public static int randInt() {
		return rand.nextInt();
	}

	public static int randInt(int max) {
		return rand.nextInt(max);
	}

	public static int randInt(int min, int max) {
		return rand.nextInt(max - min) + min;
	}
	
	public static short randShort() {
		return (short)rand.nextInt(0xFFFF);
	}

	public static short randShort(short max) {
		return (short)rand.nextInt(max);
	}

	public static short randShort(short min, short max) {
		return (short)(rand.nextInt(max - min) + min);
	}
	
	public static long randLong() {
		return (short)rand.nextLong();
	}

	public static boolean randBoolean() {
		return rand.nextBoolean();
	}

	public static float randFloat() {
		return rand.nextFloat();
	}

	public static double randDouble() {
		return rand.nextDouble();
	}

	public static String randUUID() {
		return UUID.randomUUID().toString();
	}
	
	public static String randUUID32() {
		UUID uuid = UUID.randomUUID();
		return Strings.leftPad(Long.toHexString(uuid.getMostSignificantBits()), 16, '0') + 
				Strings.leftPad(Long.toHexString(uuid.getLeastSignificantBits()), 16, '0');
	}
	
	public static String randString(int len) {
		char buf[] = new char[len];
		
		for (int i = 0; i < buf.length; i++) {
			buf[i] = CHARS[rand.nextInt(CHARS.length)];
		}
		
		return new String(buf);
	}
}
