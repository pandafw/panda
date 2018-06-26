package panda.cast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.TimeZones;
import panda.lang.reflect.TypeToken;
import panda.lang.time.DateTimes;

public class CastorTest {
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

	@Test
	public void testStringArrayToIntArray() throws Exception {
		String[] ss = {
			"1", "2", "-3", "0"
		};
		
		int[] ii = {
			1, 2, -3, 0
		};
		
		int[] iii = Castors.scast(ss, int[].class);

		Assert.assertEquals(Strings.join(ii), Strings.join(iii));
		Assert.assertTrue(Arrays.equals(ii, iii));
	}

	@Test
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
		
		Assert.assertEquals(Strings.join(ii), Strings.join(iii));
		Assert.assertTrue(Arrays.equals(ii, iii));
	}
	
	@Test
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
		
		Assert.assertEquals(Strings.join(ii), Strings.join(iii));
		Assert.assertTrue(Arrays.equals(ii, iii));
	}
	
	@Test
	public void testA2B() throws Exception {
		A a = new A();
		
		a.setAry(new A[] { new A(), new A() });
		
		List<A> al = new ArrayList<A>();
		al.add(new A());
		al.add(new A());
		a.setLst(al);
		
		try {
			Castors.scast(a, B.class);
			Assert.fail();
		}
		catch (CastException e) {
			Assert.assertEquals("/: Failed to cast class panda.cast.CastorTest$A -> class panda.cast.CastorTest$B", e.getMessage());
		}
	}

	@Test
	public void testMap2A() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();;
		
		m.put("bol", true);
		m.put("obj.bol", true);
		m.put("dummy", true);
		m.put("obj.dummy", true);
		
		A a = Castors.scast(m, A.class);
		Assert.assertNotNull(a);
		Assert.assertTrue(a.bol);
		Assert.assertNotNull(a.obj);
		Assert.assertTrue(a.obj.bol);
	}

	@Test
	public void testA2Map() throws Exception {
		Map<String, Object> e = new HashMap<String, Object>();;
		
		e.put("ary", null);
		e.put("bol", false);
		e.put("lst", null);
		e.put("map", null);
		e.put("num", 1);
		e.put("obj", null);
		e.put("str", "A");
		
		A a = new A();
		
		Map<String, Object> m = Castors.scast(a, Map.class);

		Assert.assertNotNull(m);
		Assert.assertEquals(e, m);
	}

	@Test
	public void testString2A() throws Exception {
		Map<String, Object> m = new HashMap<String, Object>();;
		
		m.put("bol", true);
		m.put("obj", "test");
		
		try {
			Castors.scast(m, A.class);
			Assert.fail();
		}
		catch (CastException e) {
		}
	}
	
	@Test
	public void testCastToPrimitive() throws Exception {
		try {
			Integer i = 2;
			Castors.scastTo("1", i);
			Assert.fail();
		}
		catch (CastException e) {
		}
	}
	
	@Test
	public void testDateCastWithTimeZone() throws Exception {
		String exp = "2018-01-02T03:04:05Z";
		Calendar act = Castors.scast(exp, Calendar.class);
		Assert.assertEquals(exp, DateTimes.format(act, DateTimes.ISO_DATETIME_TIMEZONE_FORMAT, TimeZones.GMT));
	}
}
