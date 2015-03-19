package panda.mvc.view.taglib;

import java.util.HashMap;
import java.util.Map;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.mvc.MvcConstants;

@IocBean(create="initialize")
public class TagLibraryManager {
	
	private Map<String, TagLibrary> taglibs;
	
	@IocInject(value=MvcConstants.TAGLIB_NAME, required=false)
	private String taglibName = "p";
	
	public void initialize() {
		taglibs = new HashMap<String, TagLibrary>();
		taglibs.put("p", new DefaultTagLibrary());
	}

	/**
	 * @return the taglibs
	 */
	public Map<String, TagLibrary> getTagLibraries() {
		return taglibs;
	}
}
