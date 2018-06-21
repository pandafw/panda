package panda.el;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.junit.Assert;
import org.junit.Test;

import panda.bind.json.Jsons;
import panda.lang.Arrays;
import panda.lang.Strings;

@SuppressWarnings("unchecked")
public class ELTest {
	@Test
	public void notCalculateOneNumber() {
		assertEquals(1, EL.eval("1"));
		assertEquals(0.1, EL.eval(".1"));
		assertEquals(0.1d, EL.eval("0.1"));
		assertEquals(0.1f, EL.eval("0.1f"));
		assertEquals(0.1d, EL.eval("0.1d"));
		assertEquals(true, EL.eval("true"));
		assertEquals(false, EL.eval("false"));
		assertEquals("jk", EL.eval("'jk'"));
	}

	@Test
	public void simpleCalculate() {
		// 加
		assertEquals(2, EL.eval("1+1"));
		assertEquals(2.2, EL.eval("1.1+1.1"));
		// 减
		assertEquals(1, EL.eval("2-1"));
		// 乘
		assertEquals(9, EL.eval("3*3"));
		assertEquals(0, EL.eval("3*0"));
		// 除
		assertEquals(3, EL.eval("9/3"));
		assertEquals(2.2, EL.eval("4.4/2"));
		assertEquals(9.9 / 3, EL.eval("9.9/3"));
		// 取余
		assertEquals(1, EL.eval("5%2"));
		assertEquals(1.0 % 0.1, EL.eval("1.0%0.1"));

	}

	/**
	 * 位运算
	 */
	@Test
	public void bit() {
		assertEquals(-40, EL.eval("-5<<3"));
		assertEquals(-1, EL.eval("-5>>3"));
		assertEquals(5, EL.eval("5>>>32"));
		assertEquals(-5, EL.eval("-5>>>32"));
		assertEquals(1, EL.eval("5&3"));
		assertEquals(7, EL.eval("5|3"));
		assertEquals(-6, EL.eval("~5"));
		assertEquals(6, EL.eval("5^3"));
	}

	/**
	 * 多级运算
	 */
	@Test
	public void multiStageOperation() {
		assertEquals(3, EL.eval("1 + 1 + 1"));
		assertEquals(1, EL.eval("1+1-1"));
		assertEquals(-1, EL.eval("1-1-1"));
		assertEquals(1, EL.eval("1-(1-1)"));
		assertEquals(7, EL.eval("1+2*3"));
		assertEquals(2 * 4 + 2 * 3 + 4 * 5, EL.eval("2*4+2*3+4*5"));
		assertEquals(9 + 8 * 7 + (6 + 5) * ((4 - 1 * 2 + 3)), EL.eval("9+8*7+(6+5)*((4-1*2+3))"));
		assertEquals(.3 + .2 * .5, EL.eval(".3+.2*.5"));
		assertEquals((.5 + 0.1) * .9, EL.eval("(.5 + 0.1)*.9"));
	}

	/**
	 * 空格
	 */
	@Test
	public void sikpSpace() {
		// 空格检测
		assertEquals(3, EL.eval("    1 + 2    "));
	}

	@Test
	public void testNull() {
		assertEquals(null, EL.eval("null"));
		assertTrue((Boolean)EL.eval("null == null"));
	}

	/**
	 * 逻辑运算
	 */
	@Test
	public void logical() {
		assertEquals(true, EL.eval("2 > 1"));
		assertEquals(false, EL.eval("2 < 1"));
		assertEquals(true, EL.eval("2 >= 2"));
		assertEquals(true, EL.eval("2 <= 2"));
		assertEquals(true, EL.eval("2 == 2 "));
		assertEquals(true, EL.eval("1 != 2"));
		assertEquals(true, EL.eval("!(1 == 2)"));
		assertEquals(true, EL.eval("!false"));
		assertEquals(true, EL.eval("true || false"));
		assertEquals(false, EL.eval("true && false"));
		assertEquals(false, EL.eval("false || true && false"));
	}

	/**
	 * 三元运算 ?:
	 */
	@Test
	public void threeTernary() {
		assertEquals(2, EL.eval("1>0?2:3"));
		assertEquals(2, EL.eval("1>0&&1<2?2:3"));
	}

	/**
	 * 字符串测试
	 */
	@Test
	public void string() {
		assertEquals("jk", EL.eval("'jk'"));
		assertEquals(2, EL.eval("'jk'.length()"));
		assertEquals(2, EL.eval("\"jk\".length()"));
		assertEquals("jk", EL.eval("\"    jk   \".trim()"));
		assertEquals("j\\n\\tk", EL.eval("\"j\\n\\tk\""));
		
		assertEquals("jk", EL.eval("'j' + 'k'"));
		assertEquals("j0", EL.eval("'j' + 0"));
	}

