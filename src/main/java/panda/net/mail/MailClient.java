package panda.net.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import panda.io.FileNames;
import panda.io.MimeType;
import panda.io.Streams;
import panda.io.stream.MultiWriter;
import panda.io.stream.StringBuilderWriter;
import panda.io.stream.WriterOutputStream;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.codec.binary.Base64;
import panda.lang.codec.binary.Base64OutputStream;
import panda.lang.reflect.Methods;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.Logs;
import panda.net.Mimes;
import panda.net.PrintCommandListener;
import panda.net.smtp.AuthenticatingSMTPClient;
import panda.net.smtp.SMTPClient;
import panda.net.smtp.SMTPHeader;
import panda.net.smtp.SMTPReply;
import panda.net.ssl.TrustManagers;
import panda.vfs.FileItem;

/**
 * a class for send mail
 */
public class MailClient {
	private static Log log = Logs.getLog(MailClient.class);

	private String helo = "localhost";
	private String host;
	private int port = SMTPClient.DEFAULT_PORT;
	private boolean ssl;
	private String username;
	private String password;
	
	private int connectTimeout = 5000;
	private int defaultTimeout = 10000;

	/**
	 * @return the helo
	 */
	public String getHelo() {
		return helo;
	}

	/**
	 * @param helo the helo to set
	 */
	public void setHelo(String helo) {
		this.helo = helo;
	}

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
	 * @return the ssl
	 */
	public boolean isSsl() {
		return ssl;
	}

	/**
	 * @param ssl the ssl to set
	 */
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the connectTimeout
	 */
	public int getConnectTimeout() {
		return connectTimeout;
	}

	/**
	 * @param connectTimeout the connectTimeout to set
	 */
	public void setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * @return the defaultTimeout
	 */
	public int getDefaultTimeout() {
		return defaultTimeout;
	}

	/**
	 * @param defaultTimeout the defaultTimeout to set
	 */
	public void setDefaultTimeout(int defaultTimeout) {
		this.defaultTimeout = defaultTimeout;
	}

	/**
	 * @param email email
	 */
	public void send(Email email) throws EmailException {
		if (log.isDebugEnabled()) {
			log.debug(Streams.LINE_SEPARATOR
					+ "============SEND EMAIL================================" 
					+ Streams.LINE_SEPARATOR
					+ email.toString()
					+ "======================================================"
					+ Streams.LINE_SEPARATOR
					);
		}

		if (Strings.isEmpty(host)) {
			Map<String, List<EmailAddress>> rm = email.getRcptsByDomain();
			for (Entry<String, List<EmailAddress>> en : rm.entrySet()) {
				send(en.getKey(), en.getValue(), email);
			}
		}
		else {
			send(host, port, username, password, email.getRcpts(), email);
		}
	}

