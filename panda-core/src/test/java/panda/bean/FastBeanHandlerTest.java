package panda.bean;

import panda.bean.fast.FastBeans;




/**
 * FastBeanHandlerTest
 */
public class FastBeanHandlerTest extends JavaBeanHandlerTest {

	private static Beans beans = new FastBeans();
	
	@Override
	@SuppressWarnings("unchecked")
	protected BeanHandler getBeanHandler(Class type) {
		return beans.getBeanHandler(type);
	}
	
}
