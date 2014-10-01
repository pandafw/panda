package panda.ioc.json;

import static org.junit.Assert.assertEquals;
import static panda.ioc.json.Utils.I;
import static panda.ioc.json.Utils.J;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.IocException;
import panda.ioc.ObjectProxy;
import panda.ioc.impl.ComboIocContext;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.impl.ScopeIocContext;
import panda.ioc.json.pojo.Animal;

public class ScopeJsonIocTest {

	@Test
	public void test_simple_scope() {
		DefaultIoc ioc = I(J("f1", "scope:'app',fields:{name:'F1'}"), J("f2", "scope:'MyScope',fields:{name:'F2'}"));

		Animal f1 = ioc.get(Animal.class, "f1");
		assertEquals("F1", f1.getName());

		ScopeIocContext ic = (ScopeIocContext)ioc.getContext();
		Map<String, ObjectProxy> map = ic.getObjs();
		assertEquals(1, map.size());
	}

	@Test
	public void test_error_scope() {
		DefaultIoc ioc = I(J("f1", "scope:'app',fields:{name:'F1'}"), J("f2", "scope:'MyScope',fields:{name:'F2'}"));

		Animal f1 = ioc.get(Animal.class, "f1");
		assertEquals("F1", f1.getName());

		try {
			ioc.get(Animal.class, "f2");
			Assert.fail();
		}
		catch (IocException e) {
			assertEquals("Failed to save 'f2' to MyScope IocContext", e.getMessage());
		}
	}

	@Test
	public void test_refer_from_diffenent_scope() {
		DefaultIoc ioc = I(J("f1", "type : '" + Animal.class.getName() + "' , scope:'app',fields:{name:'F1'}"),
			J("f2", "type : '" + Animal.class.getName() + "' , scope:'MyScope',fields:{another:{ref : 'f3'}}"),
			J("f3", "type : '" + Animal.class.getName() + "' , scope:'MyScope',fields:{another:{ref : 'f1'}}"));

		ComboIocContext cic = new ComboIocContext(ioc.getContext(), new ScopeIocContext("MyScope"));
		ioc.setContext(cic);
		
		Animal f2 = ioc.get(Animal.class, "f2");
		Animal f3 = ioc.get(Animal.class, "f3");
		
		assertEquals(f2.getAnother(), f3);
	}

}