	/**
	 * send email to the specified address
	 *
	 * @param ia address
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	@SuppressWarnings("unchecked")
	private void send(String domain, Collection<EmailAddress> rcpts, Email email) throws EmailException {
		List<String> hosts;
		try {
			Class mxlookup = Classes.getClass("panda.net.MXLookup");
			hosts = (List<String>)Methods.invokeStaticMethod(mxlookup, "lookup", domain);
		}
		catch (Throwable e) {
			throw new EmailException(e);
		}
		
		if (Collections.isEmpty(hosts)) {
			throw new EmailException("Failed to find MX records for domain " + domain);
		}
		
		relay(hosts.get(0), rcpts, email);
	}

	private void relay(String host, Collection<EmailAddress> rcpts, Email email) throws EmailException {
		send(host, SMTPClient.DEFAULT_PORT, null, null, rcpts, email);
	}

	private boolean isSupportStartTLS(List<String> replys) {
		for (String s : replys) {
			if ("250-STARTTLS".equals(s)) {
				return true;
			}
		}
		return false;
	}
	
	private String generateMsgId() {
		return '<' + Randoms.randDigits(24) + ".SMTPMail@" + helo + '>';
	}
	
	private void send(String host, int port, String username, String password, Collection<EmailAddress> rcpts, Email email) throws EmailException {
		// debug writer
		StringBuilderWriter dbg = null;

		AuthenticatingSMTPClient client = new AuthenticatingSMTPClient(ssl);
		try {
			if (log.isDebugEnabled()) {
				dbg = new StringBuilderWriter();
				dbg.append("\n===================== SMTP DEBUG ====================\n");
				client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(dbg), true));
			}

			// optionally set a timeout to have a faster feedback on errors
			client.setConnectTimeout(connectTimeout);
			client.setDefaultTimeout(defaultTimeout);

			// connect to the SMTP server
			if (log.isDebugEnabled()) {
				log.debug("Connect to SMTP server " + host + ":" + port);
			}
			client.connect(host, port);
			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				throw new EmailException("SMTP server refused connection.");
			}

			// you say ehlo and you specify the host you are connecting from, could be anything
			client.ehlo(helo);
			
			// if your host accepts STARTTLS, we're good everything will be encrypted, otherwise
			// we're done here
			if (isSupportStartTLS(client.getReplyStrings())) {
				client.setTrustManager(TrustManagers.getAcceptAllTrustManager());
				if (!client.execTLS()) {
					log.debug("STARTTLS was not accepted: " + client.getReplyString());
				}
			}
			
			if (username != null) {
				client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, username, password);
				checkReply(client);
			}

			client.setSender(email.getSender());
			checkReply(client);

			for (EmailAddress ea : rcpts) {
				client.addRecipient(ea.getAddress());
				checkReply(client);
			}

			Writer out = client.sendMessageData();
			if (out != null) {
				String boundary = null;
				
				SMTPHeader header = new SMTPHeader();
				header.setDate(SMTPHeader.DATE, DateTimes.getDate());
				header.set(SMTPHeader.MESSAGE_ID, generateMsgId());
				header.set(SMTPHeader.MIME_VERSION, SMTPHeader.MIME_VERSION_10);
				if (email.isHtml() || email.hasAttachments()) {
					boundary = Randoms.randDigitLetters(28);
					header.set(SMTPHeader.CONTENT_TYPE, "multipart/mixed; boundary=" + boundary);
				}
				else {
					header.set(SMTPHeader.CONTENT_TYPE, "text/plain; charset=" + email.getCharset());
				}

				String encoding = "7bit";
				String body = email.getMessage();

				if (!Strings.isAscii(body)) {
					encoding = "Base64";
					body = Base64.encodeBase64String(body.getBytes(email.getCharset()));
				}

				header.set(SMTPHeader.CONTENT_TRANSFER_ENCODING, encoding);
				header.set(SMTPHeader.FROM, email.getEncodedFrom());
				header.set(SMTPHeader.TO, email.getEncodedTos());
				if (Collections.isNotEmpty(email.getCcs())) {
					header.set(SMTPHeader.CC, email.getEncodedCcs());
				}
				header.set(SMTPHeader.SUBJECT, email.getEncodedSubject());

				Writer writer = (dbg == null) ? out : new MultiWriter(out, dbg);
				writer.write(header.toString());

				if (email.isHtml() || email.hasAttachments()) {
					// Write the main text message
					appendMsgPart(writer, boundary, encoding, body, email);
					
					// Append attachments
					appendAttachments(writer, boundary, email);
					
					writer.write("--" + boundary + "--\n\n");
				}
				else {
					writer.write(body);
				}
				
				writer.close();
				if (!client.completePendingCommand()) {
					throw new EmailException("Failure to send the email: " + client.getReplyString());
				}
			}
			else {
				throw new EmailException("Failure to send the email: " + client.getReplyString());
			}
		}
		catch (IOException e) {
			throw new EmailException(e.getMessage(), e);
		}
		catch (InvalidKeyException e) {
			throw new EmailException(e.getMessage(), e);
		}
		catch (NoSuchAlgorithmException e) {
			throw new EmailException(e.getMessage(), e);
		}
		catch (InvalidKeySpecException e) {
			throw new EmailException(e.getMessage(), e);
		}
		finally {
			try {
				client.logout();
			}
			catch (Exception e) {
			}
			try {
				client.disconnect();
			}
			catch (Exception e) {
			}
			if (dbg != null) {
				dbg.append("\r\n====================================================");
				log.debug(dbg.toString());
			}
		}
	}

	private void appendMsgPart(Writer writer, String boundary, String encoding, String body, Email email) throws IOException {
		// Write the main text message
		writer.write("--" + boundary + "\n");
		writer.write(SMTPHeader.CONTENT_TYPE);
		writer.write(": ");
		writer.write(email.isHtml() ? MimeType.TEXT_HTML : MimeType.TEXT_PLAIN);
		writer.write("; charset=");
		writer.write(email.getCharset());
		writer.write("\n");

		writer.write(SMTPHeader.CONTENT_DISPOSITION);
		writer.write(": ");
		writer.write(SMTPHeader.CONTENT_DISPOSITION_INLIE);
		writer.write("\n");

		writer.write(SMTPHeader.CONTENT_TRANSFER_ENCODING);
		writer.write(": ");
		writer.write(encoding);
		writer.write("\n\n");

		writer.write(body);
		writer.write("\n");
	}

	/**
	 * Append the given attachments to the message which is being written by the given writer.
	 * 
	 * @param boundary separates each file attachment
	 */
	private void appendAttachments(Writer writer, String boundary, Email email)
			throws IOException {
		if (Collections.isEmpty(email.getAttachments())) {
			return;
		}
		
		for (EmailAttachment ea : email.getAttachments()) {
			final String mimeType = FileNames.getContentTypeFor(ea.getName());
			
			writer.write("--");
			writer.write(boundary);
			writer.write("\n");
			
			writer.write(SMTPHeader.CONTENT_TYPE);
			writer.write(": ");
			writer.write(Mimes.encodeWord(mimeType, email.getCharset()));
			//writer.write("application/" + FileNames.getExtension(ea.getName()));
			writer.write("; name=\"");
			writer.write(Mimes.encodeWord(ea.getName(), email.getCharset()));
			writer.write("\"\n");
			
			writer.write(SMTPHeader.CONTENT_DISPOSITION);
			writer.write(": ");
			writer.write(ea.isInline() ? SMTPHeader.CONTENT_DISPOSITION_INLIE : SMTPHeader.CONTENT_DISPOSITION_ATTACHMENT);
			writer.write("; filename=\"");
			writer.write(Mimes.encodeWord(ea.getName(), email.getCharset()));
			writer.write("\"\n");

			if (ea.isInline()) {
				writer.write(SMTPHeader.CONTENT_ID);
				writer.write(": ");
				writer.write(ea.getCid());
				writer.write("\n");
			}
			writer.write(SMTPHeader.CONTENT_TRANSFER_ENCODING);
			writer.write(": Base64\n\n");
			
			WriterOutputStream wos = new WriterOutputStream(writer, Charsets.UTF_8);
			Base64OutputStream bos = new Base64OutputStream(wos);
			Object data = ea.getData();
			if (data instanceof File) {
				Streams.copy((File)data, bos);
			}
			else if (data instanceof FileItem) {
				InputStream fis = ((FileItem)data).getInputStream();
				try {
					Streams.copy(fis, bos);
				}
				finally {
					Streams.safeClose(fis);
				}
			}
			else if (data instanceof InputStream) {
				Streams.copy((InputStream)data, bos);
			}
			else if (data instanceof byte[]) {
				Streams.write((byte[])data, bos);
			}
			else if (data instanceof CharSequence) {
				Streams.write((CharSequence)data, bos, email.getCharset());
			}
			bos.flush();
			writer.write("\n");
		}
	}

	private void checkReply(SMTPClient sc) throws EmailException {
		if (SMTPReply.isNegativeTransient(sc.getReplyCode())) {
			throw new EmailException("Transient SMTP error: " + sc.getReplyString());
		}
		else if (SMTPReply.isNegativePermanent(sc.getReplyCode())) {
			throw new EmailException("Permanent SMTP error: " + sc.getReplyString());
		}
	}
}
