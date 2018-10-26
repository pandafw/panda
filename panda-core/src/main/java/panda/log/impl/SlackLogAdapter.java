package panda.log.impl;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import panda.bind.json.Jsons;
import panda.ex.slack.Attachment;
import panda.ex.slack.Message;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.log.Log;
import panda.log.LogEvent;
import panda.log.LogFormat;
import panda.log.LogFormat.SimpleLogFormat;
import panda.log.LogLevel;
import panda.log.LogLog;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;


public class SlackLogAdapter extends AbstractLogAdapter {

	/** subject format */
	private LogFormat subject = new SimpleLogFormat("[%p] %c - %m");

	/** slack web hook url */
	protected URL webhook;

	/** slack user name */
	protected String username;

	/** slack channel */
	protected String channel;
	
	/** connection timeout */
	protected int connTimeout = 5000;
	
	/** read timeout */
	protected int readTimeout = 30000;

	@Override
	protected Log getLogger(String name) {
		return new SlackLog(this, name);
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("subject".equalsIgnoreCase(name)) {
			setSubject(value);
		}
		else if ("webhook".equalsIgnoreCase(name)) {
			try {
				setWebhook(value);
			}
			catch (MalformedURLException e) {
				throw new IllegalArgumentException("Malformed URL: " + webhook, e);
			}
		}
		else if ("username".equalsIgnoreCase(name)) {
			setUsername(value);
		}
		else if ("channel".equalsIgnoreCase(name)) {
			setChannel(value);
		}
		else if ("connTimeout".equalsIgnoreCase(name)) {
			setConnTimeout(Numbers.toInt(value, 0));
		}
		else if ("readTimeout".equalsIgnoreCase(name)) {
			setReadTimeout(Numbers.toInt(value, 0));
		}
		else {
			super.setProperty(name, value);
		}
	}

	protected String getEmojiIcon(LogLevel level) {
		if (level == LogLevel.FATAL) {
			return ":boom:";
		}
		if (level == LogLevel.ERROR) {
			return ":fire:";
		}
		if (level == LogLevel.WARN) {
			return ":warning:";
		}
		if (level == LogLevel.INFO) {
			return ":droplet:";
		}
		if (level == LogLevel.DEBUG) {
			return ":bug:";
		}
		if (level == LogLevel.TRACE) {
			return ":ant:";
		}
		return ":ghost:";
	}

	protected void write(LogEvent event) {
		String title = subject.format(event);

		StringBuilder body = new StringBuilder();
		body.append(format.format(event));

		if (event.getError() != null) {
			body.append(Exceptions.getStackTrace(event.getError()));
		}

		String emoji = getEmojiIcon(event.getLevel());
		
		Message smsg = new Message();

		smsg.setUsername(username);
		smsg.setText(title);
		smsg.setIcon_emoji(emoji);
		
		Attachment a = new Attachment();
		a.setText(body.toString());
		smsg.addAttachment(a);

		sendSlackMsg(smsg);
	}

	protected void sendSlackMsg(Message msg) {
		HttpURLConnection conn = null;
		OutputStream hos = null;
		try {
			String body = Jsons.toJson(msg, true);

			conn = (HttpURLConnection)webhook.openConnection();

			conn.setConnectTimeout(connTimeout);
			conn.setReadTimeout(readTimeout);
			conn.setRequestMethod(HttpMethod.POST);
			conn.addRequestProperty(HttpHeader.CONTENT_TYPE, MimeTypes.APP_JAVASCRIPT);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			hos = conn.getOutputStream();
			DataOutputStream dos = new DataOutputStream(hos);
			dos.writeBytes(body);
			dos.flush();
			hos.flush();
		}
		catch (Throwable e) {
			LogLog.error("Error occured while sending slack notification.", e);
		}
		finally {
			Streams.safeClose(hos);
		}

		InputStream his = null;
		try {
			his = conn.getInputStream();
			Streams.safeDrain(his, readTimeout);
		}
		catch (Throwable e) {
			LogLog.error("Error occured while getting slack response.", e);
		}
		finally {
			Streams.safeClose(his);
		}
	}

	/**
	 * The <b>Subject</b> option takes a string value which should be a the subject of the e-mail
	 * message.
	 * 
	 * @param subject the subject
	 */
	public void setSubject(String subject) {
		this.subject = new SimpleLogFormat(subject);
	}

	/**
	 * @param webhook the webhook to set
	 * @throws MalformedURLException if no protocol is specified, or an
     *               unknown protocol is found, or <tt>spec</tt> is <tt>null</tt>. 
	 */
	public void setWebhook(String webhook) throws MalformedURLException {
		this.webhook = new URL(webhook);
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @param channel the channel to set
	 */
	public void setChannel(String channel) {
		this.channel = channel;
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
	 * @param readTimeout the readTimeout to set
	 */
	public void setReadTimeout(int readTimeout) {
		if (readTimeout > 0) {
			this.readTimeout = readTimeout;
		}
	}

	/**
	 * Slack log
	 */
	protected static class SlackLog extends AbstractLog {
		protected SlackLogAdapter adapter;
		
		protected SlackLog(SlackLogAdapter adapter, String name) {
			super(adapter.logs, name, adapter.threshold);
			this.adapter = adapter;
		}

		@Override
		protected void write(LogEvent event) {
			adapter.write(event);
		}
	}
}
