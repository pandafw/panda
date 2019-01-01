package panda.net.mail;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Asserts;
import panda.lang.Collections;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.net.Mimes;
import panda.vfs.FileItem;

/**
 * a class for send mail
 */
public class JavaMailClient extends MailClient {
	private static Log LOG = Logs.getLog(JavaMailClient.class);

	public JavaMailClient() {
		setLog(LOG);
	}

	/**
	 * @param email email
	 * @throws EmailException if an error occurs
	 */
	public void send(Email email) throws EmailException {
		Asserts.notNull(email.getFrom(), "Email.From is required");
		Asserts.notEmpty(email.getTos(), "Email.To is required");

		if (log != null && log.isDebugEnabled()) {
			log.debug(Streams.EOL
					+ "============ SEND EMAIL (Java Mail) =================="
					+ Streams.EOL
					+ email.toString()
					+ "======================================================"
					+ Streams.EOL);
		}

		Properties props = new Properties();

		props.put("mail.smtp.connectiontimeout", connectTimeout);
		props.put("mail.smtp.timeout", defaultTimeout);
		if (Strings.isNotEmpty(host)) {
			props.put("mail.smtp.host", host);
		}
		if (port > 0) {
			props.put("mail.smtp.port", port);
		}
		props.put("mail.smtp.ssl.enable", ssl);
		props.put("mail.smtp.starttls.enable", startTls);
		props.put("mail.debug", debug);

		javax.mail.Authenticator auth = null;
		if (Strings.isNotEmpty(username)) {
			props.put("mail.smtp.auth", "true");
			auth = new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
		}

		List<InputStream> ins = new ArrayList<InputStream>();
		
		Session session = Session.getInstance(props, auth);
		try {
			MimeMessage msg = new MimeMessage(session);

			msg.setHeader("Content-Transfer-Encoding", Mimes.BASE64);

			// Set From:
			msg.setFrom(toInternetAddress(email.getFrom()));

			// Set To:
			for (EmailAddress ea : email.getTos()) {
				msg.addRecipient(Message.RecipientType.TO, toInternetAddress(ea));
			}
			
			// Set Cc:
			if (Collections.isNotEmpty(email.getCcs())) {
				for (EmailAddress ea : email.getCcs()) {
					msg.addRecipient(Message.RecipientType.CC, toInternetAddress(ea));
				}
			}
			
			// Set Bcc:
			if (Collections.isNotEmpty(email.getBccs())) {
				for (EmailAddress ea : email.getBccs()) {
					msg.addRecipient(Message.RecipientType.BCC, toInternetAddress(ea));
				}
			}
			
			// Set ReplyTo:
			if (Collections.isNotEmpty(email.getReplyTos())) {
				Address[] rtos = new Address[email.getReplyTos().size()];
				for (int i = 0; i < email.getReplyTos().size(); i++) {
					EmailAddress ea = email.getReplyTos().get(i);
					rtos[i] = toInternetAddress(ea);
				}
				msg.setReplyTo(rtos);
			}
			
			msg.setSubject(email.getSubject(), email.getCharset());
			if (email.isHtml() || email.hasAttachments()) {
				Multipart mp = new MimeMultipart();

				MimeBodyPart textPart = new MimeBodyPart();
				textPart.setContent(email.getMessage(), email.isHtml() ? MimeTypes.TEXT_HTML : MimeTypes.TEXT_PLAIN);
				mp.addBodyPart(textPart);

				if (Collections.isNotEmpty(email.getAttachments())) {
					for (EmailAttachment ea : email.getAttachments()) {
						MimeBodyPart attachment = new MimeBodyPart();
						
						String mimeType = MimeTypes.getMimeType(ea.getName());
						attachment.setFileName(ea.getName());
						Object data = ea.getData();
						if (data instanceof File) {
							attachment.attachFile((File)data);
						}
						else if (data instanceof FileItem) {
							InputStream in = ((FileItem)data).open();
							ins.add(in);
							attachment.setContent(in, mimeType);
						}
						else if (data instanceof InputStream) {
							attachment.setContent((InputStream)data, mimeType);
						}
						else if (data instanceof byte[]) {
							attachment.setContent(Streams.toInputStream((byte[])data), mimeType);
						}
						else if (data instanceof CharSequence) {
							attachment.setContent(((CharSequence)data).toString(), mimeType);
						}
						mp.addBodyPart(attachment);
					}
				}

				msg.setContent(mp);
			}
			else {
				msg.setText(email.getMessage(), email.getCharset());
			}
			
			Transport.send(msg);
		}
		catch (IOException e) {
			throw new EmailException(e);
		}
		catch (MessagingException e) {
			throw new EmailException(e);
		}
		finally {
			for (InputStream in : ins) {
				Streams.safeClose(in);
			}
		}
	}

	private InternetAddress toInternetAddress(EmailAddress ea) throws UnsupportedEncodingException {
		return new InternetAddress(ea.getAddress(), ea.getPersonal());
	}
}
