package panda.cast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import panda.bind.json.Jsons;
import panda.cast.CastException;
import panda.cast.Castors;
import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.reflect.TypeToken;

/**
 */
public class CastorTest extends TestCase {
	public static class A {
		private boolean bol = false;
		private String str = "A";
		private Integer num = 1;
		private A obj;
		private A[] ary;
		private List<A> lst;
		private Map<String, A> map;

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
		public A getObj() {
			return obj;
		}
		public void setObj(A obj) {
			this.obj = obj;
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
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}

			A rhs = (A)obj;
			return Objects.equalsBuilder()
					.append(bol, rhs.bol)
					.append(str, rhs.str)
					.append(num, rhs.num)
					.append(obj, rhs.obj)
					.append(ary, rhs.ary)
					.append(lst, rhs.lst)
					.append(map, rhs.map)
					.isEquals();
		}
	}

	public static class B {
		private boolean bol = true;
		private StringBuilder str = new StringBuilder("B");
		private Integer num = 2;
		private B obj;
		private B[] ary;
		private List<B> lst;
		private Map<String, B> map;
		
		public boolean isBol() {
			return bol;
		}
		public void setBol(boolean bol) {
			this.bol = bol;
		}
		public StringBuilder getStr() {
			return str;
		}
		public void setStr(StringBuilder str) {
			this.str = str;
		}
		public Integer getNum() {
			return num;
		}
		public void setNum(Integer num) {
			this.num = num;
		}
		public B getObj() {
			return obj;
		}
		public void setObj(B obj) {
			this.obj = obj;
		}
		public B[] getAry() {
			return ary;
		}
		public void setAry(B[] ary) {
			this.ary = ary;
		}
		public List<B> getLst() {
			return lst;
		}
		public void setLst(List<B> lst) {
			this.lst = lst;
		}
		public Map<String, B> getMap() {
			return map;
		}
		public void setMap(Map<String, B> map) {
			this.map = map;
		}
	}

	public void testStringArrayToIntArray() throws Exception {
		String[] ss = {
			"1", "2", "-3", "0"
		};
		
		int[] ii = {
			1, 2, -3, 0
		};
		
		int[] iii = Castors.scast(ss, int[].class);

		assertEquals(Strings.join(ii), Strings.join(iii));
		assertTrue(Arrays.equals(ii, iii));
	}

	public void testStringArrayToIntList() throws Exception {
		String[] ss = {
			"1", "2", "-3", null
		};
		
		Integer[] ii = new Integer[] {
			1, 2, -3, null
		};
		
//		System.out.println(ii.length);
//		System.out.println(Strings.join(ii, ", "));
		
		List<Integer> il = Castors.scast(ss, new TypeToken<List<Integer>>(){}.getType());
		Integer[] iii = il.toArray(new Integer[0]);
		
		assertEquals(Strings.join(ii), Strings.join(iii));
		assertTrue(Arrays.equals(ii, iii));
	}
	
	public void testStringListToIntList() throws Exception {
		List<String> ss = Arrays.asList(new String[] {
			"1", "2", "-3", null
		});
		
		Integer[] ii = new Integer[] {
			1, 2, -3, null
		};
		
//		System.out.println(ii.length);
//		System.out.println(Strings.join(ii, ", "));
		
		List<Integer> il = Castors.scast(ss, new TypeToken<List<Integer>>(){}.getType());
		Integer[] iii = il.toArray(new Integer[0]);
		
		assertEquals(Strings.join(ii), Strings.join(iii));
		assertTrue(Arrays.equals(ii, iii));
	}
	
	public void testAtoB() throws Exception {
		A a = new A();
		
		a.setAry(new A[] { new A(), new A() });
		
		List<A> al = new ArrayList<A>();
		al.add(new A());
		al.add(new A());
		a.setLst(al);
		
		B b = Castors.scast(a, B.class);
		B[] bl = b.getLst().toArray(new B[0]);
		assertNotNull(bl);
		
		assertEquals(Jsons.toJson(a, true), Jsons.toJson(b, true));
	}

	
	public void testMapToA() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();;
		
		m.put("bol", true);
		m.put("obj.bol", true);
		m.put("dummy", true);
		m.put("obj.dummy", true);
		
		A a = Castors.scast(m, A.class);
		assertNotNull(a);
		assertTrue(a.bol);
		assertNotNull(a.obj);
		assertTrue(a.obj.bol);
	}

	public void testStringToA() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();;
		
		m.put("bol", true);
		m.put("obj", "test");
		
		try {
			Castors.scast(m, A.class);
			fail();
		}
		catch (CastException e) {
		}
	}
}
