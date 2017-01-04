package panda.tool.progressbar;

import java.io.PrintStream;

import panda.lang.Arrays;

/**
 * Progress bar of console
 */
public abstract class ConsoleProgressBar extends AbstractProgressBar {

	protected final static char BACKSPACE = '\b';
	
	protected PrintStream out;
	
	protected int size;
	
	protected char[] line;
	
	/**
	 * constructor
	 */
	public ConsoleProgressBar() {
		this(System.out);
	}

	/**
	 * constructor
	 * @param out output
	 */
	public ConsoleProgressBar(PrintStream out) {
		super();
		this.out = out;
	}

	/**
	 * @return out
	 */
	public PrintStream getOut() {
		return out;
	}

	/**
	 * @param out out
	 */
	public void setOut(PrintStream out) {
		this.out = out;
	}

	/**
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size size
	 */
	public void setSize(int size) {
		if (this.size != size) {
			this.size = size;
			line = new char[size];
		}
	}

	/**
	 * space
	 */
	protected void space() {
		Arrays.fill(line, ' ');
		out.print(line);
	}

	/**
	 * clear
	 */
	protected void clear() {
		Arrays.fill(line, BACKSPACE);
		out.print(line);
	}
}
