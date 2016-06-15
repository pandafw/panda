package panda.tool.mail;

import java.util.List;

import panda.net.MXLookup;

public class MxLookup {

	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			try {
				List<String> hosts = MXLookup.lookup(args[i]);
				for (int j = 0; j < hosts.size(); j++) {
					System.out.println(args[i] + "[" + j + "]: " + hosts.get(j));
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
