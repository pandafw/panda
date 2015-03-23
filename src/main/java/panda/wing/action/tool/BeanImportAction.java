package panda.wing.action.tool;

import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.util.AppActionAssist;

@At("/admin")
@Auth("sysadmin")
public class BeanImportAction extends DataImportAction {
	/**
	 * @return the assist
	 */
	protected AppActionAssist assist() {
		return (AppActionAssist)super.getAssist();
	}

	protected void prepareData(Object data) {
		assist().initCommonFields(data);
	}
}
