package panda.mvc.view.taglib;

import java.util.HashMap;
import java.util.Map;

import panda.ioc.annotation.IocBean;

@IocBean
public class TagLibraryManager {
	
	private Map<String, TagLibrary> taglibs;
	
	public TagLibraryManager() {
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
