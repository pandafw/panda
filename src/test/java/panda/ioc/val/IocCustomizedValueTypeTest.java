package panda.ioc.val;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.ioc.Ioc2;
import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.MapIocLoader;
import panda.ioc.loader.xml.meta.Pet;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;

public class IocCustomizedValueTypeTest {

	@Test
	public void test_simple_customized() {
		String json = "{xb:{name:{cc:'XiaoBai'}}}";
		Ioc2 ioc = new PandaIoc(new MapIocLoader(json));
		ioc.addValueProxyMaker(new ValueProxyMaker() {
			public ValueProxy make(IocMaking ing, IocValue iv) {
				if ("cc".equalsIgnoreCase(iv.getType())) {
					return new StaticValue("CC:" + iv.getValue());
				}
				return null;
			}

			public String[] supportedTypes() {
				return Arrays.toArray("cc");
			}
		});

		Pet pet = ioc.get(Pet.class, "xb");
		assertEquals("CC:XiaoBai", pet.getName());
	}

}
