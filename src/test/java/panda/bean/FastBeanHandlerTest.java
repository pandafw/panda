package panda.bean;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.FastBeans;



/**
 * FastBeanHandlerTest
 */
@SuppressWarnings("unchecked")
public class FastBeanHandlerTest extends JavaBeanHandlerTest {

	private static Beans bhf = new FastBeans();
	
	protected BeanHandler getBeanHandler(Class type) {
		return bhf.getBeanHandler(type);
	}
}
