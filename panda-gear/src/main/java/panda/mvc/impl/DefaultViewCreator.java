package panda.mvc.impl;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import panda.bean.BeanHandler;
import panda.bind.json.JsonException;
import panda.bind.json.JsonObject;
import panda.cast.CastException;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.meta.IocValue;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.View;
import panda.mvc.ViewCreator;
import panda.mvc.view.AltView;
import panda.mvc.view.CsvView;
import panda.mvc.view.DataView;
import panda.mvc.view.ForwardView;
import panda.mvc.view.FreemarkerView;
import panda.mvc.view.HttpStatusView;
import panda.mvc.view.JsonView;
import panda.mvc.view.JspView;
import panda.mvc.view.RedirectView;
import panda.mvc.view.ServletErrorView;
import panda.mvc.view.SitemeshFreemarkerView;
import panda.mvc.view.SitemeshJsonView;
import panda.mvc.view.SitemeshJspView;
import panda.mvc.view.SitemeshXmlView;
import panda.mvc.view.TsvView;
import panda.mvc.view.Views;
import panda.mvc.view.VoidView;
import panda.mvc.view.XlsView;
import panda.mvc.view.XlsxView;
import panda.mvc.view.XmlView;

@IocBean(type=ViewCreator.class, create="initialize")
public class DefaultViewCreator implements ViewCreator {
	@IocInject
	private Ioc ioc;
	
	@IocInject(value=MvcConstants.MVC_VIEWS, required=false)
	private Map<String, String> aliass;

	public void initialize() {
		if (aliass == null) {
			aliass = new HashMap<String, String>();
		}
		
		addView(View.SC, HttpStatusView.class);
		addView(View.ERR, ServletErrorView.class);

		addView(View.CSV, CsvView.class);
		addView(View.TSV, TsvView.class);
		addView(View.XLS, XlsView.class);
		addView(View.XLSX, XlsxView.class);

		addView(View.JSON, JsonView.class);
		addView(View.SJSON, SitemeshJsonView.class);
		addView(View.XML, XmlView.class);
		addView(View.SXML, SitemeshXmlView.class);
		
		addView(View.JSP, JspView.class);
		addView(View.SJSP, SitemeshJspView.class);
		addView(View.REDIRECT, RedirectView.class);
		addView(View.REDIRECT2, RedirectView.class);
		addView(View.FTL, FreemarkerView.class);
		addView(View.SFTL, SitemeshFreemarkerView.class);
		addView(View.FORWARD, ForwardView.class);
		addView(View.FORWARD2, ForwardView.class);
		addView(View.RAW, DataView.class);

		addView(View.ALT, AltView.class);
		addView(View.ALT2, AltView.class);

		addView(View.VOID, VoidView.class);
		addView(View.NONE, VoidView.class);
		addView(View.NULL, VoidView.class);

		// check view
		for (String a : aliass.values()) {
			createView(ioc, a);
		}
	}

	private void addView(String alias, Class<? extends View> type) {
		if (!aliass.containsKey(alias)) {
			aliass.put(alias, IocValue.TYPE_REF + type.getName());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public View create(ActionContext ac, String viewer) {
		if (Strings.isEmpty(viewer)) {
			return null;
		}

		String alias, params;
		int i = viewer.indexOf(View.SEP);
		if (i > 0) {
			alias = Strings.stripToNull(viewer.substring(0, i).toLowerCase());
			params = Strings.stripToNull(viewer.substring(i + 1));
		}
		else {
			alias = viewer.toLowerCase();
			params = null;
		}
		
		View v = createView(ac.getIoc(), alias);
		if (v == null || params == null) {
			return v;
		}
		
		if (params.length() > 3 && params.charAt(0) == '{' && params.charAt(params.length() - 1) == '}') {
			JsonObject jo = null;
			try {
				jo = JsonObject.fromJson(params);
			}
			catch (JsonException e) {
				throw new IllegalArgumentException("Invalid json parameters of view - " + viewer, e);
			}

			// set parameters
			BeanHandler bh = Mvcs.getBeans().getBeanHandler(v.getClass());
			for (Entry<String, Object> en : jo.entrySet()) {
				String pn = en.getKey();

				// translate ${..} expression
				Object pv = Mvcs.evaluate(ac, en.getValue());
				
				Type pt = bh.getPropertyType(pn);
				if (pt == null) {
					throw new IllegalArgumentException("Failed to find property('" + pn + "') of View " + v.getClass() + ", params: " + params);
				}
	
				try {
					Object cv = Mvcs.getCastors().cast(pv, pt);
					if (!bh.setPropertyValue(v, pn, cv)) {
						throw new IllegalArgumentException("Failed to set property('" + pn + "') of View " + v.getClass() + ", description: " + params);
					}
				}
				catch (CastException e) {
					throw new IllegalArgumentException("Failed to cast property('" + pn + "') of View " + v.getClass() + ", description: " + params, e);
				}
			}
		}
		else {
			params = Mvcs.translate(ac, params);
			v.setArgument(params);
		}
		
		return v;
	}
	
	private View createView(Ioc ioc, String alias) {
		if (Strings.isEmpty(alias)) {
			throw new IllegalArgumentException("Missing view name");
		}
		
		String name = aliass.get(alias);
		if (name == null) {
			name = alias;
		}

		try {
			View v;
			if (Strings.startsWithChar(name, IocValue.TYPE_REF)) {
				v = ioc.get(View.class, name.substring(1));
			}
			else {
				v = (View)Classes.newInstance(name);
			}
			return v;
		}
		catch (Exception e) {
			throw new IllegalArgumentException("Failed to create view(" + name + ")", e);
		}
	}

	@Override
	public View createDefaultView(ActionContext ac) {
		return create(ac, ac.getConfig().getDefaultView());
	}

	@Override
	public View createErrorView(ActionContext ac) {
		if (Strings.isNotEmpty(ac.getConfig().getErrorView())) {
			return create(ac, ac.getConfig().getErrorView());
		}
		return create(ac, ac.getConfig().getDefaultView());
	}

	@Override
	public View createFatalView(ActionContext ac) {
		if (Strings.isNotEmpty(ac.getConfig().getFatalView())) {
			return create(ac, ac.getConfig().getFatalView());
		}
		
		String dv = ac.getConfig().getDefaultView();
		if (dv.startsWith(View.JSON) || dv.startsWith(View.SJSON)
				|| dv.startsWith(View.XML) || dv.startsWith(View.SXML)) {
			return create(ac, dv);
		}

		return create(ac, Views.SE_INTERNAL_ERROR);
	}
}
