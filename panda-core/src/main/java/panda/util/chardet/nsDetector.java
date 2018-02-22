package panda.util.chardet ;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 *
 */
public class nsDetector extends nsPSMDetector {

	private String detectedCharset;

	public nsDetector() {
		super();
	}

	public nsDetector(LangHint langHint) {
		super(langHint);
	}

	public boolean DoIt(InputStream ins) throws IOException {
		this.HandleData(ins);
		return mDone;
	}

	public void Done() {
		this.DataEnd();
	}

	public String getDetectedCharset() {
		return detectedCharset;
	}

	protected void Report(String charset) {
		this.detectedCharset = charset;
	}

	public boolean isAscii(byte[] aBuf, int aLen) {
		for (int i = 0; i < aLen; i++) {
			if ((0x0080 & aBuf[i]) != 0) {
				return false;
			}
		}
		return true;
	}
}
