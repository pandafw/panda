package panda.app.action.property;

import java.util.Map;
import panda.app.action.crud.GenericBulkAction;
import panda.app.entity.Property;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

public abstract class PropertyBulkEnableAction extends GenericBulkAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyBulkEnableAction() {
		setType(Property.class);
		setDisplayFields(Property.ID, Property.CLAZZ, Property.LOCALE, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UPDATED_AT, Property.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * benable
	 * @param args arguments
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object benable(@Param Map<String, String[]> args) {
		return super.bupdate(args);
	}

	/**
	 * benable_execute
	 * @param args arguments
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~benable")
	@TokenProtect
	public Object benable_execute(@Param Map<String, String[]> args) {
		return super.bupdate_execute(args);
	}
	
}

