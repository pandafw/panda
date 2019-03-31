package panda.app.action.resource;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.app.entity.Resource;
import panda.dao.query.DataQuery;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/resource")
@Auth(AUTH.SUPER)
public class ResourceBulkDisableExAction extends ResourceBulkDisableAction {

	public ResourceBulkDisableExAction() {
		super();
	}

	@Override
	protected Resource getBulkUpdateSample(List<Resource> dataList, DataQuery<Resource> dq) {
		Resource d = new Resource();
		d.setStatus(VAL.STATUS_DISABLED);

		dq.excludeAll().include(Resource.STATUS);

		return d;
	}

}
