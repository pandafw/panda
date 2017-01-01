package panda.mvc;

import panda.mvc.impl.ActionInvoker;

/**
 * 路径映射
 */
public interface UrlMapping {

	/**
	 * 增加一个映射
	 * 
	 * @param maker 处理器工厂
	 * @param ai 处理器配置
	 */
	void add(ActionChainMaker maker, ActionInfo ai, MvcConfig config);

	/**
	 * 根据一个路径，获取一个动作链的调用者，并且，如果这个路径中包括统配符 '?' 或者 '*' <br>
	 * 需要为上下文对象设置好路径参数
	 * 
	 * @param ac 上下文对象
	 * @return 动作链的调用者
	 */
	ActionInvoker getActionInvoker(ActionContext ac);

	/**
	 * find action info by path
	 * 
	 * @param path path
	 * @return action info
	 */
	ActionInfo getActionInfo(String path);
}
