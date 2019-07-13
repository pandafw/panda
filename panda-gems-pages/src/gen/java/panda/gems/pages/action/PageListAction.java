package panda.gems.pages.action;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.pages.entity.Page;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!admin_path|||'/admin'}/pages")
@Auth(AUTH.ADMIN)
public class PageListAction extends GenericListAction<Page> {

	/**
	 * Constructor
	 */
	public PageListAction() {
		setType(Page.class);
		setDisplayFields(Page.ID, Page.THUMBNAIL, Page.SLUG, Page.TITLE, Page.TAG, Page.PUBLISH_DATE, Page.STATUS, Page.CREATED_AT, Page.CREATED_BY, Page.UPDATED_AT, Page.UPDATED_BY);
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
		if (displayField(Page.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.ID;
			lc.header = getFieldLabel(Page.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Page.THUMBNAIL)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.THUMBNAIL;
			lc.header = getFieldLabel(Page.THUMBNAIL);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "string";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.SLUG)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.SLUG;
			lc.header = getFieldLabel(Page.SLUG);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Page.TITLE)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.TITLE;
			lc.header = getFieldLabel(Page.TITLE);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Page.TAG)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.TAG;
			lc.header = getFieldLabel(Page.TAG);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Page.PUBLISH_DATE)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.PUBLISH_DATE;
			lc.header = getFieldLabel(Page.PUBLISH_DATE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.STATUS;
			lc.header = getFieldLabel(Page.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.CREATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.CREATED_AT;
			lc.header = getFieldLabel(Page.CREATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.CREATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.CREATED_BY;
			lc.header = getFieldLabel(Page.CREATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.createdByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.UPDATED_AT;
			lc.header = getFieldLabel(Page.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Page.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = Page.UPDATED_BY;
			lc.header = getFieldLabel(Page.UPDATED_BY);
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

