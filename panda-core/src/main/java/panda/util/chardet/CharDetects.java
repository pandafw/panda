package panda.util.chardet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import panda.lang.Exceptions;

public class CharDetects {
	public static String[] detectCharsets(InputStream content) throws IOException {
		return detectCharsets(content, LangHint.ALL);
	}

	public static String[] detectCharsets(InputStream content, LangHint lang) throws IOException {
		nsDetector det = new nsDetector(lang);

		if (det.DoIt(content)) {
			return new String[] { det.getDetectedCharset() };
		}

		det.Done();

		return det.getProbableCharsets();
	}

	public static String detectCharset(InputStream content) throws IOException {
		return detectCharset(content, LangHint.ALL);
	}

	public static String detectCharset(InputStream content, LangHint lang) throws IOException {
		nsDetector det = new nsDetector(lang);

		if (det.DoIt(content)) {
			return det.getDetectedCharset();
		}

		det.Done();
		return det.getProbableCharset();
	}

	public static String[] detectCharsets(byte[] content) {
		return detectCharsets(content, LangHint.ALL);
	}

	public static String[] detectCharsets(byte[] content, LangHint lang) {
		try {
			return detectCharsets(new ByteArrayInputStream(content), lang);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public static String detectCharset(byte[] content) {
		return detectCharset(content, LangHint.ALL);
	}

	public static String detectCharset(byte[] content, LangHint lang) {
		try {
			return detectCharset(new ByteArrayInputStream(content), lang);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
}
