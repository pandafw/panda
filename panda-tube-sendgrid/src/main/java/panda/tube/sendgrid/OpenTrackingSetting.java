package panda.tube.sendgrid;

/**
 * An open tracking settings object. This allows you to track whether the email was opened or not, but including a single pixel image in the body of the content. When the pixel is loaded, we can log
 * that the email was opened.
 */
public class OpenTrackingSetting {
	public boolean enable;
	public String substitution_tag;

}
