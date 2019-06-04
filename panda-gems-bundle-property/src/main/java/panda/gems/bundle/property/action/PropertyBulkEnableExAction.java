package panda.gems.bundle.property.action;

import java.util.List;

import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.constant.VAL;
import panda.dao.query.DataQuery;
import panda.gems.bundle.property.action.PropertyBulkEnableAction;
import panda.gems.bundle.property.entity.Property;
import panda.mvc.annotation.At;

@At("${!!super_path|||'/super'}/property")
@Auth(AUTH.SUPER)
public class PropertyBulkEnableExAction extends PropertyBulkEnableAction {
	public PropertyBulkEnableExAction() {
		super();
	}

	@Override
	protected Property getBulkUpdateSample(List<Property> dataList, DataQuery<Property> dq) {
		Property d = new Property();
		d.setStatus(VAL.STATUS_ACTIVE);

		dq.excludeAll().include(Property.STATUS);

		return d;
	}

}
