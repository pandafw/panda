package panda.el.parse;

/**
 * 转换器接口.<br>
 * 负责对字符队列进行转意,将其零散的字符转换成有具体意义的对象.
 */
public interface Parse {
	/**
	 * 提取队列顶部元素<br>
	 * 特别注意,实现本方法的子程序只应该读取自己要转换的数据,不是自己要使用的数据一律不做取操作.<br>
	 * 一句话,只读取自己能转换的,自己不能转换的一律不操作.
	 * 
	 * @param exp 表达式
	 * @return 队列顶部元素
	 */
	Object fetchItem(CharQueue exp);

}
