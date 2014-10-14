package panda.mvc.impl;

import panda.bind.json.JsonObject;
import panda.cast.Castors;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.view.ForwardView;
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
	public static final String VIEW_FORWARD = "forward";
	public static final String VIEW_FORWARD2 = "->";
	public static final String VIEW_RAW = "raw";
	public static final String VIEW_NULL = "";
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
			
			// 除高级的json format定义之外,也支持简单的缩写
			if (value.charAt(0) == '{') {
				JsonObject jo = JsonObject.fromJson(value);
				JsonView jv = Castors.scast(jo, JsonView.class);
				return jv;
			}

			JsonView jv = new JsonView();
			jv.setLocation(value);
			return jv;
		}
		
		if (VIEW_XML.equals(type)) {
			if (Strings.isBlank(value)) {
				return XmlView.DEFAULT;
			}
			
			// 除高级的json format定义之外,也支持简单的缩写
			if (value.charAt(0) == '{') {
				JsonObject jo = JsonObject.fromJson(value);
				XmlView xv = Castors.scast(jo, XmlView.class);
				return xv;
			}

			XmlView xv = new XmlView();
			xv.setLocation(value);
			return xv;
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
		
		if (VIEW_NULL.equals(type)) {
			return null;
		}
		
		log.error("Failed to find view('" + type + "')");
		return null;
	}

}
