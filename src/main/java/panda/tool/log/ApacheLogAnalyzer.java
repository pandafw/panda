package panda.tool.log;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import panda.lang.Numbers;
import panda.lang.Strings;

public class ApacheLogAnalyzer {

	public static void main(String[] args) throws IOException {
		for (String a : args) {
			System.out.print("Parsing " + a + " ... ");

			ApacheLogReader cr = new ApacheLogReader(new FileReader(a));

			int i = 0;
			long si = 0;
			long so = 0;
			
			List<String> items;
			while ((items = cr.readNext()) != null) {
				i++;
				try {
					si += Long.parseLong(items.get(9));
					so += Long.parseLong(items.get(10));
				}
				catch (NumberFormatException e) {
					System.out.println("Failed to parse line(" + i + "): " + items);
					break;
				}
			}
			System.out.println(" IN: " + Strings.leftPad(Numbers.toCommaString(si), 16) + "  OUT: " + Strings.leftPad(Numbers.toCommaString(so), 16));
			
			cr.close();
			
		}
	}


}
