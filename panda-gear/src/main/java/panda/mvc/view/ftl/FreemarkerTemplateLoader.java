package panda.mvc.view.ftl;

import java.io.File;

import javax.servlet.ServletContext;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.cache.WebappTemplateLoader;

import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.MvcConstants;
import panda.tpl.ftl.ClassTemplateLoader;
import panda.tpl.ftl.MultiTemplateLoader;

@IocBean(type=TemplateLoader.class, create="initialize")
public class FreemarkerTemplateLoader extends MultiTemplateLoader {
	private static final Log log = Logs.getLog(FreemarkerTemplateLoader.class);

	public static final String DEFAULT_WEB_PATH = "/WEB-INF/";
	
	@IocInject(value=MvcConstants.FREEMARKER_TEMPLATES_WEB_PATH, required=false)
	private String webpath = DEFAULT_WEB_PATH;

	@IocInject(value=MvcConstants.FREEMARKER_TEMPLATES, required=false)
	private String templates;
	
	@IocInject(required=false)
	private ServletContext servlet;
	
	public void initialize() {
		if (templates != null) {
			String[] ts = Strings.split(templates);
			for (String t : ts) {
				try {
					if (t.startsWith("file://")) {
						addTemplateLoader(new FileTemplateLoader(new File(t.substring(7))));
					}
					else if (t.startsWith("web://")) {
						addTemplateLoader(new WebappTemplateLoader(servlet, t.substring(6)));
					}
					else if (t.startsWith("class://")) {
						String path = t.substring(8);
						if (!path.endsWith("/")) {
							path += "/";
						}
						if (!path.startsWith("/")) {
							path = "/" + path;
						}
						addTemplateLoader(new ClassTemplateLoader(path));
					}
					else {
						log.warn("Invalid template setting - " + t);
					}
				}
				catch (Exception e) {
					log.warn("Invalid template setting - " + t, e);
				}
			}
		}
		else {
			// presume that most apps will require the class and webapp template loader
			// if people wish to
			if (servlet != null) {
				addTemplateLoader(new WebappTemplateLoader(servlet, webpath));
			}
			addTemplateLoader(new ClassTemplateLoader());
		}
	}
}
