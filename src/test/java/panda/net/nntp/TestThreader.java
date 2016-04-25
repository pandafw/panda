package panda.net.nntp;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import panda.net.nntp.Threadable;
import panda.net.nntp.Threader;

/**
 * Test the Threader
 */
public class TestThreader {

	@Test
	@SuppressWarnings("deprecation")
	// test of deprecated method
	public void testNullArray() { // NET-539
		Threader t = new Threader();
		Threadable[] messages = null;
		Assert.assertNull(t.thread(messages));
	}

	@Test
	public void testNullList() {
		Threader t = new Threader();
		List<Threadable> messages = null;
		Assert.assertNull(t.thread(messages));
	}

	@Test
	public void testNullIterable() {
		Threader t = new Threader();
		Iterable<Threadable> messages = null;
		Assert.assertNull(t.thread(messages));
	}

	@SuppressWarnings("deprecation")
	// test of deprecated method
	@Test
	public void testEmptyArray() { // NET-539
		Threader t = new Threader();
		Threadable[] messages = new Threadable[0];
		Assert.assertNull(t.thread(messages));
	}

	@Test
	public void testEmptyList() { // NET-539
		Threader t = new Threader();
		Threadable[] messages = new Threadable[0];
		final List<Threadable> asList = Arrays.asList(messages);
		Assert.assertNull(t.thread(asList));
	}

	@Test
	public void testEmptyIterable() { // NET-539
		Threader t = new Threader();
		Threadable[] messages = new Threadable[0];
		final Iterable<Threadable> asList = Arrays.asList(messages);
		Assert.assertNull(t.thread(asList));
	}

}
