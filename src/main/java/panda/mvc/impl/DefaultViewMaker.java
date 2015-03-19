package panda.mvc.impl;

import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.view.ForwardView;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.JsonView;
import panda.mvc.view.JspView;
import panda.mvc.view.RawView;
import panda.mvc.view.ServerRedirectView;
import panda.mvc.view.VoidView;
import panda.mvc.view.XmlView;

/**
 * 默认的的视图工厂类
 */
@IocBean(type=ViewMaker.class)
public class DefaultViewMaker implements ViewMaker {
	private static final Log log = Logs.getLog(DefaultViewMaker.class);
	
	public static final String VIEW_JSP = "jsp";
	public static final String VIEW_JSON = "json";
	public static final String VIEW_XML = "xml";
	public static final String VIEW_REDIRECT = "redirect";
	public static final String VIEW_REDIRECT2 = ">>";
	public static final String VIEW_IOC = "ioc";
	public static final String VIEW_HTTP = "http";
	public static final String VIEW_FREEMARKER = "ftl";
	public static final String VIEW_FORWARD = "forward";
	public static final String VIEW_FORWARD2 = "->";
	public static final String VIEW_RAW = "raw";
	public static final String VIEW_VOID = "void";

	public View make(Ioc ioc, String type, String value) {
		type = type.toLowerCase();
		if (VIEW_JSP.equals(type)) {
			return new JspView(value);
		}
		
		if (VIEW_JSON.equals(type)) {
			if (Strings.isBlank(value)) {
				return JsonView.DEFAULT;
			}
			return new JsonView(value);
		}
		
		if (VIEW_XML.equals(type)) {
			if (Strings.isBlank(value)) {
				return XmlView.DEFAULT;
			}
			return new XmlView(value);
		}
		
		if (VIEW_FREEMARKER.equals(type)) {
			return new FreemarkerView(value);
		}
		
		if (VIEW_REDIRECT.equals(type) || VIEW_REDIRECT2.equals(type)) {
			return new ServerRedirectView(value);
		}
		
		if (VIEW_FORWARD.equals(type) || VIEW_FORWARD2.equals(type)) {
			return new ForwardView(value);
		}
		
		if (VIEW_IOC.equals(type)) {
			return ioc.get(View.class, value);
		}
		
		if (VIEW_HTTP.equals(type)) {
			return new HttpStatusView(Numbers.toInt(value, 500));
		}

		if (VIEW_RAW.equals(type)) {
			return new RawView(value);
		}
		
		if (VIEW_VOID.equals(type)) {
			return VoidView.INSTANCE;
		}
		
		if (Strings.isEmpty(type)) {
			return null;
		}
		
		log.error("Failed to find view('" + type + "')");
		return null;
	}
}
