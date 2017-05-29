package panda.tool.chardet;

import java.io.InputStream;
import java.net.URL;

import panda.args.Argument;
import panda.args.Option;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.HandledException;
import panda.lang.chardet.LangHint;
import panda.tool.AbstractCommandTool;

/**
 * A class used for detect text encoding
 */
public class CharDetect extends AbstractCommandTool {
	/**
	 * main
	 * @param args arugments
	 */
	public static void main(String args[]) {
		new CharDetect().execute(args);
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String url;
	private LangHint hint;
	

	/**
	 * @param url the url to set
	 */
	@Argument(name="URL", required=true, usage="The URL to detect character encoding")
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param hint the hint to set
	 */
	@Option(opt='h', option="hint", arg="HINT", usage="Language hint. (ja | zh | cn | tw | ko)")
	public void setHint(String hint) {
		this.hint = LangHint.parse(hint);
	}

	/**
	 * execute
	 */
	public void execute() {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
			is = Streams.buffer(is);
			String charset = Charsets.detectCharset(is, hint);
			
			System.out.println(charset);
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new HandledException(e);
		}
		finally {
			Streams.safeClose(is);
		}
	}
}
