package panda.ex.sendgrid;

import panda.bind.json.Jsons;

public class Email {
	public String email;
	public String name;

	public Email(String email) {
		this.email = email;
	}

	public Email(String email, String name) {
		this.email = email;
		this.name = name;
	}

	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