	@Test
	public void test_issue_397_3() {
		int expect = 1 / 1 + 10 * (1400 - 1400) / 400;
		Object val = EL.eval("1/1+10*(1400-1400)/400");
		assertEquals(expect, val);
	}

	/**
	 * 带负数的运算
	 */
	@Test
	public void negative() {
		assertEquals(-1, EL.eval("-1"));
		assertEquals(0, EL.eval("-1+1"));
		assertEquals(-1 - -1, EL.eval("-1 - -1"));
		assertEquals(9 + 8 * 7 + (6 + 5) * (-(4 - 1 * 2 + 3)), EL.eval("9+8*7+(6+5)*(-(4-1*2+3))"));
	}

	/**
	 * 方法调用
	 */
	@Test
	public void callMethod() {
		assertEquals('j', EL.eval("'jk'.charAt(0)"));
		assertEquals("cde", EL.eval("\"abcde\".substring(2)"));
		assertEquals("b", EL.eval("\"abcde\".substring(1,2)"));
		assertEquals(true, EL.eval("\"abcd\".regionMatches(2,\"ccd\",1,2)"));
		assertEquals("bbbb", EL.eval("'  abab  '.replace('a','b').trim()"));
	}

	/**
	 * 参数
	 */
	@Test
	public void test_simple_condition() {
		Map context = new HashMap();
		context.put("a", 10);
		assertEquals(10, EL.eval("a", context));
		assertEquals(20, EL.eval("a + a", context));

		context.put("b", "abc");
		assertEquals(25, EL.eval("a + 2 +a+ b.length()", context));

		String s = "a>5?'GT 5':'LTE 5'";
		assertEquals("GT 5", EL.eval(s, context));
		context.put("a", 5);
		assertEquals("LTE 5", EL.eval(s, context));

		assertEquals("jk", EL.eval("\"j\"+\"k\""));

	}

	@Test
	public void context() {
		Map context = new HashMap();
		List<String> list = new ArrayList<String>();
		list.add("jk");
		context.put("a", list);
		assertEquals("jk", EL.eval("a.get((1-1))", context));
		assertEquals("jk", EL.eval("a.get(1-1)", context));
		assertEquals("jk", EL.eval("a.get(0)", context));

		assertTrue((Boolean)EL.eval("a==null", new HashMap()));
		try {
			assertTrue((Boolean)EL.eval("a.a", new HashMap()));
			fail();
		}
		catch (Exception e) {
		}
	}

	@Test
	public void array() {
		Map context = new HashMap();
		String[] str = new String[] { "a", "b", "c" };
		String[][] bb = new String[][] { { "a", "b" }, { "c", "d" } };
		context.put("a", str);
		context.put("b", bb);
		assertEquals("b", EL.eval("a[1]", context));
		assertEquals("b", EL.eval("a[1].toString()", context));
		assertEquals("b", EL.eval("a[2-1]", context));
		assertEquals("d", EL.eval("b[1][1]", context));
	}

	@Test
	public void field() {
		@SuppressWarnings("unused")
		class abc {
			public String name = "jk";
			public Date date = new Date(1900);
		}

		Map context = new HashMap();
		context.put("a", new abc());
		assertEquals("jk", EL.eval("a.name", context));
		assertEquals("jk", EL.eval("a['name']", context));
		assertEquals(new Long(1900), EL.eval("a.date.getTime()", context));
		
		assertFalse((Boolean)EL.eval("'java.lang.Boolean'@FALSE"));
		assertEquals(Boolean.TRUE, EL.eval("'java.lang.Boolean'@parseBoolean('true')", new ELContext(true)));
	}

	public static class MethodUtil {
		public int max(int i, int j) {
			return Math.max(i, j);
		}
		
		public int min(int a, int b) {
			return Math.min(a, b);
		}
		
		public String trim(String s) {
			return Strings.trim(s);
		}
		
		public boolean ok() {
			return true;
		}
		
		public boolean ng() {
			return false;
		}
	}
	
	/**
	 * 自定义函数
	 */
	@Test
	public void testCustomMethod() {
		Object mu = new MethodUtil();
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put("a", mu);
		
		assertEquals(2, EL.eval("max(1, 2)", mu));
		assertEquals(2, EL.eval("min(max(1, 2), 4)", mu));
		assertEquals(1, EL.eval("a.min(1, 2)", ctx));
		assertEquals("jk", EL.eval("a.trim('    jk    ')", ctx));
	}

