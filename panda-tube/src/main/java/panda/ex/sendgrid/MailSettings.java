package panda.ex.sendgrid;

/**
 * An object representing a collection of different mail settings that you can use to specify how you would like this email to be handled.
 */

public class MailSettings {
	public BccSettings bcc;
	public Setting bypass_list_management;
	public FooterSetting footer;
	public Setting sandbox_mode;
	public SpamCheckSetting spam_check;

}
