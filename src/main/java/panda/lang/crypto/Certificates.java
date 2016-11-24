package panda.lang.crypto;

import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;

public class Certificates {
	public final static String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
	public final static String END_CERT = "-----END CERTIFICATE-----";
	public final static String BEGIN_X509_CRL = "-----BEGIN X509 CRL-----";
	public final static String END_X509_CRL = "-----END X509 CRL-----";

	public static void toPem(Certificate cert, Appendable out) throws CertificateEncodingException, IOException {
		out.append(BEGIN_CERT);
		out.append(Strings.CRLF);
		out.append(Base64.encodeBase64ChunkedString(cert.getEncoded()));
		out.append(END_CERT);
		out.append(Strings.CRLF);
	}

	public static String toPem(Certificate cert) throws CertificateEncodingException {
		StringBuilder out = new StringBuilder();
		try {
			toPem(cert, out);
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
		return out.toString();
	}

}
