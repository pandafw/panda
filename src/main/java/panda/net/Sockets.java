package panda.net;

import java.net.ServerSocket;
import java.net.Socket;

public class Sockets {
	public static void safeClose(Socket s) {
		if (s != null) {
			try {
				s.close();
			}
			catch (Exception e) {
				//ingonre
			}
		}
	}

	public static void safeClose(ServerSocket s) {
		if (s != null) {
			try {
				s.close();
			}
			catch (Exception e) {
				//ingonre
			}
		}
	}
}
