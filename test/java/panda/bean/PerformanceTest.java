package panda.bean;

import panda.bean.BeanHandler;
import panda.bean.Beans;
import panda.bean.FastBeans;
import panda.lang.time.StopWatch;
import panda.log.Log;
import panda.log.Logs;
import junit.framework.TestCase;


/**
 */
public class PerformanceTest extends TestCase {

	private static Log log = Logs.getLog(PerformanceTest.class);

	private static final int PCOUNT = 10;
	private static final int ECOUNT = 5000000;
	
	/**
	 */
	public void testDirect() {
		for (int i = 0; i < PCOUNT; i++) {
			TestA a = new TestA();
			a.isBoolField();
			a.setBoolField(true);
		}

		StopWatch watch = new StopWatch(true);
		for (int i = 0; i < ECOUNT; i++) {
			TestA a = new TestA();
			a.isBoolField();
			a.setBoolField(true);
		}
		log.info("testDirect took " + watch);
	}

	/**
	 */
	@SuppressWarnings("unchecked")
	public void testJavaBeanHandler() {
		Beans.setMe(new Beans());
		BeanHandler bh = Beans.me().getBeanHandler(TestA.class);
		
		for (int i = 0; i < PCOUNT; i++) {
			Object a = bh.createObject();
			bh.getBeanValue(a, "boolField");
			bh.setBeanValue(a, "boolField", false);
		}

		StopWatch watch = new StopWatch(true);
		for (int i = 0; i < ECOUNT; i++) {
			Object a = bh.createObject();
			bh.getBeanValue(a, "boolField");
			bh.setBeanValue(a, "boolField", false);
		}
		log.info("testJavaBeanHandler took " + watch);
	}

	/**
	 */
	@SuppressWarnings("unchecked")
	public void testFastBeanHandler() {

		Beans.setMe(new FastBeans());
		BeanHandler bh = Beans.me().getBeanHandler(TestA.class);
		
		for (int i = 0; i < PCOUNT; i++) {
			Object a = bh.createObject();
			bh.getBeanValue(a, "boolField");
			bh.setBeanValue(a, "boolField", false);
		}

		StopWatch watch = new StopWatch(true);
		for (int i = 0; i < ECOUNT; i++) {
			Object a = bh.createObject();
			bh.getBeanValue(a, "boolField");
			bh.setBeanValue(a, "boolField", false);
		}
		log.info("testFastBeanHandler took " + watch);
	}
}
