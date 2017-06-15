package panda.bind.xml;

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
public class XmlBinderTest {
	public static class A {
		private Object obj = null;
		private boolean bol = false;
		private Integer num = 1;
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
		Assert.assertEquals("<doc>true</doc>", Xmls.toXml(true));
		Assert.assertEquals(true, Xmls.fromXml("<doc>true</doc>", boolean.class));

		Assert.assertEquals("<doc>12</doc>", Xmls.toXml("12"));
		Assert.assertEquals("12", Xmls.fromXml("<doc>12</doc>", String.class));

		Assert.assertEquals("<doc>34</doc>", Xmls.toXml(34));
		Assert.assertEquals(new Integer(34), (Integer)Xmls.fromXml("<doc>34</doc>", int.class));
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
		am.put("e1", new A(false, "m1", 31));
		am.put("e2", new A(true, "m2", 32));
		a.setMap(am);

		String xml = Xmls.toXml(a, true);
		System.out.println(xml);
		
		A a2 = Xmls.fromXml(xml, A.class);
		Assert.assertEquals(a, a2);
	}

	@Test
	public void testStringList() {
		List<String> list = Arrays.asList("a", "b", "c");
		String xml = Xmls.toXml(list);
//		System.out.println(xml);

		List<String> abc = Xmls.fromXml(xml, Types.paramTypeOf(List.class, String.class));
		Assert.assertEquals(list, abc);
	}

	@Test
	public void testClassProp() {
		Map<String, String> m = new LinkedHashMap<String, String>();
		
		m.put("class", "cls");
		m.put("clazz", "clz");

		String s = Xmls.toXml(m);
		Assert.assertEquals("<doc><class>cls</class><clazz>clz</clazz></doc>", s);
		
		Map dm = Xmls.fromXml(s, LinkedHashMap.class);
		Assert.assertEquals(m, dm);
		
		XmlDeserializer xd = new XmlDeserializer();
		try {
			xd.setIgnoreReadonlyProperty(false);
			xd.setIgnoreMissingProperty(true);
			xd.deserialize(s, A.class);
			Assert.fail("ignore readonly property");
		}
		catch (XmlException e) {
			Assert.assertEquals("readonly property [class] of " + A.class + " at /doc/class", e.getMessage());
		}
		
		try {
			xd.setIgnoreReadonlyProperty(true);
			xd.setIgnoreMissingProperty(false);
			xd.deserialize(s, A.class);
			Assert.fail("ignore missing property");
		}
		catch (XmlException e) {
			Assert.assertEquals("missing property [clazz] of " + A.class + " at /doc/clazz", e.getMessage());
		}

		xd.setIgnoreReadonlyProperty(true);
		xd.setIgnoreMissingProperty(true);
		A actual = xd.deserialize(s, A.class);
		A expect = new A();
		Assert.assertEquals(expect, actual);
	}
	
	@Test
	public void testMapMap() {
		Map<String, Map> m = new LinkedHashMap<String, Map>();
		Map<String, Number> m1 = new LinkedHashMap<String, Number>();
		m1.put("i1", 1);
		m.put("m1", m1);

		Map<String, Number> m2 = new LinkedHashMap<String, Number>();
		m2.put("i1", 1);
		m2.put("i2", 2);
		m.put("m2", m2);

		Map<String, Number> m3 = new LinkedHashMap<String, Number>();
		m3.put("i1", 1);
		m3.put("i2", 2);
		m3.put("i3", 3);
		m.put("m3", m3);

		String s = Xmls.toXml(m, true);
//		System.out.println(s);
		
		Map<String, Map<String, Number>> md = Xmls.fromXml(
			s, new TypeToken<Map<String, Map<String, Number>>>() {}.getType());
		
		Assert.assertTrue(Objects.equals(m, md));
	}
	
	@Test
	public void testMapList() {
		Map<String, List<Number>> m = new LinkedHashMap<String, List<Number>>();
		List<Number> l0 = new ArrayList<Number>();
		m.put("m0", l0);

		List<Number> l1 = new ArrayList<Number>();
		l1.add(11);
		l1.add(12);
		m.put("m1", l1);

		List<Number> l2 = new ArrayList<Number>();
		l2.add(21);
		l2.add(22);
		m.put("m2", l2);
		
		String s = Xmls.toXml(m, true);
//		System.out.println(s);
	
		// NOTE: <m0></m0> -> null
		m.put("m0", null);
		
		Map<String, List<Number>> m2 = Xmls.fromXml(
			s, new TypeToken<Map<String, List<Number>>>() {}.getType());
		
		Assert.assertTrue(Objects.equals(m, m2));
	}

	@Test
	public void testIgnoreBeanNullProperty() {
		A a = new A();
		Assert.assertEquals("<doc><bol>false</bol><num>1</num><str>A</str></doc>", Xmls.toXml(a));
	}

	@Test
	public void testIgnoreMapNullValue() {
		Map<String, Number> m = new LinkedHashMap<String, Number>();
		m.put("i0", 0);
		m.put("null", null);
		Assert.assertEquals("<doc><i0>0</i0></doc>", Xmls.toXml(m));
	}
	
	@Test
	public void testArrayNullValue() {
		List<Number> l = Arrays.asList(new Number[] { 0, null, 1, null, 2 });
		Assert.assertEquals("<doc><i>0</i><i></i><i>1</i><i></i><i>2</i></doc>", Xmls.toXml(l));
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void testWriter() {
		Assert.assertEquals("<doc></doc>", Xmls.toXml(new ArrayList()));
		Assert.assertEquals("<doc></doc>", Xmls.toXml(new ArrayList(), true));
		Assert.assertEquals("<doc><i>1</i><i>2</i><i>3</i></doc>", Xmls.toXml(new int[] { 1, 2, 3 }));
		Assert.assertEquals("<doc>\n\t<i>1</i>\n\t<i>2</i>\n\t<i>3</i>\n</doc>", Xmls.toXml(new int[] { 1, 2, 3 }, true));

		Assert.assertEquals("<doc></doc>", Xmls.toXml(new HashMap()));
		Assert.assertEquals("<doc></doc>", Xmls.toXml(new HashMap(), true));
		Map m = new LinkedHashMap();
		m.put("m0", 0);
		m.put("m1", 1);
		m.put("m2", 2);
		Assert.assertEquals("<doc><m0>0</m0><m1>1</m1><m2>2</m2></doc>", Xmls.toXml(m));
		Assert.assertEquals("<doc>\n\t<m0>0</m0>\n\t<m1>1</m1>\n\t<m2>2</m2>\n</doc>", Xmls.toXml(m, true));
	}

	@Test
	public void testCycleNoProp() {
		A a = new A();
		
		a.setAry(new A[] { new A(true, "a1", 11), a });

		Assert.assertEquals("<doc><ary><i><bol>true</bol><num>11</num><str>a1</str></i><i></i></ary><bol>false</bol><num>1</num><str>A</str></doc>", Xmls.toXml(a));
	}
}
