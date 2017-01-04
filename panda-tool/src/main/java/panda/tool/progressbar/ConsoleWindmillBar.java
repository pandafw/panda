package panda.tool.progressbar;

import java.io.PrintStream;


/**
 * A progress windmill of console
 */
public class ConsoleWindmillBar extends ConsoleProgressBar {

	private static final char[] WINDMILL = { '-', '\\', '|', '/' };
	
	private int frame = 0;
	
	/**
	 * constructor
	 */
	public ConsoleWindmillBar() {
		super();

		setSize(1);
	}

	/**
	 * constructor
	 * @param out output
	 */
	public ConsoleWindmillBar(PrintStream out) {
		super(out);
		
		setSize(1);
	}

	/**
	 * draw progress
	 */
	protected void draw() {
		clear();
		out.print(WINDMILL[frame]);
		out.flush();
		
		frame++;
		if (frame >= WINDMILL.length) {
			frame = 0;
		}
	}

}
