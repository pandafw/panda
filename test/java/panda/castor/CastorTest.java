package panda.castor;

import java.util.ArrayList;
import java.util.List;

import panda.bind.json.Jsons;
import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.Strings;
import panda.lang.TypeToken;
import junit.framework.TestCase;

/**
 */
public class CastorTest extends TestCase {
	public static class A {
		private boolean bol = false;
		private String str = "A";
		private Integer num = 1;
		private A[] ary;
		private List<A> lst;

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

			A a = (A)obj;
			if (bol != a.bol) {
				return false;
			}

			if (str == null) {
				if (a.str != null) {
					return false;
				}
			}
			else if (!str.equals(a.str)) {
				return false;
			}
			if (num == null) {
				if (a.num != null) {
					return false;
				}
			}
			else if (!num.equals(a.num)) {
				return false;
			}
			if (ary == null) {
				if (a.ary != null) {
					return false;
				}
			}
			else if (!ary.equals(a.ary)) {
				return false;
			}
			if (lst == null) {
				if (a.lst != null) {
					return false;
				}
			}
			else if (!lst.equals(a.lst)) {
				return false;
			}

			return true;
		}
	}

	public static class B {
		private boolean bol = true;
		private StringBuilder str = new StringBuilder("B");
		private Integer num = 2;
		private B[] ary;
		private List<B> lst;
		
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
	}

	public void testStringArrayToIntArray() throws Exception {
		String[] ss = {
			"1", "2", "-3", "a"
		};
		
		int[] ii = {
			1, 2, -3, 0
		};
		
		assertEquals(Strings.join(ii), Strings.join(Objects.cast(ss, int[].class)));
		assertTrue(Arrays.equals(ii, Objects.cast(ss, int[].class)));
	}

	public void testStringArrayToIntList() throws Exception {
		String[] ss = {
			"1", "2", "-3", "a"
		};
		
		Integer[] ii = new Integer[] {
			1, 2, -3, null
		};
		
//		System.out.println(ii.length);
//		System.out.println(Strings.join(ii, ", "));
		
		List<Integer> il = Objects.cast(ss, new TypeToken<List<Integer>>(){}.getType());
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
		
		B b = Objects.cast(a, B.class);
		B[] bl = b.getLst().toArray(new B[0]);
		assertNotNull(bl);
		
		assertEquals(Jsons.toJson(a, true), Jsons.toJson(b, true));
	}

}
