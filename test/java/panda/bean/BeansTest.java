package panda.bean;

import java.util.ArrayList;
import java.util.HashMap;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.handlers.ListBeanHandler;
import panda.bean.handlers.MapBeanHandler;
import junit.framework.TestCase;

public class BeansTest extends TestCase {
	
	public void testBeanHandler() throws Exception {
		Beans bhf = new Beans();
		
		BeanHandler bh = bhf.getBeanHandler(HashMap.class);
		assertEquals(bh.getClass(), MapBeanHandler.class);

		bh = bhf.getBeanHandler(ArrayList.class);
		assertEquals(bh.getClass(), ListBeanHandler.class);
	}
}
