package panda.ioc.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static panda.ioc.json.Utils.A;
import static panda.ioc.json.Utils.I;
import static panda.ioc.json.Utils.J;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.Ioc;
import panda.ioc.IocLoadException;
import panda.ioc.IocLoader;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.json.pojo.Animal;
import panda.ioc.json.pojo.AnimalRace;
import panda.ioc.json.pojo.IocSelf;
import panda.ioc.json.pojo.IocTO00;
import panda.ioc.loader.JsonIocLoader;
import panda.ioc.loader.MapIocLoader;

public class SimpleJsonIocTest {

	@Test
	public void test_2darray_by_map_iocvalue() {
		Map<String, Object> map = new HashMap<String, Object>();

		Map<String, Object> objMap = new HashMap<String, Object>();
		String[][] strss = new String[2][2];
		strss[0][0] = "a";
		strss[0][1] = "b";
		strss[1][0] = "c";
		strss[1][1] = "d";
		objMap.put("args", new Object[] { strss });

		map.put("obj", objMap);
		Ioc ioc = new DefaultIoc(new MapIocLoader(map));

		IocTO00 obj = ioc.get(IocTO00.class, "obj");
		assertEquals(2, obj.getStrss().length);
		assertEquals(2, obj.getStrss()[0].length);
		assertEquals("a", obj.getStrss()[0][0]);
		assertEquals("b", obj.getStrss()[0][1]);
		assertEquals("c", obj.getStrss()[1][0]);
		assertEquals("d", obj.getStrss()[1][1]);
	}

	@Test
	public void test_2darray_iocvalue() {
		Ioc ioc = I("obj:{args:[[['a','b'],['c','d']]]}");
		IocTO00 obj = ioc.get(IocTO00.class, "obj");
		assertEquals(2, obj.getStrss().length);
		assertEquals(2, obj.getStrss()[0].length);
		assertEquals("a", obj.getStrss()[0][0]);
		assertEquals("b", obj.getStrss()[0][1]);
		assertEquals("c", obj.getStrss()[1][0]);
		assertEquals("d", obj.getStrss()[1][1]);
	}

	@Test
	public void test_refer_self() {
		Ioc ioc = I(J("fox", "name:'Fox',another:'#fox'"));
		Animal f = ioc.get(Animal.class, "fox");
		assertEquals("Fox", f.getName());
		assertTrue(f == f.getAnother());
	}

//	@Test
//	public void test_empty_json_file() {
//		IocLoader loader = new JsonIocLoader(getClass().getPackage().getName().replace('.', '/') + "/empty.js");
//		Ioc ioc = new PandaIoc(loader);
//		assertEquals(0, ioc.getNames().length);
//	}

	@Test
	public void test_normal() {
		Animal a = A("age:23,name:'monkey',race:'MAMMAL'");
		assertEquals(23, a.getAge());
		assertEquals("monkey", a.getName());
		assertEquals(AnimalRace.MAMMAL, a.getRace());
	}

	@Test
	public void test_singleon() {
		Ioc ioc = I(J("fox", "name:'Fox'"));
		Animal f = ioc.get(Animal.class, "fox");
		Animal f2 = ioc.get(Animal.class, "fox");
		assertTrue(f == f2);

		ioc = I(J("fox", "singleton:false, fields: {name:'Fox'}"));
		Animal f3 = ioc.get(Animal.class, "fox");
		Animal f4 = ioc.get(Animal.class, "fox");
		assertFalse(f3 == f4);
	}

	@Test
	public void test_refer() {
		Ioc ioc = I(J("fox", "type:'" + Animal.class.getName() + "',fields:{name:'Fox'}"),
			J("rabit", "name:'Rabit',enemies:['#fox','#fox']"));
		Animal r = ioc.get(Animal.class, "rabit");
		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(2, r.getEnemies().length);
		assertTrue(f == r.getEnemies()[0]);
		assertTrue(f == r.getEnemies()[1]);
		assertEquals("Fox", f.getName());
		assertEquals("Rabit", r.getName());
	}

