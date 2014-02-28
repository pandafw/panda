package panda.lang;

import java.util.Random;

/**
 * Utility class for Random.
 * @author yf.frank.wang@gmail.com
 */
public class Randoms {
	private static final Random rand = new Random();
	
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

	public static String GUID() {
		return String.format("{%08X-%04X-%04X-%04X-%04X%08X}", 
			randInt(), randShort(), 
			randShort(), randShort(),
			randShort(), randInt());
	}
}
