package panda.task;

import panda.lang.Asserts;
import panda.lang.ErrorHandler;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * Runnable wrapper that catches any exception or error thrown from its
 * delegate Runnable and allows an {@link ErrorHandler} to handle it.
 *
 */
public class DelegatingErrorHandlingRunnable implements Runnable {

	private final Runnable delegate;

	private final ErrorHandler errorHandler;


	/**
	 * Create a new DelegatingErrorHandlingRunnable.
	 * @param delegate the Runnable implementation to delegate to
	 * @param errorHandler the ErrorHandler for handling any exceptions
	 */
	public DelegatingErrorHandlingRunnable(Runnable delegate, ErrorHandler errorHandler) {
		Asserts.notNull(delegate, "Delegate must not be null");
		Asserts.notNull(errorHandler, "ErrorHandler must not be null");
		this.delegate = delegate;
		this.errorHandler = errorHandler;
	}

	@Override
	public void run() {
		try {
			this.delegate.run();
		}
		catch (UndeclaredThrowableException ex) {
			this.errorHandler.handleError(ex.getUndeclaredThrowable());
		}
		catch (Throwable ex) {
			this.errorHandler.handleError(ex);
		}
	}

	@Override
	public String toString() {
		return "DelegatingErrorHandlingRunnable for " + this.delegate;
	}

}