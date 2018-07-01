package panda.tool;

import java.io.File;

import panda.io.Streams;
import panda.net.URLHelper;

public class UrlDecode {

	public static void main(String[] args) throws Exception {
		String s = Streams.toString(new File(args[0]));
		s = URLHelper.decodeURL(s);
		System.out.print(s);
	}

}
