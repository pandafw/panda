package panda.log.log4j;


import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import panda.bind.json.Jsons;
import panda.ex.slack.Attachment;
import panda.ex.slack.Message;
import panda.io.MimeType;
import panda.io.Streams;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.Https;

public class SlackLogAppender extends AbstractAppender {
	/** default subject */
	private String subject;

	/** subject layout */
	protected Layout subjectLayout;

	/** slack web hook url */
	protected String webhook;

	/** slack user name */
	protected String username;

	/** slack channel */
	protected String channel;
	
	/** connection timeout */
	protected int connTimeout = 5000;
	
	/** read timeout */
	protected int readTimeout = 30000;

	/** webhook url */
	private URL url;
	
	/**
	 * Construct
	 */
	public SlackLogAppender() {
	}

	/**
	 * This method determines if there is a sense in attempting to append.
	 * <p>
	 * It checks whether there is a set output target and also if there is a set layout. If these
	 * checks fail, then the boolean value <code>false</code> is returned.
	 */
	protected boolean checkEntryConditions() {
		if (this.webhook == null) {
			errorHandler.error("webhook url not configured.");
			return false;
		}
		
		try {
			this.url = new URL(webhook);
		}
		catch (MalformedURLException e) {
			errorHandler.error("malformed webhook url: " + webhook);
			return false;
		}

		return super.checkEntryConditions();
	}

	/**
	 * Perform SMTPAppender specific appending actions, mainly adding the event to a cyclic buffer
	 * and checking if the event triggers an e-mail to be sent.
	 */
	protected void subAppend(LoggingEvent event) {
		sendSlackMsg(event);
	}

	/**
	 * Layout body of email message.
	 */
	protected String formatBody(LoggingEvent event) {
		StringBuilder sb = new StringBuilder();
		outputLogEvent(sb, event);
		return sb.toString();
	}

	protected String getEmojiIcon(LoggingEvent event) {
		Level level = event.getLevel();
		if (level.isGreaterOrEqual(Level.FATAL)) {
			return ":boom:";
		}
		if (level.isGreaterOrEqual(Level.ERROR)) {
			return ":fire:";
		}
		if (level.isGreaterOrEqual(Level.WARN)) {
			return ":warning:";
		}
		if (level.isGreaterOrEqual(Level.INFO)) {
			return ":droplet:";
		}
		if (level.isGreaterOrEqual(Level.DEBUG)) {
			return ":bug:";
		}
		if (level.isGreaterOrEqual(Level.TRACE)) {
			return ":ant:";
		}
		return ":ghost:";
	}

	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 */
	protected void sendSlackMsg(LoggingEvent event) {
		String emoji = getEmojiIcon(event);
		
		String title = getSubject();
		if (subjectLayout != null) {
			title = subjectLayout.format(event);
		}

		String body = formatBody(event);
		
		Message msg = new Message();

		msg.setUsername(username);
		msg.setText(title);
		msg.setIcon_emoji(emoji);
		
		Attachment a = new Attachment();
		a.setText(body);
		msg.addAttachment(a);

		sendSlackMsg(msg);
	}

	protected void sendSlackMsg(Message msg) {
		HttpURLConnection conn = null;
		OutputStream hos = null;
		try {
			String body = Jsons.toJson(msg, true);

			conn = (HttpURLConnection)url.openConnection();

			Https.ignoreValidateCertification(conn);

			conn.setConnectTimeout(connTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestMethod(HttpMethod.POST.toString());
			conn.addRequestProperty(HttpHeader.CONTENT_TYPE, MimeType.APP_JAVASCRIPT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			hos = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(hos);
			dos.writeBytes(body);
			dos.flush();
			hos.flush();
		}
		catch (Exception e) {
			LogLog.error("Error occured while sending slack message.", e);
		}
		finally {
			Streams.safeClose(hos);
		}

		InputStream his = null;
		try {
			his = conn.getInputStream();
//			String s = Streams.toString(his);
//			System.out.println(s);
			Streams.safeDrain(his, readTimeout);
		}
		catch (Exception e) {
			LogLog.error("Error occured while reading slack result.", e);
		}
		finally {
			Streams.safeClose(his);
		}
	}

	/**
	 * Returns value of the <b>Subject</b> option.
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * The <b>Subject</b> option takes a string value which should be a the subject of the e-mail
	 * message.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the subjectLayout
	 */
	public Layout getSubjectLayout() {
		return subjectLayout;
	}

	/**
	 * @param subjectLayout the subjectLayout to set
	 */
	public void setSubjectLayout(Layout subjectLayout) {
		this.subjectLayout = subjectLayout;
	}

	/**
	 * @return the webhook
	 */
	public String getWebhook() {
		return webhook;
	}

	/**
	 * @param webhook the webhook to set
	 */
	public void setWebhook(String webhook) {
		this.webhook = webhook;
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
	 * @return the channel
	 */
	public String getChannel() {
		return channel;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
	}

	/**
	 * @return the connTimeout
	 */
	public int getConnTimeout() {
		return connTimeout;
	}

	/**
	 * @param connTimeout the connTimeout to set
	 */
	public void setConnTimeout(int connTimeout) {
		if (connTimeout > 0) {
			this.connTimeout = connTimeout;
		}
	}

	/**
	 * @return the readTimeout
	 */
	public int getReadTimeout() {
		return readTimeout;
	}

	/**
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		if (readTimeout > 0) {
			this.readTimeout = readTimeout;
		}
	}

}
