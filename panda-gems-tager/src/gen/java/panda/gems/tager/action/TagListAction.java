package panda.gems.tager.action;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.gems.tager.entity.Tag;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!admin_path|||'/admin'}/tags")
@Auth(AUTH.ADMIN)
public class TagListAction extends GenericListAction<Tag> {

	/**
	 * Constructor
	 */
	public TagListAction() {
		setType(Tag.class);
		setDisplayFields(Tag.ID, Tag.NAME, Tag.KIND, Tag.CODE);
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
		if (displayField(Tag.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = Tag.ID;
			lc.header = getFieldLabel(Tag.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Tag.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = Tag.NAME;
			lc.header = getFieldLabel(Tag.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(Tag.KIND)) {
			ListColumn lc = new ListColumn();
			lc.name = Tag.KIND;
			lc.header = getFieldLabel(Tag.KIND);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "string";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(Tag.CODE)) {
			ListColumn lc = new ListColumn();
			lc.name = Tag.CODE;
			lc.header = getFieldLabel(Tag.CODE);
			lc.hidden = false;
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

