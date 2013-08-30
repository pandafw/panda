package panda.bind.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.bind.json.Jsons;
import panda.bind.xml.Xmls;
import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.TypeToken;
import panda.lang.Types;
import panda.lang.builder.EqualsBuilder;
import junit.framework.TestCase;

/**
 */
public class XmlBinderTest extends TestCase {
	public static class A {
		private Object obj = null;
		private boolean bol = false;
		private String str = "A";
		private Integer num = 1;
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
		public String getStr() {
			return str;
		}
		public void setStr(String str) {
			this.str = str;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
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
		//System.out.println(json);
		
		A a2 = Jsons.fromJson(json, A.class);
		assertEquals(a, a2);
	}

	public void testStringList() {
		List<String> list = Arrays.asList("a", "b", "c");
		String json = Jsons.toJson(list);

		List<String> abc = Jsons.fromJson(json, Types.paramTypeOf(List.class, String.class));
		assertEquals(list, abc);
	}

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
		
		assertTrue(Objects.equals(m, m2));
	}

	public void testIgnoreBeanNullProperty() {
		A a = new A();
		assertEquals("<doc><bol>false</bol><num>1</num><str>A</str></doc>", Xmls.toXml(a));
	}

	public void testIgnoreMapNullValue() {
		Map<String, Number> m = new LinkedHashMap<String, Number>();
		m.put("i0", 0);
		m.put("null", null);
		assertEquals("<doc><i0>0</i0></doc>", Xmls.toXml(m));
	}
	
	public void testArrayNullValue() {
		List<Number> l = Arrays.asList(new Number[] { 0, null, 1, null, 2 });
		assertEquals("<doc><i>0</i><i></i><i>1</i><i></i><i>2</i></doc>", Xmls.toXml(l));
	}
	
	@SuppressWarnings("unchecked")
	public void testWriter() {
		assertEquals("<doc></doc>", Xmls.toXml(new ArrayList()));
		assertEquals("<doc></doc>", Xmls.toXml(new ArrayList(), true));
		assertEquals("<doc><i>1</i><i>2</i><i>3</i></doc>", Xmls.toXml(new int[] { 1, 2, 3 }));
		assertEquals("<doc>\n\t<i>1</i>\n\t<i>2</i>\n\t<i>3</i>\n</doc>", Xmls.toXml(new int[] { 1, 2, 3 }, true));

		assertEquals("<doc></doc>", Xmls.toXml(new HashMap()));
		assertEquals("<doc></doc>", Xmls.toXml(new HashMap(), true));
		Map m = new LinkedHashMap();
		m.put("m0", 0);
		m.put("m1", 1);
		m.put("m2", 2);
		assertEquals("<doc><m0>0</m0><m1>1</m1><m2>2</m2></doc>", Xmls.toXml(m));
		assertEquals("<doc>\n\t<m0>0</m0>\n\t<m1>1</m1>\n\t<m2>2</m2>\n</doc>", Xmls.toXml(m, true));
	}

	public void testCycleNoProp() {
		A a = new A();
		
		a.setAry(new A[] { new A(true, "a1", 11), a });

		assertEquals("<doc><ary><i><bol>true</bol><num>11</num><str>a1</str></i><i></i></ary><bol>false</bol><num>1</num><str>A</str></doc>", Xmls.toXml(a));
	}
}
