package panda.net.bsd;

import java.io.IOException;

/***
 * RLoginClient is very similar to {@link panda.net.bsd.RCommandClient}, from which it is derived,
 * and uses the rcmd() facility implemented in RCommandClient to implement the functionality of the
 * rlogin command that first appeared in 4.2BSD Unix. rlogin is a command used to login to a remote
 * machine from a trusted host, sometimes without issuing a password. The trust relationship is the
 * same as described in the documentation for {@link panda.net.bsd.RCommandClient}.
 * <p>
 * As with virtually all of the client classes in panda.net, this class derives from
 * SocketClient. But it relies on the connection methods defined in RcommandClient which ensure that
 * the local Socket will originate from an acceptable rshell port. The way to use RLoginClient is to
 * first connect to the server, call the {@link #rlogin rlogin() } method, and then fetch the
 * connection's input and output streams. Interaction with the remote command is controlled entirely
 * through the I/O streams. Once you have finished processing the streams, you should invoke
 * {@link panda.net.bsd.RExecClient#disconnect disconnect() } to clean up properly.
 * <p>
 * The standard output and standard error streams of the remote process are transmitted over the
 * same connection, readable from the input stream returned by
 * {@link panda.net.bsd.RExecClient#getInputStream getInputStream() }
 * <p>
 * Unlike RExecClient and RCommandClient, it is not possible to tell the rlogind daemon to return
 * the standard error stream over a separate connection.
 * {@link panda.net.bsd.RExecClient#getErrorStream getErrorStream() } will always return null. The
 * standard input of the remote process can be written to through the output stream returned by
 * {@link panda.net.bsd.RExecClient#getOutputStream getOutputSream() }
 * 
 * @see panda.net.SocketClient
 * @see RExecClient
 * @see RCommandClient
 ***/

public class RLoginClient extends RCommandClient {
	/***
	 * The default rlogin port. Set to 513 in BSD Unix and according to RFC 1282.
	 ***/
	public static final int DEFAULT_PORT = 513;

	/***
	 * The default RLoginClient constructor. Initializes the default port to
	 * <code> DEFAULT_PORT </code>.
	 ***/
	public RLoginClient() {
		setDefaultPort(DEFAULT_PORT);
	}

	/***
	 * Logins into a remote machine through the rlogind daemon on the server to which the
	 * RLoginClient is connected. After calling this method, you may interact with the remote login
	 * shell through its standard input and output streams. Standard error is sent over the same
	 * stream as standard output. You will typically be able to detect the termination of the remote
	 * login shell after reaching end of file on its standard output (accessible through
	 * {@link #getInputStream getInputStream() }. Disconnecting from the server or closing the
	 * process streams before reaching end of file will terminate the remote login shell in most
	 * cases.
	 * <p>
	 * If user authentication fails, the rlogind daemon will request that a password be entered
	 * interactively. You will be able to read the prompt from the output stream of the RLoginClient
	 * and write the password to the input stream of the RLoginClient.
	 * 
	 * @param localUsername The user account on the local machine that is trying to login to the
	 *            remote host.
	 * @param remoteUsername The account name on the server that is being logged in to.
	 * @param terminalType The name of the user's terminal (e.g., "vt100", "network", etc.)
	 * @param terminalSpeed The speed of the user's terminal, expressed as a baud rate or bps (e.g.,
	 *            9600 or 38400)
	 * @exception IOException If the rlogin() attempt fails. The exception will contain a message
	 *                indicating the nature of the failure.
	 ***/
	public void rlogin(String localUsername, String remoteUsername, String terminalType, int terminalSpeed)
			throws IOException {
		rexec(localUsername, remoteUsername, terminalType + "/" + terminalSpeed, false);
	}

	/***
	 * Same as the other rlogin method, but no terminal speed is defined.
	 * 
	 * @param localUsername the local user
	 * @param remoteUsername the remote user
	 * @param terminalType the terminal type
	 * @throws IOException on error
	 ***/
	public void rlogin(String localUsername, String remoteUsername, String terminalType) throws IOException {
		rexec(localUsername, remoteUsername, terminalType, false);
	}

}
