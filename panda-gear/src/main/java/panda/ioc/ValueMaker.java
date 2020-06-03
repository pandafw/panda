package panda.ioc;

import panda.ioc.meta.IocValue;

/**
 * 本接口设计用来提供容器支持更多种类的值的类型。
 * <p>
 * 你可以通过 Ioc 接口，增加你自定义的 ValueProxyMaker，你自定义的 ValueProxyMaker 会 比容器内内置的 ValueProxyMaker 更优先。即，后加入优先
 * 
 */
public interface ValueMaker {
	/**
	 * @param im 对象装配时
	 * @param iv 字段装配信息
	 * @return 值代理对象
	 */
	ValueProxy make(IocMaking im, IocValue iv);

	/**
	 * @param im 对象装配时
	 * @param ivs 字段装配信息
	 * @return 值代理对象
	 */
	ValueProxy make(IocMaking im, IocValue[] ivs);
}
