package panda.wing.action.resource;

import java.util.List;

import panda.mvc.annotation.At;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.constant.RC;
import panda.wing.entity.Resource;

@At("${super_context}/resource")
@Auth(AUTH.SUPER)
public class ResourceBulkExAction extends ResourceBulkAction {

	/**
	 * Constructor
	 */
	public ResourceBulkExAction() {
	}

	private void addReloadMessage() {
		addApplicationMessage(RC.MESSAGE_RESOURCE_UPDATED);
	}

	@Override
	protected int deleteDataList(List<Resource> dataList) {
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