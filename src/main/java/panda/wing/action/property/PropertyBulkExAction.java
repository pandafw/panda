package panda.wing.action.property;

import java.util.List;

import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.constant.RC;
import panda.wing.entity.Property;

@At("${super_context}/property")
@Auth(AUTH.SUPER)
public class PropertyBulkExAction extends PropertyBulkAction {

	/**
	 * Constructor
	 */
	public PropertyBulkExAction() {
	}

	private void addReloadMessage() {
		addApplicationMessage(RC.MESSAGE_PROPERTY_UPDATED);
	}

	@Override
	protected int deleteDataList(List<Property> dataList) {
		int cnt = super.deleteDataList(dataList);

		if (cnt > 0) {
	//		for (Resource data : dataList) {
	//			if (!Application.getDatabaseResourceLoader().putResource(data.getClazz(),
	//				data.getLanguage(), data.getCountry(), null, data.getName(), data.getValue())) {
	//				addReloadMessage();
	//				break;
	//			}
	//		}
			addReloadMessage();
		}
		
		return cnt;
	}
}