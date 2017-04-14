package panda.ioc.aop.config.impl;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.aop.impl.DefaultMirrorFactory;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.JsonIocLoader;

public class JsonAopConfigrationTest {

	@Test
	public void test_jsonAop() {
		DefaultIoc ioc = new DefaultIoc(new JsonIocLoader(JsonAopConfigrationTest.class.getPackage().getName().replace('.', '/') + "/jsonfile-aop.js"));
		ioc.setMirrorFactory(new DefaultMirrorFactory(ioc));
		test(ioc);
	}

	@Test
	public void test_jsonAop2() {
		DefaultIoc ioc = new DefaultIoc(new JsonIocLoader(JsonAopConfigrationTest.class.getPackage().getName().replace('.', '/') + "/jsonfile-aop2.js"));
		ioc.setMirrorFactory(new DefaultMirrorFactory(ioc));
		test(ioc);
	}

	private void test(Ioc ioc) {
		Assert.assertTrue(ioc.getNames().size() > 0);

		for (String name : ioc.getNames()) {
			ioc.get(null, name);
		}
		MyMI mi = ioc.get(MyMI.class, "myMI");
		assertTrue(mi.getTime() == 0);
		Pet2 pet2 = ioc.get(Pet2.class, "pet2");
		pet2.sing();
		assertTrue(mi.getTime() == 1);
		pet2.sing();
		assertTrue(mi.getTime() == 2);
	}
}
