package panda.ex.sendgrid;

/**
 * A setting object that allows you to test the content of your email for spam.
 */
public class SpamCheckSetting {
	public Boolean enable;
	public Integer threshold;
	public String post_to_url;
}
