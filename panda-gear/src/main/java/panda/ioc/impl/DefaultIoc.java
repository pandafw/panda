package panda.ioc.impl;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import panda.bean.Beans;
import panda.cast.Castors;
import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocLoader;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.ObjectWeaver;
import panda.ioc.Scope;
import panda.ioc.ValueMaker;
import panda.ioc.annotation.IocBean;
import panda.ioc.aop.Mirrors;
import panda.ioc.bean.IocProxy;
import panda.ioc.bean.IocProxyBeanHandler;
import panda.ioc.loader.AnnotationIocLoader;
import panda.ioc.loader.CachedIocLoader;
import panda.ioc.loader.CachedIocLoaderImpl;
import panda.ioc.meta.IocObject;
import panda.lang.Exceptions;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.Texts;
import panda.log.Log;
import panda.log.Logs;

public class DefaultIoc implements Ioc, Cloneable {

	private static final Log log = Logs.getLog(DefaultIoc.class);
	
	/**
	 * lock
	 */
	private Object lock = new Object();

	/**
	 * 对象默认生命周期范围名
	 */
	private String defaultScope;

	/**
	 * singleton object context
	 */
	private IocContext context;

	/**
	 * 读取配置文件的 Loader
	 */
	private IocLoader loader;
	
	/**
	 * 装配对象的逻辑
	 */
	private ObjectMaker objectMaker;

	/**
	 * Value Maker
	 */
	private ValueMaker valueMaker;

	/**
	 * weaver cache
	 */
	private Map<String, ObjectWeaver> weavers;

	/**
	 * 反射工厂，封装 AOP 的逻辑
	 */
	private Mirrors mirrors;

	private Beans beans;
	
	private Castors castors;

	/**
	 * deposed
	 */
	private boolean deposed = false;

	public DefaultIoc(IocLoader loader) {
		this(loader, new ScopeIocContext(Scope.APP), Scope.APP);
	}

	public DefaultIoc(IocLoader loader, IocContext context, String defaultScope) {
		this(loader, context, defaultScope, new DefaultObjectMaker());
	}

	protected DefaultIoc(IocLoader loader, IocContext context, String defaultScope, ObjectMaker objMaker) {
		this(loader, context, defaultScope, objMaker, null);
	}

	protected DefaultIoc(IocLoader loader, IocContext context, String defaultScope, ObjectMaker objMaker, Mirrors mirrors) {
		this.objectMaker = objMaker;
		this.defaultScope = defaultScope;
		this.context = context;
		this.loader = (loader instanceof CachedIocLoader ? loader : CachedIocLoaderImpl.create(loader));

		// Class Factory for AOP
		this.mirrors = (mirrors == null ? new Mirrors() : mirrors);
		
		valueMaker = new DefaultValueProxyMaker();
		weavers = new ConcurrentHashMap<String, ObjectWeaver>();

		// register IocProxyBeanHandler
		beans = Beans.i();
		beans.register(IocProxy.class, new IocProxyBeanHandler<IocProxy>(beans, IocProxy.class));

		castors = Castors.i();
	}

	public DefaultIoc(DefaultIoc ioc) {
		this.objectMaker = ioc.objectMaker;
		this.loader = ioc.loader;
		this.context = ioc.context;
		this.defaultScope = ioc.defaultScope;
		this.mirrors = ioc.mirrors;
		this.lock = ioc.lock;
		this.valueMaker = ioc.valueMaker;
		this.weavers = ioc.weavers;
		this.beans = ioc.beans;
		this.castors = ioc.castors;
	}

	@Override
	public boolean has(Class<?> type, String name) {
		if (Strings.isEmpty(name)) {
			name = getBeanName(type);
		}
		return has(name);
	}

	@Override
	public boolean has(Class<?> type) {
		String name = getBeanName(type);
		return has(name);
	}

	@Override
	public boolean has(String name) {
		// 从上下文缓存中获取对象代理
		ObjectProxy op = fetch(name);
		if (op != null) {
			return true;
		}

		return loader.has(name);
	}

	@Override
	public <T> T getIfExists(Class<T> type) throws IocException {
		return getIfExists(type, null);
	}

	@Override
	public <T> T getIfExists(Class<T> type, String name) throws IocException {
		if (Strings.isEmpty(name)) {
			name = getBeanName(type);
		}
		
		if (has(name)) {
			return get(type, name);
		}
		return null;
	}

	@Override
	public <T> T get(Class<T> type) throws IocException {
		return get(type, null);
	}

