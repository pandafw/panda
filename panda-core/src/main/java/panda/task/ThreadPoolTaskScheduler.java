package panda.task;
import panda.lang.Asserts;
import panda.lang.ErrorHandler;
import panda.lang.ErrorHandlers;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of {@link TaskScheduler} interface, wrapping
 * a native {@link java.util.concurrent.ScheduledThreadPoolExecutor}.
 *
 * @see #setPoolSize
 * @see #setThreadFactory
 * @see #setErrorHandler
 */
public class ThreadPoolTaskScheduler extends TaskExecutorSupport implements TaskScheduler, AsyncTaskExecutor {

	private volatile int poolSize = 1;

	private volatile ScheduledExecutorService scheduledExecutor;

	private volatile ErrorHandler errorHandler;


	/**
	 * Set the ScheduledExecutorService's pool size.
	 * Default is 1.
	 * <p><b>This setting can be modified at runtime, for example through JMX.</b>
	 * 
	 * @param poolSize the poolSize
	 */
	public void setPoolSize(int poolSize) {
		Asserts.isTrue(poolSize > 0, "'poolSize' must be 1 or higher");
		this.poolSize = poolSize;
		if (this.scheduledExecutor instanceof ScheduledThreadPoolExecutor) {
			((ScheduledThreadPoolExecutor) this.scheduledExecutor).setCorePoolSize(poolSize);
		}
	}

	/**
	 * Set a custom {@link ErrorHandler} strategy.
	 * 
	 * @param errorHandler the errorHandler
	 */
	public void setErrorHandler(ErrorHandler errorHandler) {
		Asserts.notNull(errorHandler, "'errorHandler' must not be null");
		this.errorHandler = errorHandler;
	}

	@Override
	protected ExecutorService initializeExecutor(
			ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

		this.scheduledExecutor = createExecutor(this.poolSize, threadFactory, rejectedExecutionHandler);
		return this.scheduledExecutor;
	}

	/**
	 * Create a new {@link ScheduledExecutorService} instance.
	 * <p>The default implementation creates a {@link ScheduledThreadPoolExecutor}.
	 * Can be overridden in subclasses to provide custom {@link ScheduledExecutorService} instances.
	 * @param poolSize the specified pool size
	 * @param threadFactory the ThreadFactory to use
	 * @param rejectedExecutionHandler the RejectedExecutionHandler to use
	 * @return a new ScheduledExecutorService instance
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor
	 */
	protected ScheduledExecutorService createExecutor(
			int poolSize, ThreadFactory threadFactory, RejectedExecutionHandler rejectedExecutionHandler) {

		return new ScheduledThreadPoolExecutor(poolSize, threadFactory, rejectedExecutionHandler);
	}

	/**
	 * Return the underlying ScheduledExecutorService for native access.
	 * @return the underlying ScheduledExecutorService (never {@code null})
	 * @throws IllegalStateException if the ThreadPoolTaskScheduler hasn't been initialized yet
	 */
	public ScheduledExecutorService getScheduledExecutor() throws IllegalStateException {
		Asserts.validState(this.scheduledExecutor != null, "ThreadPoolTaskScheduler not initialized");
		return this.scheduledExecutor;
	}

	/**
	 * Return the underlying ScheduledThreadPoolExecutor, if available.
	 * @return the underlying ScheduledExecutorService (never {@code null})
	 * @throws IllegalStateException if the ThreadPoolTaskScheduler hasn't been initialized yet
	 * or if the underlying ScheduledExecutorService isn't a ScheduledThreadPoolExecutor
	 * @see #getScheduledExecutor()
	 */
	public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() throws IllegalStateException {
		Asserts.validState(this.scheduledExecutor instanceof ScheduledThreadPoolExecutor,
				"No ScheduledThreadPoolExecutor available");
		return (ScheduledThreadPoolExecutor) this.scheduledExecutor;
	}

