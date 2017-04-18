package panda.wordpress.bean;

import panda.bind.json.Jsons;


public class BaseBean {
	public String toString() {
		return Jsons.toJson(this, true);
	}
}
