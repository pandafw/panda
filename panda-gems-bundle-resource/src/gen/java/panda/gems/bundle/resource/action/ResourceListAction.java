package panda.gems.bundle.resource.action;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.bundle.resource.entity.Resource;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!super_path|||'/super'}/resource")
@Auth(AUTH.SUPER)
public class ResourceListAction extends GenericListAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceListAction() {
		setType(Resource.class);
		setDisplayFields(Resource.ID, Resource.CLAZZ, Resource.LOCALE, Resource.SOURCE, Resource.STATUS, Resource.UPDATED_AT, Resource.UPDATED_BY);
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
		if (displayField(Resource.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.ID;
			lc.header = getFieldLabel(Resource.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Resource.CLAZZ)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.CLAZZ;
			lc.header = getFieldLabel(Resource.CLAZZ);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Resource.LOCALE)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.LOCALE;
			lc.header = getFieldLabel(Resource.LOCALE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("appLocaleMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Resource.SOURCE)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.SOURCE;
			lc.header = getFieldLabel(Resource.SOURCE);
			lc.hidden = true;
			columns.add(lc);
		}
		if (displayField(Resource.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.STATUS;
			lc.header = getFieldLabel(Resource.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Resource.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.UPDATED_AT;
			lc.header = getFieldLabel(Resource.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Resource.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Resource.UPDATED_BY;
			lc.header = getFieldLabel(Resource.UPDATED_BY);
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

