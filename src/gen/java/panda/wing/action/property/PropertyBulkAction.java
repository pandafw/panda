package panda.wing.action.property;

import java.util.Map;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.GenericBulkAction;
import panda.wing.entity.Property;

public abstract class PropertyBulkAction extends GenericBulkAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyBulkAction() {
		setType(Property.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

