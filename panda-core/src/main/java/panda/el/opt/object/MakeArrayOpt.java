package panda.el.opt.object;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.opt.AbstractOpt;
import panda.el.opt.RunMethod;
import panda.lang.Arrays;

/**
 * '{}' Array Container Generator.
 */
public class MakeArrayOpt extends AbstractOpt implements RunMethod {
	public int getPriority() {
		return 1;
	}

	@Override
	public void wrap(Queue<Object> operand) {
	}

	public Object calculate(ELContext ec) {
		throw new ELException("calculate() is unsupported by '{}'");
	}

	public Object run(ELContext ec, List<?> param) {
		Class<?> cs = null;
		for (Object o : param) {
			if (o != null) {
				if (cs == null) {
					cs = o.getClass();
				}
				else if (!cs.isAssignableFrom(o.getClass())){
					cs = Object.class;
					break;
				}
			}
		}
		
		if (cs == null) {
			return Arrays.EMPTY_OBJECT_ARRAY;
		}
		
		if (cs == Object.class) {
			return param.toArray();
		}
		
		Object a = Array.newInstance(cs, param.size());
		int i = 0;
		for (Object o : param) {
			Array.set(a, i++, o);
		}
		return a;
	}

	public String operator() {
		return "array";
	}
}
