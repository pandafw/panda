package panda.lang;

import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

/**
 * 
 * @author yf.frank.wang@gmail.com
 *
 */
public class HtmlUtils {
	public static Document parseDOM(String html) {
		Tidy tidy = new Tidy();
		tidy.setShowErrors(0);
		tidy.setShowWarnings(false);
		tidy.setQuiet(true);
		tidy.setXHTML(true);

		Document doc = tidy.parseDOM(new StringReader(html), null);
		return doc;
	}
}
