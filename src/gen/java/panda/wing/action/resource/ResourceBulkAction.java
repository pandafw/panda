package panda.wing.action.resource;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.wing.action.GenericBulkAction;
import panda.wing.entity.Resource;

public abstract class ResourceBulkAction extends GenericBulkAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceBulkAction() {
		setType(Resource.class);
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
	public Object bdelete() {
		return super.bdelete();
	}

	/**
	 * bdelete_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object bdelete_execute() {
		return super.bdelete_execute();
	}
	
}

