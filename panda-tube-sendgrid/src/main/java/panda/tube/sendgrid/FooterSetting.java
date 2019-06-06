package panda.tube.sendgrid;

import panda.bind.json.Jsons;

/**
 * An object representing the default footer that you would like included on every email.
 */
public class FooterSetting {
	public Boolean enable;
	public String text;
	public String html;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
