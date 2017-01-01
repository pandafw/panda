package panda.lang;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Simple customizable helper class for creating threads. Provides various
 * bean properties, such as thread name prefix, thread priority, etc.
 */
public class ThreadCreator implements ThreadFactory {

	private String threadNamePrefix;

	private int threadPriority = Thread.NORM_PRIORITY;

	private boolean daemon = false;

	private ThreadGroup threadGroup;

	private AtomicInteger threadCount = new AtomicInteger(0);


	/**
	 * Create a new ThreadCreator with default thread name prefix.
	 */
	public ThreadCreator() {
		this.threadNamePrefix = getDefaultThreadNamePrefix();
	}

	/**
	 * Create a new ThreadCreator with the given thread name prefix.
	 * @param threadNamePrefix the prefix to use for the names of newly created threads
	 */
	public ThreadCreator(String threadNamePrefix) {
		this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
	}


	/**
	 * Specify the prefix to use for the names of newly created threads.
	 */
	public void setThreadNamePrefix(String threadNamePrefix) {
		this.threadNamePrefix = (threadNamePrefix != null ? threadNamePrefix : getDefaultThreadNamePrefix());
	}

	/**
	 * Return the thread name prefix to use for the names of newly
	 * created threads.
	 */
	public String getThreadNamePrefix() {
		return this.threadNamePrefix;
	}

	/**
	 * Set the priority of the threads that this factory creates.
	 * Default is 5.
	 * @see java.lang.Thread#NORM_PRIORITY
	 */
	public void setThreadPriority(int threadPriority) {
		this.threadPriority = threadPriority;
	}

	/**
	 * Return the priority of the threads that this factory creates.
	 */
	public int getThreadPriority() {
		return this.threadPriority;
	}

	/**
	 * Set whether this factory is supposed to create daemon threads,
	 * just executing as long as the application itself is running.
	 * <p>Default is "false": Concrete factories usually support explicit
	 * cancelling. Hence, if the application shuts down, Runnables will
	 * by default finish their execution.
	 * <p>Specify "true" for eager shutdown of threads which still
	 * actively execute a Runnable.
	 * @see java.lang.Thread#setDaemon
	 */
	public void setDaemon(boolean daemon) {
		this.daemon = daemon;
	}

	/**
	 * Return whether this factory should create daemon threads.
	 */
	public boolean isDaemon() {
		return this.daemon;
	}

	/**
	 * Specify the name of the thread group that threads should be created in.
	 * @see #setThreadGroup
	 */
	public void setThreadGroupName(String name) {
		this.threadGroup = new ThreadGroup(name);
	}

	/**
	 * Specify the thread group that threads should be created in.
	 * @see #setThreadGroupName
	 */
	public void setThreadGroup(ThreadGroup threadGroup) {
		this.threadGroup = threadGroup;
	}

	/**
	 * Return the thread group that threads should be created in
	 * (or {@code null}) for the default group.
	 */
	public ThreadGroup getThreadGroup() {
		return this.threadGroup;
	}


	/**
	 * Template method for the creation of a Thread.
	 * <p>Default implementation creates a new Thread for the given
	 * Runnable, applying an appropriate thread name.
	 * @param runnable the Runnable to execute
	 * @see #nextThreadName()
	 */
	public Thread createThread(Runnable runnable) {
		Thread thread = new Thread(getThreadGroup(), runnable, nextThreadName());
		thread.setPriority(getThreadPriority());
		thread.setDaemon(isDaemon());
		return thread;
	}

	@Override
	public Thread newThread(Runnable runnable) {
		return createThread(runnable);
	}

	/**
	 * Return the thread name to use for a newly created thread.
	 * <p>Default implementation returns the specified thread name prefix
	 * with an increasing thread count appended: for example,
	 * "SimpleAsyncTaskExecutor-0".
	 * @see #getThreadNamePrefix()
	 */
	protected String nextThreadName() {
		int threadNumber = threadCount.incrementAndGet();
		return getThreadNamePrefix() + '-' + threadNumber;
	}

	/**
	 * Build the default thread name prefix for this factory.
	 * @return the default thread name prefix (never {@code null})
	 */
	protected String getDefaultThreadNamePrefix() {
		return getClass().getSimpleName();
	}

}