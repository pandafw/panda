package panda.net.ftp;

/**
 * This interface adds the aspect of configurability by means of a supplied FTPClientConfig object
 * to other classes in the system, especially listing parsers.
 */
public interface Configurable {

	/**
	 * @param config the object containing the configuration data
	 * @throws IllegalArgumentException if the elements of the <code>config</code> are somehow
	 *             inadequate to configure the Configurable object.
	 */
	public void configure(FTPClientConfig config);
}