	@Test
	public void speed() {
		SimpleSpeedTest z = new SimpleSpeedTest();
		int num = 4988;
		String elstr = "num + (i - 1 + 2 - 3 + 4 - 5 + 6 - 7)-z.abc(i)";
		int i = 5000;
		Map con = new HashMap();
		con.put("num", num);
		con.put("i", i);
		con.put("z", z);
		assertEquals(num + (i - 1 + 2 - 3 + 4 - 5 + 6 - 7) - z.abc(i), EL.eval(elstr, con));
	}

	@Test
	public void lssue_486() {
		assertEquals(2 + (-3), EL.eval("2+(-3)"));
		assertEquals(2 + -3, EL.eval("2+-3"));
		assertEquals(2 * -3, EL.eval("2*-3"));
		assertEquals(-2 * -3, EL.eval("-2*-3"));
		assertEquals(2 / -3, EL.eval("2/-3"));
		assertEquals(2 % -3, EL.eval("2%-3"));
	}

	/**
	 * map测试
	 */
	@Test
	public void map() {
		Map context = new HashMap();
		context.put("a", Jsons.fromJson("{'x':10,'y':50,'txt':'Hello'}", Map.class));

		assertEquals(100, EL.eval("a.get('x')*10", context));
		assertEquals(100, EL.eval("a.x*10", context));
		assertEquals(100, EL.eval("a['x']*10", context));
		assertEquals("Hello-40", EL.eval("a.get('txt')+(a.get('x')-a.get('y'))", context));
	}

	/**
	 * list测试
	 */
	@Test
	public void list() {
		Map context = new HashMap();
		List<String> list = new ArrayList<String>();
		context.put("b", list);
		assertEquals(0, EL.eval("b.size()", context));
		list.add("");
		assertEquals(1, EL.eval("b.size()", context));
		EL.eval("b.add('Q\nQ')", context);
		assertEquals(2, EL.eval("b.size()", context));
	}

	@SuppressWarnings("unused")
	@Test
	public void complexOperation() {
		assertEquals(1000 + 100.0 * 99 - (600 - 3 * 15) % (((68 - 9) - 3) * 2 - 100) + 10000 % 7 * 71,
			EL.eval("1000+100.0*99-(600-3*15)%(((68-9)-3)*2-100)+10000%7*71"));
		assertEquals(6.7 - 100 > 39.6 ? true ? 4 + 5 : 6 - 1 : !(100 % 3 - 39.0 < 27) ? 8 * 2 - 199 : 100 % 3,
			EL.eval("6.7-100>39.6 ? 5==5? 4+5:6-1 : !(100%3-39.0<27) ? 8*2-199: 100%3"));

		Map vars = new HashMap();
		vars.put("i", 100);
		vars.put("pi", 3.14f);
		vars.put("d", -3.9);
		vars.put("b", (byte)4);
		vars.put("bool", false);
		vars.put("t", "");
		String t = "i * pi + (d * b - 199) / (1 - d * pi) - (2 + 100 - i / pi) % 99 ==i * pi + (d * b - 199) / (1 - d * pi) - (2 + 100 - i / pi) % 99";
		// t =
		// "i * pi + (d * b - 199) / (1 - d * pi) - (2 + 100 - i / pi) % 99";
		assertEquals(true, EL.eval(t, vars));

		// assertEquals('A' == ('A') || 'B' == 'B' && "ABCD" == "" && 'A' ==
		// 'A', el.eval(vars,
		// "'A' == 'A' || 'B' == 'B' && 'ABCD' == t &&  'A' == 'A'"));
		assertEquals(true || true && false && true,
			EL.eval("'A' == 'A' || 'B' == 'B' && 'ABCD' == t &&  'A' == 'A'", vars));


		String expr = "(min != null && max != null) ? (min + '~' + max) : (min != null ? ('>= ' + min) : (max != null ? ('<= ' + max) : ''))";
		vars.clear();
		vars.put("min", 1);
		assertEquals(">= 1", EL.eval(expr, vars));

		vars.clear();
		vars.put("max", 2);
		assertEquals("<= 2", EL.eval(expr, vars));

		vars.clear();
		vars.put("min", 1);
		vars.put("max", 2);
		assertEquals("1~2", EL.eval(expr, vars));
	}

	@Test
	public void testIntValue() {
		Map context = new HashMap();
		context.put("a", new BigDecimal("7"));
		context.put("b", new BigDecimal("3"));
		assertEquals(10, EL.eval("a.add(b).intValue()", context));
	}

