package panda.lang.chardet ;

/**
 * 
 *
 */
public class CharDetector extends nsPSMDetector {

	private String detectedCharset;

	public CharDetector() {
		super();
	}

	public CharDetector(LangHint langHint) {
		super(langHint);
	}

	protected boolean isAscii(byte[] aBuf, int aLen) {
		for (int i = 0; i < aLen; i++) {
			if ((0x0080 & aBuf[i]) != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean detect(byte[] in, int len) {
		if (mDone) {
			return true;
		}
		
		this.HandleData(in, len);
		return mDone;
	}

	public void done() {
		this.DataEnd();
	}

	public String getDetectedCharset() {
		return detectedCharset;
	}

	protected void Report(String charset) {
		this.detectedCharset = charset;
	}
}
