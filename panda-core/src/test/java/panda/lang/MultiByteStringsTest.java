package panda.lang;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests {@link panda.lang.MultiByteStrings}.
 */
public class MultiByteStringsTest {

	@Test
	public void TestASCIIConvert() {
		int l = MultiByteCharsTest.fullASCIIChars.length;
		for (int i = 0; i < 100; i++) {
			StringBuilder src = new StringBuilder();
			StringBuilder han = new StringBuilder();
			StringBuilder zen = new StringBuilder();
			for (int n = 0; n < 100; n++) {
				int p = Randoms.randInt(l);
				char h = MultiByteCharsTest.halfASCIIChars[p];
				char z = MultiByteCharsTest.fullASCIIChars[p];
				src.append(h);
				src.append(z);
				han.append(h);
				han.append(h);
				char sz = MultiByteCharsTest.specialZ(z);
				zen.append(sz);
				zen.append(z);

				String s = src.toString();
				assertEquals(s, han.toString(), MultiByteStrings.toHalfWidth(s));
				assertEquals(s, zen.toString(), MultiByteStrings.toFullWidth(s));
			}
		}
	}

}
