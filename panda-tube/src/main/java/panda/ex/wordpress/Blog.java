package panda.ex.wordpress;

import panda.bind.json.Jsons;

/**
 * Class that keeps the information on a blog for a user
 */
public class Blog {
	public String blogid;
	public String blogName;
	public String url;
	public String xmlrpc;
	public Boolean isAdmin;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
