package panda.bean;




/**
 * FastBeanHandlerTest
 */
public class FastBeanHandlerTest extends JavaBeanHandlerTest {

	private static Beans bhf = new FastBeans();
	
	protected Beans getBeans() {
		return bhf;
	}
	
}
