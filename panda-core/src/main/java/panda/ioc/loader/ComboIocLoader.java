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

	public static final String JS = "js";
	public static final String JSON = "json";
	public static final String XML = "xml";
	public static final String ANNO = "anno";
	public static final String ANNOTATION = "annotation";

	/**
	 * 类别名
	 */
	protected Map<String, Class<? extends IocLoader>> alias = new HashMap<String, Class<? extends IocLoader>>();

	private List<IocLoader> iocLoaders = new ArrayList<IocLoader>();
	
	protected ComboIocLoader() {
		initAlias();
	}
	
	public ComboIocLoader(IocLoader... loaders) {
		this();
		
		for (IocLoader iocLoader : loaders) {
			if (iocLoader != null) {
				iocLoaders.add(iocLoader);
			}
		}
		checkBeanNames();
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
	 * 
	 * @param args the arguments
	 */
	public ComboIocLoader(String ... args) {
		this();
		initLoaders(args);
	}
	
	protected void initLoaders(String ... args) {
		List<Object> argsList = null;
		String loader = null;
		for (String a : args) {
			if (a.length() > 0 && a.charAt(0) == '*') {
				if (argsList != null) {
					addIocLoader(loader, argsList);
				}
				loader = a.substring(1);
				argsList = new ArrayList<Object>();
			}
			else {
				if (argsList == null) {
					throw new IllegalArgumentException("ioc args without Loader ClassName. " + args);
				}
				argsList.add(getLoaderArgument(a));
			}
		}
		if (loader != null) {
			addIocLoader(loader, argsList);
		}

		checkBeanNames();
	}

	protected void initAlias() {
		alias.put(JS, JsonIocLoader.class);
		alias.put(JSON, JsonIocLoader.class);
		alias.put(XML, XmlIocLoader.class);
		alias.put(ANNO, AnnotationIocLoader.class);
		alias.put(ANNOTATION, AnnotationIocLoader.class);
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
	
	protected void addIocLoader(String name, List<Object> args) {
		iocLoaders.add(createIocLoader(name, args));
	}
	
	protected IocLoader createIocLoader(String name, List<Object> args) {
		Class<? extends IocLoader> klass = getLoaderClass(name);
		if (Collections.isEmpty(args)) {
			return Classes.born(klass);
		}

		boolean ss = true;
		for (Object o : args) {
			if (!(o instanceof String)) {
				ss = false;
				break;
			}
		}
		
		if (ss) {
			String[] as = args.toArray(new String[args.size()]);
			return Classes.born(klass, new Object[] { as }, new Class[] { String[].class });
		}
		
		Object[] as = args.toArray(new Object[args.size()]);
		return Classes.born(klass, as);
	}

	protected Object getLoaderArgument(String a) {
		return a;
	}
	
	protected Class<? extends IocLoader> getAliasClass(String name) {
		return alias.get(name);
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends IocLoader> getLoaderClass(String className) {
		Class<? extends IocLoader> cls = getAliasClass(className);
		if (cls == null) {
			try {
				cls = (Class<? extends IocLoader>)Classes.getClass(className);
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
