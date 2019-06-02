package panda.app.action.property;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Property;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!super_path|||'/super'}/property")
@Auth(AUTH.SUPER)
public class PropertyListAction extends GenericListAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyListAction() {
		setType(Property.class);
		setDisplayFields(Property.ID, Property.CLAZZ, Property.LOCALE, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UPDATED_AT, Property.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object list(@Param @VisitValidate Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_print
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object list_print(@Param @VisitValidate Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * expo_csv
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object expo_csv(@Param @VisitValidate QueryerEx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField(Property.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.ID;
			lc.header = getFieldLabel(Property.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Property.CLAZZ)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.CLAZZ;
			lc.header = getFieldLabel(Property.CLAZZ);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Property.LOCALE)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.LOCALE;
			lc.header = getFieldLabel(Property.LOCALE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("appLocaleMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Property.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.NAME;
			lc.header = getFieldLabel(Property.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Property.VALUE)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.VALUE;
			lc.header = getFieldLabel(Property.VALUE);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Property.MEMO)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.MEMO;
			lc.header = getFieldLabel(Property.MEMO);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Property.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.STATUS;
			lc.header = getFieldLabel(Property.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Property.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.UPDATED_AT;
			lc.header = getFieldLabel(Property.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Property.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Property.UPDATED_BY;
			lc.header = getFieldLabel(Property.UPDATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.updatedByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.expo_csv(qr, columns);
	}
	
	/**
	 * expo_json
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SJSON)
	public Object expo_json(@Param @VisitValidate QueryerEx qr) {
		return super.expo_json(qr);
	}
	
	/**
	 * expo_xml
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SXML)
	public Object expo_xml(@Param @VisitValidate QueryerEx qr) {
		return super.expo_xml(qr);
	}
	
}

