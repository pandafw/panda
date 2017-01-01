package panda.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import panda.io.Streams;
import panda.io.stream.ByteArrayOutputStream;
import panda.lang.Arrays;
import panda.lang.Strings;
import panda.lang.Threads;
import panda.log.Log;
import panda.log.Logs;

public class SocketRelay implements Runnable {
	private final static Log log = Logs.getLog(SocketRelay.class);

	protected int bufferSize = 1024 * 16;
	protected int timeout = 5000;
	protected InetSocketAddress address;
	protected boolean relaying = true;

	public SocketRelay() {
		super();
	}

	/**
	 * @param address
	 */
	public SocketRelay(InetSocketAddress address) {
		super();
		this.address = address;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the address
	 */
	public InetSocketAddress getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void stop() {
		relaying = false;
	}
	
	Map<String, Relayer> relays = new HashMap<String, Relayer>();
	protected synchronized void addClient(String id, Socket client) {
		Relayer r = relays.get(id);
		if (r == null) {
			r = new Relayer();
			relays.put(id, r);
		}
		r.addClient(client);
	}

	public void run() {
		ServerSocket listener = null;
		try {
			log.debug("Listening on " + address);
			listener = new ServerSocket();
			listener.bind(address, 50);
			while (relaying) {
				Connector c = new Connector(listener.accept());
				c.start();
			}
		}
		catch (IOException e) {
			log.error(e);
		}
		finally {
			for (Relayer r : relays.values()) {
				r.quit();
			}
			for (Relayer r : relays.values()) {
				if (!r.isAlive()) {
					Threads.safeJoin(r);
				}
			}
			Sockets.safeClose(listener);
		}
	}

	protected class Connector extends Thread {
		protected Socket client;
		
		public Connector(Socket client) {
			this.client = client;
		}
		
		public void run() {
			try {
				log.debug("Accept client " + client);
				InputStream is = client.getInputStream();
				OutputStream os = client.getOutputStream();
				
				@SuppressWarnings("resource")
				ByteArrayOutputStream buf = new ByteArrayOutputStream();

				long start = System.currentTimeMillis();
				while (start + timeout > System.currentTimeMillis()) {
					int a = is.available();
					if (a > 0) {
						buf.write(is, a);
						byte[] bs = buf.toByteArray();
						if (log.isTraceEnabled()) {
							log.trace(client + "> " + new String(bs));
						}
						int i = Arrays.indexOf(bs, (byte)0x0a);
						if (i > 0) {
							String s = new String(bs, 0, i);
							if (s.startsWith("HELO ")) {
								String k = Strings.strip(s.substring(5));
								if (Strings.isEmpty(k)) {
									os.write(("ERROR :Invalid connector id <" + k + ">\n").getBytes());
									os.flush();
									Sockets.safeClose(client);
								}
								else {
									addClient(k, client);
									return;
								}
							}
							else {
								os.write(("ERROR :Invalid connector command <" + s + ">\n").getBytes());
								os.flush();
								Sockets.safeClose(client);
							}
							return;
						}
					}
				}
				os.write(("ERROR :Timeout for connect\n").getBytes());
				os.flush();
				Sockets.safeClose(client);
			}
			catch (Exception e) {
				log.warn("Socket error: " + client, e);
				Sockets.safeClose(client);
			}
		}
	}
	
	protected class Relayer extends Thread {
		protected List<Socket> clients = new ArrayList<Socket>();
		protected ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		protected boolean relaying;

		public Relayer() {
		}

		public void addClient(Socket client) {
			log.debug("Add client " + client);
			synchronized (clients) {
				clients.add(client);
				if (clients.size() > 1 && !this.isAlive()) {
					start();
				}
			}
		}

		private void removeClient(Socket client) {
			synchronized (clients) {
				log.debug("Close client " + client);
				Sockets.safeClose(client);
				clients.remove(client);
			}
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
				relaying = true;
				
				while (relaying) {
					synchronized (clients) {
						if (clients.size() < 2) {
							Threads.safeSleep(10);
							continue;
						}
					}
					relay();
				}
			}
			catch (Throwable e) {
				log.error(e);
			}
			finally {
				for (Socket s : clients) {
					log.debug("Close " + s);
					Sockets.safeClose(s);
				}
			}
		}
		
		private void relay() {
			synchronized (clients) {
				for (int i = 0; i < clients.size(); i++) {
					relay(clients.get(i));
				}
			}
		}
		
		private void relay(Socket s) {
			buffer.reset();
			try {
				InputStream is = s.getInputStream();
				int a = is.available();
				if (a < 1) {
					return;
				}

				if (buffer.write(is, a) < 1) {
					return;
				}
			}
			catch (IOException e) {
				log.debug("Failed to read data from " + s, e);
				removeClient(s);
				return;
			}

			for (int i = clients.size() - 1; i >= 0; i--) {
				Socket c = clients.get(i);
				if (c == s) {
					continue;
				}
				
				try {
					InputStream bis = buffer.toInputStream();
					if (log.isDebugEnabled()) {
						log.debug("Relay [" + bis.available() + "] " + s + " -> " + c);
					}

					OutputStream os = c.getOutputStream();
					Streams.copy(bis, os);
					os.flush();
				}
				catch (IOException e) {
					log.debug("Failed to send data to " + s, e);
					removeClient(c);
				}
			}
		}
	}
}
