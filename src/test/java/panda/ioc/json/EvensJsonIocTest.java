package panda.ioc.json;

import static org.junit.Assert.assertEquals;
import static panda.ioc.json.Utils.I;
import static panda.ioc.json.Utils.J;

import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.json.pojo.Animal;
import panda.ioc.json.pojo.WhenCreateAnimal;
import panda.ioc.json.pojo.WhenCreateFox;
import panda.ioc.json.pojo.WhenDeposeAnimal;
import panda.ioc.json.pojo.WhenFetchAnimal;
import panda.ioc.loader.JsonIocLoader;

public class EvensJsonIocTest {

	@Test
	public void test_init_with_field() {
		String s = "fields: {name:'Fox'},";
		s = s + "\nevents:{";
		s = s + "\n    create: '" + WhenCreateFox.class.getName() + "'";
		s = s + "\n}";
		Ioc ioc = I(J("fox", s));

		Animal fox = ioc.get(Animal.class, "fox");
		assertEquals("$Fox", fox.getName());
	}

	@Test
	public void test_events_for_singleton() {
		String s = "fields: {name:'Fox'},";
		s = s + "\nevents:{";
		s = s + "\n    fetch: 'onFetch',";
		s = s + "\n    create: 'onCreate',";
		s = s + "\n    depose: 'onDepose'";
		s = s + "\n}";
		Ioc ioc = I(J("fox", s));

		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.get(Animal.class, "fox");

		assertEquals(1, f.getCreateTime());
		assertEquals(2, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.reset();
		assertEquals(1, f.getCreateTime());
		assertEquals(2, f.getFetchTime());
		assertEquals(1, f.getDeposeTime());
	}

	@Test
	public void test_events_for_un_singleton() {
		String s = "singleton:false, fields: {name:'Fox'},";
		s = s + "\nevents:{";
		s = s + "\n    fetch: 'onFetch',";
		s = s + "\n    create: 'onCreate',";
		s = s + "\n    depose: 'onDepose'";
		s = s + "\n}";
		Ioc ioc = I(J("fox", s));

		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.get(Animal.class, "fox");

		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.reset();
		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());
	}

	@Test
	public void test_events_by_trigger_for_singleton() {
		String s = "fields: {name:'Fox'},";
		s = s + "\nevents:{";
		s = s + "\n    fetch: '" + WhenFetchAnimal.class.getName() + "',";
		s = s + "\n    create: '" + WhenCreateAnimal.class.getName() + "',";
		s = s + "\n    depose: '" + WhenDeposeAnimal.class.getName() + "'";
		s = s + "\n}";
		Ioc ioc = I(J("fox", s));

		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(10, f.getCreateTime());
		assertEquals(10, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.get(Animal.class, "fox");

		assertEquals(10, f.getCreateTime());
		assertEquals(20, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.reset();
		assertEquals(10, f.getCreateTime());
		assertEquals(20, f.getFetchTime());
		assertEquals(10, f.getDeposeTime());
	}

	@Test
	public void test_events_by_trigger_for_un_singleton() {
		String s = "singleton:false, fields: {name:'Fox'},";
		s = s + "\nevents:{";
		s = s + "\n    fetch: '" + WhenFetchAnimal.class.getName() + "',";
		s = s + "\n    create: '" + WhenCreateAnimal.class.getName() + "',";
		s = s + "\n    depose: '" + WhenDeposeAnimal.class.getName() + "'";
		s = s + "\n}";
		Ioc ioc = I(J("fox", s));

		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(10, f.getCreateTime());
		assertEquals(10, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.get(Animal.class, "fox");

		assertEquals(10, f.getCreateTime());
		assertEquals(10, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.reset();
		assertEquals(10, f.getCreateTime());
		assertEquals(10, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());
	}

	@Test
	public void test_event_from_parent() {
		Ioc ioc = new DefaultIoc(new JsonIocLoader(this.getClass().getPackage().getName().replace('.', '/') + "/events.js"));
		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(0, f.getDeposeTime());

		ioc.depose();
		assertEquals(1, f.getCreateTime());
		assertEquals(1, f.getFetchTime());
		assertEquals(1, f.getDeposeTime());
	}
}
