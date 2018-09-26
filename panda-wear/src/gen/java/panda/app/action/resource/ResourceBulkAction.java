package panda.app.action.resource;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Resource;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${super_path}/resource")
@Auth(AUTH.SUPER)
public class ResourceBulkAction extends GenericBulkAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceBulkAction() {
		setType(Resource.class);
		addDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LANGUAGE, Resource.COUNTRY, Resource.SOURCE, Resource.STATUS, Resource.UPDATED_AT, Resource.UPDATED_BY, Resource.UPDATED_BY_USER);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

