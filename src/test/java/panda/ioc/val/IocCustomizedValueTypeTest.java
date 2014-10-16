package panda.ioc.val;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.ValueProxyMaker;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.MapIocLoader;
import panda.ioc.loader.xml.meta.Pet;
import panda.ioc.meta.IocValue;
import panda.lang.Arrays;

public class IocCustomizedValueTypeTest {

	@Test
	public void test_simple_customized() {
		String json = "{xb:{name:{cc:'XiaoBai'}}}";
		DefaultIoc ioc = new DefaultIoc(new MapIocLoader(json));
		ioc.addValueProxyMaker(new ValueProxyMaker() {
			public ValueProxy make(IocMaking ing, IocValue iv) {
				if ("cc".equalsIgnoreCase(iv.getType())) {
					final String v = ("CC:" + iv.getValue());
					return new ValueProxy() {
						public Object get(IocMaking ing) {
							return v;
						}
					};
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
