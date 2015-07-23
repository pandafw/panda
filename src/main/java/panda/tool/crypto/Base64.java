package panda.tool.crypto;

import panda.lang.Charsets;
import panda.lang.Strings;


/**
 */
public class Base64 {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("Base64 d|e <string>");
			System.exit(1);
		}
		
		boolean d = "D".equalsIgnoreCase(args[0]);
		for (int i = 1; i < args.length; i++) {
			try {
				String s = null;
				if (d) {
					s = Strings.newStringUtf8(panda.lang.codec.binary.Base64.decodeBase64(args[i]));
				}
				else {
					s = panda.lang.codec.binary.Base64.encodeBase64String(args[i].getBytes(Charsets.CS_UTF_8));
				}
				System.out.println(s);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
