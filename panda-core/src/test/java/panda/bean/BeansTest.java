package panda.bean;

import java.util.ArrayList;
import java.util.HashMap;

import panda.bean.handler.ListBeanHandler;
import panda.bean.handler.MapBeanHandler;

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
