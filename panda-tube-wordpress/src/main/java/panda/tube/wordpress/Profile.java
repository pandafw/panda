package panda.tube.wordpress;

import panda.bind.json.Jsons;

public class Profile {
	public String first_name;
	public String last_name;
	public String url;
	public String display_name;
	public String nickname;
	public String nicename;
	public String bio;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}