	@Override
	public <T> T get(Class<T> type, String name) throws IocException {
		if (Strings.isEmpty(name)) {
			name = getBeanName(type);
			if (Strings.isEmpty(name)) {
				throw new IocException("Missing name for " + type);
			}
		}

		if (log.isDebugEnabled()) {
			log.debugf("Get '%s'<%s>", name, type);
		}

		IocObject iobj = null;
		
		// 创建对象创建时
		IocMaking im = createIocMaking(name);

		// 从上下文缓存中获取对象代理
		ObjectProxy op = fetch(name);

		try {
			// 如果未发现对象
			if (op == null) {
				// 线程同步
				synchronized (lock) {
					// 再次读取
					op = fetch(name);

					// 如果未发现对象
					if (op == null) {
						if (log.isDebugEnabled()) {
							log.debug("\t >> Load definition");
						}
						
						// 读取对象定义
						iobj = loadIocObject(type, name);

						if (log.isDebugEnabled()) {
							log.debugf("\t >> Make...'%s'<%s>: <%s>", name, type, iobj.getType());
						}

						// 根据对象定义，创建对象，maker 会自动的缓存对象到 context 中
						if (iobj.isSingleton()) {
							op = objectMaker.makeSingleton(im, iobj);
						}
					}
				}
			}
			
			if (op == null) {
				op = objectMaker.makeDynamic(im, iobj);
			}
			
			return (T)op.get(type, im);
		}
		catch (IocException e) {
			throw e;
		}
		catch (Throwable e) {
			throw new IocException("Failed to getBean(" + type + ", " + name + ")", Exceptions.unwrapThrow(e));
		}
	}

	protected ObjectProxy fetch(String name) {
		return context.fetch(name);
	}
	
	protected String getBeanName(Class<?> type) {
		return AnnotationIocLoader.getBeanName(type, type.getAnnotation(IocBean.class));
	}
	
	protected IocMaking createIocMaking(String name) {
		return new DefaultIocMaking(name, this);
	}

	protected IocObject loadIocObject(Class<?> type, String name) {
		IocObject iobj = loader.load(name);
		if (iobj == null) {
			for (String iocBeanName : loader.getNames()) {
				// 相似性少于3 --> 大小写错误,1-2个字符调换顺序或写错
				if (3 > Texts.computeLevenshteinDistance(name.toLowerCase(), iocBeanName.toLowerCase())) {
					throw new IocException(String.format("Undefined object '%s' but found similar name '%s'", name,
						iocBeanName));
				}
			}
			throw new IocException("Undefined object '" + name + "'");
		}

		// 检查对象级别
		if (Strings.isEmpty(iobj.getScope())) {
			iobj.setScope(defaultScope);
		}

		// 修正对象类型
		if (iobj.getType() == null) {
			if (type == null || type.equals(Object.class)) {
				throw new IocException(String.format("NULL TYPE object '%s'", name));
			}
			iobj = iobj.clone();
			iobj.setType(type);
		}

		return iobj;
	}

	@Override
	public void depose() {
		if (deposed) {
			if (log.isWarnEnabled()) {
				log.info("Can't depose a Ioc twice!");
			}
			return;
		}
		
		context.depose();
		deposed = true;
		if (loader instanceof CachedIocLoader) {
			((CachedIocLoader)loader).clear();
		}

		if (log.isDebugEnabled()) {
			log.debug("!!!Ioc is deposed, you can't use it anymore");
		}
	}

	@Override
	public void reset() {
		context.clear();
		if (loader instanceof CachedIocLoader) {
			((CachedIocLoader)loader).clear();
		}
	}

	@Override
	public Set<String> getNames() {
		return loader.getNames();
	}

	/**
	 * @return the context
	 */
	@Override
	public IocContext getContext() {
		return context;
	}

	/**
	 * @param context the context to set
	 */
	public void setContext(IocContext context) {
		this.context = context;
	}

	/**
	 * @return the defaultScope
	 */
	public String getDefaultScope() {
		return defaultScope;
	}

	/**
	 * @param defaultScope the defaultScope to set
	 */
	public void setDefaultScope(String defaultScope) {
		this.defaultScope = defaultScope;
	}

	/**
	 * @return the objectMaker
	 */
	public ObjectMaker getObjectMaker() {
		return objectMaker;
	}

	/**
	 * @param objectMaker the objectMaker to set
	 */
	public void setObjectMaker(ObjectMaker objectMaker) {
		this.objectMaker = objectMaker;
	}

	/**
	 * @return the valueMaker
	 */
	public ValueMaker getValueMaker() {
		return valueMaker;
	}

	/**
	 * @param valueMaker the valueMaker to set
	 */
	public void setValueMaker(ValueMaker valueMaker) {
		this.valueMaker = valueMaker;
	}

	/**
	 * @return the weavers
	 */
	public Map<String, ObjectWeaver> getWeavers() {
		return weavers;
	}

	/**
	 * @param weavers the weavers to set
	 */
	public void setWeavers(Map<String, ObjectWeaver> weavers) {
		this.weavers = weavers;
	}

	/**
	 * @return the mirrors
	 */
	public Mirrors getMirrors() {
		return mirrors;
	}

	/**
	 * @param mirrors the mirrors to set
	 */
	public void setMirrors(Mirrors mirrors) {
		this.mirrors = mirrors;
	}

	/**
	 * @return the beans
	 */
	public Beans getBeans() {
		return beans;
	}

	/**
	 * @param beans the beans to set
	 */
	public void setBeans(Beans beans) {
		this.beans = beans;
	}

	/**
	 * @return the castors
	 */
	public Castors getCastors() {
		return castors;
	}

	/**
	 * @param castors the castors to set
	 */
	public void setCastors(Castors castors) {
		this.castors = castors;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("loader", loader)
				.toString();
	}
}
