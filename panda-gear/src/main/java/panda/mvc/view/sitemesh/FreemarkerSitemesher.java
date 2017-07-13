package panda.mvc.view.sitemesh;

import java.io.IOException;
import java.io.StringReader;
import java.io.Writer;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.view.ftl.FreemarkerHelper;
import panda.mvc.view.sitemesh.SitemeshConfig.SitemeshDecorator;

import freemarker.template.TemplateException;

@IocBean(type=Sitemesher.class, create="initialize", singleton=false)
public class FreemarkerSitemesher implements Sitemesher {
	@IocInject
	protected ActionContext context;
	
	@IocInject
	protected SitemeshManager smmgr;
	
	protected SitemeshDecorator decorator;

	public void initialize() {
		decorator = smmgr.findDecorator(context);
	}

	@Override
	public boolean needMesh() {
		return decorator != null && Strings.isNotEmpty(decorator.page);
	}
	
	@Override
	public void meshup(Writer out, String src) throws IOException {
		FastPageParser pp = new FastPageParser();
		Page page = pp.parse(new StringReader(src));

		FreemarkerHelper fh = context.getIoc().get(FreemarkerHelper.class);
		try {
			fh.execTemplate(out, decorator.page, page);
		}
		catch (TemplateException e) {
			throw new IOException("Failed to meshup " + context.getPath() + " by " + decorator.page, e);
		}
	}
}
