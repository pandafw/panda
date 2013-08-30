package panda.util.progressbar;

import java.io.PrintStream;

import panda.lang.Strings;

/**
 * Percent bar of console
 * @author yf.frank.wang@gmail.com
 */
public class ConsolePercentBar extends ConsoleProgressBar {

	private int savePercent;
	
	/**
	 * constructor
	 */
	public ConsolePercentBar() {
		super();
		init();
	}

	/**
	 * constructor
	 * @param out output
	 */
	public ConsolePercentBar(PrintStream out) {
		super(out);
		init();
	}

	/**
	 * init progress
	 */
	protected void init() {
		setSize(4);
		savePercent = -1;
		space();
	}
	
	/**
	 * draw progress
	 */
	protected void draw() {
		int percent = getPercent();
		if (savePercent != percent) {
			savePercent = percent;

			clear();
			out.print(Strings.leftPad(String.valueOf(percent), 3) + "%");
			out.flush();
		}
	}
}
