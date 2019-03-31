package panda.app.action.resource;

import panda.app.action.crud.GenericImportAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Resource;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/resource")
@Auth(AUTH.SUPER)
public class ResourceImportAction extends GenericImportAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceImportAction() {
		setType(Resource.class);
		addDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LOCALE, Resource.SOURCE, Resource.STATUS, Resource.UPDATED_AT, Resource.UPDATED_BY, Resource.UPDATED_BY_USER);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * importx
	 * @param arg argument
	 * @return result or view
	 */
	@At("import")
	@To(value=Views.SFTL, error=Views.SFTL)
	@TokenProtect
	public Object importx(@Param Arg arg) {
		return super.importx(arg);
	}
	
}

