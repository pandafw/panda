package panda.el.issue125;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import panda.el.El;

@SuppressWarnings("unchecked")
public class Issue125Test {
	@Test
	public void test() throws InstantiationException, IllegalAccessException {
		String[] a = new String[] { "a", "b" };
		Map<String, String[]> map = new HashMap<String, String[]>();
		map.put("a", a);
		El exp = new El("util.test(map['a'])");
		Map context = new HashMap();
		context.put("util", StringUtil.class.newInstance());
		context.put("map", map);
		assertEquals("ab", exp.eval(context));
	}

	@Test
	public void test2() {
		String[] a = new String[] { "a", "b" };
		String[] b = new String[] { "1", "2" };
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", a);
		map.put("b", b);
		El exp = new El("util.test(map['a'][0],map['b'][0])"); // 预编译结果为一个 El 对象
		Map context = new HashMap();
		context.put("util", new StringUtil());
		context.put("map", map);
		System.out.println(exp.eval(context));
	}

	/**
	 * 原因级结论 由于","操作符的权重设置不正确, 导致错误的逆波兰解析. @ TODO 整理一下这个问题的测试, 别这么乱
	 */
}
