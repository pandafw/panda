package panda.ioc.val;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Test;

import panda.ioc.IocMaking;
import panda.ioc.ValueProxy;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.DefaultValueProxyMaker;
import panda.ioc.loader.MapIocLoader;
import panda.ioc.loader.xml.meta.Pet;
import panda.ioc.meta.IocValue;

public class IocCustomizedValueTypeTest {

	public static class MyValueProxyMaker extends DefaultValueProxyMaker {
		public ValueProxy make(IocMaking ing, IocValue iv) {
			if ("cc".equalsIgnoreCase(iv.getType())) {
				final String v = ("CC:" + iv.getValue());
				return new ValueProxy() {
					public Object get(IocMaking ing) {
						return v;
					}
				};
			}
			return super.make(ing, iv);
		}

		public Set<String> supportedTypes() {
			Set<String> ss = super.supportedTypes();
			ss.add("cc");
			return ss;
		}
	}
	
	@Test
	public void test_simple_customized() {
		String json = "{xb:{name:{cc:'XiaoBai'}}}";
		DefaultIoc ioc = new DefaultIoc(new MapIocLoader(json));
		ioc.setValueProxyMaker(new MyValueProxyMaker());

		Pet pet = ioc.get(Pet.class, "xb");
		assertEquals("CC:XiaoBai", pet.getName());
	}

}
