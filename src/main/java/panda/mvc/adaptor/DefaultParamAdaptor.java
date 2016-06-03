package panda.mvc.adaptor;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.io.MimeType;
import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Charsets;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.MvcConstants;
import panda.mvc.Mvcs;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.ejector.FormParamEjector;
import panda.mvc.adaptor.ejector.JsonParamEjector;
import panda.mvc.adaptor.ejector.MultiPartParamEjector;
import panda.mvc.adaptor.ejector.XmlParamEjector;
import panda.mvc.annotation.param.Attr;
import panda.mvc.annotation.param.Header;
import panda.mvc.annotation.param.IocObj;
import panda.mvc.annotation.param.Param;
import panda.net.http.HttpMethod;
import panda.servlet.ServletRequestHeaderMap;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * 将整个 HTTP 请求作为名值对来处理
 */
@IocBean(type=ParamAdaptor.class)
public class DefaultParamAdaptor implements ParamAdaptor {
	private static Log log = Logs.getLog(DefaultParamAdaptor.class);

	private static final String ATTR_EJECTOR = ParamEjector.class.getName();

	@IocInject(value=MvcConstants.IO_INPUT_ENCODING, required=false)
	protected String encoding = Charsets.UTF_8;
	
	protected ParamEjector getParamEjector(ActionContext ac) {
		ParamEjector ejector = (ParamEjector)ac.getRequest().getAttribute(ATTR_EJECTOR);
		if (ejector != null) {
			return ejector;
		}
		
		HttpServletRequest req = ac.getRequest();
		if (HttpMethod.POST.equalsIgnoreCase(req.getMethod())) {
			String contentType = Strings.lowerCase(req.getContentType());
			if (Strings.isEmpty(contentType)) {
				//skip
			}
			else if (contentType.startsWith(MimeType.MULTIPART_PREFIX)) {
				ejector = ac.getIoc().get(MultiPartParamEjector.class);
			}
			else if (contentType.startsWith(MimeType.APP_STREAM)) {
				ejector = ac.getIoc().get(MultiPartParamEjector.class);
			}
			else if (contentType.startsWith(MimeType.TEXT_XML)) {
				ejector = ac.getIoc().get(XmlParamEjector.class);
			}
			else if (contentType.startsWith(MimeType.APP_JSON) 
					|| contentType.startsWith(MimeType.TEXT_JAVASCRIPT)
					|| contentType.startsWith(MimeType.TEXT_JSON)) {
				ejector = ac.getIoc().get(JsonParamEjector.class);
			}
		}

		if (ejector == null) {
			ejector = ac.getIoc().get(FormParamEjector.class);
		}
		
		req.setAttribute(ATTR_EJECTOR, ejector);
		
		return ejector;
	}
	
	public static String indexedName(int i, Param param) {
		if (param == null) {
			return '~' + String.valueOf(i);
		}

		String pn = param.value();
		if (Strings.isEmpty(pn)) {
			return "";
		}
		
		if (pn.endsWith(".*")) {
			pn = pn.substring(0, pn.length() - 2);
			if (pn.length() == 0) {
				throw new IllegalArgumentException("Illegal prefix of @Param('" + param.value() + "')");
			}
		}
		return pn;
	}
	
	protected void addArg(LinkedHashMap<String, Object> args, String name, Object obj) {
		if (args.containsKey(name)) {
			throw new IllegalArgumentException("Duplicated @Param(" + name + ").");
		}
		args.put(name, obj);
	}
	
	public static boolean hasParam(Annotation[] as) {
		for (Annotation a : as) {
			if (a instanceof Param) {
				return true;
			}
		}
		return false;
	}
	
