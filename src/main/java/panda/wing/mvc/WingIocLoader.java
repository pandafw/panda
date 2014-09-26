package panda.wing.mvc;

import java.util.ArrayList;
import java.util.List;

import panda.filepool.dao.DaoFilePool;
import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.lang.Arrays;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.mvc.view.ftl.MvcFreemarkerTemplateLoader;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;
import panda.wing.util.DaoClientMaker;

public class WingIocLoader extends MvcDefaultIocLoader {
	public WingIocLoader() {
		List<Object> clss = new ArrayList<Object>();
		clss.addAll(Arrays.asList(DEFAULTS));
		
		clss.remove(LocalFilePool.class);
		clss.add(DaoFilePool.class);

		clss.remove(ResourceBundleLoader.class);
		clss.add(AppResourceBundleLoader.class);
		
		clss.add(AppSettings.class);
		clss.add(DaoClientMaker.class);
		
		clss.remove(MvcFreemarkerTemplateLoader.class);
		clss.add(AppFreemarkerTemplateLoader.class);

		init(clss);
	}
}
