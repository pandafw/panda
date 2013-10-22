package panda.lang;

import org.junit.Assert;
import org.junit.Test;

public class AsiaStringsTest {

	@Test
	public void test01() {
		Assert.assertEquals(AsiaStrings.HANKAKU_ASCII.length(), AsiaStrings.ZENKAKU_ASCII.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_NORMAL.length(), AsiaStrings.ZENKAKU_NORMAL.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_KASATAHA.length(), AsiaStrings.ZENKAKU_KASATAHA.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_KASATAHA.length(), AsiaStrings.ZENKAKU_GAZADABA.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_WAOU.length(), AsiaStrings.ZENKAKU_WAOU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_WAOU.length(), AsiaStrings.ZENKAKU_VAVO.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_DAKU.length(), AsiaStrings.ZENKAKU_DAKU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU_HANDAKU.length(), AsiaStrings.ZENKAKU_HANDAKU.length());
		Assert.assertEquals(AsiaStrings.HANKAKU.length(), AsiaStrings.ZENKAKU.length());

		System.out.println(AsiaStrings.HANKAKU_ASCII);
		System.out.println(AsiaStrings.ZENKAKU_ASCII);
		System.out.println(AsiaStrings.HANKAKU_NORMAL);
		System.out.println(AsiaStrings.ZENKAKU_NORMAL);
		System.out.println(AsiaStrings.HANKAKU_KASATAHA);
		System.out.println(AsiaStrings.ZENKAKU_KASATAHA);
		System.out.println(AsiaStrings.ZENKAKU_GAZADABA);
		System.out.println(AsiaStrings.HANKAKU_WAOU);
		System.out.println(AsiaStrings.ZENKAKU_WAOU);
		System.out.println(AsiaStrings.ZENKAKU_VAVO);
		System.out.println(AsiaStrings.HANKAKU_HANDAKU);
		System.out.println(AsiaStrings.ZENKAKU_HANDAKU);
	}
}
