package examples.ntp;

import java.io.IOException;
import java.net.InetAddress;

import panda.net.time.TimeTCPClient;
import panda.net.time.TimeUDPClient;

/***
 * This is an example program demonstrating how to use the TimeTCPClient and TimeUDPClient classes.
 * This program connects to the default time service port of a specified server, retrieves the time,
 * and prints it to standard output. See <A HREF="ftp://ftp.rfc-editor.org/in-notes/rfc868.txt"> the
 * spec </A> for details. The default is to use the TCP port. Use the -udp flag to use the UDP port.
 * <p>
 * Usage: TimeClient [-udp] <hostname>
 ***/
public final class TimeClient {

	public static final void timeTCP(String host) throws IOException {
		TimeTCPClient client = new TimeTCPClient();
		try {
			// We want to timeout if a response takes longer than 60 seconds
			client.setDefaultTimeout(60000);
			client.connect(host);
			System.out.println(client.getDate());
		}
		finally {
			client.disconnect();
		}
	}

	public static final void timeUDP(String host) throws IOException {
		TimeUDPClient client = new TimeUDPClient();

		// We want to timeout if a response takes longer than 60 seconds
		client.setDefaultTimeout(60000);
		client.open();
		System.out.println(client.getDate(InetAddress.getByName(host)));
		client.close();
	}

	public static void main(String[] args) {

		if (args.length == 1) {
			try {
				timeTCP(args[0]);
			}
			catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		else if (args.length == 2 && args[0].equals("-udp")) {
			try {
				timeUDP(args[1]);
			}
			catch (IOException e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		else {
			System.err.println("Usage: TimeClient [-udp] <hostname>");
			System.exit(1);
		}

	}

}
