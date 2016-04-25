package panda.net.tftp;

/***
 * A class used to signify the occurrence of an error in the creation of a TFTP packet. It is not
 * declared final so that it may be subclassed to identify more specific errors. You would only want
 * to do this if you were building your own TFTP client or server on top of the
 * {@link panda.net.tftp.TFTP} class if you wanted more functionality than the
 * {@link panda.net.tftp.TFTPClient#receiveFile receiveFile()} and
 * {@link panda.net.tftp.TFTPClient#sendFile sendFile()} methods provide.
 * 
 * @see TFTPPacket
 * @see TFTP
 ***/

public class TFTPPacketException extends Exception {

	private static final long serialVersionUID = -8114699256840851439L;

	/***
	 * Simply calls the corresponding constructor of its superclass.
	 ***/
	public TFTPPacketException() {
		super();
	}

	/***
	 * Simply calls the corresponding constructor of its superclass.
	 * 
	 * @param message the message
	 ***/
	public TFTPPacketException(String message) {
		super(message);
	}
}
