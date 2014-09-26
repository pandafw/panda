package panda.wing.mvc;

import panda.mvc.ActionContext;

public class ActionContext2 extends ActionContext {

	private ActionAssist assist;
	private ActionConsts consts;
	
	/**
	 * @return the assist
	 */
	public ActionAssist getAssist() {
		if (assist == null) {
			assist = getIoc().get(ActionAssist.class);
		}
		return assist;
	}

	/**
	 * @return the consts
	 */
	public ActionConsts getConsts() {
		if (consts == null) {
			consts = getIoc().get(ActionConsts.class);
		}
		return consts;
	}
	
	
}
