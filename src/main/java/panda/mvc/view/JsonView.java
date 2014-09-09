package panda.mvc.view;

import java.io.IOException;

import panda.mvc.ActionContext;
import panda.mvc.View;

/**
 * 将数据采用json方式输出的试图实现
 */
public class JsonView implements View {

	private boolean pretty;
	private String location;

	public static final JsonView COMPACT = new JsonView();
	
	public JsonView() {
	}

	/**
	 * @return the pretty
	 */
	public boolean isPretty() {
		return pretty;
	}

	/**
	 * @param pretty the pretty to set
	 */
	public void setPretty(boolean pretty) {
		this.pretty = pretty;
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

	public void render(ActionContext ac, Object obj) throws IOException {
		//TODO
	}
}
