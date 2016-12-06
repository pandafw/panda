package panda.net.smtp;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.net.InternetHeader;

/**
 * https://tools.ietf.org/html/rfc4021
 */
public class SMTPHeader extends InternetHeader implements Cloneable, Serializable {
	private static final long serialVersionUID = 5L;
	
	/**
	 * Date format pattern used to parse date headers in RFC 2822 format.
	 */
	public static final String PATTERN_RFC2822 = "EEE, dd MMM yyyy HH:mm:ss Z"; // Fri, 21 Nov 1997 09:55:06 -0600

	/**
	 * Date format used to format date headers in RFC 2822 format.
	 */
	public static final FastDateFormat FDF_RFC2822 = FastDateFormat.getInstance(PATTERN_RFC2822, Locale.ENGLISH);

	public static final String CONTENT_DISPOSITION       = "Content-Disposition";
	public static final String CONTENT_ID                = "Content-ID";
	public static final String CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
	public static final String CONTENT_TYPE              = "Content-Type";
	public static final String DATE                      = "Date";
	public static final String MIME_VERSION              = "MIME-Version";
	public static final String MESSAGE_ID                = "Message-ID";

	public static final String FROM                      = "From";
	public static final String TO                        = "To";
	public static final String CC                        = "Cc";
	public static final String BCC                       = "Bcc";
	public static final String REPLY_TO                  = "Reply-To";
	public static final String IN_REPLY_TO               = "In-Reply-To";
	public static final String SUBJECT                   = "Subject";
	public static final String SENDER                    = "Sender";

	public static final String MIME_VERSION_10           = "1.0";
	public static final String CONTENT_DISPOSITION_INLIE = "inline";
	public static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";

	public static final String LIST_SUBSCRIBE            = "List-Subscribe";
	public static final String LIST_POST                 = "List-Post";
	public static final String LIST_OWNER                = "List-Owner";
	public static final String LIST_ID                   = "List-Id";
	public static final String LIST_ARCHIVE              = "List-Archive";
	public static final String LIST_HELP                 = "List-Help";
	public static final String LIST_UNSUBSCRIBE          = "List-Unsubscribe";

	public static final String RESENT_FROM       = "Resent-From";
	public static final String RESENT_TO         = "Resent-To";
	public static final String RESENT_CC         = "Resent-Cc";
	public static final String RESENT_DATE       = "Resent-Date";
	public static final String RESENT_MESSAGE_ID = "Resent-Message-ID";
	public static final String RESENT_SENDER     = "Resent-Sender";

	public static final String REFERENCES        = "References";
	public static final String DKIM_SIGNATUR     = "DKIM-Signature";

	// -------------------------------------------------------------
	public static SMTPHeader create() {
		SMTPHeader header = new SMTPHeader();
		return header;
	}

	// -------------------------------------------------------------
	public SMTPHeader() {
	}

	public SMTPHeader(String from, String to, String subject) {
		this();
		set(FROM, from);
		set(TO, to);
		set(SUBJECT, subject);
	}

	//-------------------------------------------------
	@Override
	protected Date parseDate(String value) {
		return DateTimes.safeParse(FDF_RFC2822, value);
	}
	
	@Override
	protected String formatDate(Date value) {
		return FDF_RFC2822.format(value);
	}

	@Override
	public SMTPHeader clone() {
		SMTPHeader hh = new SMTPHeader();
		hh.putAll(this);
		return hh;
	}
}
