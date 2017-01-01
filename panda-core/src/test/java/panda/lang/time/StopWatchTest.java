package panda.lang.time;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

/**
 * TestCase for StopWatch.
 *
 */
public class StopWatchTest {

	// -----------------------------------------------------------------------
	@Test
	public void testStopWatchSimple() {
		final StopWatch watch = new StopWatch(false);
		watch.start();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.stop();
		final long time = watch.getTime();
		assertEquals(time, watch.getTime());

		assertTrue(time >= 500);
		assertTrue(time < 700);

		watch.reset();
		assertEquals(0, watch.getTime());
	}

	@Test
	public void testStopWatchSimpleGet() {
		final StopWatch watch = new StopWatch(false);
		assertEquals(0, watch.getTime());
		assertEquals("0 ms", watch.toString());

		watch.start();
		try {
			Thread.sleep(500);
		}
		catch (final InterruptedException ex) {
		}
		assertTrue(watch.getTime() < 2000);
	}

	@Test
	public void testStopWatchSplit() {
		final StopWatch watch = new StopWatch(false);
		watch.start();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.split();
		final long splitTime = watch.getSplitTime();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.unsplit();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.stop();
		final long totalTime = watch.getTime();

		assertTrue(splitTime >= 500);
		assertTrue(splitTime < 700);
		assertTrue(totalTime >= 1500);
		assertTrue(totalTime < 1900);
	}

	@Test
	public void testStopWatchSuspend() {
		final StopWatch watch = new StopWatch(false);
		watch.start();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.suspend();
		final long suspendTime = watch.getTime();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.resume();
		try {
			Thread.sleep(550);
		}
		catch (final InterruptedException ex) {
		}
		watch.stop();
		final long totalTime = watch.getTime();

		assertTrue(suspendTime >= 500);
		assertTrue(suspendTime < 700);
		assertTrue(totalTime >= 1000);
		assertTrue(totalTime < 1300);
	}

	@Test
	public void testLang315() {
		final StopWatch watch = new StopWatch(false);
		watch.start();
		try {
			Thread.sleep(200);
		}
		catch (final InterruptedException ex) {
		}
		watch.suspend();
		final long suspendTime = watch.getTime();
		try {
			Thread.sleep(200);
		}
		catch (final InterruptedException ex) {
		}
		watch.stop();
		final long totalTime = watch.getTime();
		assertTrue(suspendTime == totalTime);
	}

	// test bad states
	@Test
	public void testBadStates() {
		final StopWatch watch = new StopWatch(false);
		try {
			watch.stop();
			fail("Calling stop on an unstarted StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.stop();
			fail("Calling stop on an unstarted StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.suspend();
			fail("Calling suspend on an unstarted StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.split();
			fail("Calling split on a non-running StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.unsplit();
			fail("Calling unsplit on an unsplit StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.resume();
			fail("Calling resume on an unsuspended StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		watch.start();

		try {
			watch.start();
			fail("Calling start on a started StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.unsplit();
			fail("Calling unsplit on an unsplit StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.getSplitTime();
			fail("Calling getSplitTime on an unsplit StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		try {
			watch.resume();
			fail("Calling resume on an unsuspended StopWatch should throw an exception. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}

		watch.stop();

		try {
			watch.start();
			fail("Calling start on a stopped StopWatch should throw an exception as it needs to be reset. ");
		}
		catch (final IllegalStateException ise) {
			// expected
		}
	}

	@Test
	public void testGetStartTime() {
		final long beforeStopWatch = System.currentTimeMillis();
		final StopWatch watch = new StopWatch(false);
		try {
			watch.getStartTime();
			fail("Calling getStartTime on an unstarted StopWatch should throw an exception");
		}
		catch (final IllegalStateException expected) {
			// expected
		}
		watch.start();
		try {
			watch.getStartTime();
			Assert.assertTrue(watch.getStartTime() >= beforeStopWatch);
		}
		catch (final IllegalStateException ex) {
			fail("Start time should be available: " + ex.getMessage());
		}
		watch.reset();
		try {
			watch.getStartTime();
			fail("Calling getStartTime on a reset, but unstarted StopWatch should throw an exception");
		}
		catch (final IllegalStateException expected) {
			// expected
		}
	}

	@Test
	public void testBooleanStates() {
		final StopWatch watch = new StopWatch(false);
		assertFalse(watch.isStarted());
		assertFalse(watch.isSuspended());
		assertTrue(watch.isStopped());

		watch.start();
		assertTrue(watch.isStarted());
		assertFalse(watch.isSuspended());
		assertFalse(watch.isStopped());

		watch.suspend();
		assertTrue(watch.isStarted());
		assertTrue(watch.isSuspended());
		assertFalse(watch.isStopped());

		watch.stop();
		assertFalse(watch.isStarted());
		assertFalse(watch.isSuspended());
		assertTrue(watch.isStopped());
	}

}
