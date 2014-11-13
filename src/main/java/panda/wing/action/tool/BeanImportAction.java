package panda.wing.action.tool;

import panda.wing.util.AppActionAssist;

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
