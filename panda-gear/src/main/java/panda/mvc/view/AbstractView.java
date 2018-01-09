package panda.mvc.view;

import panda.bind.json.JsonObject;
import panda.mvc.Mvcs;
import panda.mvc.View;

public abstract class AbstractView implements View {

	protected String location;
	
	/**
	 * @param location the location
	 */
	public AbstractView(String location) {
		if (location == null) {
			return;
		}

		if (location.length() > 3 
				&& location.charAt(0) == '{' && location.charAt(location.length() - 1) == '}') {
			JsonObject jo = JsonObject.fromJson(location);
			Mvcs.getCastors().castTo(jo, this);
		}
		else {
			this.location = location;
		}
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return this.getClass().getName() + ": " + location;
	}
}
