package panda.ioc;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.a.A;
import panda.ioc.a.B;
import panda.ioc.a.C;
import panda.ioc.a.D1;
import panda.ioc.a.D2;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;

public class MultiThreadIocTest {
	@Test
	public void test_thread8() {
		final Ioc ioc = new DefaultIoc(new AnnotationIocLoader(C.class.getPackage().getName()));

		final StringBuffer sb = new StringBuffer();
		
		final A a0 = ioc.get(A.class);
		final B b0 = ioc.get(B.class);
		final C c0 = ioc.get(C.class);
		final D1 d01 = ioc.get(D1.class);
		final D2 d02 = ioc.get(D2.class);

		Assert.assertEquals(a0, b0);
		Assert.assertEquals(c0.a, a0);
		Assert.assertEquals(c0.b, b0);
		Assert.assertEquals("{\"a\":0,\"b\":1}", b0.jo.toString());
		Assert.assertEquals("[0,1]", b0.ja.toString());

		LinkedList<Thread> ts = new LinkedList<Thread>();
		for (int i = 0; i < 8; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 0; i < 1000; i++) {
						A a = ioc.get(A.class);
						B b = ioc.get(B.class);
						C c = ioc.get(C.class);
						D1 d1 = ioc.get(D1.class);
						D2 d2 = ioc.get(D2.class);
						
						if (a0 != a) {
							sb.append("a0 != a\n");
							return;
						}
						if (b0 != b) {
							sb.append("b0 != b\n");
							return;
						}
						if (c0 != c) {
							sb.append("c0 != c\n");
							return;
						}
						if (d01 == d1) {
							sb.append("d01 == d1\n");
							return;
						}
						if (d02 == d2) {
							sb.append("d02 == d2\n");
							return;
						}
						if (d1.self != d1) {
							sb.append("d1.self != d1\n");
							return;
						}
						if (d2.self != d2) {
							sb.append("d2.self != d2\n");
							return;
						}
						if (d1.d2 == d2) {
							sb.append("d1.d2 == d2\n");
							return;
						}
						if (d2.d1 == d1) {
							sb.append("d2.d1 == d1\n");
							return;
						}

						if (!"{\"a\":0,\"b\":1}".equals(b.jo.toString())) {
							sb.append("{\\\"a\\\":0,\\\"b\\\":1} != " + b.jo.toString() + "\n");
							return;
						}
						if (!"[0,1]".equals(b.ja.toString())) {
							sb.append("[0,1] != " + b.ja.toString() + "\n");
							return;
						}
					}
				}
			});
			t.start();
			ts.add(t);
		}
		
		while (!ts.isEmpty()) {
			Thread t = ts.peek();
			try {
				t.join();
				ts.poll();
			}
			catch (InterruptedException e) {
			}
		}
		
		if (sb.length() > 0) {
			Assert.fail(sb.toString());
		}
	}

}
