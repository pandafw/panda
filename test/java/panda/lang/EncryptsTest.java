package panda.lang;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Random;

import org.junit.Test;

import panda.lang.codec.binary.Hex;

/**
 * test class for Encrypts
 */
public class EncryptsTest {
	private final byte[] testData = new byte[1024 * 1024];

	/*
	 * (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		new Random().nextBytes(testData);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInternalNoSuchAlgorithmException() {
		Encrypts.getDigest("Bogus Bogus");
	}

	@Test
	public void testMd2Hex() throws IOException {
		// Examples from RFC 1319
		assertEquals("8350e5a3e24c153df2275c9f80692773", Encrypts.md2Hex(""));

		assertEquals("32ec01ec4a6dac72c0ab96fb34c0b5d1", Encrypts.md2Hex("a"));

		assertEquals("da853b0d3f88d99b30283a69e6ded6bb", Encrypts.md2Hex("abc"));

		assertEquals("ab4f496bfb2a530b219ff33031fe06b0", Encrypts.md2Hex("message digest"));

		assertEquals("4e8ddff3650292ab5a4108c3aa47940b", Encrypts.md2Hex("abcdefghijklmnopqrstuvwxyz"));

		assertEquals("da33def2a42df13975352846c30338cd", Encrypts.md2Hex("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz" + "0123456789"));

		assertEquals("d5976f79d83d3a0dc9806c3c66f3efd8", Encrypts.md2Hex("1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"));

		assertEquals(Encrypts.md2Hex(testData), Encrypts.md2Hex(new ByteArrayInputStream(testData)));
	}

	/**
	 * An MD2 hash converted to hex should always be 32 characters.
	 */
	@Test
	public void testMd2HexLength() {
		String hashMe = "this is some string that is longer than 32 characters";
		String hash = Encrypts.md2Hex(Strings.getBytesUtf8(hashMe));
		assertEquals(32, hash.length());

		hashMe = "length < 32";
		hash = Encrypts.md2Hex(Strings.getBytesUtf8(hashMe));
		assertEquals(32, hash.length());
	}

	/**
	 * An MD2 hash should always be a 16 element byte[].
	 */
	@Test
	public void testMd2Length() {
		String hashMe = "this is some string that is longer than 16 characters";
		byte[] hash = Encrypts.md2(Strings.getBytesUtf8(hashMe));
		assertEquals(16, hash.length);

		hashMe = "length < 16";
		hash = Encrypts.md2(Strings.getBytesUtf8(hashMe));
		assertEquals(16, hash.length);
	}

