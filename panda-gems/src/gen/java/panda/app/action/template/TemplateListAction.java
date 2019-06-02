package panda.app.action.template;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.app.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!super_path|||'/super'}/template")
@Auth(AUTH.SUPER)
public class TemplateListAction extends GenericListAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateListAction() {
		setType(Template.class);
		setDisplayFields(Template.ID, Template.NAME, Template.LOCALE, Template.STATUS, Template.UPDATED_AT, Template.UPDATED_BY);
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
		if (displayField(Template.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.ID;
			lc.header = getFieldLabel(Template.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Template.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.NAME;
			lc.header = getFieldLabel(Template.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Template.LOCALE)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.LOCALE;
			lc.header = getFieldLabel(Template.LOCALE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("appLocaleMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Template.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.STATUS;
			lc.header = getFieldLabel(Template.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Template.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.UPDATED_AT;
			lc.header = getFieldLabel(Template.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Template.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Template.UPDATED_BY;
			lc.header = getFieldLabel(Template.UPDATED_BY);
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

