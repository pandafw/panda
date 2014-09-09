package panda.mvc;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import panda.ioc.Ioc;
import panda.mvc.config.AtMap;

/**
 * 这个接口是一个抽象封装
 * <p>
 * 如果是通过 Servlet 方式加载的 Mvc， 只需要根据 ServletConfig 来实现一下这个接口 同理， Filter 方式，甚至不是标准的 JSP/Servlet
 * 容器，只要实现了这个接口，都可以 正常的调用 Loading 接口
 */
public interface MvcConfig {

	/**
	 * @return 当前应用的 IOC 容器实例
	 */
	Ioc getIoc();

	/**
	 * @return 当前应用的根路径
	 */
	String getAppRoot();

	/**
	 * @return 当前应用的名称
	 */
	String getAppName();

	/**
	 * 获取配置的参数
	 * 
	 * @param name 参数名
	 * @return 参数值
	 */
	String getInitParameter(String name);

	/**
	 * 获取配置参数的名称列表
	 * 
	 * @return 配置参数的名称列表
	 */
	List<String> getInitParameterNames();

	/**
	 * 获取配置的主模块，一般的说是存放在 initParameter 集合下的 "modules" 属性 值为一个 class 的全名
	 * 
	 * @return 配置的主模块，null - 如果没有定义这个参数
	 */
	Class<?> getMainModule();

	/**
	 * 在你的模块中通过 '@At' 声明的入口函数，可以存储在 AtMap 中，这个函数提供一个 AtMap 的实例
	 */
	AtMap getAtMap();

	/**
	 * 根据 MainModule 中的 '@LoadingBy' 得到一个加载逻辑的实现类
	 * 
	 * @return 加载逻辑
	 */
	Loading createLoading();

	/**
	 * 如果在非 JSP/SERVLET 容器内，这个函数不保证返回正确的结果
	 * 
	 * @return 当前应用的上下文对象
	 */
	ServletContext getServletContext();

	/**
	 * 加载时上下文包括环境变量，以及 "app.root" 等信息
	 * 
	 * @return 加载时上下文
	 */
	Map<String, Object> getLoadingContext();

}