package panda.lang;

import java.util.Random;

/**
 * Utility class for Random.
 * @author yf.frank.wang@gmail.com
 */
public class Randoms {
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

	public static String randGUID() {
		return String.format("{%08X-%04X-%04X-%04X-%04X%08X}", 
			randInt(), randShort(), 
			randShort(), randShort(),
			randShort(), randInt());
	}
	
	public static String randString(int len) {
		char buf[] = new char[len];
		
		for (int i = 0; i < buf.length; i++) {
			buf[i] = CHARS[rand.nextInt(CHARS.length)];
		}
		
		return new String(buf);
	}
}
