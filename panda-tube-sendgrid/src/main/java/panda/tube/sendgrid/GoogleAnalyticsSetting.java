package panda.tube.sendgrid;

import panda.bind.json.Jsons;

/**
 * An object configuring the tracking provided by Google Analytics.
 */
public class GoogleAnalyticsSetting {
	public Boolean enable;
	public String utm_source;
	public String utm_term;
	public String utm_content;
	public String utm_campaign;
	public String utm_medium;

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
