package panda.gems.users.action;

import java.util.ArrayList;
import java.util.List;
import panda.app.action.crud.GenericListAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.dao.query.DataQuery;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.QueryerEx;
import panda.mvc.view.Views;
import panda.mvc.view.util.ListColumn;

@At("${!!admin_path|||'/admin'}/users")
@Auth(AUTH.ADMIN)
public class UserListAction extends GenericListAction<User> {

	/**
	 * Constructor
	 */
	public UserListAction() {
		setType(User.class);
		setDisplayFields(User.ID, User.NAME, User.EMAIL, User.ROLE, User.STATUS, User.CREATED_AT, User.CREATED_BY, User.UPDATED_AT, User.UPDATED_BY);
	}


	/*----------------------------------------------------------------------*
	 * Joins
	 *----------------------------------------------------------------------*/
	/**
	 * add query joins
	 * @param dq data query
	 */
	@Override
	protected void addQueryJoins(DataQuery<User> dq) {
		super.addQueryJoins(dq);

		UserQuery eq = new UserQuery(dq);
		eq.autoLeftJoinCU();
		eq.autoLeftJoinUU();
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
	 * list_json
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SJSON)
	public Object list_json(@Param @VisitValidate Queryer qr) {
		return super.list_json(qr);
	}
	
	/**
	 * list_xml
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(Views.SXML)
	public Object list_xml(@Param @VisitValidate Queryer qr) {
		return super.list_xml(qr);
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
		if (displayField(User.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = User.ID;
			lc.header = getFieldLabel(User.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = User.NAME;
			lc.header = getFieldLabel(User.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.EMAIL)) {
			ListColumn lc = new ListColumn();
			lc.name = User.EMAIL;
			lc.header = getFieldLabel(User.EMAIL);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.ROLE)) {
			ListColumn lc = new ListColumn();
			lc.name = User.ROLE;
			lc.header = getFieldLabel(User.ROLE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("authRoleMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = User.STATUS;
			lc.header = getFieldLabel(User.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.CREATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = User.CREATED_AT;
			lc.header = getFieldLabel(User.CREATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.CREATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = User.CREATED_BY;
			lc.header = getFieldLabel(User.CREATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.createdByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = User.UPDATED_AT;
			lc.header = getFieldLabel(User.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = User.UPDATED_BY;
			lc.header = getFieldLabel(User.UPDATED_BY);
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
	 * expo_xlsx
	 * @param qr queryer
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object expo_xlsx(@Param @VisitValidate QueryerEx qr) {
		List<ListColumn> columns = new ArrayList<ListColumn>();
		if (displayField(User.ID)) {
			ListColumn lc = new ListColumn();
			lc.name = User.ID;
			lc.header = getFieldLabel(User.ID);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.NAME)) {
			ListColumn lc = new ListColumn();
			lc.name = User.NAME;
			lc.header = getFieldLabel(User.NAME);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.EMAIL)) {
			ListColumn lc = new ListColumn();
			lc.name = User.EMAIL;
			lc.header = getFieldLabel(User.EMAIL);
			lc.hidden = false;
			columns.add(lc);
		}
		if (displayField(User.ROLE)) {
			ListColumn lc = new ListColumn();
			lc.name = User.ROLE;
			lc.header = getFieldLabel(User.ROLE);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("authRoleMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.STATUS)) {
			ListColumn lc = new ListColumn();
			lc.name = User.STATUS;
			lc.header = getFieldLabel(User.STATUS);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "code";
			lcf.codemap = consts().get("dataStatusMap");
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.CREATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = User.CREATED_AT;
			lc.header = getFieldLabel(User.CREATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.CREATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = User.CREATED_BY;
			lc.header = getFieldLabel(User.CREATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.createdByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.UPDATED_AT)) {
			ListColumn lc = new ListColumn();
			lc.name = User.UPDATED_AT;
			lc.header = getFieldLabel(User.UPDATED_AT);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "datetime";
			lc.format = lcf;
			columns.add(lc);
		}
		if (displayField(User.UPDATED_BY)) {
			ListColumn lc = new ListColumn();
			lc.name = User.UPDATED_BY;
			lc.header = getFieldLabel(User.UPDATED_BY);
			lc.hidden = false;
			ListColumn.Format lcf = new ListColumn.Format();
			lcf.type = "expr";
			lcf.expr = "top.updatedByUser";
			lc.format = lcf;
			columns.add(lc);
		}
		return super.expo_xlsx(qr, columns);
	}
	
}

