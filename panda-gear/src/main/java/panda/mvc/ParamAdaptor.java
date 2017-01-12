package panda.mvc;


/**
 * 这是一个处理 HTTP 请求的扩展点。通过它，你可以用任何你想要的方式来为你的入口函数准备参数。 
 * 你可以通过注解 '@AdaptBy' 来声明你的入口函数具体将采用哪个适配器，默认的 框架将采用 DefaultParamAdaptor 来适配参数。当然，你也可以声明你自己的适配方法
 * <br>
 * <b>适配器的生成顺序</b>
 * <ul>
 * <li>首先通过Ioc.get(type)来生成适配器。
 * <li>Ioc生成失败的话，通过Classes.born(type)。
 * </ul>
 * 
 * <br>Note: If the adaptor has only 1 instance, the adaptor should be Thread Safe !
 * 
 */
public interface ParamAdaptor {
	public static final char ORIGINAL = 'o';
	public static final char STRIP_TO_NULL = 'N';
	public static final char STRIP_TO_EMPTY = 'E';
	public static final char TRIM_TO_NULL = 'n';
	public static final char TRIM_TO_EMPTY = 'e';

	/**
	 * @param ac ActionContext
	 */
	void adapt(ActionContext ac);
}
