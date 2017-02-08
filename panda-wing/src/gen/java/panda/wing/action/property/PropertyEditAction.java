package panda.wing.action.property;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.wing.action.crud.GenericEditAction;
import panda.wing.auth.Auth;
import panda.wing.constant.AUTH;
import panda.wing.entity.Property;

@At("${super_path}/property")
@Auth(AUTH.SUPER)
public class PropertyEditAction extends GenericEditAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyEditAction() {
		setType(Property.class);
		addDisplayFields(Property.ID, Property.CLAZZ, Property.LANGUAGE, Property.COUNTRY, Property.NAME, Property.VALUE, Property.MEMO, Property.STATUS, Property.UUSID, Property.UUSNM, Property.UTIME);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * view
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object view(@Param Property key) {
		return super.view(key);
	}

	/**
	 * view_input
	 */
	@At
	@To(value="sftl:~view", error="sftl:~view")
	public Object view_input(@Param Property data) {
		return super.view_input(data);
	}

	/**
	 * print
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object print(@Param Property key) {
		return super.print(key);
	}

	/**
	 * print_input
	 */
	@At
	@To(value="sftl:~print", error="sftl:~print")
	public Object print_input(@Param Property data) {
		return super.print_input(data);
	}

	/**
	 * add
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object add() {
		return super.add();
	}

	/**
	 * add_input
	 */
	@At
	@To(value="sftl:~add", error="sftl:~add")
	public Object add_input(@Param Property data) {
		return super.add_input(data);
	}

	/**
	 * add_confirm
	 */
	@At
	@To(value=View.SFTL, error="sftl:~add")
	public Object add_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.add_confirm(data);
	}

	/**
	 * add_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~add")
	public Object add_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.add_execute(data);
	}

	/**
	 * copy
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object copy(@Param Property key) {
		return super.copy(key);
	}

	/**
	 * copy_input
	 */
	@At
	@To(value="sftl:~copy", error="sftl:~copy")
	public Object copy_input(@Param Property data) {
		return super.copy_input(data);
	}

	/**
	 * copy_confirm
	 */
	@At
	@To(value=View.SFTL, error="sftl:~copy")
	public Object copy_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.copy_confirm(data);
	}

	/**
	 * copy_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~copy")
	public Object copy_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.copy_execute(data);
	}

	/**
	 * edit
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object edit(@Param Property key) {
		return super.edit(key);
	}

	/**
	 * edit_input
	 */
	@At
	@To(value="sftl:~edit", error="sftl:~edit")
	public Object edit_input(@Param Property data) {
		return super.edit_input(data);
	}

	/**
	 * edit_confirm
	 */
	@At
	@To(value=View.SFTL, error="sftl:~edit")
	public Object edit_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.edit_confirm(data);
	}

	/**
	 * edit_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:~edit")
	public Object edit_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="{ fields: { 'clazz': '', 'language': '', 'country': '', 'name': '' } }", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Property data) {
		return super.edit_execute(data);
	}

	/**
	 * delete
	 */
	@At
	@To(value=View.SFTL, error=View.SFTL)
	public Object delete(@Param Property key) {
		return super.delete(key);
	}

	/**
	 * delete_execute
	 */
	@At
	@To(value=View.SFTL, error="sftl:delete")
	public Object delete_execute(@Param Property key) {
		return super.delete_execute(key);
	}

}

