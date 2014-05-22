package panda.ioc.json;

import static org.junit.Assert.*;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.impl.PandaIoc;
import panda.ioc.loader.MapIocLoader;

public class RecurReferJsonIocTest {

	public static class RA {

		public String nm;

		public RB rb;

	}

	public static class RB {

		public String nm;

		public RA ra;

	}

	@Test
	public void test_refer_each_other() {
		String s = "{";
		s += "a:{type:'" + RA.class.getName() + "',";
		s += "fields:{nm:'A', rb:{ref:'b'}}";
		s += "},";
		s += "b:{type:'" + RB.class.getName() + "',";
		s += "fields:{nm:'B', ra:{ref:'a'}}";
		s += "}";
		s += "}";

		Ioc ioc = new PandaIoc(new MapIocLoader(s));
		RA a = ioc.get(RA.class, "a");
		assertEquals("A", a.nm);
		assertEquals("B", a.rb.nm);

		RB b = ioc.get(RB.class, "b");
		assertEquals("A", b.ra.nm);
		assertEquals("B", b.nm);
	}
}