	public void adapt(ActionContext ac) {
		Method method = ac.getMethod();
		Annotation[][] pass = method.getParameterAnnotations();
		if (pass.length == 0) {
			// no parameters
			ac.setParams(new LinkedHashMap<String, Object>());
			ac.setArgs(Arrays.EMPTY_OBJECT_ARRAY);
			return;
		}
		
		Class<?>[] clazs = method.getParameterTypes();
		Type[] types = method.getGenericParameterTypes();

		if (pass.length == 1 && !hasParam(pass[0])) {
			Object o = adaptByParamType(ac, clazs[0]);
			if (o == null) {
				// adapt by path arguments
				o = adaptByPathArg(ac, "0", types[0], 0);
				if (o == null) {
					o = ejectByAll(ac, types[0], null);
				}
			}
			
			ac.setArgs(new Object[] { o });
			ac.setParams(o);
			return;
		}

		LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>(types.length);
		int p = 0;
		for (int i = 0; i < pass.length; i++) {
			Annotation[] pas = pass[i];
			Param param = null;
			Attr attr = null;
			IocObj ioco = null;
			Header reqh = null;

			// find @Param & @Attr & @IocObj in current annotations
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
					break;
				}
				else if (pa instanceof Attr) {
					attr = (Attr)pa;
					break;
				}
				else if (pa instanceof IocObj) {
					ioco = (IocObj)pa;
					break;
				}
				else if (pa instanceof Header) {
					reqh = (Header)pa;
					break;
				}
			}
			
			String name = indexedName(i, param);
			
			// If has @Attr
			if (null != attr) {
				addArg(args, name, adaptByAttr(ac, attr));
				continue;
			}

			// If has @IocObj
			if (null != ioco) {
				addArg(args, name, adaptByIoc(ac, clazs[i], ioco.value()));
				continue;
			}

			if (null != reqh) {
				addArg(args, name, adaptByReqHeader(ac, types[i], reqh.value()));
				continue;
			}
			
			// Adapt by @param annotation
			if (param != null) {
				Object o = adaptByParamAnno(ac, name, types[i], param);
				addArg(args, name, o);
				continue;
			}

			// And adapt by default support types
			Object o = adaptByParamType(ac, clazs[i]);
			if (o != null) {
				addArg(args, name, o);
				continue;
			}
			
			o = adaptByPathArg(ac, name, types[i], p++);
			if (o != null) {
				addArg(args, name, o);
				continue;
			}
			
