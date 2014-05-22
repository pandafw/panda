package panda.ioc.val;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import panda.ioc.Ioc2;
import panda.ioc.IocContext;
import panda.ioc.annotation.Bean;
import panda.ioc.impl.ComboContext;
import panda.ioc.impl.PandaIoc;
import panda.ioc.impl.ScopeContext;
import panda.ioc.loader.MapIocLoader;

public class DefaultValueTypes {

	@Bean("obj")
	public static class TestReferContext {
		IocContext ic;
	}

	@Test
	public void test_refer_context() {
		IocContext context = new ScopeContext("abc");
		String json = "{obj:{singleton:false,fields:{ic:{ref:'$conText'}}}}";
		Ioc2 ioc = new PandaIoc(new MapIocLoader(json), context, "abc");
		TestReferContext trc = ioc.get(TestReferContext.class);
		assertTrue(context == trc.ic);

		IocContext context2 = new ScopeContext("rrr");
		trc = ioc.get(TestReferContext.class, "obj", context2);
		assertTrue(trc.ic instanceof ComboContext);
	}
}
