package panda.lang;

import org.junit.Assert;
import org.junit.Test;

public class AsiaStringsTest {
	@Test
	public void printLine() {
		System.out.println("HANKAKU_ASCII:   " + AsiaStrings.HANKAKU_ASCII);
		System.out.println("ZENKAKU_ASCII:   " + AsiaStrings.ZENKAKU_ASCII);
		System.out.println("HANKAKU_NORMAL:  " + AsiaStrings.HANKAKU_NORMAL);
		System.out.println("ZENKAKU_NORMAL:  " + AsiaStrings.ZENKAKU_NORMAL);
		System.out.println("HANKAKU_KASATAHA:" + AsiaStrings.HANKAKU_KASATAHA);
		System.out.println("ZENKAKU_KASATAHA:" + AsiaStrings.ZENKAKU_KASATAHA);
		System.out.println("ZENKAKU_GAZADABA:" + AsiaStrings.ZENKAKU_GAZADABA);
		System.out.println("HANKAKU_WAOU:    " + AsiaStrings.HANKAKU_WAOU);
		System.out.println("ZENKAKU_WAOU:    " + AsiaStrings.ZENKAKU_WAOU);
		System.out.println("ZENKAKU_VAVO:    " + AsiaStrings.ZENKAKU_VAVO);
		System.out.println("HANKAKU_HANDAKU: " + AsiaStrings.HANKAKU_HANDAKU);
		System.out.println("ZENKAKU_HANDAKU: " + AsiaStrings.ZENKAKU_HANDAKU);
	}
	
	private void print(String name, String zen, String han) {
		Assert.assertEquals(zen.length(), han.length());
		
		System.out.println(Strings.center(name, 40, '/'));
		for (int i = 0; i < zen.length(); i++) {
			char z = zen.charAt(i);
			char h = han.charAt(i);
			System.out.println("{ 0x" + Strings.leftPad(Integer.toString(z, 16), 4, '0') 
				+ ", 0x" + Strings.leftPad(Integer.toString(h, 16), 4, '0') + " },  //"
				+ z + " --> " + h + ' ');
		}
	}
	
	@Test
	public void printPair() {
		print("HANKAKU_ASCII:   ", AsiaStrings.ZENKAKU_ASCII, AsiaStrings.HANKAKU_ASCII);
		print("HANKAKU_NORMAL:  ", AsiaStrings.ZENKAKU_NORMAL, AsiaStrings.HANKAKU_NORMAL);
		print("HANKAKU_KASATAHA:", AsiaStrings.ZENKAKU_KASATAHA, AsiaStrings.HANKAKU_KASATAHA);
		print("HANKAKU_WAOU:    ", AsiaStrings.ZENKAKU_WAOU, AsiaStrings.HANKAKU_WAOU);
		print("HANKAKU_HANDAKU: ", AsiaStrings.ZENKAKU_HANDAKU, AsiaStrings.HANKAKU_HANDAKU);
	}
	
	@Test
	public void testLength() {
		Assert.assertEquals(AsiaStrings.HANKAKU_ASCII.length(), AsiaStrings.ZENKAKU_ASCII.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_NORMAL.length(), AsiaStrings.ZENKAKU_NORMAL.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_KASATAHA.length(), AsiaStrings.ZENKAKU_KASATAHA.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_KASATAHA.length(), AsiaStrings.ZENKAKU_GAZADABA.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_WAOU.length(), AsiaStrings.ZENKAKU_WAOU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_WAOU.length(), AsiaStrings.ZENKAKU_VAVO.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_DAKU.length(), AsiaStrings.ZENKAKU_DAKU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_HANDAKU.length(), AsiaStrings.ZENKAKU_HANDAKU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU.length(), AsiaStrings.ZENKAKU.length());
	}
}
