package panda.mvc.processor;

import panda.mvc.ActionContext;
import panda.mvc.Processor;

/**
 * 抽象的Processor实现. 任何Processor实现都应该继承这个类,以获取正确的执行逻辑.
 */
public abstract class AbstractProcessor implements Processor {
	/**
	 * 继续执行下一个Processor
	 * <p/>
	 * <b>一般情形下都不应该覆盖这个方法<b>
	 * 
	 * @param ac 执行方法的上下文
	 * @throws Throwable
	 */
	protected void doNext(ActionContext ac) throws Throwable {
		ac.getChain().doNext(ac);
	}
}
