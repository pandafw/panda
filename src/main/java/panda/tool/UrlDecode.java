package panda.tool;

import java.io.File;

import panda.io.Files;
import panda.net.URLHelper;

public class UrlDecode {

	public static void main(String[] args) throws Exception {
		String s = Files.readFileToString(new File(args[0]));
		s = URLHelper.decodeURL(s);
		System.out.print(s);
	}

}
