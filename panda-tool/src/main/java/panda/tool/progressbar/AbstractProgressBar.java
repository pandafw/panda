package panda.tool.progressbar;

import panda.util.Percent;

/**
 * AbstractProgressBar
 */
public abstract class AbstractProgressBar extends Percent implements Runnable {
	/**
	 * DEFAULT_SPEED = 50;
	 */
	public static int DEFAULT_SPEED = 50;
	
	private int speed;
	
	private boolean daemon;

	private volatile Thread thread;

	/**
	 * constructor
	 */
	public AbstractProgressBar() {
		daemon = true;
		speed = DEFAULT_SPEED;
	}

	/**
	 * @return speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * 10 ....50(default).... 1000
	 * fast ................. slow
	 * @param speed speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the daemon
	 */
	public boolean isDaemon() {
		return daemon;
	}

	/**
	 * @param daemon the daemon to set
	 */
	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	/**
	 * start
	 */
	public void start() {
		if (thread == null) {
			thread = new Thread(this);
			thread.setDaemon(daemon);
			thread.start();
		}
	}

	/**
	 * stop
	 */
	public void stop() {
		thread = null;
		draw();
	}

	/**
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread thisThread = Thread.currentThread();
		while (thread == thisThread) {
			draw();
			try {
				Thread.sleep(speed);
			}
			catch (InterruptedException e) {	
			}
		}
	}

	/**
	 * draw progress
	 */
	protected abstract void draw();
}
