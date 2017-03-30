package panda.tool.net;

import java.net.InetSocketAddress;

import org.apache.commons.cli.CommandLine;

import panda.lang.Numbers;
import panda.tool.socket.SocketRelay;
import panda.util.tool.AbstractCommandTool;

/**
 */
public class Relay {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	public static class Main extends AbstractCommandTool {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main main = new Main();
			
			Object cg = new Relay();

			main.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("h", "host", "Listen Host (default: 0.0.0.0)", false);
			addCommandLineOption("p", "port", "Listen Port (default: 8888)", false);
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("h")) {
				setParameter("host", cl.getOptionValue("h").trim());
			}
			if (cl.hasOption("p")) {
				setParameter("port", Numbers.toInt(cl.getOptionValue("p").trim()));
			}
		}
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
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		SocketRelay sr = new SocketRelay(new InetSocketAddress(host, port));
		sr.run();
	}
}
