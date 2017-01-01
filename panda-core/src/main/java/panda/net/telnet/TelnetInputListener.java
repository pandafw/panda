package panda.net.telnet;

/***
 * Listener interface used for notification that incoming data is available to be read.
 * 
 * @see TelnetClient
 ***/
public interface TelnetInputListener {

	/***
	 * Callback method invoked when new incoming data is available on a {@link TelnetClient}'s
	 * {@link TelnetClient#getInputStream input stream}.
	 * 
	 * @see TelnetClient#registerInputListener
	 ***/
	public void telnetInputAvailable();
}