	@Test
	public void testFloat() {
		assertEquals(EL.eval("0.1354*((70-8)%70)*100"), 0.1354 * ((70 - 8) % 70) * 100);
		assertEquals(EL.eval("0.1354*((70d-8)/70)*100"), 0.1354 * ((70d - 8) / 70) * 100);
		assertEquals(EL.eval("0.5006*(70/600*100)"), 0.5006 * (70 / 600 * 100));
	}

	@Test
	public void testStaticMethod() {
		Map context = new HashMap();
		context.put("strings", Strings.class);
		context.put("math", Math.class);
		assertEquals("a", EL.eval("strings@trim(\"  a  \")", context));
		assertEquals(2, EL.eval("math@max(1, 2)", context));
	}

	@Test
	public void testIssueQuestionOpe() {
		Map context = new HashMap();
		context.put("a", 123);
		context.put("b", 20);
		Object o = EL.eval("a>b?a:b", context);
		assertEquals(123, o);
	}

	public static class Static {
		public static final String info = "xxx";

		public static String printParam(String x) {
			return x;
		}
	}
	
	@Test
	public void testStaticMember() {
		Map context = new HashMap();
		context.put("s", Static.class);
		context.put("a", new Static());

		assertEquals("yyy", EL.eval("@printParam('yyy')", new Static()));
		assertEquals("xxx", EL.eval("@info", new Static()));
		assertEquals("xxx", EL.eval("s@printParam(s@info)", context));
		assertEquals("xxx", EL.eval("a@printParam(a@info)", context));
	}

	public static class SelfRef {
		public String name;
		public SelfRef child;
		public List<String> list = new ArrayList<String>();

		public SelfRef(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	@Test
	public void testSelfRef() {
		Map context = new HashMap();
		SelfRef item = new SelfRef("item");
		item.child = new SelfRef("child");
		context.put("item", item);

		assertEquals("child", EL.eval("item.child.getName()", context));
		assertEquals(0, EL.eval("item.list.size()", context));
	}

	@Test
	public void testThreadSafe1() throws InterruptedException {
		int size = 100;
		final CountDownLatch count = new CountDownLatch(size);
		final List<Integer> error = new ArrayList<Integer>();
		for (int index = 0; index < size; index++) {
			new Thread() {
				public void run() {
					try {
						EL.eval("1+1");
					}
					catch (Exception e) {
						error.add(1);
					}
					finally {
						count.countDown();
					}
				}
			}.start();
		}
		count.await();
		if (error.size() > 0) {
			fail();
		}
	}

	@Test
	public void testThreadSafe2() throws InterruptedException {
		final EL el = EL.parse("a+1");

		int size = 100;
		final CountDownLatch count = new CountDownLatch(size);
		final List<Integer> error = new ArrayList<Integer>();
		for (int index = 0; index < size; index++) {
			new Thread() {
				public void run() {
					try {
						Map m = new HashMap();
						m.put("a", 0);
						for (int i = 0; i < 10000; i++) {
							m.put("a", el.eval(m));
						}
						assertEquals(10000, m.get("a"));
					}
					catch (Exception e) {
						error.add(1);
					}
					finally {
						count.countDown();
					}
				}

			}.start();
		}
		count.await();
		if (error.size() > 0) {
			fail();
		}
	}


	@Test
	public void testList() {
		Map context = new HashMap();
		List<String> list = new ArrayList<String>();
		list.add("jk");
		context.put("list", list);
		context.put("System", System.class);

		EL.eval("list.add(list.get(0))", context);
		assertEquals(2, list.size());
	}

	@Test
	public void testStaticSystem() {
		Map context = new HashMap();
		List<String> list = new ArrayList<String>();
		list.add("jk");
		context.put("list", list);
		context.put("System", System.class);

		Object val = EL.eval("System@getenv('PATH').getClass().getName()", context);
		Assert.assertEquals("java.lang.String", val);

		Object val2 = EL.eval("'java.lang.System'@getenv('PATH').getClass().getName()", context);
		Assert.assertEquals("java.lang.String", val2);

		Object val3 = EL.eval("'System'@getenv('PATH').getClass().getName()", context);
		Assert.assertEquals("java.lang.String", val3);
	}

	public static class ListTest {
		private List<String> list;

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}
	}

	@Test
	public void testListTest() {
		Map context = new HashMap();

		context.put("String", String.class);

		ListTest lt = new ListTest();
		List<String> list = new ArrayList<String>();
		list.add("123");
		lt.setList(list);
		context.put("map", lt);

		assertEquals("123", EL.eval("String@valueOf(123)", context));
		assertEquals("123", EL.eval("map.list.get(0)", context));
	}

