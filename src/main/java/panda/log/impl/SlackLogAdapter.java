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
import panda.io.MimeType;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogLevel;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.net.http.Https;


public class SlackLogAdapter extends AbstractLogAdapter {

	/** default subject */
	private String subject;

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


	public Log getLogger(String name) {
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

	protected void log(String name, LogLevel level, String msg, Throwable t) {
		String title = level.toString() + " " + subject;

		StringBuilder body = new StringBuilder();
		body.append(DateTimes.timestampFormat().format(DateTimes.getDate()))
			.append(' ')
			.append(Strings.rightPad(level.toString(), 5))
			.append(" [")
			.append(Thread.currentThread().getName())
			.append("] ")
			.append(name)
			.append(" - ")
			.append(msg)
			.append('\n');

		if (t != null) {
			body.append(Exceptions.getStackTrace(t)).append('\n');
		}

		String emoji = getEmojiIcon(level);
		
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
			e.printStackTrace();
		}
		finally {
			Streams.safeClose(hos);
		}

		InputStream his = null;
		try {
			his = conn.getInputStream();
			Streams.safeDrain(his, readTimeout);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			Streams.safeClose(his);
		}
	}

	/**
	 * The <b>Subject</b> option takes a string value which should be a the subject of the e-mail
	 * message.
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @param webhook the webhook to set
	 * @throws MalformedURLException 
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
	 * File log
	 */
	protected static class SlackLog extends AbstractLog {
		protected SlackLogAdapter adapter;
		protected String name;
		
		protected SlackLog(SlackLogAdapter adapter, String name) {
			super(name);
			this.adapter = adapter;
			this.name = name;
		}

		@Override
		public void log(LogLevel level, String msg, Throwable t) {
			adapter.log(name, level, msg, t);
		}
	}
}
