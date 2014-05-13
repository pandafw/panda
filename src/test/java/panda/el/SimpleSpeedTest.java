package panda.el;

import java.util.Map;

import panda.bind.json.Jsons;
import panda.lang.Strings;
import panda.lang.time.StopWatch;

/**
 * 一个基本的速度测试。 同 Java 代码相比，大约慢了 100－300倍 随着表达式的复杂，会更慢
 */
@SuppressWarnings("unchecked")
public class SimpleSpeedTest {

	static int max = 50000;
	static int i = 0;

	public int abc(int i) {
		return i % 13;
	}

	public static void main(String[] args) throws SecurityException, NoSuchMethodException {
		final SimpleSpeedTest z = new SimpleSpeedTest();
		final String elstr = "num + (i - 1 + 2 - 3 + 4 - 5 + 6 - 7)-z.abc(i)";
		final Map context = Jsons.fromJson("{'num':0}", Map.class);
		context.put("z", z);

		System.out.println("\n" + Strings.repeat('=', 100));

		StopWatch sw = StopWatch.run(new Runnable() {
			public void run() {
				int num = 0;
				for (int i = 0; i < max; i++)
					num = num + (i - 1 + 2 - 3 + 4 - 5 + 6 - 7) - z.abc(i);
				System.out.println("Num: " + num);
			}
		});
		System.out.println("\n" + Strings.repeat('=', 100));

		StopWatch sw3 = StopWatch.run(new Runnable() {
			public void run() {
				context.put("num", 0);
				for (int i = 0; i < max; i++) {
					context.put("i", i);
					context.put("num", El.eval(context, elstr));
				}
				System.out.println("Num: " + context.get("num"));
			}
		});
		System.out.println("\n" + Strings.repeat('=', 100));

		StopWatch sw4 = StopWatch.run(new Runnable() {
			public void run() {
				El el2pre = new El(elstr);
				context.put("num", 0);
				context.put("z", z);
				for (int i = 0; i < max; i++) {
					context.put("i", i);
					context.put("num", el2pre.eval(context));
				}
				System.out.println("Num: " + context.get("num"));
			}
		});
		System.out.println("\n" + Strings.repeat('=', 100));

		StopWatch sw5 = StopWatch.run(new Runnable() {
			public void run() {
				El el2pre = new El(elstr);
				context.put("num", 0);
				context.put("z", z);
				for (int i = 0; i < max; i++) {
					context.put("i", i);
					context.put("num", el2pre.eval(context));
				}
				System.out.println("Num: " + context.get("num"));
			}
		});
		System.out.println("\n" + Strings.repeat('=', 100));

		System.out.printf("\n%20s : %s", "Invoke", sw.toString());
		System.out.printf("\n%20s : %s", "Reflect", sw3.toString());
		System.out.printf("\n%20s : %s", "Reflect", sw4.toString());
		System.out.printf("\n%20s : %s", "Reflect", sw5.toString());
		System.out.println();

	}

}
