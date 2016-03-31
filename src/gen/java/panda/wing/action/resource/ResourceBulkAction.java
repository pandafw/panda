package panda.wing.action.resource;

import java.util.Map;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.wing.action.crud.GenericBulkAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Resource;

@At("${super_context}/resource")
@Auth(AUTH.SUPER)
public class ResourceBulkAction extends GenericBulkAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceBulkAction() {
		setType(Resource.class);
		addDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LANGUAGE, Resource.COUNTRY, Resource.SOURCE, Resource.STATUS, Resource.UUSID, Resource.UTIME);
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

