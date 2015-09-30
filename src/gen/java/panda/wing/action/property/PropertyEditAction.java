package panda.wing.action.property;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validates;
import panda.wing.action.GenericEditAction;
import panda.wing.entity.Property;

public abstract class PropertyEditAction extends GenericEditAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyEditAction() {
		setType(Property.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
}

