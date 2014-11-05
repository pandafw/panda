package panda.wing.mvc;

import java.util.ArrayList;
import java.util.List;

import panda.filepool.dao.DaoFilePool;
import panda.filepool.local.LocalFilePool;
import panda.io.resource.ResourceBundleLoader;
import panda.lang.Arrays;
import panda.mvc.ioc.loader.MvcAnnotationIocLoader;
import panda.mvc.ioc.loader.MvcDefaultIocLoader;
import panda.mvc.view.ftl.MvcFreemarkerTemplateLoader;
import panda.wing.lucene.LuceneProvider;
import panda.wing.util.AppFreemarkerTemplateLoader;
import panda.wing.util.AppResourceBundleLoader;
import panda.wing.util.AppSettings;
import panda.wing.util.DaoClientMaker;

public class WingIocLoader extends MvcAnnotationIocLoader {
	public WingIocLoader() {
		List<Object> clss = new ArrayList<Object>();
		clss.addAll(Arrays.asList(MvcDefaultIocLoader.DEFAULTS));
		
		clss.remove(LocalFilePool.class);
		clss.add(DaoFilePool.class);

		clss.remove(ResourceBundleLoader.class);
		clss.add(AppResourceBundleLoader.class);
		
		clss.remove(MvcFreemarkerTemplateLoader.class);
		clss.add(AppFreemarkerTemplateLoader.class);

		clss.add(AppSettings.class);
		clss.add(DaoClientMaker.class);
		clss.add(LuceneProvider.class);
		
		init(clss);
	}
}
