package panda.ioc.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.meta.IocObject;
import panda.lang.Classes;
import panda.lang.Collections;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

public class ComboIocLoader implements IocLoader {

	private static final Log log = Logs.getLog(ComboIocLoader.class);

	/**
	 * 类别名
	 */
	protected Map<String, Class<? extends IocLoader>> alias = new HashMap<String, Class<? extends IocLoader>>();

	private List<IocLoader> iocLoaders = new ArrayList<IocLoader>();

	public ComboIocLoader(IocLoader... loaders) {
		initAlias();
		for (IocLoader iocLoader : loaders) {
			if (iocLoader != null) {
				iocLoaders.add(iocLoader);
			}
		}
		checkBeanNames();
	}

	protected void initAlias() {
		alias.put("js", JsonIocLoader.class);
		alias.put("json", JsonIocLoader.class);
		alias.put("xml", XmlIocLoader.class);
		alias.put("anno", AnnotationIocLoader.class);
		alias.put("annotation", AnnotationIocLoader.class);
	}

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

		checkBeanNames();
	}

	private void checkBeanNames() {
		Set<String> beanNames = new HashSet<String>();
		for (IocLoader loader : iocLoaders) {
			for (String beanName : loader.getNames()) {
				if (!beanNames.add(beanName) && log.isWarnEnabled()) {
					log.warnf("Found Duplicate beanName=%s, please check you config!", beanName);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createIocLoader(Class cls, List<String> args) {
		Class<? extends IocLoader> klass = (Class<? extends IocLoader>)cls;
		if (Collections.isEmpty(args)) {
			iocLoaders.add(Classes.born(klass));
		}
		else {
			iocLoaders.add(Classes.born(klass, args.toArray(new String[args.size()])));
		}
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

	public Set<String> getNames() {
		Set<String> ns = new HashSet<String>();
		for (IocLoader iocLoader : iocLoaders) {
			ns.addAll(iocLoader.getNames());
		}
		return ns;
	}

	public boolean has(String name) {
		for (IocLoader iocLoader : iocLoaders) {
			if (iocLoader.has(name)) {
				return true;
			}
		}
		return false;
	}

	public IocObject load(String name) throws IocLoadException {
		for (IocLoader iocLoader : iocLoaders) {
			if (iocLoader.has(name)) {
				IocObject iocObject = iocLoader.load(name);
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
