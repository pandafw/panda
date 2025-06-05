package panda.el.opt.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import panda.el.ELContext;
import panda.el.ELException;
import panda.el.ELObj;
import panda.el.Operator;
import panda.el.RunMethod;
import panda.el.opt.DoubleOp;

/**
 * 方法体封装. 主要是把方法的左括号做为边界
 */
public class MethodInvokeOp extends DoubleOp {

	public int params = 0;

	public int getPriority() {
		return 1;
	}

	public void wrap(Queue<Object> rpn) {
		if (params <= 0) {
			left = rpn.poll();
		} else {
			right = rpn.poll();
			left = rpn.poll();
		}
	}

	public Object calculate(ELContext ec) {
		return fetchMethod(ec).invoke(ec, fetchParam(ec));
	}

	private RunMethod fetchMethod(ELContext ec) {
		if (left instanceof RunMethod) {
			return (RunMethod)left;
		}

		if (left instanceof ELObj) {
			return new AccessOp(null, left);
		}

		throw new ELException("left is unsupported method: " + left + (left == null ? "" : " / " + left.getClass()));
	}

	/**
	 * 取得方法执行的参数
	 */
	@SuppressWarnings("unchecked")
	private List<Object> fetchParam(ELContext ec) {
		List<Object> rvals = new ArrayList<Object>();
		if (right != null) {
			if (right instanceof CommaOp) {
				rvals = (List<Object>)((CommaOp)right).calculate(ec);
			} else {
				rvals.add(calculateItem(ec, right));
			}
		}

		if (!rvals.isEmpty()) {
			for (int i = 0; i < rvals.size(); i++) {
				if (rvals.get(i) instanceof Operator) {
					rvals.set(i, ((Operator)rvals.get(i)).calculate(ec));
				}
			}
		}
		return rvals;
	}

	public String operator() {
		return "method";
	}

	public String toString() {
		return super.toString() + "(" + params + ")";
	}
}
