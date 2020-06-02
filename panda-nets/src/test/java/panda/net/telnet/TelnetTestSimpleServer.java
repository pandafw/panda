package panda.net.telnet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * Simple TCP server. Waits for connections on a TCP port in a separate thread.
 ***/
public class TelnetTestSimpleServer implements Runnable {
	ServerSocket serverSocket = null;
	Socket clientSocket = null;
	Thread listener = null;

	/*
	 * test of client-driven subnegotiation. <p>
	 * @param port - server port on which to listen.
	 * @throws IOException on error
	 */
	public TelnetTestSimpleServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);

		listener = new Thread(this);

		listener.start();
	}

	// @Override
	public void run() {
		boolean bError = false;
		while (!bError) {
			try {
				clientSocket = serverSocket.accept();
				synchronized (clientSocket) {
					try {
						clientSocket.wait();
					}
					catch (Exception e) {
						System.err.println("Exception in wait, " + e.getMessage());
					}
					try {
						clientSocket.close();
					}
					catch (Exception e) {
						System.err.println("Exception in close, " + e.getMessage());
					}
				}
			}
			catch (IOException e) {
				bError = true;
			}
		}

		try {
			serverSocket.close();
		}
		catch (Exception e) {
			System.err.println("Exception in close, " + e.getMessage());
		}
	}

	public void disconnect() {
		if (clientSocket == null) {
			return;
		}
		synchronized (clientSocket) {
			try {
				clientSocket.notify();
			}
			catch (Exception e) {
				System.err.println("Exception in notify, " + e.getMessage());
			}
		}
	}

	public void stop() {
		listener.interrupt();
		try {
			serverSocket.close();
		}
		catch (Exception e) {
			System.err.println("Exception in close, " + e.getMessage());
		}
	}

	public InputStream getInputStream() throws IOException {
		if (clientSocket != null) {
			return (clientSocket.getInputStream());
		}
		else {
			return (null);
		}
	}

	public OutputStream getOutputStream() throws IOException {
		if (clientSocket != null) {
			return (clientSocket.getOutputStream());
		}
		else {
			return (null);
		}
	}
}