	public static class InnerClass {
		public static class A {
			B b = new B();

			public B getB() {
				return b;
			}

			public void setB(B b) {
				this.b = b;
			}
		}

		public static class B {
			public boolean isPass(String a) {
				return true;
			}
		}
	}

	@Test
	public void testInnerClass() {
		EL el = new EL("a[0].b.isPass('')?'1':'2'");
		Map ctx = new HashMap();
		ctx.put("a", new Object[] { new InnerClass.A() });
		assertEquals("1", el.eval(ctx));
	}

	public static class ArrayTest {
		public static String test(String[] names) {
			StringBuffer sb = new StringBuffer();
			for (String name : names) {
				sb.append(name);
			}
			return sb.toString();
		}
	}

	@Test
	public void testLiteralArray() throws InstantiationException, IllegalAccessException {
		EL exp = new EL("{'a','b'}");
		Assert.assertTrue(Arrays.equals(new Object[] { "a", "b" }, (Object[])exp.eval()));
	}

	@Test
	public void testLiteralArrayParams() throws InstantiationException, IllegalAccessException {
		EL exp = new EL("util.test({'a', 'b'})");
		Map context = new HashMap();
		context.put("util", ArrayTest.class.newInstance());
		assertEquals("ab", exp.eval(context));
	}
	
	@Test
	public void testMapArray() throws InstantiationException, IllegalAccessException {
		String[] a = new String[] { "a", "b" };
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("a", a);
		EL exp = new EL("util.test(map['a'])");
		Map context = new HashMap();
		context.put("util", ArrayTest.class.newInstance());
		context.put("map", map);
		assertEquals("ab", exp.eval(context));
	}

	@Test
	public void testMapArray2() {
		String[] a = new String[] { "a", "b" };
		String[] b = new String[] { "1", "2" };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", a);
		map.put("b", b);
		EL exp = new EL("util@test({map['a'][0],map['b'][0]})"); 
		Map context = new HashMap();
		context.put("util", new ArrayTest());
		context.put("map", map);
		System.out.println(exp.eval(context));
	}

	@Test
	public void test_Strict() {
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put("obj", Arrays.toMap("pet", null));

		ELContext ec = new ELContext(ctx, true);

		EL.eval("obj.pet", ec);
		EL.eval("!!(obj.pet)", ec);
		
		try {
			EL.eval("(obj.pet.name) == null", ec);
			Assert.fail("(obj.pet.name) == null");
		}
		catch (Exception e) {
		}
	}

	@Test
	public void test_Nullable() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("obj", Arrays.toMap("pet", null));
		m.put("girls", new ArrayList<String>());

		ELContext ctx = new ELContext(m, true);
		EL.eval("obj.pet", ctx);
		EL.eval("!!(obj.pet)", ctx);

		assertTrue((Boolean)EL.eval("!!(obj.pet.name) == null", ctx));
		assertTrue((Boolean)EL.eval("!(!(!!(obj.pet.name) == null))", ctx));
	}

	public void raiseError() {
		throw new RuntimeException("raise");
	}
	
	@Test
	public void test_Exception() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("obj", this);

		try {
			EL.eval("obj.raiseError()");
		}
		catch (RuntimeException e) {
			assertEquals("Failed to eval('obj.raiseError()', null)", e.getMessage());
		}
		EL.eval("!!(obj.raiseError())");
		EL.eval("!!(obj.raiseError())");
		EL.eval("!!obj.raiseError()");
	}

	@Test
	public void test_Priority() {
		Object mu = new MethodUtil();
		Map<String, Object> ctx = new HashMap<String, Object>();
		ctx.put("a", mu);
		
		assertEquals(Boolean.FALSE, EL.eval("!a.ok()", ctx));
		assertEquals(Boolean.TRUE, EL.eval("!a.ng()", ctx));
	}

	@Test
	public void test_Orable() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("obj", Arrays.toMap("pet", null));
		m.put("girls", new ArrayList<String>());

		ELContext ctx = new ELContext(m, true);
		assertEquals("cat", EL.eval("!!(obj.pet.name) ||| 'cat'", ctx));
		assertEquals("dog", EL.eval("!!(obj.girls) ||| 'dog'", ctx));
		assertEquals("cat", EL.eval("!!obj.pet.name ||| 'cat'", ctx));
		assertEquals("dog", EL.eval("!!obj.girls ||| 'dog'", ctx));

		assertEquals("cat", EL.eval("obj.pet.name ||| 'cat'", m));
		assertEquals("dog", EL.eval("obj.girls ||| 'dog'", m));
	}
}
