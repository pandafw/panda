package panda.app.action.property;

import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Property;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validates;

@At("${super_path}/property")
@Auth(AUTH.SUPER)
public class PropertyListAction extends GenericListAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyListAction() {
		setType(Property.class);
		addDisplayFields(Property.ID, Property.CLAZZ, Property.LANGUAGE, Property.COUNTRY, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UUSID, Property.UTIME);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_print
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list_print(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * list_json
	 */
	@At
	@To(all=View.JSON)
	public Object list_json(@Param @Validates Queryer qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 */
	@At
	@To(all=View.XML)
	public Object list_xml(@Param @Validates Queryer qr) {
		return super.list_xml(qr);
	}
	
}