	@Test
	public void testMd5Hex() throws IOException {
		// Examples from RFC 1321
		assertEquals("d41d8cd98f00b204e9800998ecf8427e", Encrypts.md5Hex(""));

		assertEquals("0cc175b9c0f1b6a831c399e269772661", Encrypts.md5Hex("a"));

		assertEquals("900150983cd24fb0d6963f7d28e17f72", Encrypts.md5Hex("abc"));

		assertEquals("f96b697d7cb7938d525a2f31aaf161d0", Encrypts.md5Hex("message digest"));

		assertEquals("c3fcd3d76192e4007dfb496cca67e13b", Encrypts.md5Hex("abcdefghijklmnopqrstuvwxyz"));

		assertEquals("d174ab98d277d9f5a5611c2c9f419d9f", Encrypts.md5Hex("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "abcdefghijklmnopqrstuvwxyz" + "0123456789"));

		assertEquals("57edf4a22be3c955ac49da2e2107b67a", Encrypts.md5Hex("1234567890123456789012345678901234567890"
				+ "1234567890123456789012345678901234567890"));

		assertEquals(Encrypts.md5Hex(testData), Encrypts.md5Hex(new ByteArrayInputStream(testData)));
	}

	/**
	 * An MD5 hash converted to hex should always be 32 characters.
	 */
	@Test
	public void testMd5HexLength() {
		String hashMe = "this is some string that is longer than 32 characters";
		String hash = Encrypts.md5Hex(Strings.getBytesUtf8(hashMe));
		assertEquals(32, hash.length());

		hashMe = "length < 32";
		hash = Encrypts.md5Hex(Strings.getBytesUtf8(hashMe));
		assertEquals(32, hash.length());
	}

	/**
	 * An MD5 hash should always be a 16 element byte[].
	 */
	@Test
	public void testMd5Length() {
		String hashMe = "this is some string that is longer than 16 characters";
		byte[] hash = Encrypts.md5(Strings.getBytesUtf8(hashMe));
		assertEquals(16, hash.length);

		hashMe = "length < 16";
		hash = Encrypts.md5(Strings.getBytesUtf8(hashMe));
		assertEquals(16, hash.length);
	}

	@Test
	public void testSha1Hex() throws IOException {
		// Examples from FIPS 180-1
		assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", Encrypts.sha1Hex("abc"));

		assertEquals("a9993e364706816aba3e25717850c26c9cd0d89d", Encrypts.sha1Hex(Strings.getBytesUtf8("abc")));

		assertEquals("84983e441c3bd26ebaae4aa1f95129e5e54670f1", Encrypts.sha1Hex("abcdbcdecdefdefgefghfghighij"
				+ "hijkijkljklmklmnlmnomnopnopq"));
		assertEquals(Encrypts.sha1Hex(testData), Encrypts.sha1Hex(new ByteArrayInputStream(testData)));
	}

	@Test
	public void testSha1UpdateWithByteArray() {
		final String d1 = "C'est un homme qui rentre dans un café, et plouf";
		final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

		MessageDigest messageDigest = Encrypts.sha1();
		messageDigest.update(d1.getBytes());
		messageDigest.update(d2.getBytes());
		final String expectedResult = Hex.encodeHexString(messageDigest.digest());

		messageDigest = Encrypts.sha1();
		Encrypts.updateDigest(messageDigest, d1.getBytes());
		Encrypts.updateDigest(messageDigest, d2.getBytes());
		final String actualResult = Hex.encodeHexString(messageDigest.digest());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testSha1UpdateWithString() {
		final String d1 = "C'est un homme qui rentre dans un café, et plouf";
		final String d2 = "C'est un homme, c'est qu'une tête, on lui offre un cadeau: 'oh... encore un chapeau!'";

		MessageDigest messageDigest = Encrypts.sha1();
		messageDigest.update(Strings.getBytesUtf8(d1));
		messageDigest.update(Strings.getBytesUtf8(d2));
		final String expectedResult = Hex.encodeHexString(messageDigest.digest());

		messageDigest = Encrypts.sha1();
		Encrypts.updateDigest(messageDigest, d1);
		Encrypts.updateDigest(messageDigest, d2);
		final String actualResult = Hex.encodeHexString(messageDigest.digest());

		assertEquals(expectedResult, actualResult);
	}

	@Test
	public void testSha256() throws IOException {
		// Examples from FIPS 180-2
		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad", Encrypts.sha256Hex("abc"));
		assertEquals("ba7816bf8f01cfea414140de5dae2223b00361a396177a9cb410ff61f20015ad",
			Encrypts.sha256Hex(Strings.getBytesUtf8("abc")));
		assertEquals("248d6a61d20638b8e5c026930c3e6039a33ce45964ff2167f6ecedd419db06c1",
			Encrypts.sha256Hex("abcdbcdecdefdefgefghfghighijhijkijkljklmklmnlmnomnopnopq"));

		assertEquals(Encrypts.sha256Hex(testData), Encrypts.sha256Hex(new ByteArrayInputStream(testData)));
	}

	@Test
	public void testSha384() throws IOException {
		// Examples from FIPS 180-2
		assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed"
				+ "8086072ba1e7cc2358baeca134c825a7", Encrypts.sha384Hex("abc"));
		assertEquals("cb00753f45a35e8bb5a03d699ac65007272c32ab0eded1631a8b605a43ff5bed"
				+ "8086072ba1e7cc2358baeca134c825a7", Encrypts.sha384Hex(Strings.getBytesUtf8("abc")));
		assertEquals("09330c33f71147e83d192fc782cd1b4753111b173b3b05d22fa08086e3b0f712"
				+ "fcc7c71a557e2db966c3e9fa91746039",
			Encrypts.sha384Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn"
					+ "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
		assertEquals(Encrypts.sha384Hex(testData), Encrypts.sha384Hex(new ByteArrayInputStream(testData)));
	}

	@Test
	public void testSha512() throws IOException {
		// Examples from FIPS 180-2
		assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a"
				+ "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f", Encrypts.sha512Hex("abc"));
		assertEquals("ddaf35a193617abacc417349ae20413112e6fa4e89a97ea20a9eeee64b55d39a"
				+ "2192992a274fc1a836ba3c23a3feebbd454d4423643ce80e2a9ac94fa54ca49f",
			Encrypts.sha512Hex(Strings.getBytesUtf8("abc")));
		assertEquals("8e959b75dae313da8cf4f72814fc143f8f7779c6eb9f7fa17299aeadb6889018"
				+ "501d289e4900f7e4331b99dec4b5433ac7d329eeb6dd26545e96e55b874be909",
			Encrypts.sha512Hex("abcdefghbcdefghicdefghijdefghijkefghijklfghijklmghijklmn"
					+ "hijklmnoijklmnopjklmnopqklmnopqrlmnopqrsmnopqrstnopqrstu"));
		assertEquals(Encrypts.sha512Hex(testData), Encrypts.sha512Hex(new ByteArrayInputStream(testData)));
	}

}
