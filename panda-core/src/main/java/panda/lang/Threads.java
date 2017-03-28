package panda.lang;

public class Threads {
	/**
	 * start a runnable and return the thread object
	 * @param r a runnable object
	 * @return thread object
	 */
	public static Thread start(Runnable r) {
		Thread t = new Thread(r);
		t.start();
		return t;
	}
	
	/**
	 * start a runnable and return the thread object
	 * @param r a runnable object
	 * @param name thread name
	 * @return thread object
	 */
	public static Thread start(Runnable r, String name) {
		Thread t = new Thread(r, name);
		t.start();
		return t;
	}

	/**
	 * join, do not throw exception
	 * 
	 * @param t the thread
	 */
	public static void safeJoin(Thread t) {
		try {
			t.join();
		}
		catch (InterruptedException e) {
		}
	}
	
	/**
	 * sleep, do not throw exception
	 * @param ms milliseconds
	 */
	public static void safeSleep(long ms) {
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
		}
	}
}
