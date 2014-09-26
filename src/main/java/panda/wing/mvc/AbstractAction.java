package panda.wing.mvc;

import panda.ioc.annotation.IocInject;
import panda.mvc.util.ActionSupport;

/**
 * ActionSupport
 */
public class AbstractAction extends ActionSupport {
	/*------------------------------------------------------------
	 * bean
	 *------------------------------------------------------------*/
	@IocInject
	protected ActionAssist assist;
	
	@IocInject
	protected ActionConsts consts;

	/**
	 * @return the assist
	 */
	public ActionAssist getAssist() {
		return assist;
	}

	/**
	 * @return the consts
	 */
	public ActionConsts getConsts() {
		return consts;
	}
}