			// if argument type is primitive, we should cast null to default primitive value
			o = cast(ac, name, null, types[i], null);
			addArg(args, name, o);
		}
		
		// remove unnamed parameter
		LinkedHashMap<String, Object> params = new LinkedHashMap<String, Object>(types.length);
		for (Entry<String, Object> en : args.entrySet()) {
			if (en.getKey().length() == 0 || en.getKey().charAt(0) != '~') {
				params.put(en.getKey(), en.getValue());
			}
		}
		if (params.size() == 1 && params.containsKey("")) {
			ac.setParams(params.get(""));
		}
		else {
			ac.setParams(params);
		}
		ac.setArgs(args.values().toArray());
	}

	protected Object adaptByAttr(ActionContext ac, Attr attr) {
		String name = attr.value();
		if (Scope.APP.equals(attr.scope())) {
			return ac.getServlet().getAttribute(name);
		}
		if (Scope.SESSION.equals(attr.scope())) {
			HttpSession session = ac.getRequest().getSession(false);
			if (session == null) {
				return null;
			}
			return session.getAttribute(name);
		}
		if (Scope.REQUEST.equals(attr.scope())) {
			return ac.getRequest().getAttribute(name);
		}
		
		Object re = ac.getRequest().getAttribute(name);
		if (null != re) {
			return re;
		}
		
		HttpSession session = ac.getRequest().getSession(false);
		if (session != null) {
			re = session.getAttribute(name);
			if (null != re) {
				return re;
			}
		}

		return ac.getServlet().getAttribute(name);
	}

	protected Object adaptByIoc(ActionContext ac, Class<?> cls, String name) {
		Ioc ioc = ac.getIoc();
		if (null == ioc) {
			throw new RuntimeException("You need define @IocBy in main module!!!");
		}
		
		if (Strings.isBlank(name)) {
			return ioc.get(cls);
		}
		return ioc.get(cls, name);
	}

	protected Object adaptByReqHeader(ActionContext ac, Type type, String name) {
		Object val;
		HttpServletRequest req = ac.getRequest();
		if (ParamEjector.ALL.equals(name)) {
			val = new ServletRequestHeaderMap(req);
		}
		else {
			val = req.getHeader(name);
		}

		return cast(ac, '^' + name, val, type, null);
	}

	protected Object adaptByParamType(ActionContext ac, Class<?> type) {
		// Request
		if (ServletRequest.class.isAssignableFrom(type)) {
			return ac.getRequest();
		}

		// Response
		if (ServletResponse.class.isAssignableFrom(type)) {
			return ac.getResponse();
		}

		// Session
		if (HttpSession.class.isAssignableFrom(type)) {
			return ac.getRequest().getSession(true);
		}
		
		// ServletContext
		if (ServletContext.class.isAssignableFrom(type)) {
			return ac.getServlet();
		}

		// ActionContext
		if (ActionContext.class.isAssignableFrom(type)) {
			return ac;
		}
		
		// Ioc
		if (Ioc.class.isAssignableFrom(type)) {
			return ac.getIoc();
		}

		// InputStream
		if (InputStream.class.isAssignableFrom(type)) {
			try {
				return ac.getRequest().getInputStream();
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}

		// Reader
		if (Reader.class.isAssignableFrom(type)) {
			try {
				return ac.getRequest().getReader();
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}

		return null;
	}

	protected Object adaptByParamAnno(ActionContext ac, String name, Type type, Param param) {
		Object o;
		String pm = param.value();

		// FORM
		if (Strings.isEmpty(pm)) {
			o = ejectByAll(ac, type, param.format());
		}
		else if (pm.endsWith(".*")) {
			o = ejectByPrefix(ac, type, pm.substring(0, pm.length() - 1), param.format());
		}
		else {
			o = ejectByNamedParam(ac, type, pm, param.format());
		}
		
		if (o instanceof String) {
			switch (param.strip()) {
			case STRIP_TO_NULL:
				o = Strings.stripToNull((String)o);
				break;
			case STRIP_TO_EMPTY:
				o = Strings.stripToEmpty((String)o);
				break;
			case TRIM_TO_NULL:
				o = Strings.trimToNull((String)o);
				break;
			case TRIM_TO_EMPTY:
				o = Strings.trimToEmpty((String)o);
				break;
			}
		}
		return o;
	}
	
	protected Object ejectByNamedParam(ActionContext ac, Type type, String name, String format) {
		ParamEjector pe = getParamEjector(ac);
		Object param = pe.eject(name);

		if (param != null) {
			boolean sc = Classes.isArrayOrCollection(param.getClass());
			boolean tc = Classes.isArrayOrCollection(Types.getRawType(type));
			if (sc && !tc) {
				param = Beans.getProperty(param, "0");
			}
			else if (!sc && tc) {
				param = new Object[] { param };
			}
		}
		
		return cast(ac, name, param, type, format);
	}

	protected Object ejectByAll(ActionContext ac, Type type, String format) {
		ParamEjector pe = getParamEjector(ac);
		Object a = pe.eject();
		Object o = cast(ac, null, a, type, format);
		return o;
	}

	@SuppressWarnings("unchecked")
	protected Object ejectByPrefix(ActionContext ac, Type type, String prefix, String format) {
		if (Strings.isEmpty(prefix)) {
			throw new IllegalArgumentException("Illegal @Param prefix value: '" + prefix + "'");
		}

		BeanHandler bh = Beans.i().getBeanHandler(type);
		Object target = null;
		
		ParamEjector pe = getParamEjector(ac);
		for (String key : pe.keys()) {
			if (key.startsWith(prefix)) {
				Object val = pe.eject(key);
				String bn = prefix == null ? key : key.substring(prefix.length());
				if (target == null) {
					target = Types.born(Types.getDefaultImplType(type));
				}
				Type pt = bh.getBeanType(target, bn);
				if (pt == null) {
					log.warn("Failed to set form value (" + key + "=" + val + ") to " + type + ", no property of " + bn);
				}
				else {
					Object cv = cast(ac, key, val, pt, format);
					if (!bh.setBeanValue(target, bn, cv)) {
						log.warn("Failed to set form value (" + key + "=" + val + ") to " + type);
					}
				}
			}
		}
		
		return target;
	}

	protected Object adaptByPathArg(ActionContext ac, String name, Type type, int index) {
		List<String> args = ac.getPathArgs();
		if (index < args.size()) {
			return cast(ac, name, args.get(index), type, null);
		}

		return null;
	}
	
	protected <T> T cast(ActionContext ac, String name, Object value, Type type, String format) {
		return Mvcs.castValue(ac, name, value, type, format);
	}
}
