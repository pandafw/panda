package panda.ex.sendgrid;

/**
 * A subscription tracking setting object. Subscription tracking allows you to insert a subscription management link at the bottom of the text and html bodies of your email. If you would like to
 * specify the location of the link within your email, you may use the substitution_tag.
 */
public class SubscriptionTrackingSetting {
	public Boolean enable;
	public String text;
	public String html;
	public String substitution_tag;

}
