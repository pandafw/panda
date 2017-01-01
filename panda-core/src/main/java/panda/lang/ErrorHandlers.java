package panda.lang;

import panda.log.Log;
import panda.log.Logs;
import panda.task.DelegatingErrorHandlingRunnable;

import java.util.concurrent.Future;

/**
 * Utility methods for decorating tasks with error handling.
 *
 * <p><b>NOTE:</b> This class is intended for internal use by scheduler
 * implementations. It is only public so that it may be accessed from impl classes
 * within other packages. It is <i>not</i> intended for general use.
 *
 */
public abstract class ErrorHandlers {

	/**
	 * An ErrorHandler strategy that will log the Exception but perform
	 * no further handling. This will suppress the error so that
	 * subsequent executions of the task will not be prevented.
	 */
	public static final ErrorHandler LOG_AND_SUPPRESS_ERROR_HANDLER = new LoggingErrorHandler();

	/**
	 * An ErrorHandler strategy that will log at error level and then
	 * re-throw the Exception. Note: this will typically prevent subsequent
	 * execution of a scheduled task.
	 */
	public static final ErrorHandler LOG_AND_PROPAGATE_ERROR_HANDLER = new PropagatingErrorHandler();


	/**
	 * Decorate the task for error handling. If the provided {@link ErrorHandler}
	 * is not {@code null}, it will be used. Otherwise, repeating tasks will have
	 * errors suppressed by default whereas one-shot tasks will have errors
	 * propagated by default since those errors may be expected through the
	 * returned {@link Future}. In both cases, the errors will be logged.
	 */
	public static DelegatingErrorHandlingRunnable decorateTaskWithErrorHandler(
			Runnable task, ErrorHandler errorHandler, boolean isRepeatingTask) {

		if (task instanceof DelegatingErrorHandlingRunnable) {
			return (DelegatingErrorHandlingRunnable) task;
		}
		ErrorHandler eh = (errorHandler != null ? errorHandler : getDefaultErrorHandler(isRepeatingTask));
		return new DelegatingErrorHandlingRunnable(task, eh);
	}

	/**
	 * Return the default {@link ErrorHandler} implementation based on the boolean
	 * value indicating whether the task will be repeating or not. For repeating tasks
	 * it will suppress errors, but for one-time tasks it will propagate. In both
	 * cases, the error will be logged.
	 */
	public static ErrorHandler getDefaultErrorHandler(boolean isRepeatingTask) {
		return (isRepeatingTask ? LOG_AND_SUPPRESS_ERROR_HANDLER : LOG_AND_PROPAGATE_ERROR_HANDLER);
	}


	/**
	 * An {@link ErrorHandler} implementation that logs the Throwable at error
 	 * level. It does not perform any additional error handling. This can be
 	 * useful when suppression of errors is the intended behavior.
	 */
	private static class LoggingErrorHandler implements ErrorHandler {

		private final Log logger = Logs.getLog(LoggingErrorHandler.class);

		@Override
		public void handleError(Throwable t) {
			if (logger.isErrorEnabled()) {
				logger.error("Unexpected error occurred in scheduled task.", t);
			}
		}
	}


	/**
	 * An {@link ErrorHandler} implementation that logs the Throwable at error
	 * level and then propagates it.
	 */
	private static class PropagatingErrorHandler extends LoggingErrorHandler {

		@Override
		public void handleError(Throwable t) {
			super.handleError(t);
			Exceptions.rethrowRuntime(t);
		}
	}

}