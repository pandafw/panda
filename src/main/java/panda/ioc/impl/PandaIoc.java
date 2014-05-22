package panda.ioc.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import panda.ioc.Ioc2;
import panda.ioc.IocContext;
import panda.ioc.IocException;
import panda.ioc.IocLoader;
import panda.ioc.IocLoading;
import panda.ioc.IocMaking;
import panda.ioc.ObjectMaker;
import panda.ioc.ObjectProxy;
import panda.ioc.ValueProxyMaker;
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

public class PandaIoc implements Ioc2 {

	private final Object lock_get = new Object();

	private static final Log log = Logs.getLog(PandaIoc.class);

	private static final String DEF_SCOPE = "app";

	/**
	 * 读取配置文件的 Loader
	 */
	private IocLoader loader;

	/**
	 * 缓存对象上下文环境
	 */
	private IocContext context;
	
	/**
	 * 装配对象的逻辑
	 */
	private ObjectMaker maker;
	
	/**
	 * 可扩展的"字段值"生成器
	 */
	private List<ValueProxyMaker> vpms;
	
	/**
	 * 反射工厂，封装 AOP 的逻辑
	 */
	private MirrorFactory mirrors;
	
	/**
	 * 对象默认生命周期范围名
	 */
	private String defaultScope;
	
	/**
	 * <ul>
	 * <li>缓存支持的对象值类型
	 * <li>如果 addValueProxyMaker() 被调用，这个缓存会被清空
	 * <li>createLoading() 将会检查这个缓存
	 * </ul>
	 */
	private Set<String> supportedTypes;

	public PandaIoc(IocLoader loader) {
		this(loader, new ScopeContext(DEF_SCOPE), DEF_SCOPE);
	}

	public PandaIoc(IocLoader loader, IocContext context, String defaultScope) {
		this(new DefaultObjectMaker(), loader, context, defaultScope);
	}

	protected PandaIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope) {
		this(maker, loader, context, defaultScope, null);
	}

	protected PandaIoc(ObjectMaker maker, IocLoader loader, IocContext context, String defaultScope, MirrorFactory mirrors) {
		this.maker = maker;
		this.defaultScope = defaultScope;
		this.context = context;
		if (loader instanceof CachedIocLoader) {
			this.loader = loader;
		}
		else {
			this.loader = CachedIocLoaderImpl.create(loader);
		}
		
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

	/**
	 * @return 一个新创建的 IocLoading 对象
	 */
	private IocLoading createLoading() {
		if (null == supportedTypes) {
			synchronized (this) {
				if (null == supportedTypes) {
					supportedTypes = new HashSet<String>();
					for (ValueProxyMaker maker : vpms) {
						String[] ss = maker.supportedTypes();
						if (ss != null) {
							for (String s : ss) {
								supportedTypes.add(s);
							}
						}
					}
				}
			}
		}
		return new IocLoading(supportedTypes);
	}

	public <T> T get(Class<T> type) throws IocException {
		String bn = AnnotationIocLoader.getBeanName(type);
		return get(type, bn);
	}

	public <T> T get(Class<T> type, String name, IocContext context) throws IocException {
		if (log.isDebugEnabled()) {
			log.debugf("Get '%s'<%s>", name, type);
		}
		
		// 创建对象创建时
		IocMaking ing = makeIocMaking(context, name);
		IocContext cntx = ing.getContext();

		// 从上下文缓存中获取对象代理
		ObjectProxy op = cntx.fetch(name);

		// 如果未发现对象
		if (null == op) {
			// 线程同步
			synchronized (lock_get) {
				// 再次读取
				op = cntx.fetch(name);

				// 如果未发现对象
				if (null == op) {
					try {
						if (log.isDebugEnabled()) {
							log.debug("\t >> Load definition");
						}
						
						// 读取对象定义
						IocObject iobj = loader.load(createLoading(), name);
						if (null == iobj) {
							for (String iocBeanName : loader.getName()) {
								// 相似性少于3 --> 大小写错误,1-2个字符调换顺序或写错
								if (3 > Texts.computeLevenshteinDistance(name.toLowerCase(), iocBeanName.toLowerCase())) {
									throw new IocException("Undefined object '%s' but found similar name '%s'", name,
										iocBeanName);
								}
							}
							throw new IocException("Undefined object '%s'", name);
						}

						// 修正对象类型
						if (null == iobj.getType()) {
							if (null == type) {
								throw new IocException("NULL TYPE object '%s'", name);
							}
							else {
								iobj.setType(type);
							}
						}
						
						// 检查对象级别
						if (Strings.isBlank(iobj.getScope())) {
							iobj.setScope(defaultScope);
						}

						// 根据对象定义，创建对象，maker 会自动的缓存对象到 context 中
						if (log.isDebugEnabled()) {
							log.debugf("\t >> Make...'%s'<%s>", name, type);
						}
						op = maker.make(ing, iobj);
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
		
		synchronized (lock_get) {
			return (T)op.get(type, ing);
		}
	}

	public <T> T get(Class<T> type, String name) {
		return this.get(type, name, null);
	}

	public boolean has(String name) {
		return loader.has(name);
	}

	private boolean deposed = false;

	public void depose() {
		if (deposed) {
			if (log.isWarnEnabled()) {
				log.info("Can't depose a Ioc twice!");
			}
			return;
		}
		context.depose();
		deposed = true;
		if (loader instanceof CachedIocLoader)
			((CachedIocLoader)loader).clear();
		if (log.isDebugEnabled())
			log.debug("!!!Ioc is deposed, you can't use it anymore");
	}

	public void reset() {
		context.clear();
		if (loader instanceof CachedIocLoader)
			((CachedIocLoader)loader).clear();
	}

	public String[] getNames() {
		return loader.getName();
	}

	public void addValueProxyMaker(ValueProxyMaker vpm) {
		vpms.add(0, vpm);// 优先使用最后加入的ValueProxyMaker
	}

	public IocContext getIocContext() {
		return context;
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

	/**
	 * 暴露IocMaking的创建过程
	 */
	protected IocMaking makeIocMaking(IocContext context, String name) {
		// 连接上下文
		IocContext cntx;
		if (null == context || context == this.context) {
			cntx = this.context;
		}
		else {
			if (log.isTraceEnabled()) {
				log.trace("Link contexts");
			}
			cntx = new ComboContext(context, this.context);
		}
		return new IocMaking(this, mirrors, cntx, maker, vpms, name);
	}

	@Override
	public String toString() {
		return Objects.toStringBuilder(this)
				.append("loader", loader)
				.toString();
	}
}
