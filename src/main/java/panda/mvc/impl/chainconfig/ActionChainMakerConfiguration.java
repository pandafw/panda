package panda.mvc.impl.chainconfig;

import java.util.List;

/**
 * DefaultActionChainMaker内部使用的接口,用于读取配置文件
 */
public interface ActionChainMakerConfiguration {

	public List<String> getProcessors(String key);

	public String getErrorProcessor(String key);
}