	@Test
	public void test_array_and_refer() {
		Ioc ioc = I(J("fox", "name:'Fox'"),
			J("rabit", "name:'Rabit',enemies:['#fox:" + Animal.class.getName() + "',null]"));

		Animal r = ioc.get(Animal.class, "rabit");
		Animal f = ioc.get(Animal.class, "fox");
		assertEquals(2, r.getEnemies().length);
		assertTrue(f == r.getEnemies()[0]);
		assertEquals("Fox", f.getName());
		assertEquals("Rabit", r.getName());
		assertNull(r.getEnemies()[1]);
	}

	@Test
	public void test_env() {
		Animal f = A("name:'env:PATH',misc:['env:PATH']");
		assertTrue(f.getName().length() > 0);
		assertEquals(f.getName(), f.getMisc().get(0).toString());
	}

	@Test
	public void test_map() {
		Animal f = A("map : {asia:34, europe: 45}");
		assertEquals(34, f.getMap().get("asia").intValue());
		assertEquals(45, f.getMap().get("europe").intValue());
		f = A("relations: { a: { name:'AAA' }, b: { name:'BBB' } }");
		assertEquals(2, f.getRelations().size());
		assertEquals("AAA", f.getRelations().get("a").getName());
		assertEquals("BBB", f.getRelations().get("b").getName());
	}

	@Test
	public void test_el_simple() {
		Ioc ioc = I(J("fox", "name:'${\"fox\".toUpperCase()}', age:'${$ioc.names.length}'"));
		Animal fox = ioc.get(Animal.class, "fox");
		assertEquals("FOX", fox.getName());
		assertEquals(1, fox.getAge());
	}

	@Test
	public void test_el_with_arguments() {
		Ioc ioc = I(J("fox", "name:'Fox',age:10"),
			J("wolf", "name:'${fox.showName(\"_\", 2, \"W\")}',age:'${fox.age}'"));
		Animal fox = ioc.get(Animal.class, "fox");
		Animal wolf = ioc.get(Animal.class, "wolf");
		assertEquals("Fox", fox.getName());
		assertEquals(fox.getAge(), wolf.getAge());
		assertEquals("__W", wolf.getName());
	}

	@Test
	public void test_parent() {
		Ioc ioc = I(J("fox", "name:'P',age:10"), J("f2", "parent:'fox',fields:{age:5}"));
		Animal fox = ioc.get(Animal.class, "fox");
		assertEquals("P", fox.getName());
		assertEquals(10, fox.getAge());
		Animal f2 = ioc.get(Animal.class, "f2");
		assertEquals("P", f2.getName());
		assertEquals(5, f2.getAge());
	}

	@Test
	public void test_muilt_parent() {
		Ioc ioc = I(J("fox", "name:'P',age:10"), J("f2", "parent:'fox'"), J("f3", "parent:'f2'"));
		Animal f3 = ioc.get(Animal.class, "f3");
		assertEquals(10, f3.getAge());
	}

	@Test
	public void test_create_by_args() {
		Ioc ioc = I(J("fox", "age:10"), J("xb", "parent:'fox',args:['XiaoBai']"));
		Animal xb = ioc.get(Animal.class, "xb");
		assertEquals("XiaoBai", xb.getName());
	}

	@Test
	public void test_break_parent() {
		try {
			Ioc ioc = I(J("f2", "parent:'f3'"), J("f3", "parent:'f2'"));
			ioc.get(Animal.class, "f3");
			Assert.fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Cycled parent (id: f3, parent: f3)", e.getMessage());
		}
	}

	@Test
	public void test_break_parent2() {
		try {
			Ioc ioc = I(J("fox", "name:'P',age:10"), J("f2", "parent:'x'"), J("f3", "parent:'y'"));
			ioc.get(Animal.class, "f3");
			Assert.fail();
		}
		catch (IllegalArgumentException e) {
			assertEquals("Cycled parent (id: f3, parent: y)", e.getMessage());
		}
	}

	@Test
	public void test_load_from_reader() throws IocLoadException {
		Reader r = new InputStreamReader(getClass().getResourceAsStream("main.js"));
		IocLoader loader = new JsonIocLoader(r);
		assertTrue(loader.getNames().size() > 0);
	}

	@Test
	public void test_get_ioc_self() {
		Ioc ioc = I(J("iocV", "type:'" + IocSelf.class.getName() + "',fields:{ioc:'#$iOc'}"));
		assertEquals(ioc, ioc.get(IocSelf.class, "iocV").getIoc());
	}
}
