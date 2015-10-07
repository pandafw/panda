package panda.net;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import panda.lang.Charsets;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;

public class SocketForward implements Runnable {
	private final static Log log = Logs.getLog(SocketForward.class);

	protected int bufferSize = 10240;
	protected int timeout;
	protected InetSocketAddress localAddr;
	protected InetSocketAddress remoteAddr;
	protected boolean relaying = true;

	/**
	 * 
	 */
	public SocketForward() {
		super();
	}

	/**
	 * @param remoteAddr
	 */
	public SocketForward(InetSocketAddress remoteAddr) {
		this(new InetSocketAddress((InetAddress)null, remoteAddr.getPort()), remoteAddr);
	}

	/**
	 * @param localAddr
	 * @param remoteAddr
	 */
	public SocketForward(InetSocketAddress localAddr, InetSocketAddress remoteAddr) {
		super();
		this.localAddr = localAddr;
		this.remoteAddr = remoteAddr;
	}

	/**
	 * @return the localAddr
	 */
	public InetSocketAddress getLocalAddr() {
		return localAddr;
	}

	/**
	 * @param localAddr the localAddr to set
	 */
	public void setLocalAddr(InetSocketAddress localAddr) {
		this.localAddr = localAddr;
	}

	/**
	 * @return the remoteAddr
	 */
	public InetSocketAddress getRemoteAddr() {
		return remoteAddr;
	}

	/**
	 * @param remoteAddr the remoteAddr to set
	 */
	public void setRemoteAddr(InetSocketAddress remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public void stop() {
		relaying = false;
	}
	
	protected Relay createRelay(Socket socket) {
		return new Relay(socket);
	}
	
	public void run() {
		List<Relay> relays = new ArrayList<Relay>();
		ServerSocket listener = null;
		try {
			log.debug("Listening on " + localAddr);
			listener = new ServerSocket();
			listener.bind(localAddr, 50);
			while (relaying) {
				Relay r = createRelay(listener.accept());
				relays.add(r);
				r.start();
			}
		}
		catch (IOException e) {
			log.error(e);
		}
		finally {
			for (Relay r : relays) {
				r.quit();
			}
			while (!relays.isEmpty()) {
				for (Relay r : relays) {
					if (!r.isAlive()) {
						relays.remove(r);
						break;
					}
				}
			}
			Sockets.safeClose(listener);
		}
	}

	protected class Relay extends Thread {
		protected Socket client;
		protected Socket server;
		protected boolean relaying;

		public Relay(Socket client) {
			this.client = client;
		}

		public void quit() {
			this.relaying = false;
		}
		
		/**
		 * Services this thread's client by first sending the client a welcome message then
		 * repeatedly reading strings and sending back the capitalized version of the string.
		 */
		public void run() {
			try {
				server = new Socket();
				
				if (log.isDebugEnabled()) {
					log.debug("Relay " + client + " <-> " + remoteAddr);
//					log.debug("Bind to " + localAddr.getHostName() + ":" + client.getPort());
				}

//				server.bind(new InetSocketAddress(localAddr.getAddress(), client.getPort()));

				log.debug("Connect to " + remoteAddr);
				server.connect(remoteAddr, timeout);

				relaying = true;
				byte[] buffer = new byte[bufferSize];
				
				InputStream cis = client.getInputStream();
				OutputStream cos = client.getOutputStream();
				InputStream sis = server.getInputStream();
				OutputStream sos = server.getOutputStream();
				
				while (relaying) {
					relay(true, cis, sos, buffer);
					relay(false, sis, cos, buffer);
				}
			}
			catch (EOFException e) {
				log.debug(e.getMessage());
			}
			catch (IOException e) {
				log.warn(e);
			}
			catch (Throwable e) {
				log.error(e);
			}
			finally {
				log.debug("Close " + client);
				Sockets.safeClose(client);

				log.debug("Close " + server);
				Sockets.safeClose(server);
			}
		}
		
		protected int relay(boolean toup, InputStream is, OutputStream os, byte[] buf) throws IOException {
			int t = 0;
			int n = 0;
			int a = is.available();
			if (a > 0) {
				if (log.isTraceEnabled()) {
					log.trace(client + (toup ? " -> " : " <- ") + server);
				}
				while (t < a) {
					n = is.read(buf);
					os.write(buf, 0, n);
					t += n;
					if (log.isTraceEnabled()) {
						log.trace(Strings.newString(buf, 0, n, Charsets.UTF_8));
					}
				}
				os.flush();
				if (log.isDebugEnabled()) {
					log.debug(client + (toup ? " -> " : " <- ") + server + " [" + t + "]");
				}
			}
			return t;
		}
	}
}
