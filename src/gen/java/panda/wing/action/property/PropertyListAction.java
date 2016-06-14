package panda.wing.action.property;

import java.util.ArrayList;
import java.util.List;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.bean.Queryer;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.view.tag.ListColumn;
import panda.wing.action.crud.GenericListAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Property;

@At("${super_context}/property")
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
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object list(@Param @Validates Queryer qr) {
		return super.list(qr);
	}
	
	/**
	 * list_csv
	 */
	@At
	@Ok(View.FTL)
	@Err(View.FTL)
	public Object list_csv(@Param @Validates Queryer qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField("id")) {
			ListColumn lc = new ListColumn();
			lc.name = "id";
			lc.header = getFieldLabel("id");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("clazz")) {
			ListColumn lc = new ListColumn();
			lc.name = "clazz";
			lc.header = getFieldLabel("clazz");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("language")) {
			ListColumn lc = new ListColumn();
			lc.name = "language";
			lc.header = getFieldLabel("language");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getLocaleLanguageMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("country")) {
			ListColumn lc = new ListColumn();
			lc.name = "country";
			lc.header = getFieldLabel("country");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getLocaleCountryMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("name")) {
			ListColumn lc = new ListColumn();
			lc.name = "name";
			lc.header = getFieldLabel("name");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("value")) {
			ListColumn lc = new ListColumn();
			lc.name = "value";
			lc.header = getFieldLabel("value");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("memo")) {
			ListColumn lc = new ListColumn();
			lc.name = "memo";
			lc.header = getFieldLabel("memo");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("status")) {
			ListColumn lc = new ListColumn();
			lc.name = "status";
			lc.header = getFieldLabel("status");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().getDataStatusMap();
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("uusid")) {
			ListColumn lc = new ListColumn();
			lc.name = "uusid";
			lc.header = getFieldLabel("uusid");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("utime")) {
			ListColumn lc = new ListColumn();
			lc.name = "utime";
			lc.header = getFieldLabel("utime");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.list_csv(qr, columns);
	}
	
	/**
	 * list_print
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object list_print(@Param @Validates Queryer qr) {
		return super.list_print(qr);
	}
	
	/**
	 * list_json
	 */
	@At
	@Ok(View.JSON)
	@Err(View.JSON)
	public Object list_json(@Param @Validates Queryer qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 */
	@At
	@Ok(View.XML)
	@Err(View.XML)
	public Object list_xml(@Param @Validates Queryer qr) {
		return super.list_xml(qr);
	}
	
}

