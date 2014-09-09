package panda.mvc.adaptor;

import java.util.Set;

/**
 * 参数提取器
 */
public interface ParamEjector {
	/**
	 * 根据名称提取值
	 */
	public Object eject(String name);

	/**
	 * 键
	 */
	public Set<String> keys();
}
