package panda.mvc.impl;

import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.View;
import panda.mvc.ViewMaker;
import panda.mvc.view.AltView;
import panda.mvc.view.CsvView;
import panda.mvc.view.ForwardView;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.JsonView;
import panda.mvc.view.JspView;
import panda.mvc.view.RawView;
import panda.mvc.view.RedirectView;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.mvc.view.SitemeshJspView;
import panda.mvc.view.TsvView;
import panda.mvc.view.VoidView;
import panda.mvc.view.XlsView;
import panda.mvc.view.XlsxView;
import panda.mvc.view.XmlView;
import panda.net.http.HttpStatus;

/**
 * Default View Maker
 */
@IocBean(type=ViewMaker.class)
public class DefaultViewMaker implements ViewMaker {
	private static final Log log = Logs.getLog(DefaultViewMaker.class);
	
	public View make(Ioc ioc, String viewstr) {
		if (Strings.isEmpty(viewstr)) {
			return null;
		}

		String type, value;
		int pos = viewstr.indexOf(':');
		if (pos > 0) {
			type = Strings.stripToNull(viewstr.substring(0, pos).toLowerCase());
			value = Strings.stripToNull(viewstr.substring(pos + 1));
		}
		else {
			type = viewstr.toLowerCase();
			value = null;
		}
		
		return make(ioc, type, value);
	}
	
	protected View make(Ioc ioc, String type, String value) {
		if (View.ALT.equals(type) || View.ALT2.equals(type)) {
			return new AltView(value);
		}
		
		if (View.JSP.equals(type)) {
			return new JspView(value);
		}
		
		if (View.SJSP.equals(type)) {
			return new SitemeshJspView(value);
		}
		
		if (View.FTL.equals(type)) {
			return new FreemarkerView(value);
		}
		
		if (View.SFTL.equals(type)) {
			return new SitemeshFreemarkerView(value);
		}
		
		if (View.JSON.equals(type)) {
			if (Strings.isBlank(value)) {
				return JsonView.DEFAULT;
			}
			return new JsonView(value);
		}
		
		if (View.XML.equals(type)) {
			if (Strings.isBlank(value)) {
				return XmlView.DEFAULT;
			}
			return new XmlView(value);
		}
		
		if (View.CSV.equals(type)) {
			if (Strings.isBlank(value)) {
				return CsvView.DEFAULT;
			}
			return new CsvView(value);
		}
		
		if (View.TSV.equals(type)) {
			if (Strings.isBlank(value)) {
				return TsvView.DEFAULT;
			}
			return new TsvView(value);
		}
		
		if (View.XLS.equals(type)) {
			if (Strings.isBlank(value)) {
				return XlsView.DEFAULT;
			}
			return new XlsView(value);
		}
		
		if (View.XLSX.equals(type)) {
			if (Strings.isBlank(value)) {
				return XlsxView.DEFAULT;
			}
			return new XlsxView(value);
		}
		
		if (View.REDIRECT.equals(type) || View.REDIRECT2.equals(type)) {
			return new RedirectView(value);
		}
		
		if (View.FORWARD.equals(type) || View.FORWARD2.equals(type)) {
			return new ForwardView(value);
		}
		
		if (View.IOC.equals(type)) {
			return ioc.get(View.class, value);
		}
		
		if (View.HTTP.equals(type)) {
			return new HttpStatusView(Numbers.toInt(value, HttpStatus.SC_INTERNAL_SERVER_ERROR));
		}

		if (View.RAW.equals(type)) {
			return new RawView(value);
		}
		
		if (View.VOID.equals(type) || View.NONE.equals(type) || View.NULL.equals(type)) {
			return VoidView.INSTANCE;
		}
		
		if (Strings.isEmpty(type)) {
			return null;
		}
		
		log.error("Failed to find view('" + type + "')");
		return null;
	}
}
