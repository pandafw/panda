package panda.task;

import java.util.concurrent.Executor;

/**
 * Simple task executor interface that abstracts the execution
 * of a {@link Runnable}.
 *
 * <p>Implementations can use all sorts of different execution strategies,
 * such as: synchronous, asynchronous, using a thread pool, and more.
 *
 * <p>Equivalent to JDK 1.5's {@link java.util.concurrent.Executor}
 * interface
 *
 * @see java.util.concurrent.Executor
 */
public interface TaskExecutor extends Executor {

	/**
	 * Execute the given {@code task}.
	 * <p>The call might return immediately if the implementation uses
	 * an asynchronous execution strategy, or might block in the case
	 * of synchronous execution.
	 * @param task the {@code Runnable} to execute (never {@code null})
	 */
	@Override
	void execute(Runnable task);

}