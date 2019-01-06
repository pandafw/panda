package panda.app.action.resource;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.entity.Resource;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

public abstract class ResourceBulkDisableAction extends GenericBulkAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceBulkDisableAction() {
		setType(Resource.class);
		addDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LANGUAGE, Resource.COUNTRY, Resource.SOURCE, Resource.STATUS, Resource.UPDATED_AT, Resource.UPDATED_BY, Resource.UPDATED_BY_USER);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdisable
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdisable(@Param Map<String, String[]> args) {
		return super.bupdate(args);
	}

	/**
	 * bdisable_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error="sftl:~bdisable")
	public Object bdisable_execute(@Param Map<String, String[]> args) {
		return super.bupdate_execute(args);
	}
	
}

