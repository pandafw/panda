package panda.gems.users.action;

import panda.app.action.crud.GenericEditAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.dao.query.DataQuery;
import panda.gems.users.entity.User;
import panda.gems.users.entity.query.UserQuery;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;

@At("${!!super_path|||'/super'}/user")
@Auth(AUTH.SUPER)
public class UserViewAction extends GenericEditAction<User> {

	/**
	 * Constructor
	 */
	public UserViewAction() {
		setType(User.class);
		setDisplayFields(User.ID, User.NAME, User.EMAIL, User.PASSWORD, User.ROLE, User.STATUS, User.CREATED_AT, User.CREATED_BY, User.CREATED_BY_NAME, User.CREATED_BY_USER, User.UPDATED_AT, User.UPDATED_BY, User.UPDATED_BY_NAME, User.UPDATED_BY_USER);
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
	 * view
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object view(@Param User key) {
		return super.view(key);
	}

	/**
	 * view_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~view", error="sftl:~view")
	public Object view_input(@Param User data) {
		return super.view_input(data);
	}

	/**
	 * print
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object print(@Param User key) {
		return super.print(key);
	}

	/**
	 * print_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~print", error="sftl:~print")
	public Object print_input(@Param User data) {
		return super.print_input(data);
	}

}

