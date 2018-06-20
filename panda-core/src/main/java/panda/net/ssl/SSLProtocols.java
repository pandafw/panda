package panda.net.ssl;

public class SSLProtocols {
	public static final String SSLv2 = "SSLv2";
	
	public static final String SSLv3 = "SSLv3";
	
	public static final String TLSv1 = "TLSv1";
	
	public static final String TLSv1_1 = "TLSv1.1";
	
	public static final String TLSv1_2 = "TLSv1.2";

	public static final String[] TLS_ONLY = new String[] { TLSv1, TLSv1_1, TLSv1_2 };
}
