package panda.tube.zendesk;

import panda.bind.json.Jsons;

public class Collaborator {

	private String name;
	private String email;
	
	public Collaborator() {
	}
	
	/**
	 * @param name use name
	 * @param email user email
	 */
	public Collaborator(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
