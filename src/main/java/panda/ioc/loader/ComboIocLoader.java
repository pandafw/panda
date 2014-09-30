package panda.ioc.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.IocLoadException;
import panda.ioc.meta.IocObject;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

public class ComboIocLoader implements IocLoader {

	private static final Log log = Logs.getLog(ComboIocLoader.class);

	/**
	 * 类别名
	 */
	private static Map<String, Class<? extends IocLoader>> alias = new HashMap<String, Class<? extends IocLoader>>();
	static {
		alias.put("js", JsonIocLoader.class);
		alias.put("json", JsonIocLoader.class);
		alias.put("xml", XmlIocLoader.class);
		alias.put("anno", AnnotationIocLoader.class);
		alias.put("annotation", AnnotationIocLoader.class);
	}

	private List<IocLoader> iocLoaders = new ArrayList<IocLoader>();

	/**
	 * <p/>
	 * Example:
	 * <p/>
	 * <pre>{ 
	 *   "*panda.ioc.loader.JsonIocLoader",
	 *   "dao.js",
	 *   "service.js", 
	 *   "*panda.ioc.loader.XmlIocLoader",
	 *   "config.xml"
	 * }</code>
	 * <p/>
	 */
	public ComboIocLoader(String ... args) {
		this((Object[])args);
	}
	
	/**
	 * <p/>
	 * Example:
	 * <p/>
	 * <code>{ JsonIocLoader.class,"dao.js","service.js", XmlIocLoader.class,"config.xml"}</code>
	 * <p/>
	 */
	public ComboIocLoader(Object ... args) {
		ArrayList<String> argsList = null;
		Class loaderCls = null;
		for (Object a : args) {
			if (a instanceof Class) {
				if (argsList != null) {
					createIocLoader(loaderCls, argsList);
				}
				loaderCls = (Class)a;
				argsList = new ArrayList<String>();
			}
			else if (a instanceof String && ((String)a).length() > 0 && ((String)a).charAt(0) == '*') {
				if (argsList != null) {
					createIocLoader(loaderCls, argsList);
				}
				loaderCls = getLoaderClass(((String)a).substring(1));
				argsList = new ArrayList<String>();
			}
			else {
				if (argsList == null) {
					throw new IllegalArgumentException("ioc args without Loader ClassName. " + args);
				}
				argsList.add(a.toString());
			}
		}
		if (loaderCls != null) {
			createIocLoader(loaderCls, argsList);
		}
		
		Set<String> beanNames = new HashSet<String>();
		for (IocLoader loader : iocLoaders) {
			for (String beanName : loader.getName()) {
				if (!beanNames.add(beanName) && log.isWarnEnabled()) {
					log.warnf("Found Duplicate beanName=%s, pls check you config!", beanName);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createIocLoader(Class cls, List<String> args) {
		Class<? extends IocLoader> klass = (Class<? extends IocLoader>)cls;
		iocLoaders.add(Classes.born(klass, args.toArray(new String[args.size()])));
	}

	protected Class<? extends IocLoader> getAliasClass(String name) {
		return alias.get(name);
	}
	
	protected Class getLoaderClass(String className) {
		Class cls = getAliasClass(className);
		if (cls == null) {
			try {
				cls = Classes.getClass(className);
			}
			catch (ClassNotFoundException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		return cls;
	}

	public ComboIocLoader(IocLoader... loaders) {
		for (IocLoader iocLoader : loaders) {
			if (iocLoader != null) {
				iocLoaders.add(iocLoader);
			}
		}
	}

	public String[] getName() {
		ArrayList<String> list = new ArrayList<String>();
		for (IocLoader iocLoader : iocLoaders) {
			for (String name : iocLoader.getName()) {
				list.add(name);
			}
		}
		return list.toArray(new String[list.size()]);
	}

	public boolean has(String name) {
		for (IocLoader iocLoader : iocLoaders) {
			if (iocLoader.has(name)) {
				return true;
			}
		}
		return false;
	}

	public IocObject load(IocLoading loading, String name) throws IocLoadException {
		for (IocLoader iocLoader : iocLoaders) {
			if (iocLoader.has(name)) {
				IocObject iocObject = iocLoader.load(loading, name);
				if (log.isDebugEnabled()) {
					log.debugf("Found IocObject(%s) in IocLoader(%s)", name, iocLoader.getClass().getSimpleName() + "@"
							+ iocLoader.hashCode());
				}
				return iocObject;
			}
		}
		return null;
	}

	public String toString() {
		return iocLoaders.toString();
	}
}
