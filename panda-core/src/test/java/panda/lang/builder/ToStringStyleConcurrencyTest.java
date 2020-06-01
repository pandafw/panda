package panda.lang.builder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

/**
 * Tests concurrent access for the default {@link ToStringStyle}.
 * <p>
 * The {@link ToStringStyle} class includes a registry to avoid infinite loops for objects with circular references. We
 * want to make sure that we do not get concurrency exceptions accessing this registry.
 * </p>
 * <p>
 * This test passes but only tests one aspect of the issue.
 * </p>
 * 
 */
public class ToStringStyleConcurrencyTest {

	static class CollectionHolder<T extends Collection<?>> {
		T collection;

		CollectionHolder(final T collection) {
			this.collection = collection;
		}
	}

	private static final List<Integer> LIST;
	private static final int LIST_SIZE = 100000;
	private static final int REPEAT = 100;

	static {
		LIST = new ArrayList<Integer>(LIST_SIZE);
		for (int i = 0; i < LIST_SIZE; i++) {
			LIST.add(Integer.valueOf(i));
		}
	}

	@Test
	public void testLinkedList() throws InterruptedException, ExecutionException {
		this.testConcurrency(new CollectionHolder<List<Integer>>(new LinkedList<Integer>()));
	}

	@Test
	public void testArrayList() throws InterruptedException, ExecutionException {
		this.testConcurrency(new CollectionHolder<List<Integer>>(new ArrayList<Integer>()));
	}

	@Test
	public void testCopyOnWriteArrayList() throws InterruptedException, ExecutionException {
		this.testConcurrency(new CollectionHolder<List<Integer>>(
			new CopyOnWriteArrayList<Integer>()));
	}

	private void testConcurrency(final CollectionHolder<List<Integer>> holder)
			throws InterruptedException, ExecutionException {
		final List<Integer> list = holder.collection;
		// make a big array that takes a long time to toString()
		list.addAll(LIST);
		// Create a thread pool with two threads to cause the most contention on the underlying
		// resource.
		final ExecutorService threadPool = Executors.newFixedThreadPool(2);
		// Consumes toStrings
		final Callable<Integer> consumer = new Callable<Integer>() {
			public Integer call() {
				for (int i = 0; i < REPEAT; i++) {
					// Calls ToStringStyle
					new ToStringBuilder(holder).append(holder.collection);
				}
				return Integer.valueOf(REPEAT);
			}
		};
		final Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
		tasks.add(consumer);
		tasks.add(consumer);
		final List<Future<Integer>> futures = threadPool.invokeAll(tasks);
		for (final Future<Integer> future : futures) {
			future.get();
		}
	}
}
