package panda.ioc;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import panda.ioc.a.A;
import panda.ioc.a.B;
import panda.ioc.a.C;
import panda.ioc.a.D;
import panda.ioc.impl.DefaultIoc;
import panda.ioc.loader.AnnotationIocLoader;

public class MultiThreadIocTest {
	@Test
	public void test_thread8() {
		Ioc ioc = new DefaultIoc(new AnnotationIocLoader(C.class.getPackage().getName()));

		StringBuffer sb = new StringBuffer();
		
		A a0 = ioc.get(A.class);
		B b0 = ioc.get(B.class);
		C c0 = ioc.get(C.class);
		D d0 = ioc.get(D.class);

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
					for (int i = 0; i < 10; i++) {
						A a = ioc.get(A.class);
						B b = ioc.get(B.class);
						C c = ioc.get(C.class);
						D d = ioc.get(D.class);
						
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
						if (d0 == d) {
							sb.append("d0 != d\n");
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
