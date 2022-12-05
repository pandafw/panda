package panda.lang;

import java.util.Random;
import java.util.UUID;

/**
 * Utility class for Random.
 */
public abstract class Randoms {
	public static final int UUID_LENGTH = 36;
	public static final int UUID32_LENGTH = 32;
	
	private static final Random rand = new Random();
	private static final char[] NUMBERS = Strings.NUMBERS.toCharArray();
	private static final char[] LETTER_NUMBERS = Strings.LETTER_NUMBERS.toCharArray();
	private static final char[] LETTER_SYMNUMS = Strings.SYMBOL_NUMBER_LETTERS.toCharArray();

	public static int randInt() {
		return rand.nextInt();
	}

	public static int randInt(int max) {
		return rand.nextInt(max);
	}

	public static int randInt(int min, int max) {
		return rand.nextInt(max + 1 - min) + min;
	}
	
	public static short randShort() {
		return (short)rand.nextInt(0xFFFF);
	}

	public static short randShort(short max) {
		return (short)rand.nextInt(max);
	}

	public static short randShort(short min, short max) {
		return (short)(rand.nextInt(max + 1 - min) + min);
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
	
	public static String randNumbers(int len) {
		return randString(len, NUMBERS);
	}
	
	public static String randLetterNumbers(int len) {
		return randString(len, LETTER_NUMBERS);
	}
	
	public static String randString(int len) {
		return randString(len, LETTER_SYMNUMS);
	}
	
	public static String randString(int len, char[] chars) {
		char buf[] = new char[len];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = chars[rand.nextInt(chars.length)];
		}
		
		return new String(buf);
	}
	
	public static String randString(int len, String chars) {
		char buf[] = new char[len];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = chars.charAt(rand.nextInt(chars.length()));
		}
		
		return new String(buf);
	}
}
