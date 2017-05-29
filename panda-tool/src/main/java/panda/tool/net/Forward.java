package panda.tool.net;

import java.net.InetSocketAddress;

import panda.args.Option;
import panda.tool.AbstractCommandTool;
import panda.tool.socket.SocketForward;

/**
 */
public class Forward extends AbstractCommandTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new Forward().execute(args);
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
	@Option(opt='h', option="localhost", arg="HOST", usage="Local Host")
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
	@Option(opt='p', option="localport", arg="PORT", usage="Local Port")
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
	@Option(opt='H', option="remotehost", required=true, arg="HOST", usage="Remote Host")
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
	@Option(opt='P', option="remoteport", arg="PORT", usage="Remote Port")
	public void setRemoteport(int remoteport) {
		this.remoteport = remoteport;
	}


	/**
	 * execute
	 */
	public void execute() {
		if (localport == 0) {
			localport = remoteport;
		}
		
		SocketForward sf = new SocketForward(new InetSocketAddress(localhost, localport), new InetSocketAddress(remotehost, remoteport));
		sf.run();
	}
}
