package panda.ioc.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import panda.ioc.Ioc;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.IocMaking;
import panda.ioc.IocLoadException;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.Scope;
import panda.ioc.ValueProxyMaker;
import panda.ioc.annotation.IocBean;
import panda.ioc.aop.MirrorFactory;
import panda.ioc.aop.impl.DefaultMirrorFactory;
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
	private ObjectMaker maker;

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
	 * 可扩展的"字段值"生成器
	 */
	private List<ValueProxyMaker> vpms;

	/**
	 * helper class for IocLoad
	 */
	private IocLoading loading;

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

	protected DefaultIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope, MirrorFactory mirrors) {
		this.lock = new Object();
		this.maker = maker;
		this.defaultScope = defaultScope;
		this.context = context;
		if (loader instanceof CachedIocLoader) {
			this.loader = loader;
		}
		else {
			this.loader = CachedIocLoaderImpl.create(loader);
		}
		this.loading = new IocLoading();
		
		vpms = new ArrayList<ValueProxyMaker>(5); // 预留五个位置，足够了吧
		addValueProxyMaker(new DefaultValueProxyMaker());

		// 初始化类工厂， 这是同 AOP 的连接点
		if (mirrors == null) {
			this.mirrors = new DefaultMirrorFactory(this);
		}
		else {
			this.mirrors = mirrors;
		}
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
		if (null == op) {
			// 线程同步
			synchronized (lock) {
				// 再次读取
				op = context.fetch(name);

				// 如果未发现对象
				if (null == op) {
					try {
						if (log.isDebugEnabled()) {
							log.debug("\t >> Load definition");
						}
						
						// 读取对象定义
						IocObject iobj = loader.load(loading, name);
						if (null == iobj) {
							for (String iocBeanName : loader.getNames()) {
								// 相似性少于3 --> 大小写错误,1-2个字符调换顺序或写错
								if (3 > Texts.computeLevenshteinDistance(name.toLowerCase(), iocBeanName.toLowerCase())) {
									throw new IocException("Undefined object '%s' but found similar name '%s'", name,
										iocBeanName);
								}
							}
							throw new IocLoadException("Undefined object '" + name + "'");
						}

						// 修正对象类型
						if (null == iobj.getType()) {
							if (null == type) {
								throw new IocException("NULL TYPE object '%s'", name);
							}
							iobj.setType(type);
						}
						
						// 检查对象级别
						if (Strings.isBlank(iobj.getScope())) {
							iobj.setScope(defaultScope);
						}

						// 根据对象定义，创建对象，maker 会自动的缓存对象到 context 中
						if (log.isDebugEnabled()) {
							log.debugf("\t >> Make...'%s'<%s>", name, type);
						}
						op = maker.make(imk, iobj);
					}
					// 处理异常
					catch (IocException e) {
						throw e;
					}
					catch (Throwable e) {
						throw new IocException(Exceptions.unwrapThrow(e), "For object [%s] - type:[%s]", name, type);
					}
				}
			}
		}
		
		synchronized (lock) {
			return (T)op.get(type, imk);
		}
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

	public void addValueProxyMaker(ValueProxyMaker vpm) {
		synchronized (lock) {
			vpms.add(0, vpm);// 优先使用最后加入的ValueProxyMaker
			loading.getSupportedTypes().addAll(Arrays.asList(vpm.supportedTypes()));
		}
	}

	public IocContext getContext() {
		return context;
	}
	
	public void setContext(IocContext context) {
		this.context = context;
	}

	public void setMaker(ObjectMaker maker) {
		this.maker = maker;
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
		return new IocMaking(this, mirrors, maker, vpms, name);
	}

	@Override
	public DefaultIoc clone() {
		DefaultIoc ni = new DefaultIoc();

		ni.maker = this.maker;
		ni.loader = this.loader;
		ni.context = this.context;
		ni.defaultScope = this.defaultScope;
		ni.mirrors = this.mirrors;
		ni.lock = this.lock;
		ni.vpms = this.vpms;
		ni.loading = this.loading;
		
		return ni;
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder()
				.append("loader", loader)
				.toString();
	}
}
