package panda.task;

import panda.lang.Asserts;
import panda.lang.ThreadCreator;
import panda.log.Log;
import panda.log.Logs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * Base class for classes that are setting up a
 * {@code java.util.concurrent.ExecutorService}
 * (typically a {@link java.util.concurrent.ThreadPoolExecutor}).
 * Defines common configuration settings and common lifecycle handling.
 *
 * @see java.util.concurrent.ExecutorService
 * @see java.util.concurrent.Executors
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public abstract class TaskExecutorSupport extends ThreadCreator {

	protected final Log log = Logs.getLog(getClass());

	private ThreadFactory threadFactory = this;

	private RejectedExecutionHandler rejectedExecutionHandler = new ThreadPoolExecutor.AbortPolicy();

	private boolean waitForTasksToCompleteOnShutdown = false;

	private int awaitTerminationSeconds = 0;

	private ExecutorService executor;

	/**
	 * @return the name
	 */
	public String getName() {
		return getThreadNamePrefix();
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		setThreadNamePrefix(name);
	}

	/**
	 * Set the ThreadFactory to use for the ExecutorService's thread pool.
	 * Default is the underlying ExecutorService's default thread factory.
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		this.threadFactory = (threadFactory != null ? threadFactory : this);
	}

	/**
	 * Set the RejectedExecutionHandler to use for the ExecutorService.
	 * Default is the ExecutorService's default abort policy.
	 * @see java.util.concurrent.ThreadPoolExecutor.AbortPolicy
	 */
	public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
		this.rejectedExecutionHandler =
				(rejectedExecutionHandler != null ? rejectedExecutionHandler : new ThreadPoolExecutor.AbortPolicy());
	}

	/**
	 * Set whether to wait for scheduled tasks to complete on shutdown,
	 * not interrupting running tasks and executing all tasks in the queue.
	 * <p>Default is "false", shutting down immediately through interrupting
	 * ongoing tasks and clearing the queue. Switch this flag to "true" if you
	 * prefer fully completed tasks at the expense of a longer shutdown phase.
	 * <p>Note that application container shutdown continues while ongoing tasks
	 * are being completed. If you want this executor to block and wait for the
	 * termination of tasks before the rest of the container continues to shut
	 * down - e.g. in order to keep up other resources that your tasks may need -,
	 * set the {@link #setAwaitTerminationSeconds "awaitTerminationSeconds"}
	 * property instead of or in addition to this property.
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 */
	public void setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
		this.waitForTasksToCompleteOnShutdown = waitForJobsToCompleteOnShutdown;
	}

	/**
	 * Set the maximum number of seconds that this executor is supposed to block
	 * on shutdown in order to wait for remaining tasks to complete their execution
	 * before the rest of the container continues to shut down. This is particularly
	 * useful if your remaining tasks are likely to need access to other resources
	 * that are also managed by the container.
	 * <p>By default, this executor won't wait for the termination of tasks at all.
	 * It will either shut down immediately, interrupting ongoing tasks and clearing
	 * the remaining task queue - or, if the
	 * {@link #setWaitForTasksToCompleteOnShutdown "waitForTasksToCompleteOnShutdown"}
	 * flag has been set to {@code true}, it will continue to fully execute all
	 * ongoing tasks as well as all remaining tasks in the queue, in parallel to
	 * the rest of the container shutting down.
	 * <p>In either case, if you specify an await-termination period using this property,
	 * this executor will wait for the given time (max) for the termination of tasks.
	 * As a rule of thumb, specify a significantly higher timeout here if you set
	 * "waitForTasksToCompleteOnShutdown" to {@code true} at the same time,
	 * since all remaining tasks in the queue will still get executed - in contrast
	 * to the default shutdown behavior where it's just about waiting for currently
	 * executing tasks that aren't reacting to thread interruption.
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#awaitTermination
	 */
	public void setAwaitTerminationSeconds(int awaitTerminationSeconds) {
		this.awaitTerminationSeconds = awaitTerminationSeconds;
	}

	/**
	 * Set up the ExecutorService.
	 */
	public void initialize() {
		Asserts.validState(executor == null, "ExecutorService " + getName() + " already initialized");
		if (log.isInfoEnabled()) {
			log.info("Initializing ExecutorService " + getName());
		}
		executor = initializeExecutor(this.threadFactory, this.rejectedExecutionHandler);
	}

	/**
	 * Create the target {@link java.util.concurrent.ExecutorService} instance.
	 * Called by {@code afterPropertiesSet}.
	 * @param threadFactory the ThreadFactory to use
	 * @param rejectedExecutionHandler the RejectedExecutionHandler to use
	 * @return a new ExecutorService instance
	 */
	protected abstract ExecutorService initializeExecutor(
			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler);


	/**
	 * Perform a shutdown on the underlying ExecutorService.
	 * @see java.util.concurrent.ExecutorService#shutdown()
	 * @see java.util.concurrent.ExecutorService#shutdownNow()
	 * @see #awaitTerminationIfNecessary()
	 */
	public void shutdown() {
		Asserts.validState(executor != null, "ExecutorService " + getName() + " not initialized");

		if (log.isInfoEnabled()) {
			log.info("Shutting down ExecutorService " + getName());
		}
		if (waitForTasksToCompleteOnShutdown) {
			executor.shutdown();
		}
		else {
			executor.shutdownNow();
		}
		awaitTerminationIfNecessary();
		executor = null;
	}

	/**
	 * Wait for the executor to terminate, according to the value of the
	 * {@link #setAwaitTerminationSeconds "awaitTerminationSeconds"} property.
	 */
	private void awaitTerminationIfNecessary() {
		if (awaitTerminationSeconds > 0) {
			try {
				if (!executor.awaitTermination(awaitTerminationSeconds, TimeUnit.SECONDS)) {
					if (log.isWarnEnabled()) {
						log.warn("Timed out while waiting for executor" + getName() + " to terminate");
					}
				}
			}
			catch (InterruptedException ex) {
				if (log.isWarnEnabled()) {
					log.warn("Interrupted while waiting for executor" + getName() + " to terminate");
				}
				Thread.currentThread().interrupt();
			}
		}
	}

}