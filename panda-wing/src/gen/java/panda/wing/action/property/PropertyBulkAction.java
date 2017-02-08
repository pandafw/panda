package panda.wing.action.property;

import java.util.Map;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.wing.action.crud.GenericBulkAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Property;

@At("${super_path}/property")
@Auth(AUTH.SUPER)
public class PropertyBulkAction extends GenericBulkAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyBulkAction() {
		setType(Property.class);
		addDisplayFields(Property.ID, Property.CLAZZ, Property.LANGUAGE, Property.COUNTRY, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UUSID, Property.UTIME);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * bdelete
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object bdelete(@Param Map<String, String[]> args) {
		return super.bdelete(args);
	}

	/**
	 * bdelete_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~bdelete")
	public Object bdelete_execute(@Param Map<String, String[]> args) {
		return super.bdelete_execute(args);
	}
	
}

