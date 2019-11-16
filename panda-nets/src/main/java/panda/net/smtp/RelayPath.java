package panda.net.smtp;

import java.util.Enumeration;
import java.util.Vector;

/***
 * A class used to represent forward and reverse relay paths. The SMTP MAIL command requires a
 * reverse relay path while the SMTP RCPT command requires a forward relay path. See RFC 821 for
 * more details. In general, you will not have to deal with relay paths.
 * 
 * @see SMTPClient
 ***/

public final class RelayPath {
	Vector<String> _path;
	String _emailAddress;

	/***
	 * Create a relay path with the specified email address as the ultimate destination.
	 * <p>
	 * 
	 * @param emailAddress The destination email address.
	 ***/
	public RelayPath(String emailAddress) {
		_path = new Vector<String>();
		_emailAddress = emailAddress;
	}

	/***
	 * Add a mail relay host to the relay path. Hosts are added left to right. For example, the
	 * following will create the path
	 * <code><b> &lt; @bar.com,@foo.com:foobar@foo.com &gt; </b></code>
	 * 
	 * <pre>
	 * path = new RelayPath(&quot;foobar@foo.com&quot;);
	 * path.addRelay(&quot;bar.com&quot;);
	 * path.addRelay(&quot;foo.com&quot;);
	 * </pre>
	 * <p>
	 * 
	 * @param hostname The host to add to the relay path.
	 ***/
	public void addRelay(String hostname) {
		_path.addElement(hostname);
	}

	/***
	 * Return the properly formatted string representation of the relay path.
	 * <p>
	 * 
	 * @return The properly formatted string representation of the relay path.
	 ***/
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		Enumeration<String> hosts;

		buffer.append('<');

		hosts = _path.elements();

		if (hosts.hasMoreElements()) {
			buffer.append('@');
			buffer.append(hosts.nextElement());

			while (hosts.hasMoreElements()) {
				buffer.append(",@");
				buffer.append(hosts.nextElement());
			}
			buffer.append(':');
		}

		buffer.append(_emailAddress);
		buffer.append('>');

		return buffer.toString();
	}

}
