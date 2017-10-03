package panda.lang.comparator;

import java.util.Comparator;

public class DictionaryComparator implements Comparator<byte[]> {
	private final static DictionaryComparator i = new DictionaryComparator();
	
	public static DictionaryComparator i() {
		return i;
	}
	
	public DictionaryComparator() {
	}

	public int bitCompare(byte b1, byte b2) {
		int int1 = b1 & 0xFF;
		int int2 = b2 & 0xFF;
		return int1 - int2;
	}

	public int compare(byte[] bs1, byte[] bs2) {
		int minLength = bs1.length > bs2.length ? bs2.length : bs1.length;
		for (int i = 0; i < minLength; i++) {
			int bitCompare = bitCompare(bs1[i], bs2[i]);
			if (bitCompare != 0) {
				return bitCompare;
			}
		}

		if (bs1.length > bs2.length) {
			return 1;
		}
		else if (bs1.length < bs2.length) {
			return -1;
		}
		return 0;
	}
}

