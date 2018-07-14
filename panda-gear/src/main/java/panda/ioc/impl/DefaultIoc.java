package panda.ioc.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import panda.bean.Beans;
import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.ObjectWeaver;
import panda.ioc.Scope;
import panda.ioc.ValueProxyMaker;
import panda.ioc.annotation.IocBean;
import panda.ioc.aop.MirrorFactory;
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
	 * 装配对象的逻辑
	 */
	private ObjectMaker objMaker;

	/**
	 * 读取配置文件的 Loader
	 */
	private IocLoader loader;

	/**
	 * 缓存对象上下文环境
	 */
	private IocContext context;
	
	/**
	 * 对象默认生命周期范围名
	 */
	private String defaultScope;
	
	/**
	 * 反射工厂，封装 AOP 的逻辑
	 */
	private MirrorFactory mirrors;

	//-----------------------------------------------------------
	// internal use
	//
	/**
	 * lock
	 */
	private Object lock;
	
	/**
	 * Value Proxy Maker
	 */
	private ValueProxyMaker vpMaker;

	/**
	 * weaver cache
	 */
	private Map<String, ObjectWeaver> weavers;
	
	/**
	 * deposed
	 */
	private boolean deposed = false;

	private DefaultIoc() {
	}

	public DefaultIoc(IocLoader loader) {
		this(loader, new ScopeIocContext(Scope.APP), Scope.APP);
	}

	public DefaultIoc(IocLoader loader, IocContext context, String defaultScope) {
		this(new DefaultObjectMaker(), loader, context, defaultScope);
	}

	protected DefaultIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope) {
		this(maker, loader, context, defaultScope, null);
	}

	protected DefaultIoc(ObjectMaker objMaker, IocLoader loader, IocContext context, String defaultScope, MirrorFactory mirrors) {
		// register IocProxyBeanHandler
		Beans beans = Beans.i();
		beans.register(IocProxy.class, new IocProxyBeanHandler<IocProxy>(beans, IocProxy.class));

		this.lock = new Object();
		this.objMaker = objMaker;
		this.defaultScope = defaultScope;
		this.context = context;
		this.loader = (loader instanceof CachedIocLoader ? loader : CachedIocLoaderImpl.create(loader));
		
		setValueProxyMaker(new DefaultValueProxyMaker());
		weavers = new HashMap<String, ObjectWeaver>();

		// Class Factory for AOP
		this.mirrors = (mirrors == null ? new MirrorFactory() : mirrors);
	}

	public <T> T getIfExists(Class<T> type) throws IocException {
		if (has(type)) {
			return get(type);
		}
		return null;
	}

	public <T> T getIfExists(Class<T> type, String name) throws IocException {
		if (name == null) {
			name = getBeanName(type);
		}
		
		if (has(name)) {
			return get(type, name);
		}
		return null;
	}
	
	public <T> T get(Class<T> type) throws IocException {
		return get(type, null);
	}

	public <T> T get(Class<T> type, String name) throws IocException {
		if (name == null) {
			name = getBeanName(type);
		}

		if (log.isDebugEnabled()) {
			log.debugf("Get '%s'<%s>", name, type);
		}
		
		// 创建对象创建时
		IocMaking imk = makeIocMaking(name);

		// 从上下文缓存中获取对象代理
		ObjectProxy op = context.fetch(name);

		// 如果未发现对象
		if (op == null) {
			// 线程同步
			synchronized (lock) {
				// 再次读取
				op = context.fetch(name);

				// 如果未发现对象
				if (op == null) {
					try {
						if (log.isDebugEnabled()) {
							log.debug("\t >> Load definition");
						}
						
						// 读取对象定义
						IocObject iobj = loader.load(name);
						if (iobj == null) {
							for (String iocBeanName : loader.getNames()) {
								// 相似性少于3 --> 大小写错误,1-2个字符调换顺序或写错
								if (3 > Texts.computeLevenshteinDistance(name.toLowerCase(), iocBeanName.toLowerCase())) {
									throw new IocException(String.format("Undefined object '%s' but found similar name '%s'", name,
										iocBeanName));
								}
							}
							throw new IocLoadException("Undefined object '" + name + "'");
						}
						
						// 检查对象级别
						if (Strings.isBlank(iobj.getScope())) {
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

						if (log.isDebugEnabled()) {
							log.debugf("\t >> Make...'%s'<%s>: <%s>", name, type, iobj.getType());
						}

						// 根据对象定义，创建对象，maker 会自动的缓存对象到 context 中
						op = objMaker.make(imk, iobj);
					}
					catch (IocException e) {
						throw e;
					}
					catch (Throwable e) {
						throw new IocException(String.format("For object [%s] - type:[%s]", name, type), Exceptions.unwrapThrow(e));
					}
				}
			}
		}
		
		synchronized (lock) {
			return (T)op.get(type, imk);
		}
	}

	public boolean has(Class<?> type, String name) {
		if (Strings.isEmpty(name)) {
			name = getBeanName(type);
		}
		return has(name);
	}

	public boolean has(Class<?> type) {
		String name = getBeanName(type);
		return has(name);
	}

	public boolean has(String name) {
		// 从上下文缓存中获取对象代理
		ObjectProxy op = context.fetch(name);
		if (op != null) {
			return true;
		}

		return loader.has(name);
	}

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

	public void reset() {
		context.clear();
		if (loader instanceof CachedIocLoader) {
			((CachedIocLoader)loader).clear();
		}
	}

	public Set<String> getNames() {
		return loader.getNames();
	}

	public void setValueProxyMaker(ValueProxyMaker vpm) {
		synchronized (lock) {
			vpMaker = vpm;
		}
	}

	public IocContext getContext() {
		return context;
	}
	
	public void setContext(IocContext context) {
		this.context = context;
	}

	public void setObjMaker(ObjectMaker maker) {
		this.objMaker = maker;
	}

	public void setMirrorFactory(MirrorFactory mirrors) {
		this.mirrors = mirrors;
	}

	public void setDefaultScope(String defaultScope) {
		this.defaultScope = defaultScope;
	}

	protected String getBeanName(Class<?> type) {
		return AnnotationIocLoader.getBeanName(type, type.getAnnotation(IocBean.class));
	}
	
	protected IocMaking makeIocMaking(String name) {
		return new IocMaking(name, this, mirrors, objMaker, vpMaker, weavers);
	}

	@Override
	public DefaultIoc clone() {
		DefaultIoc ni = new DefaultIoc();

		ni.objMaker = this.objMaker;
		ni.loader = this.loader;
		ni.context = this.context;
		ni.defaultScope = this.defaultScope;
		ni.mirrors = this.mirrors;
		ni.lock = this.lock;
		ni.vpMaker = this.vpMaker;
		ni.weavers = this.weavers;
		
		return ni;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("loader", loader)
				.toString();
	}
}
