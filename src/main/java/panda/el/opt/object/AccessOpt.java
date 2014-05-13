package panda.el.opt.object;

import java.util.List;

import panda.bean.Beans;
import panda.el.ElContext;
import panda.el.ElException;
import panda.el.Operator;
import panda.el.obj.ElObj;
import panda.el.opt.RunMethod;
import panda.el.opt.TwoTernary;
import panda.lang.Exceptions;
import panda.lang.Methods;

/**
 * 访问符:'.'
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class AccessOpt extends TwoTernary implements RunMethod {
	public int getPriority() {
		return 1;
	}

	public Object calculate(ElContext ec) {
		// 如果直接调用计算方法,那基本上就是直接调用属性了吧...我也不知道^^
		Object obj = fetchVar(ec);
		if (obj == null) {
			throw new ElException("obj is NULL, can't call obj." + right);
		}
		return Beans.getProperty(obj, right.toString());
	}

	public Object run(ElContext ec, List<Object> param) {
		Object obj = fetchVar(ec);
		if (obj == null) {
			throw new NullPointerException();
		}

		Object[] arg = param.toArray();
		
		try {
			if (obj instanceof Class) {
				return Methods.invokeStaticMethod((Class)obj, right.toString(), arg);
			}
			return Methods.invokeMethod(obj, right.toString(), arg);
		}
		catch (Exception e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	/**
	 * 取得变得的值
	 */
	public Object fetchVar(ElContext ec) {
		if (left instanceof Operator) {
			return ((Operator)left).calculate(ec);
		}
		if (left instanceof ElObj) {
			return ((ElObj)left).getObj(ec);
		}
		return left;
	}

	public String fetchSelf() {
		return ".";
	}
}
