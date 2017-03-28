package panda.lang;

/**
 * A strategy for handling errors. 
 */
public interface ErrorHandler {

	/**
	 * Handle the given error, possibly re-throwing it as a fatal exception.
	 * @param t the throwable object
	 */
	void handleError(Throwable t);

}
