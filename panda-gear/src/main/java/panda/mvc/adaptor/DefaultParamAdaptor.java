package panda.mvc.adaptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.io.MimeTypes;
import panda.ioc.Ioc;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Arrays;
import panda.lang.Classes;
import panda.lang.Strings;
import panda.lang.reflect.Types;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.ParamAdaptor;
import panda.mvc.adaptor.ejector.FormParamEjector;
import panda.mvc.adaptor.ejector.JsonParamEjector;
import panda.mvc.adaptor.ejector.MultiPartParamEjector;
import panda.mvc.adaptor.ejector.XmlParamEjector;
import panda.mvc.annotation.param.Header;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.param.PathArg;
import panda.net.http.HttpMethod;
import panda.servlet.ServletRequestHeaderMap;


@IocBean(type=ParamAdaptor.class)
public class DefaultParamAdaptor implements ParamAdaptor {
	private static Log log = Logs.getLog(DefaultParamAdaptor.class);

	private static final String ATTR_EJECTOR = ParamEjector.class.getName();

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
			else if (contentType.startsWith(MimeTypes.MULTIPART_PREFIX)) {
				ejector = ac.getIoc().get(MultiPartParamEjector.class);
			}
			else if (contentType.startsWith(MimeTypes.APP_STREAM)) {
				ejector = ac.getIoc().get(MultiPartParamEjector.class);
			}
			else if (contentType.startsWith(MimeTypes.TEXT_XML)) {
				ejector = ac.getIoc().get(XmlParamEjector.class);
			}
			else if (contentType.startsWith(MimeTypes.APP_JSON) 
					|| contentType.startsWith(MimeTypes.TEXT_JAVASCRIPT)
					|| contentType.startsWith(MimeTypes.TEXT_JSON)) {
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

		int idxPathArg = 0;
		LinkedHashMap<String, Object> args = new LinkedHashMap<String, Object>(types.length);
		for (int i = 0; i < pass.length; i++) {
			Annotation[] pas = pass[i];
			Param param = null;
			IocInject ioci = null;
			Header reqh = null;
			PathArg patha = null;

			// find @Param & @Attr & @IocObj in current annotations
			for (Annotation pa : pas) {
				if (pa instanceof Param) {
					param = (Param)pa;
				}
				else if (pa instanceof IocInject) {
					ioci = (IocInject)pa;
				}
				else if (pa instanceof Header) {
					reqh = (Header)pa;
				}
				else if (pa instanceof PathArg) {
					patha = (PathArg)pa;
				}
			}
			
			String name = indexedName(i, param);
			
			// If has @IocInject
			if (ioci != null) {
				addArg(args, name, adaptByIocInject(ac, clazs[i], ioci));
				continue;
			}

			// If has @Header
			if (reqh != null) {
				addArg(args, name, adaptByReqHeader(ac, types[i], reqh.value()));
				continue;
			}
			
			// Adapt by @Param annotation
			if (param != null) {
				Object o = adaptByParamAnno(ac, name, types[i], param);
				addArg(args, name, o);
				continue;
			}

			// Adapt by @PathArg annotation
			if (patha != null) {
				int p = patha.value() < 0 ? idxPathArg++ : patha.value();
				Object o = adaptByPathArg(ac, name, types[i], p);
				if (o != null) {
					addArg(args, name, o);
					continue;
				}
			}
			
			// if argument type is primitive, we should cast null to default primitive value
			Object o = cast(ac, name, null, types[i], null);
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

	protected Object adaptByIocInject(ActionContext ac, Class<?> cls, IocInject ioci) {
		Ioc ioc = ac.getIoc();
		if (ioc == null) {
			throw new RuntimeException("You need define @IocBy in main module!!!");
		}

		if (ioci.type() != Object.class) {
			cls = ioci.type();
		}
		
		return ioci.required() ? ioc.get(cls, ioci.value()) : ioc.getIfExists(cls, ioci.value());
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
		return Mvcs.castValueWithErrors(ac, name, value, type, format);
	}
}
