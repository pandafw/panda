package panda.app.action.property;

import panda.app.action.crud.GenericImportAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Property;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/property")
@Auth(AUTH.SUPER)
public class PropertyImportAction extends GenericImportAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyImportAction() {
		setType(Property.class);
		setDisplayFields(Property.ID, Property.CLAZZ, Property.LOCALE, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UPDATED_AT, Property.UPDATED_BY);
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

