package panda.wing.action.tool;

import java.util.ArrayList;
import java.util.List;

import panda.ioc.annotation.IocInject;
import panda.mvc.annotation.At;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.AbstractAction;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;


@At
public class MemCacheAction extends AbstractAction {
	protected final static String CKEY_RESOURCE = "resource";
	
	protected final static String CKEY_TEMPLATE = "template";
	
	@IocInject
	protected AppResourceBundleLoader arbLoader;

	@IocInject
	protected AppFreemarkerTemplateLoader aftLoader;

	/**
	 * Constructor
	 */
	public MemCacheAction() {
		super();
	}

	/**
	 * @return cache key list
	 */
	@At("/admin/memcache")
	@Ok("ftl")
	public List<String> getCacheKeyList() {
		List<String> ckl = new ArrayList<String>();

		if (arbLoader.getDatabaseResourceLoader() != null) {
			ckl.add(CKEY_RESOURCE);
		}

		if (aftLoader.getDatabaseTemplateLoader() != null) {
			ckl.add(CKEY_TEMPLATE);
		}
		
		return ckl;
	}
}