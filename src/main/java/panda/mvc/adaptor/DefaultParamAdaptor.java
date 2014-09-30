package panda.mvc.adaptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.castor.Castors;
import panda.ioc.Ioc;
import panda.ioc.Scope;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.ActionInfo;
import panda.mvc.MvcConfig;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.ejector.FormParamEjector;
import panda.mvc.adaptor.ejector.JsonParamEjector;
import panda.mvc.adaptor.ejector.MultiPartParamEjector;
import panda.mvc.adaptor.ejector.XmlParamEjector;
import panda.mvc.annotation.param.Attr;
import panda.mvc.annotation.param.Header;
import panda.mvc.annotation.param.IocObj;
import panda.mvc.annotation.param.Param;
import panda.net.http.HttpContentType;
import panda.net.http.HttpMethod;
import panda.servlet.ServletRequestHeaderMap;


/**
 * 将整个 HTTP 请求作为名值对来处理
 */
public class DefaultParamAdaptor implements ParamAdaptor {
	private static Log log = Logs.getLog(DefaultParamAdaptor.class);

	private static final String ATTR_EJECTOR = ParamEjector.class.getName();
	
	public void init(MvcConfig config, ActionInfo ai) {
	}

	protected ParamEjector getParamEjector(ActionContext ac) {
		ParamEjector ejector = (ParamEjector)ac.getRequest().getAttribute(ATTR_EJECTOR);
		if (ejector != null) {
			return ejector;
		}
		
		HttpServletRequest req = ac.getRequest();
		if (HttpMethod.POST == HttpMethod.parse(req.getMethod())) {
			String contentType = Strings.lowerCase(req.getContentType());
			if (contentType.startsWith(HttpContentType.MULTIPART_PREFIX)) {
				ejector = new MultiPartParamEjector(ac);
			}
			else if (contentType.startsWith(HttpContentType.APP_STREAM)) {
				ejector = new MultiPartParamEjector(ac);
			}
			else if (contentType.startsWith(HttpContentType.TEXT_XML)) {
				ejector = new XmlParamEjector(ac);
			}
			else if (contentType.startsWith(HttpContentType.APP_JSON) 
					|| contentType.startsWith(HttpContentType.TEXT_JAVASCRIPT)
					|| contentType.startsWith(HttpContentType.TEXT_JSON)) {
				ejector = new JsonParamEjector(ac);
			}
		}

		if (ejector == null) {
			ejector = new FormParamEjector(ac);
		}
		
		ac.getRequest().setAttribute(ATTR_EJECTOR, ejector);
		
		return ejector;
	}
	
	public Object[] adapt(ActionContext ac) {
		Method method = ac.getMethod();
		Annotation[][] annss = method.getParameterAnnotations();
		Class<?>[] clazs = method.getParameterTypes();
		Type[] types = method.getGenericParameterTypes();
		Object[] objs = new Object[types.length];

		if (objs.length == 1 && annss[0].length == 0) {
			objs[0] = adaptByParamType(ac, clazs[0]);
			if (objs[0] == null) {
				// adapt by path arguments
				objs[0] = adaptByPathArg(ac, types[0], 0);

				if (objs[0] == null) {
					objs[0] = ejectByAll(ac, types[0]);
				}
			}
			
			return objs;
		}

		int p = 0;
		for (int i = 0; i < annss.length; i++) {
			Annotation[] anns = annss[i];
			Param param = null;
			Attr attr = null;
			IocObj iocObj = null;
			Header reqHeader = null;

			// find @Param & @Attr & @IocObj in current annotations
			for (int x = 0; x < anns.length; x++) {
				if (anns[x] instanceof Param) {
					param = (Param)anns[x];
					break;
				}
				else if (anns[x] instanceof Attr) {
					attr = (Attr)anns[x];
					break;
				}
				else if (anns[x] instanceof IocObj) {
					iocObj = (IocObj)anns[x];
					break;
				}
				else if (anns[x] instanceof Header) {
					reqHeader = (Header)anns[x];
					break;
				}
			}
			
			// If has @Attr
			if (null != attr) {
				objs[i] = adaptByAttr(ac, attr);
				continue;
			}

			// If has @IocObj
			if (null != iocObj) {
				objs[i] = adaptByIoc(ac, clazs[i], iocObj.value());
				continue;
			}

			if (null != reqHeader) {
				objs[i] = adaptByReqHeader(ac, types[i], reqHeader.value());
				continue;
			}

			// And adapt by default support types
			objs[i] = adaptByParamType(ac, clazs[i]);
			if (objs[i] != null) {
				continue;
			}
			
			// Adapt by @param annotation
			if (param != null) {
				objs[i] = adaptByParamAnno(ac, types[i], param);
				if (objs[i] != null) {
					continue;
				}
			}
			
			objs[i] = adaptByPathArg(ac, types[i], p++);
			if (objs[i] != null) {
				continue;
			}
			
			objs[i] = cast(null, types[i]);
		}
		
		return objs;
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

		return cast(val, type);
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

	protected Object adaptByParamAnno(ActionContext ac, Type type, Param param) {
		String pm = param.value();

		// FORM
		if (Strings.isEmpty(pm)) {
			return ejectByAll(ac, type);
		}
		if (pm.startsWith("^")) {
			return ejectByPrefix(ac, type, pm.substring(1));
		}
		
		return ejectByNamedParam(ac, type, pm);
	}
	
	protected Object ejectByNamedParam(ActionContext ac, Type type, String name) {
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
		
		return cast(param, type);
	}

	protected Object ejectByAll(ActionContext ac, Type type) {
		ParamEjector pe = getParamEjector(ac);
		Object a = pe.eject();
		Object o = cast(a, type);
		return o;
	}

	@SuppressWarnings("unchecked")
	protected Object ejectByPrefix(ActionContext ac, Type type, String prefix) {
		BeanHandler bh = Beans.i().getBeanHandler(type);
		Object target = Types.born(Types.getDefaultImplType(type));
		
		ParamEjector pe = getParamEjector(ac);
		for (String key : pe.keys()) {
			if (Strings.isEmpty(prefix) || key.startsWith(prefix)) {
				Object val = pe.eject(key);
				String bn = prefix == null ? key : key.substring(prefix.length());
				Type pt = bh.getBeanType(target, bn);
				if (pt == null) {
					log.warn("Failed to set form value (" + key + "=" + val + ") to " + type + ", no property of " + bn);
				}
				else {
					Object cv = cast(val, pt);
					if (!bh.setBeanValue(target, bn, cv)) {
						log.warn("Failed to set form value (" + key + "=" + val + ") to " + type);
					}
				}
			}
		}
		
		return target;
	}

	protected Object adaptByPathArg(ActionContext ac, Type type, int index) {
		List<String> args = ac.getPathArgs();
		if (index < args.size()) {
			return cast(args.get(index), type);
		}

		return null;
	}
	
	protected <T> T cast(Object value, Type type) {
		return Castors.scast(value, type);
	}
}
