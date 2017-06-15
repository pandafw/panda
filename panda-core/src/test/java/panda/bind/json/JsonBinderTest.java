package panda.bind.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.builder.EqualsBuilder;
import panda.lang.reflect.TypeToken;
import panda.lang.reflect.Types;

/**
 */
public class JsonBinderTest {
	public static class A {
		private Object obj = null;
		private boolean bol = false;
		private Integer num = 1;
		private long lng = 2L;
		private String str = "A";
		private A[] ary;
		private List<A> lst;
		private Map<String, A> map;

		public A() {
		}
		public A(boolean b, String s, Integer n) {
			bol = b;
			str = s;
			num = n;
		}
		
		public Object getObj() {
			return obj;
		}
		public void setObj(Object obj) {
			this.obj = obj;
		}
		public boolean isBol() {
			return bol;
		}
		public void setBol(boolean bol) {
			this.bol = bol;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
		}
		public long getLng() {
			return lng;
		}
		public void setLng(long lng) {
			this.lng = lng;
		}
		public String getStr() {
			return str;
		}
		public void setStr(String str) {
			this.str = str;
		}
		public A[] getAry() {
			return ary;
		}
		public void setAry(A[] ary) {
			this.ary = ary;
		}
		public List<A> getLst() {
			return lst;
		}
		public void setLst(List<A> lst) {
			this.lst = lst;
		}
		public Map<String, A> getMap() {
			return map;
		}
		public void setMap(Map<String, A> map) {
			this.map = map;
		}
		@Override
		public boolean equals(Object rhs) {
			if (this == rhs) {
				return true;
			}
			if (rhs == null) {
				return false;
			}
			if (getClass() != rhs.getClass()) {
				return false;
			}

			A a = (A)rhs;
			return new EqualsBuilder()
				.append(obj, a.obj)
				.append(bol, a.bol)
				.append(str, a.str)
				.append(num, a.num)
				.append(ary, a.ary)
				.append(lst, a.lst)
				.append(map, a.map)
				.build();
		}
	}

	@Test
	public void testImmutable() {
		Assert.assertEquals("true", Jsons.toJson(true));
		Assert.assertEquals(true, Jsons.fromJson("true", boolean.class));
		Assert.assertEquals(true, Jsons.fromJson("/**aa*/true", boolean.class));

		Assert.assertEquals("\"12\"", Jsons.toJson("12"));
		Assert.assertEquals("12", Jsons.fromJson("'12'", String.class));

		Assert.assertEquals("34", Jsons.toJson(34));
		Assert.assertEquals(new Integer(34), (Integer)Jsons.fromJson("34", int.class));
	}
	
	@Test
	public void testSerialize() {
		A a = new A();
		
		a.setAry(new A[] { new A(true, "a1", 11), new A(false, "a2", 12) });
		
		List<A> al = new ArrayList<A>();
		al.add(new A(false, "l1", 21));
		al.add(new A(true, "l2", 22));
		a.setLst(al);

		Map<String, A> am = new HashMap<String, A>();
		am.put("1", new A(false, "m1", 31));
		am.put("2", new A(true, "m2", 32));
		a.setMap(am);

		String json = Jsons.toJson(a, true);
		
		A a2 = Jsons.fromJson(json, A.class);
		Assert.assertEquals(a, a2);
		
		json = Jsons.toJson(al, true);
		List<A> al2 = Jsons.fromJson(json, Types.paramTypeOf(List.class, A.class));
		Assert.assertEquals(al, al2);
	}

	@Test
	public void testStringList() {
		List<String> list = Arrays.asList("a", "b", "c");
		String json = Jsons.toJson(list);

		List<String> abc = Jsons.fromJson(json, Types.paramTypeOf(List.class, String.class));
		Assert.assertEquals(list, abc);
	}

