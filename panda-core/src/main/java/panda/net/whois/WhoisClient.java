package panda.net.whois;

import java.io.IOException;
import java.io.InputStream;

import panda.net.finger.FingerClient;

/***
 * The WhoisClient class implements the client side of the Internet Whois Protocol defined in RFC
 * 954. To query a host you create a WhoisClient instance, connect to the host, query the host, and
 * finally disconnect from the host. If the whois service you want to query is on a non-standard
 * port, connect to the host at that port. Here's a sample use:
 * 
 * <pre>
 * WhoisClient whois;
 * 
 * whois = new WhoisClient();
 * 
 * try {
 * 	whois.connect(WhoisClient.DEFAULT_HOST);
 * 	System.out.println(whois.query(&quot;foobar&quot;));
 * 	whois.disconnect();
 * }
 * catch (IOException e) {
 * 	System.err.println(&quot;Error I/O exception: &quot; + e.getMessage());
 * 	return;
 * }
 * </pre>
 ***/

public final class WhoisClient extends FingerClient {
	/***
	 * The default whois host to query. It is set to whois.internic.net.
	 ***/
	public static final String DEFAULT_HOST = "whois.internic.net";

	/***
	 * The default whois port. It is set to 43 according to RFC 954.
	 ***/
	public static final int DEFAULT_PORT = 43;

	/***
	 * The default whois constructor. Initializes the default port to <code> DEFAULT_PORT </code>.
	 ***/
	public WhoisClient() {
		setDefaultPort(DEFAULT_PORT);
	}

	/***
	 * Queries the connected whois server for information regarding the given handle. It is up to
	 * the programmer to be familiar with the handle syntax of the whois server. You must first
	 * connect to a whois server before calling this method, and you should disconnect afterward.
	 * 
	 * @param handle The handle to lookup.
	 * @return The result of the whois query.
	 * @exception IOException If an I/O error occurs during the operation.
	 ***/
	public String query(String handle) throws IOException {
		return query(false, handle);
	}

	/***
	 * Queries the connected whois server for information regarding the given handle and returns the
	 * InputStream of the network connection. It is up to the programmer to be familiar with the
	 * handle syntax of the whois server. You must first connect to a finger server before calling
	 * this method, and you should disconnect after finishing reading the stream.
	 * 
	 * @param handle The handle to lookup.
	 * @return The InputStream of the network connection of the whois query. Can be read to obtain
	 *         whois results.
	 * @exception IOException If an I/O error occurs during the operation.
	 ***/
	public InputStream getInputStream(String handle) throws IOException {
		return getInputStream(false, handle);
	}

}
