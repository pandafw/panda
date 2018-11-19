package panda.tool.net;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

import panda.args.Argument;
import panda.args.Option;
import panda.bind.json.JsonSerializer;
import panda.bind.json.Jsons;
import panda.ex.slack.Message;
import panda.ex.slack.Slacks;
import panda.io.MimeTypes;
import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.Exceptions;
import panda.net.http.HttpHeader;
import panda.net.http.HttpMethod;
import panda.tool.AbstractCommandTool;

/**
 */
public class Slack extends AbstractCommandTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new Slack().execute(args);
	}

	/**
	 * Constructor
	 */
	public Slack() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String webhook;
	protected Message message = new Message();

	/**
	 * @param webhook the webhook to set
	 */
	@Option(opt='h', option="webhook", arg="WEBHOOK", usage="WebHook URL", required=true)
	public void setWebhook(String webhook) {
		this.webhook = webhook;
	}

	/**
	 * @param channel the channel to set
	 */
	@Option(opt='c', option="channel", arg="CHANNEL", usage="channel", required=true)
	public void setChannel(String channel) {
		message.setChannel(channel);
	}

	/**
	 * @param username the username to set
	 */
	@Option(opt='u', option="username", arg="USERNAME", usage="username")
	public void setUsername(String username) {
		message.setUsername(username);
	}

	/**
	 * @param icon_emoji the icon_emoji to set
	 */
	@Option(opt='E', option="emoji", arg="EMOJI", usage="Icon Emoji")
	public void setIcon_emoji(String icon_emoji) {
		message.setIcon_emoji(icon_emoji);
	}

	/**
	 * @param icon_url the icon_url to set
	 */
	@Option(opt='I', option="icon", arg="ICON-URL", usage="Icon URL")
	public void setIcon_url(String icon_url) {
		message.setIcon_url(icon_url);
	}

	/**
	 * @param text the text to set
	 */
	@Argument(name="TEXT", required=true, usage="Message Text")
	public void setText(String text) {
		message.setText(text);
	}

	/**
	 * execute
	 */
	public void execute() {
		try {
			System.out.println("Send SlackMsg: " + message.getText());
			
			String r = Slacks.sendWebHook(webhook, message);
//			String r = sendSlackMsg(message);
	
			System.out.print(r);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	protected String sendSlackMsg(Message msg) throws Exception {
		URL webhook = new URL(this.webhook);
		
		HttpURLConnection conn = null;
		OutputStream hos = null;
		try {
			conn = (HttpURLConnection)webhook.openConnection();

			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			conn.setRequestMethod(HttpMethod.POST);
			conn.addRequestProperty(HttpHeader.CONTENT_TYPE, MimeTypes.APP_JAVASCRIPT + "; charset=" + Charsets.UTF_8);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			
			hos = conn.getOutputStream();
			OutputStream bos = Streams.buffer(hos);
			Writer writer = Streams.toWriter(bos, Charsets.UTF_8);
			JsonSerializer js = Jsons.newJsonSerializer();
			js.serialize(msg, writer);

			writer.flush();
			bos.flush();
			hos.flush();
		}
		finally {
			Streams.safeClose(hos);
		}

		InputStream his = null;
		try {
			his = conn.getInputStream();
			return Streams.toString(his, Charsets.UTF_8);
		}
		finally {
			Streams.safeClose(his);
		}
	}
}