	@Test
	public void testClassProp() {
		Map<String, String> m = new LinkedHashMap<String, String>();
		
		m.put("class", "cls");
		m.put("clazz", "clz");

		String s = Jsons.toJson(m);
		Assert.assertEquals("{\"class\":\"cls\",\"clazz\":\"clz\"}", s);
		
		JsonObject jo = JsonObject.fromJson(s);
		Assert.assertEquals(s, jo.toString());
		
		JsonDeserializer jd = new JsonDeserializer();
		try {
			jd.setIgnoreReadonlyProperty(false);
			jd.setIgnoreMissingProperty(true);
			jd.deserialize(s, A.class);
			Assert.fail("ignore readonly property");
		}
		catch (JsonException e) {
			Assert.assertEquals("readonly property [class] of " + A.class + " at 9 [character 10 line 1]", e.getMessage());
		}
		
		try {
			jd.setIgnoreReadonlyProperty(true);
			jd.setIgnoreMissingProperty(false);
			jd.deserialize(s, A.class);
			Assert.fail("ignore missing property");
		}
		catch (JsonException e) {
			Assert.assertEquals("missing property [clazz] of " + A.class + " at 23 [character 24 line 1]", e.getMessage());
		}

		jd.setIgnoreReadonlyProperty(true);
		jd.setIgnoreMissingProperty(true);
		A actual = jd.deserialize(s, A.class);
		A expect = new A();
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testMapList() {
		Map<String, List<Number>> m = new LinkedHashMap<String, List<Number>>();
		List<Number> l0 = new ArrayList<Number>();
		m.put("0", l0);

		List<Number> l1 = new ArrayList<Number>();
		l1.add(11);
		l1.add(12);
		m.put("1", l1);

		List<Number> l2 = new ArrayList<Number>();
		l2.add(21);
		l2.add(22);
		m.put("2", l2);
		
		String s = Jsons.toJson(m, true);
		//System.out.println(s);
		
		Map<String, List<Number>> m2 = Jsons.fromJson(
			s, new TypeToken<Map<String, List<Number>>>() {}.getType());
		
		Assert.assertTrue(Objects.equals(m, m2));
	}

	@Test
	public void testIgnoreBeanNullProperty() {
		A a = new A();
		String s = Jsons.toJson(a);
		JsonObject jo = JsonObject.fromJson(s);
		Assert.assertTrue(jo.isNull("obj"));
	}

	@Test
	public void testIgnoreMapNullValue() {
		Map<String, Number> m = new LinkedHashMap<String, Number>();
		m.put("0", 0);
		m.put("null", null);
		Assert.assertEquals("{\"0\":0}", Jsons.toJson(m));
	}
	
	@Test
	public void testArrayNullValue() {
		List<Number> l = Arrays.asList(new Number[] { 0, null, 1, null, 2 });
		Assert.assertEquals("[0,null,1,null,2]", Jsons.toJson(l));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testWriter() {
		Assert.assertEquals("[]", Jsons.toJson(new ArrayList()));
		Assert.assertEquals("[]", Jsons.toJson(new ArrayList(), true));
		Assert.assertEquals("[1,2,3]", Jsons.toJson(new int[] { 1, 2, 3 }));
		Assert.assertEquals("[\n\t1, \n\t2, \n\t3\n]", Jsons.toJson(new int[] { 1, 2, 3 }, true));

		Assert.assertEquals("{}", Jsons.toJson(new HashMap()));
		Assert.assertEquals("{}", Jsons.toJson(new HashMap(), true));
		Map m = new LinkedHashMap();
		m.put("0", 0);
		m.put("1", 1);
		m.put("2", 2);
		Assert.assertEquals("{\"0\":0,\"1\":1,\"2\":2}", Jsons.toJson(m));
		Assert.assertEquals("{\n\t\"0\": 0, \n\t\"1\": 1, \n\t\"2\": 2\n}", Jsons.toJson(m, true));
	}

	@Test
	public void testCycleNoProp() {
		A a = new A();
		
		a.setAry(new A[] { new A(true, "a1", 11), a });

		String s = Jsons.toJson(a);
		JsonObject jo = JsonObject.fromJson(s);
		JsonArray ja = jo.getJsonArray("ary"); 
		Assert.assertEquals(2, ja.length());
		Assert.assertNull(ja.get(1));
	}

	@Test
	public void testParseComments() {
		String json = "/* this is outer comment */[\n" + "  // this is a comment\n" + "\t\"a\",\n"
				+ "  /* this is another comment */\n" + "  \"b\",\n"
				+ "  # this is yet another comment\n" + "  \"c\"\n" + "]";

		List<String> abc = Jsons.fromJson(json, new TypeToken<List<String>>() {}.getType());
		Assert.assertEquals(Arrays.asList("a", "b", "c"), abc);

		//System.out.println(Jsons.toJson(abc, true));
	}

	@Test
	public void testKeyUnquote() {
		Map<String, Number> e = new LinkedHashMap<String, Number>();
		e.put("a", 0);
		e.put("null", null);

		Map a = Jsons.fromJson("{a:0, 'null':null}", Map.class);
		Assert.assertEquals(e, a);
	}
}
