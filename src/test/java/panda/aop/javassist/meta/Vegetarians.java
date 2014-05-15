package panda.aop.javassist.meta;

import java.lang.reflect.Modifier;

public class Vegetarians {

	public static int run(Vegetarian r, int distance) {
		return r.run(distance);
	}

	public static void main(String[] args) {
		System.out.println(panda.lang.Strings.leftPad(Integer.toBinaryString(Modifier.ABSTRACT), 32, '0'));
	}
}
