package panda.tool.net;

import java.net.InetSocketAddress;

import org.apache.commons.cli.CommandLine;

import panda.lang.Numbers;
import panda.tool.socket.SocketForward;
import panda.util.tool.AbstractCommandTool;

/**
 */
public class Forward {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	public static class Main extends AbstractCommandTool {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main main = new Main();
			
			Object cg = new Forward();

			main.execute(cg, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("lh", "localhost", "Local Host", false);
			addCommandLineOption("lp", "localport", "Local Port", false);
			addCommandLineOption("rh", "remotehost", "Remote Host", true);
			addCommandLineOption("rp", "remoteport", "Remote Port", false);
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("lh")) {
				setParameter("localhost", cl.getOptionValue("lh").trim());
			}
			if (cl.hasOption("lp")) {
				setParameter("localport", Numbers.toInt(cl.getOptionValue("lp").trim()));
			}
			if (cl.hasOption("rh")) {
				setParameter("remotehost", cl.getOptionValue("rh").trim());
			}
			if (cl.hasOption("rp")) {
				setParameter("remoteport", Numbers.toInt(cl.getOptionValue("rp").trim()));
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public Forward() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String localhost = "0.0.0.0";
	protected int localport;
	protected String remotehost;
	protected int remoteport = 80;


	/**
	 * @return the localhost
	 */
	public String getLocalhost() {
		return localhost;
	}


	/**
	 * @param localhost the localhost to set
	 */
	public void setLocalhost(String localhost) {
		this.localhost = localhost;
	}


	/**
	 * @return the localport
	 */
	public int getLocalport() {
		return localport;
	}


	/**
	 * @param localport the localport to set
	 */
	public void setLocalport(int localport) {
		this.localport = localport;
	}


	/**
	 * @return the remotehost
	 */
	public String getRemotehost() {
		return remotehost;
	}


	/**
	 * @param remotehost the remotehost to set
	 */
	public void setRemotehost(String remotehost) {
		this.remotehost = remotehost;
	}


	/**
	 * @return the remoteport
	 */
	public int getRemoteport() {
		return remoteport;
	}


	/**
	 * @param remoteport the remoteport to set
	 */
	public void setRemoteport(int remoteport) {
		this.remoteport = remoteport;
	}


	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		if (localport == 0) {
			localport = remoteport;
		}
		
		SocketForward sf = new SocketForward(new InetSocketAddress(localhost, localport), new InetSocketAddress(remotehost, remoteport));
		sf.run();
	}
}
