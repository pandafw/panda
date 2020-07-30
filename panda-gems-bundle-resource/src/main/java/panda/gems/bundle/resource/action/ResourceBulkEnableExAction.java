package panda.gems.bundle.resource.action;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.dao.query.DataQuery;
import panda.gems.bundle.resource.entity.Resource;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/resource")
@Auth(AUTH.SUPER)
public class ResourceBulkEnableExAction extends ResourceBulkEnableAction {
	public ResourceBulkEnableExAction() {
		super();
	}

	@Override
	protected Resource getBulkUpdateSample(List<Resource> dataList, DataQuery<Resource> dq) {
		Resource d = new Resource();
		d.setStatus(VAL.STATUS_ACTIVE);

		dq.excludeAll().include(Resource.STATUS);

		return d;
	}

}
