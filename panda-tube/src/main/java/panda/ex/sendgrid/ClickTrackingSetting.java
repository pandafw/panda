package panda.ex.sendgrid;

import panda.bind.json.Jsons;

/**
 * Settings to determine how you would like to track the 
 * metrics of how your recipients interact with your email.
 */
public class ClickTrackingSetting {
	public Boolean enable;
	public Boolean enable_text;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
