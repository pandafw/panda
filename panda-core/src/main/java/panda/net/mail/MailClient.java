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
import panda.io.stream.StringBuilderWriter;
import panda.io.stream.WriterOutputStream;
import panda.lang.Arrays;
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
import panda.net.Mimes;
import panda.net.PrintCommandListener;
import panda.net.io.CRLFLineWriter;
import panda.net.mail.dkim.DkimSigner;
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
	private Log log;

	private String helo = "localhost";
	private String host;
	private int port = SMTPClient.DEFAULT_PORT;
	private boolean ssl;
	private boolean tls = true;
	private String username;
	private String password;
	
	private int connectTimeout = 5000;
	private int defaultTimeout = 15000;

	/**
	 * @return the log
	 */
	public Log getLog() {
		return log;
	}

	/**
	 * @param log the log to set
	 */
	public void setLog(Log log) {
		this.log = log;
	}

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
	 * @return the tls
	 */
	public boolean isTls() {
		return tls;
	}

	/**
	 * @param tls the tls to set
	 */
	public void setTls(boolean tls) {
		this.tls = tls;
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
		if (Strings.isEmpty(email.getMsgId())) {
			email.setMsgId(generateMsgId());
		}

		if (log != null && log.isDebugEnabled()) {
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
			send(Arrays.asList(host), port, username, password, email.getRcpts(), email);
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
		
		send(hosts, SMTPClient.DEFAULT_PORT, null, null, rcpts, email);
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

	private String errmsg(String host, int port, String msg) {
		return host + ':' + port + "> " + msg;
	}

	private void close(AuthenticatingSMTPClient client) {
		if (client == null) {
			return;
		}
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
	}
	
	private void send(List<String> hosts, int port, String username, String password, Collection<EmailAddress> rcpts, Email email) throws EmailException {
		String host = null;
		
		// debug writer
		StringBuilderWriter dbg = null;

		AuthenticatingSMTPClient client = null;
		try {
			// connect to the SMTP server
			for (int i = 0; i < hosts.size(); i++) {
				host = hosts.get(i);

				client = new AuthenticatingSMTPClient(ssl);
				client.setConnectTimeout(connectTimeout);
				client.setDefaultTimeout(defaultTimeout);

				if (log != null && log.isDebugEnabled()) {
					dbg = new StringBuilderWriter();
					dbg.append("\n===================== SMTP DEBUG ====================\n");
					client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(dbg), true));

					log.debug("Connect to SMTP server " + host + ":" + port);
				}

				try {
					client.connect(host, port);
					break;
				}
				catch (IOException e) {
					if (i >= hosts.size() - 1) {
						throw new EmailException(errmsg(host, port, e.getMessage()));
					}
					if (log != null && log.isDebugEnabled()) {
						log.debug("Failed to connect SMTP server " + host + ":" + port, e);
					}
					close(client);
				}
			}
			if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
				throw new EmailException(errmsg(host, port, client.getReplyString()));
			}

			if (log != null && log.isDebugEnabled()) {
				log.debug("Send EHLO " + helo + " to SMTP Server " + host + ':' + port);
			}
			
			// you say ehlo and you specify the host you are connecting from, could be anything
			client.ehlo(helo);
			
			// if your host accepts STARTTLS, we're good everything will be encrypted, otherwise
			// we're done here
			if (tls && isSupportStartTLS(client.getReplyStrings())) {
				try {
					client.setTrustManager(TrustManagers.getAcceptAllTrustManager());
					if (client.execTLS()) {
						// ehlo again for some mail service
						client.ehlo(helo);
					}
					else {
						if (log != null && log.isDebugEnabled()) {
							log.debug("STARTTLS was not accepted: " + client.getReplyString());
						}
					}
				}
				catch (IOException e) {
					close(client);

					// reconnect
					client = new AuthenticatingSMTPClient(ssl);
					client.setConnectTimeout(connectTimeout);
					client.setDefaultTimeout(defaultTimeout);

					if (log != null && log.isDebugEnabled()) {
						dbg = new StringBuilderWriter();
						dbg.append("\n===================== SMTP DEBUG ====================\n");
						client.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(dbg), true));

						log.debug("Failed to STARTTLS", e);
						log.debug("Reconnect to SMTP server " + host + ":" + port);
					}

					client.connect(host, port);
					
					if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
						throw new EmailException(errmsg(host, port, client.getReplyString()));
					}

					if (log != null && log.isDebugEnabled()) {
						log.debug("Send EHLO " + helo + " to SMTP Server " + host + ':' + port);
					}
					client.ehlo(helo);
				}
			}
			
			if (username != null) {
				client.auth(AuthenticatingSMTPClient.AUTH_METHOD.LOGIN, username, password);
				checkReply(host, port, client);
			}

			client.setSender(email.getSender());
			checkReply(host, port, client);

			for (EmailAddress ea : rcpts) {
				client.addRecipient(ea.getAddress());
				checkReply(host, port, client);
			}

			Writer out = client.sendMessageData(dbg);
			if (out == null) {
				throw new EmailException(errmsg(host, port, client.getReplyString()));
			}
			
			String boundary = null;
				
			SMTPHeader header = new SMTPHeader();
			header.setDate(SMTPHeader.DATE, email.getDate() == null ? DateTimes.getDate() : email.getDate());
			header.set(SMTPHeader.FROM, email.getEncodedFrom());
			header.set(SMTPHeader.TO, email.getEncodedTos());
			if (Collections.isNotEmpty(email.getCcs())) {
				header.set(SMTPHeader.CC, email.getEncodedCcs());
			}
			if (Collections.isNotEmpty(email.getBccs())) {
				header.set(SMTPHeader.BCC, email.getEncodedBccs());
			}
			if (Collections.isNotEmpty(email.getReplyTos())) {
				header.set(SMTPHeader.REPLY_TO, email.getEncodedReplyTos());
			}

			header.set(SMTPHeader.MESSAGE_ID, email.getMsgId());
			header.set(SMTPHeader.SUBJECT, email.getEncodedSubject());
			header.set(SMTPHeader.MIME_VERSION, SMTPHeader.MIME_VERSION_10);

			if (email.isHtml() || email.hasAttachments()) {
				boundary = Randoms.randDigitLetters(28);
				header.set(SMTPHeader.CONTENT_TYPE, "multipart/mixed; boundary=" + boundary);
			}
			else {
				header.set(SMTPHeader.CONTENT_TYPE, "text/plain; charset=" + email.getCharset());
			}

			String encoding = Mimes.BIT7;
			String message = email.getMessage();

			if (!Strings.isAscii(message)) {
				encoding = Mimes.BASE64;
				message = Base64.encodeBase64ChunkedString(message.getBytes(email.getCharset()));
			}
			header.set(SMTPHeader.CONTENT_TRANSFER_ENCODING, encoding);

			if (email.isApplyDKIMSignature()) {
				DkimSigner signer = new DkimSigner(email.getDkimDomain(), email.getDkimSelector(), email.getDkimPrivateKey());
				
				signer.setIdentity(email.getFrom().getAddress());

				// build body
				String body = buildBody(email, encoding, message, boundary);
				
				String signature = signer.sign(header, body);
				
				// write header
				header.write(out);
				out.write(signature);
				out.write("\n\n");

				// write body
				out.write(body);
			}
			else {
				// write header
				header.write(out);
				out.write("\n");

				// write body
				writeBody(out, email, encoding, message, boundary);
			}
			
			out.flush();
			out.close();
			if (!client.completePendingCommand()) {
				throw new EmailException(errmsg(host, port, client.getReplyString()));
			}
		}
		catch (IOException e) {
			throw new EmailException(errmsg(host, port, e.getMessage()), e);
		}
		catch (InvalidKeyException e) {
			throw new EmailException(errmsg(host, port, e.getMessage()), e);
		}
		catch (InvalidKeySpecException e) {
			throw new EmailException(errmsg(host, port, e.getMessage()), e);
		}
		catch (NoSuchAlgorithmException e) {
			throw new EmailException(errmsg(host, port, e.getMessage()), e);
		}
		finally {
			close(client);
			if (dbg != null) {
				dbg.append("\r\n====================================================");
				log.debug(dbg.toString());
			}
		}
	}

	private String buildBody(Email email, String encoding, String message, String boundary) throws IOException {
		StringBuilderWriter sbw = new StringBuilderWriter();
		CRLFLineWriter clw = new CRLFLineWriter(sbw);

		writeBody(clw, email, encoding, message, boundary);
		
		clw.close();
		return sbw.toString();
	}
	
	private void writeBody(Writer out, Email email, String encoding, String message, String boundary) throws IOException {
		if (email.isHtml() || email.hasAttachments()) {
			// Write the message part
			out.write("--" + boundary + "\n");
			out.write(SMTPHeader.CONTENT_TYPE);
			out.write(": ");
			out.write(email.isHtml() ? MimeType.TEXT_HTML : MimeType.TEXT_PLAIN);
			out.write("; charset=");
			out.write(email.getCharset());
			out.write("\n");

			out.write(SMTPHeader.CONTENT_DISPOSITION);
			out.write(": ");
			out.write(SMTPHeader.CONTENT_DISPOSITION_INLIE);
			out.write("\n");

			out.write(SMTPHeader.CONTENT_TRANSFER_ENCODING);
			out.write(": ");
			out.write(encoding);
			out.write("\n\n");

			out.write(message);
			out.write("\n");
			
			// Append attachments
			writeAttachments(out, email, boundary);
			
			out.write("--" + boundary + "--\n\n");
		}
		else {
			out.write(message);
		}
	}

	/**
	 * Append the given attachments to the message which is being written by the given writer.
	 * 
	 * @param boundary separates each file attachment
	 */
	private void writeAttachments(Writer out, Email email, String boundary) throws IOException {
		if (Collections.isEmpty(email.getAttachments())) {
			return;
		}
		
		for (EmailAttachment ea : email.getAttachments()) {
			final String mimeType = FileNames.getContentTypeFor(ea.getName());
			
			out.write("--");
			out.write(boundary);
			out.write("\n");
			
			out.write(SMTPHeader.CONTENT_TYPE);
			out.write(": ");
			out.write(Mimes.encodeWord(mimeType, email.getCharset()));
			//writer.write("application/" + FileNames.getExtension(ea.getName()));
			out.write("; name=\"");
			out.write(Mimes.encodeWord(ea.getName(), email.getCharset()));
			out.write("\"\n");
			
			out.write(SMTPHeader.CONTENT_DISPOSITION);
			out.write(": ");
			out.write(ea.isInline() ? SMTPHeader.CONTENT_DISPOSITION_INLIE : SMTPHeader.CONTENT_DISPOSITION_ATTACHMENT);
			out.write("; filename=\"");
			out.write(Mimes.encodeWord(ea.getName(), email.getCharset()));
			out.write("\"\n");

			if (ea.isInline()) {
				out.write(SMTPHeader.CONTENT_ID);
				out.write(": ");
				out.write(ea.getCid());
				out.write("\n");
			}
			out.write(SMTPHeader.CONTENT_TRANSFER_ENCODING);
			out.write(": ");
			out.write(Mimes.BASE64);
			out.write("\n\n");
			
			WriterOutputStream wos = new WriterOutputStream(out, Charsets.UTF_8);
			Base64OutputStream bos = new Base64OutputStream(wos, true, Base64.MIME_CHUNK_SIZE);
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
			out.write("\n");
		}
	}

	private void checkReply(String host, int port, SMTPClient sc) throws EmailException {
		if (SMTPReply.isNegativeTransient(sc.getReplyCode())) {
			throw new EmailException(errmsg(host, port, sc.getReplyString()));
		}
		
		if (SMTPReply.isNegativePermanent(sc.getReplyCode())) {
			throw new EmailException(errmsg(host, port, sc.getReplyString()));
		}
	}
}
