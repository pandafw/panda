package panda.ex.sendgrid;

import panda.bind.json.Jsons;

/**
 * An object in which you may specify the content of your email.
 */
public class Content {
	public String type;
	public String value;

	public Content() {
	}

	public Content(String type, String value) {
		this.type = type;
		this.value = value;
	}

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
