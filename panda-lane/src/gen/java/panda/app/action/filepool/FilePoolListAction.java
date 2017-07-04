package panda.app.action.filepool;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerOx;
import panda.mvc.validation.annotation.Validates;
import panda.mvc.view.util.ListColumn;
import panda.vfs.dao.DaoFileItem;

@At("${super_path}/filepool")
@Auth(AUTH.SUPER)
public class FilePoolListAction extends GenericListAction<DaoFileItem> {

	/**
	 * Constructor
	 */
	public FilePoolListAction() {
		setType(DaoFileItem.class);
		addDisplayFields(DaoFileItem.ID, DaoFileItem.NAME, DaoFileItem.SIZE, DaoFileItem.DATE, DaoFileItem.FLAG);
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
	 * list_csv
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object list_csv(@Param @Validates QueryerOx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField("id")) {
			ListColumn lc = new ListColumn();
			lc.name = "id";
			lc.header = getFieldLabel("id");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("name")) {
			ListColumn lc = new ListColumn();
			lc.name = "name";
			lc.header = getFieldLabel("name");
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField("size")) {
			ListColumn lc = new ListColumn();
			lc.name = "size";
			lc.header = getFieldLabel("size");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "intcomma";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("date")) {
			ListColumn lc = new ListColumn();
			lc.name = "date";
			lc.header = getFieldLabel("date");
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "timestamp";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField("flag")) {
			ListColumn lc = new ListColumn();
			lc.name = "flag";
			lc.header = getFieldLabel("flag");
			lc.hidden = false;
			columns.add(lc);
		}
		return super.list_csv(qr, columns);
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
	public Object list_json(@Param @Validates QueryerOx qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 */
	@At
	@To(all=View.XML)
	public Object list_xml(@Param @Validates QueryerOx qr) {
		return super.list_xml(qr);
	}
	
}

