package panda.ioc.json;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocLoader;
import panda.ioc.impl.PandaIoc;
import panda.ioc.json.pojo.Mammal;
import panda.ioc.loader.JsonIocLoader;

public class AopJsonIocTest {

	@Test
	public void test_simple() {
		IocLoader il = new JsonIocLoader(AopJsonIocTest.class.getPackage().getName().replace('.', '/') + "/aop.js");
		Ioc ioc = new PandaIoc(il);
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
