package panda.net.smtp;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Iterators;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.net.InternetHeader;

/**
 * https://tools.ietf.org/html/rfc4021
 */
public class SMTPHeader extends InternetHeader implements Cloneable, Serializable {
	private static final long serialVersionUID = 2L;
	
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
	public static final String SUBJECT                   = "Subject";

	public static final String MIME_VERSION_10           = "1.0";
	public static final String CONTENT_DISPOSITION_INLIE = "inline";
	public static final String CONTENT_DISPOSITION_ATTACHMENT = "attachment";

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
	public Object clone() {
		SMTPHeader hh = new SMTPHeader();
		hh.map.putAll(map);
		return hh;
	}
	
	@Override
	public String toString() {
		try {
			StringBuilder sb = new StringBuilder();
			toString(sb);
			return sb.toString();
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	public void toString(Appendable writer) throws IOException {
		for (Map.Entry<String, Object> en : entrySet()) {
			String key = en.getKey();
			writer.append(key).append(": ");

			Object val = en.getValue();
			if (val != null) {
				Iterator it = Iterators.asIterator(en.getValue());
				while (it.hasNext()) {
					writer.append(it.next().toString());
					if (it.hasNext()) {
						writer.append(',');
					}
				}
			}
			writer.append(Streams.LINE_SEPARATOR_UNIX);
		}
	}
}
