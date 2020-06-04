package panda.lang.chardet;

public class CharDetects {
	public static String detectCharset(byte[] bs) {
		return detectCharset(LangHint.ALL, bs);
	}

	public static String detectCharset(LangHint lang, byte[] bs) {
		return detectCharset(lang, bs, bs.length);
	}

	public static String detectCharset(LangHint lang, byte[] bs, int len) {
		CharDetector det = new CharDetector(lang);

		if (det.detect(bs, len)) {
			return det.getDetectedCharset();
		}

		det.done();
		return det.getProbableCharset();
	}
}
