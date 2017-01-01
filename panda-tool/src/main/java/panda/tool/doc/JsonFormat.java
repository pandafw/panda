package panda.tool.doc;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;

import panda.bind.json.Jsons;
import panda.lang.Charsets;

public class JsonFormat {
	public static void main(String[] args) throws Exception {
		InputStream in = System.in;
		PrintStream ou = System.out;
		if (args.length > 0) {
			in = new FileInputStream(args[0]);
		}
		if (args.length > 1) {
			ou = new PrintStream(args[1]);
		}
		
		Object json = Jsons.fromJson(in, Charsets.UTF_8);
		Jsons.toJson(json, ou, true);
		
		ou.close();
	}
}
