package panda.mvc;

/**
 * 整个应用启动以及关闭的时候需要做的额外逻辑
 */
public interface Setup {

	/**
	 * 启动时，额外逻辑
	 */
	void initialize();

	/**
	 * 关闭时，额外逻辑
	 */
	void destroy();
}
