package panda.log.log4j;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.net.SMTPAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.TriggeringEventEvaluator;

import panda.lang.Charsets;

public class SmtpLogAppender extends SMTPAppender {
	/** subject layout */
	protected Layout subjectLayout;

	/**
	 * Construct
	 */
	public SmtpLogAppender() {
	}

	/**
	 * Construct
	 */
	public SmtpLogAppender(TriggeringEventEvaluator evaluator) {
		super(evaluator);
	}

	/**
	 * @return the subjectLayout
	 */
	public Layout getSubjectLayout() {
		return subjectLayout;
	}

	/**
	 * @param subjectLayout
	 *            the subjectLayout to set
	 */
	public void setSubjectLayout(Layout subjectLayout) {
		this.subjectLayout = subjectLayout;
	}

	protected boolean setMsgSubject(String subject) {
		try {
			msg.setSubject("");
			msg.setSubject(MimeUtility.encodeText(subject, Charsets.UTF_8, null));
			return true;
		} 
		catch (UnsupportedEncodingException ex) {
			LogLog.error("Unable to encode SMTP subject", ex);
			return false;
		} 
		catch (MessagingException ex) {
			LogLog.error("Unable to set SMTP subject", ex);
			return false;
		}
	}

	/**
	 * Send the contents of the cyclic buffer as an e-mail message.
	 */
	@Override
	protected void sendBuffer() {
		if (subjectLayout != null) {
			if (cb.length() > 0) {
				StringBuilder sb = new StringBuilder();
				LoggingEvent event = cb.get(cb.length() - 1);
				sb.append(subjectLayout.format(event));
				if (!setMsgSubject(sb.toString())) {
					setMsgSubject(getSubject());
				}
			}
			else {
				setMsgSubject(getSubject());
			}
		}
		super.sendBuffer();
	}
}
