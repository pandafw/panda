package panda.util.progressbar;

import java.io.PrintStream;

import panda.lang.Strings;

/**
 * remain time information of progress on console
 */
public class ConsoleRemainTimeBar extends ConsoleProgressBar {

	private long startTime;
	
	private long lastTime;
	
	/**
	 * constructor
	 */
	public ConsoleRemainTimeBar() {
		super();
		
		setSpeed(500);
		setSize(8);
	}

	/**
	 * constructor
	 * @param out output
	 */
	public ConsoleRemainTimeBar(PrintStream out) {
		super(out);
		
		setSpeed(500);
		setSize(8);
	}

	
	/**
	 * @see ConsoleProgressBar#start()
	 */
	@Override
	public void start() {
		startTime = System.currentTimeMillis();
		lastTime = startTime;
		super.start();
	}

	/**
	 * draw progress
	 */
	protected void draw() {
		clear();

		String st;
		if (value <= min) {
			st = "??:??:??";
		}
		else {
			lastTime = System.currentTimeMillis();

			long elapsedTime = lastTime - startTime;
			
			long remainTime = (long)(elapsedTime / (value - min) * (max - value));
			long remainSecond = remainTime / 1000;
			
			long hh = remainSecond / 3600;
			long mm = remainSecond % 3600; mm /= 60;
			long ss = remainSecond % 60;
	
			st = Strings.leftPad(String.valueOf(hh), 2, '0') 
					+ ':' 
					+ Strings.leftPad(String.valueOf(mm), 2, '0')
					+ ':'
					+ Strings.leftPad(String.valueOf(ss), 2, '0');
		}
		
		setSize(st.length());
		
		out.print(st);
		out.flush();
	}

}
