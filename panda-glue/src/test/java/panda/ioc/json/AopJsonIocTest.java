package panda.ioc.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.ioc.IocLoader;
import panda.ioc.aop.impl.DefaultMirrorFactory;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.json.pojo.Mammal;
import panda.ioc.loader.JsonIocLoader;

public class AopJsonIocTest {

	@Test
	public void test_simple() {
		IocLoader il = new JsonIocLoader(AopJsonIocTest.class.getPackage().getName().replace('.', '/') + "/aop.js");
		DefaultIoc ioc = new DefaultIoc(il);
		ioc.setMirrorFactory(new DefaultMirrorFactory());
		StringBuilder sb = ioc.get(StringBuilder.class, "sb");
		Mammal fox = ioc.get(Mammal.class, "fox");

		assertEquals("Fox", fox.getName());
		assertEquals("B:getName0;A:getName0;", sb.toString());
		sb.delete(0, sb.length());
		fox.getName();
		fox.getName();
		assertEquals("B:getName0;A:getName0;B:getName0;A:getName0;", sb.toString());

	}
}
