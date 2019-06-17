package panda.gems.users.entity.query;

import panda.app.entity.query.SCUQuery;
import panda.dao.entity.Entities;
import panda.dao.query.ComparableCondition;
import panda.dao.query.DataQuery;
import panda.dao.query.Query;
import panda.dao.query.StringCondition;
import panda.gems.users.entity.User;

public class UserQuery extends SCUQuery<User, UserQuery> {
	/**
	 * Constructor
	 */
	public UserQuery() {
		super(Entities.i().getEntity(User.class));
	}

	/**
	 * Constructor
	 * @param query the query to set
	 */
	public UserQuery(DataQuery<User> query) {
		super(query);
	}

	//----------------------------------------------------------------------
	// field conditions
	//----------------------------------------------------------------------
	/**
	 * @return condition of id
	 */
	public ComparableCondition<UserQuery, Long> id() {
		return new ComparableCondition<UserQuery, Long>(this, User.ID);
	}

	/**
	 * @return condition of name
	 */
	public StringCondition<UserQuery> name() {
		return new StringCondition<UserQuery>(this, User.NAME);
	}

	/**
	 * @return condition of email
	 */
	public StringCondition<UserQuery> email() {
		return new StringCondition<UserQuery>(this, User.EMAIL);
	}

	/**
	 * @return condition of password
	 */
	public StringCondition<UserQuery> password() {
		return new StringCondition<UserQuery>(this, User.PASSWORD);
	}

	/**
	 * @return condition of role
	 */
	public StringCondition<UserQuery> role() {
		return new StringCondition<UserQuery>(this, User.ROLE);
	}


	//----------------------------------------------------------------------
	// auto joins
	//----------------------------------------------------------------------
	/**
	 * auto left join CU
	 * @return this
	 */
	public UserQuery autoLeftJoinCU() {
		autoLeftJoin(User._JOIN_CU_);
		return this;
	}

	/**
	 * auto left join CU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoLeftJoinCU(Query<?> jq) {
		autoLeftJoin(User._JOIN_CU_, jq);
		return this;
	}

	/**
	 * auto right join CU
	 * @return this
	 */
	public UserQuery autoRightJoinCU() {
		autoRightJoin(User._JOIN_CU_);
		return this;
	}

	/**
	 * auto right join CU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoRightJoinCU(Query<?> jq) {
		autoRightJoin(User._JOIN_CU_, jq);
		return this;
	}

	/**
	 * auto inner join CU
	 * @return this
	 */
	public UserQuery autoInnerJoinCU() {
		autoInnerJoin(User._JOIN_CU_);
		return this;
	}

	/**
	 * auto inner join CU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoInnerJoinCU(Query<?> jq) {
		autoInnerJoin(User._JOIN_CU_, jq);
		return this;
	}

	/**
	 * auto join CU
	 * @return this
	 */
	public UserQuery autoJoinCU() {
		autoJoin(User._JOIN_CU_);
		return this;
	}

	/**
	 * auto join CU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoJoinCU(Query<?> jq) {
		autoJoin(User._JOIN_CU_, jq);
		return this;
	}

	/**
	 * auto left join UU
	 * @return this
	 */
	public UserQuery autoLeftJoinUU() {
		autoLeftJoin(User._JOIN_UU_);
		return this;
	}

	/**
	 * auto left join UU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoLeftJoinUU(Query<?> jq) {
		autoLeftJoin(User._JOIN_UU_, jq);
		return this;
	}

	/**
	 * auto right join UU
	 * @return this
	 */
	public UserQuery autoRightJoinUU() {
		autoRightJoin(User._JOIN_UU_);
		return this;
	}

	/**
	 * auto right join UU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoRightJoinUU(Query<?> jq) {
		autoRightJoin(User._JOIN_UU_, jq);
		return this;
	}

	/**
	 * auto inner join UU
	 * @return this
	 */
	public UserQuery autoInnerJoinUU() {
		autoInnerJoin(User._JOIN_UU_);
		return this;
	}

	/**
	 * auto inner join UU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoInnerJoinUU(Query<?> jq) {
		autoInnerJoin(User._JOIN_UU_, jq);
		return this;
	}

	/**
	 * auto join UU
	 * @return this
	 */
	public UserQuery autoJoinUU() {
		autoJoin(User._JOIN_UU_);
		return this;
	}

	/**
	 * auto join UU
	 * @param jq join table query
	 * @return this
	 */
	public UserQuery autoJoinUU(Query<?> jq) {
		autoJoin(User._JOIN_UU_, jq);
		return this;
	}

}