	/**
	 * @return the current pool size.
	 * <p>Requires an underlying {@link ScheduledThreadPoolExecutor}.
	 * @see #getScheduledThreadPoolExecutor()
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#getPoolSize()
	 */
	public int getPoolSize() {
		if (this.scheduledExecutor == null) {
			// Not initialized yet: assume initial pool size.
			return this.poolSize;
		}
		return getScheduledThreadPoolExecutor().getPoolSize();
	}

	/**
	 * @return the number of currently active threads.
	 * <p>Requires an underlying {@link ScheduledThreadPoolExecutor}.
	 * @see #getScheduledThreadPoolExecutor()
	 * @see java.util.concurrent.ScheduledThreadPoolExecutor#getActiveCount()
	 */
	public int getActiveCount() {
		if (this.scheduledExecutor == null) {
			// Not initialized yet: assume no active threads.
			return 0;
		}
		return getScheduledThreadPoolExecutor().getActiveCount();
	}


	// SchedulingTaskExecutor implementation

	@Override
	public void execute(Runnable task) {
		Executor executor = getScheduledExecutor();
		executor.execute(errorHandlingTask(task, false));
	}

	@Override
	public void execute(Runnable task, long startTimeout) {
		execute(task);
	}

	@Override
	public Future<?> submit(Runnable task) {
		ExecutorService executor = getScheduledExecutor();
		return executor.submit(errorHandlingTask(task, false));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		ExecutorService executor = getScheduledExecutor();
		if (this.errorHandler != null) {
			task = new DelegatingErrorHandlingCallable<T>(task, this.errorHandler);
		}
		return executor.submit(task);
	}

	// TaskScheduler implementation

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Trigger trigger) {
		ScheduledExecutorService executor = getScheduledExecutor();
		ErrorHandler errorHandler =
				(this.errorHandler != null ? this.errorHandler : ErrorHandlers.getDefaultErrorHandler(true));
		return new ReschedulingRunnable(task, trigger, executor, errorHandler).schedule();
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, Date startTime) {
		ScheduledExecutorService executor = getScheduledExecutor();
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return executor.schedule(errorHandlingTask(task, false), initialDelay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> schedule(Runnable task, long delay) {
		ScheduledExecutorService executor = getScheduledExecutor();
		return executor.schedule(errorHandlingTask(task, false), delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period) {
		ScheduledExecutorService executor = getScheduledExecutor();
		return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, Date startTime, long period) {
		ScheduledExecutorService executor = getScheduledExecutor();
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return executor.scheduleAtFixedRate(errorHandlingTask(task, true), initialDelay, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long period) {
		ScheduledExecutorService executor = getScheduledExecutor();
		return executor.scheduleAtFixedRate(errorHandlingTask(task, true), 0, period, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long initialDelay, long delay) {
		ScheduledExecutorService executor = getScheduledExecutor();
		return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, Date startTime, long delay) {
		ScheduledExecutorService executor = getScheduledExecutor();
		long initialDelay = startTime.getTime() - System.currentTimeMillis();
		return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), initialDelay, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public ScheduledFuture<?> scheduleWithFixedDelay(Runnable task, long delay) {
		ScheduledExecutorService executor = getScheduledExecutor();
		return executor.scheduleWithFixedDelay(errorHandlingTask(task, true), 0, delay, TimeUnit.MILLISECONDS);
	}

	private Runnable errorHandlingTask(Runnable task, boolean isRepeatingTask) {
		return ErrorHandlers.decorateTaskWithErrorHandler(task, this.errorHandler, isRepeatingTask);
	}


	private static class DelegatingErrorHandlingCallable<V> implements Callable<V> {

		private final Callable<V> delegate;

		private final ErrorHandler errorHandler;

		DelegatingErrorHandlingCallable(Callable<V> delegate, ErrorHandler errorHandler) {
			this.delegate = delegate;
			this.errorHandler = errorHandler;
		}

		@Override
		public V call() throws Exception {
			try {
				return delegate.call();
			}
			catch (Throwable t) {
				this.errorHandler.handleError(t);
				return null;
			}
		}
	}

}