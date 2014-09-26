package panda.wing.action.tool;

import java.util.ArrayList;
import java.util.List;

import panda.wing.ServletApplet;
import panda.wing.action.BaseAction;


/**
 */
public class MemCacheAction extends BaseAction {
	protected final static String CKEY_RESOURCE = "resource";
	
	protected final static String CKEY_TEMPLATE = "template";
	
	/**
	 * Constructor
	 */
	public MemCacheAction() {
		super();
	}

	/**
	 * @return cache key list
	 */
	public List<String> getCacheKeyList() {
		List<String> ckl = new ArrayList<String>();

		if (ServletApplet.i().getDatabaseResourceLoader() != null) {
			ckl.add(CKEY_RESOURCE);
		}

		if (ServletApplet.i().getDatabaseTemplateLoader() != null) {
			ckl.add(CKEY_TEMPLATE);
		}
		
		return ckl;
	}

	/**
	 * @return SUCCESS
	 */
	public String index() {
		return SUCCESS;
	}
}