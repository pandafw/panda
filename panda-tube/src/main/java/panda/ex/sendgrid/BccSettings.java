package panda.ex.sendgrid;

import panda.bind.json.Jsons;

/**
 * This object allows you to have a blind carbon copy automatically sent to the specified email address for every email that is sent.
 */
public class BccSettings {
	public Boolean enable;
	public String email;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
