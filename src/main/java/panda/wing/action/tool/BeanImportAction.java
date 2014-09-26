package panda.wing.action.tool;

import panda.wing.action.BaseActionAssist;

public class BeanImportAction extends DataImportAction {
	/**
	 * @return the assist
	 */
	protected BaseActionAssist assist() {
		return (BaseActionAssist)super.getAssist();
	}

	protected void prepareData(Object data) {
		assist().initCommonFields(data);
	}
}
