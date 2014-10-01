package panda.mvc;

import java.util.LinkedHashMap;

/**
 * 这是一个处理 HTTP 请求的扩展点。通过它，你可以用任何你想要的方式来为你的入口函数准备参数。 
 * 你可以通过注解 '@AdaptBy' 来声明你的入口函数具体将采用哪个适配器，默认的 框架将采用 DefaultParamAdaptor 来适配参数。当然，你也可以声明你自己的适配方法
 * <p>
 * 你还需要知道的是：你每一个入口函数，框架都会为你建立一个新的适配器的实例。
 * <p>
 * <b>注意：</b>
 * <ul>
 * <li>如果你要写自己的实现类，这个类必须有一个public 的默认构造函数，如果你的构造函数需要参数，则必须是 String 类型的。参数的值由 注解 '@AdaptBy.args'
 * 来填充，它的默认值为 长度为0的空字符串数组。
 * <li>适配器对于一个 URL只会有一份实例。
 * <li>容器假定你的适配器不会占有什么不可释放的资源，必要的时候，它会允许垃圾回收器回收你的适配器实例
 * </ul>
 * 
 * !!! ThreadSafe !!!
 */
public interface ParamAdaptor {
	/**
	 * 这个函数将在你的适配器生命周期内，这个函数将只被调用一次。它用来告诉你的适配器，你需要适配什么方法。
	 */
	void init(MvcConfig config, ActionInfo ai);

	/**
	 * 你的适配器需要根据传入的 request 生成函数的调用参数
	 * 
	 * @param ac ActionContext
	 * @return 调用参数数组
	 */
	LinkedHashMap<String, Object> adapt(ActionContext ac);

}
