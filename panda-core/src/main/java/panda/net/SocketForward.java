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

import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Strings;
import panda.lang.Threads;
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
	 * @param remoteAddr the remote address
	 */
	public SocketForward(InetSocketAddress remoteAddr) {
		this(new InetSocketAddress((InetAddress)null, remoteAddr.getPort()), remoteAddr);
	}

	/**
	 * @param localAddr the local address
	 * @param remoteAddr the remote address
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
	
	protected Forward createRelay(Socket socket) {
		return new Forward(socket);
	}
	
	public void run() {
		List<Forward> relays = new ArrayList<Forward>();
		ServerSocket listener = null;
		try {
			log.debug("Listening on " + localAddr);
			log.debug("Forwarding to " + remoteAddr);

			listener = new ServerSocket();
			listener.bind(localAddr, 50);
			while (relaying) {
				Forward r = createRelay(listener.accept());
				relays.add(r);
				r.start();
			}
		}
		catch (IOException e) {
			log.error(e);
		}
		finally {
			for (Forward r : relays) {
				r.quit();
			}

			for (Forward r : relays) {
				if (!r.isAlive()) {
					Threads.safeJoin(r);
				}
			}
			Sockets.safeClose(listener);
		}
	}

	protected class Forward extends Thread {
		protected Socket client;
		protected Socket server;
		protected boolean relaying;

		public Forward(Socket client) {
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
					log.debug("Forward " + client + " <-> " + remoteAddr);
				}

//				log.debug("Bind to " + localAddr.getHostName() + ":" + client.getPort());
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
					forward(true, cis, sos, buffer);
					forward(false, sis, cos, buffer);
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
		
		protected int forward(boolean toup, InputStream is, OutputStream os, byte[] buf) throws IOException {
			int a = is.available();
			if (a <= 0) {
				return a;
			}

			int t = 0;
			if (log.isTraceEnabled()) {
				log.trace(client + (toup ? " -> " : " <- ") + server);
				int n = 0;
				while (t < a) {
					n = is.read(buf);
					os.write(buf, 0, n);
					t += n;
					if (log.isTraceEnabled()) {
						log.trace(Strings.newString(buf, 0, n, Charsets.UTF_8));
					}
				}
			}
			else {
				t = (int)Streams.copyLarge(is, os, 0, a, buf);
			}

			os.flush();
			if (log.isDebugEnabled()) {
				log.debug(client + (toup ? " -> " : " <- ") + server + " [" + t + "]");
			}
			return t;
		}
	}
}
