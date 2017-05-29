package panda.tool.net;

import java.net.InetSocketAddress;

import panda.args.Option;
import panda.tool.AbstractCommandTool;
import panda.tool.socket.SocketRelay;

/**
 */
public class Relay extends AbstractCommandTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new Relay().execute(args);
	}

	/**
	 * Constructor
	 */
	public Relay() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String host = "0.0.0.0";
	protected int port = 8888;

	/**
	 * @return the host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @param host the host to set
	 */
	@Option(opt='h', option="host", arg="HOST", usage="Listen Host (default: 0.0.0.0)")
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	@Option(opt='p', option="port", arg="PORT", usage="Listen Port (default: 8888)")
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * execute
	 */
	public void execute() {
		SocketRelay sr = new SocketRelay(new InetSocketAddress(host, port));
		sr.run();
	}
}
