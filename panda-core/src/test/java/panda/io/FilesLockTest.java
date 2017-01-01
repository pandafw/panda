package panda.io;

import java.io.File;

import org.junit.Assert;

import panda.io.Files.FileLocker;
import panda.lang.Threads;

/**
 * This is used to test Files.lock() method.
 */
public class FilesLockTest extends FileBasedTestCase {
	// This class has been broken out from FilesTestCase
	// to solve issues as per BZ 38927

	public FilesLockTest(final String name) {
		super(name);
	}

	/** @see junit.framework.TestCase#setUp() */
	@Override
	protected void setUp() throws Exception {
		getTestDirectory().mkdirs();
	}

	/** @see junit.framework.TestCase#tearDown() */
	@Override
	protected void tearDown() throws Exception {
		Files.deleteDir(getTestDirectory());
	}

	protected static class Rlock implements Runnable {
		public Throwable ex;
		private boolean wait;
		private long delay;
		private String name;
		
		public Rlock(String name, boolean wait) {
			this.name = name;
			this.wait = wait;
		}
		
		public void alock(long time) {
			this.delay = time;
			Thread t = new Thread(this);
			t.start();
		}

		public boolean lock(long time) {
			FileLocker fl = null;
			try {
				File f = new File(getTestDirectory(), name);
				fl = wait ? Files.lock(f) : Files.tryLock(f);
				if (time > 0) {
					Threads.safeSleep(time);
				}
				return fl != null;
			}
			catch (Exception e) {
				ex = e;
				return false;
			}
			finally {
				if (fl != null) {
					Streams.safeClose(fl);
				}
			}
		}

		@Override
		public void run() {
			lock(delay);
		}
		
	}
	// -----------------------------------------------------------------------
	public void testLock() {
		String name = "1.lock";

		Rlock l1 = new Rlock(name, true);
		l1.alock(1000);
		
		Threads.safeSleep(100);
		Rlock l2 = new Rlock(name, false);
		Assert.assertFalse(l2.lock(0));
		
		Threads.safeSleep(1500);
		Assert.assertTrue(l2.lock(0));
	}

}
