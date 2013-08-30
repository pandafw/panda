package panda.util.progressbar;

import java.io.PrintStream;

import panda.lang.Arrays;
import panda.lang.Strings;

/**
 * Gauge bar of console
 * @author yf.frank.wang@gmail.com
 */
public class ConsoleGaugeBar extends ConsoleProgressBar {

	private final static int DEFAULT_SIZE = 78;
	
	private final static char DEFAULT_FOREGROUND_CHAR = '>';
	
	private final static char DEFAULT_PROGRESS_CHAR = '>';
	
	private final static int DEFAULT_PROGRESS_CHAR_SIZE = 3;
	
	private final static char DEFAULT_BACKGROUND_CHAR = '|';
	
	private char progressChar;
	
	private int progressCharSize;
	
	private char foregroundChar;

	private char backgroundChar;
	
	private boolean drawPercent;
	
	private int saveProgress;
	
	private int savePercent;
	
	/**
	 * @return foregroundChar
	 */
	public char getForegroundChar() {
		return foregroundChar;
	}

	/**
	 * @param foregroundChar foregroundChar
	 */
	public void setForegroundChar(char foregroundChar) {
		this.foregroundChar = foregroundChar;
	}

	/**
	 * @return backgroundChar
	 */
	public char getBackgroundChar() {
		return backgroundChar;
	}

	/**
	 * @param backgroundChar backgroundChar
	 */
	public void setBackgroundChar(char backgroundChar) {
		this.backgroundChar = backgroundChar;
	}

	/**
	 * @return progressChar
	 */
	public char getProgressChar() {
		return progressChar;
	}

	/**
	 * @param progressChar progressChar
	 */
	public void setProgressChar(char progressChar) {
		this.progressChar = progressChar;
	}

	/**
	 * @return progressCharSize
	 */
	public int getProgressCharSize() {
		return progressCharSize;
	}

	/**
	 * @param progressCharSize progressCharSize
	 */
	public void setProgressCharSize(int progressCharSize) {
		this.progressCharSize = progressCharSize;
	}

	/**
	 * @return drawPercent
	 */
	public boolean isDrawPercent() {
		return drawPercent;
	}

	/**
	 * @param drawPercent drawPercent
	 */
	public void setDrawPercent(boolean drawPercent) {
		this.drawPercent = drawPercent;
	}

	/**
	 * constructor
	 */
	public ConsoleGaugeBar() {
		super();
		init();
	}

	/**
	 * constructor
	 * @param out output
	 */
	public ConsoleGaugeBar(PrintStream out) {
		super(out);
		init();
	}

	/**
	 * init progress
	 */
	protected void init() {
		setSize(DEFAULT_SIZE);
		setProgressChar(DEFAULT_PROGRESS_CHAR);
		setProgressCharSize(DEFAULT_PROGRESS_CHAR_SIZE);
		setForegroundChar(DEFAULT_FOREGROUND_CHAR);
		setBackgroundChar(DEFAULT_BACKGROUND_CHAR);
		
		saveProgress = -1;
		savePercent = -1;

		space();
	}
	
	/**
	 * draw progress
	 */
	protected void draw() {
		int percent = getPercent();
		int size = drawPercent ? this.size - 5 : this.size;
		int progress = percent * size / 100;

		if (drawPercent) {
			if (savePercent == percent && saveProgress == progress) {
				return;
			}
		}
		else {
			if (saveProgress == progress) {
				return;
			}
		}
		
		savePercent = percent;
		saveProgress = progress;

		clear();
		
		if (percent == 100) {
			Arrays.fill(line, 0, size, foregroundChar);
		}
		else {
			if (progress > progressCharSize) {
				Arrays.fill(line, 0, progress - progressCharSize, foregroundChar);
				Arrays.fill(line, progress - progressCharSize, progress, progressChar);
			}
			else if (progress > 1) {
				Arrays.fill(line, 0, progress, progressChar);
			}
			if (progress < size) {
				Arrays.fill(line, progress, size, backgroundChar);
			}
		}
		if (drawPercent) {
			out.print(new String(line, 0, size));
			out.print(Strings.leftPad(String.valueOf(percent), 4) + "%");
		}
		else {
			out.print(line);
		}
		out.flush();
	}

}
