package panda.wing.action.resource;

import panda.mvc.View;
import panda.mvc.annotation.At;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.view.Err;
import panda.mvc.annotation.view.Ok;
import panda.mvc.validation.Validators;
import panda.mvc.validation.annotation.Validate;
import panda.mvc.validation.annotation.Validates;
import panda.wing.action.GenericEditAction;
import panda.wing.entity.Resource;

public abstract class ResourceEditAction extends GenericEditAction<Resource> {

	/**
	 * Constructor
	 */
	public ResourceEditAction() {
		setType(Resource.class);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * view
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object view(@Param Resource key) {
		return super.view(key);
	}

	/**
	 * view_input
	 */
	@At
	@Ok("sftl:~view")
	@Err("sftl:~view")
	public Object view_input(@Param Resource data) {
		return super.view_input(data);
	}

	/**
	 * print
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object print(@Param Resource key) {
		return super.print(key);
	}

	/**
	 * print_input
	 */
	@At
	@Ok("sftl:~print")
	@Err("sftl:~print")
	public Object print_input(@Param Resource data) {
		return super.print_input(data);
	}

	/**
	 * add
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object add() {
		return super.add();
	}

	/**
	 * add_input
	 */
	@At
	@Ok("sftl:~add")
	@Err("sftl:~add")
	public Object add_input(@Param Resource data) {
		return super.add_input(data);
	}

	/**
	 * add_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~add")
	public Object add_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.add_confirm(data);
	}

	/**
	 * add_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~add")
	public Object add_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.add_execute(data);
	}

	/**
	 * copy
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object copy(@Param Resource key) {
		return super.copy(key);
	}

	/**
	 * copy_input
	 */
	@At
	@Ok("sftl:~copy")
	@Err("sftl:~copy")
	public Object copy_input(@Param Resource data) {
		return super.copy_input(data);
	}

	/**
	 * copy_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~copy")
	public Object copy_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.copy_confirm(data);
	}

	/**
	 * copy_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~copy")
	public Object copy_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.copy_execute(data);
	}

	/**
	 * edit
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object edit(@Param Resource key) {
		return super.edit(key);
	}

	/**
	 * edit_input
	 */
	@At
	@Ok("sftl:~edit")
	@Err("sftl:~edit")
	public Object edit_input(@Param Resource data) {
		return super.edit_input(data);
	}

	/**
	 * edit_confirm
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~edit")
	public Object edit_confirm(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.edit_confirm(data);
	}

	/**
	 * edit_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:~edit")
	public Object edit_execute(@Param @Validates({
			@Validate(value=Validators.REQUIRED, params="fields: [ 'clazz', 'language', 'country' ]", msgId=Validators.MSGID_REQUIRED),
			@Validate(value=Validators.VISIT)
			}) Resource data) {
		return super.edit_execute(data);
	}

	/**
	 * delete
	 */
	@At
	@Ok(View.SFTL)
	@Err(View.SFTL)
	public Object delete(@Param Resource key) {
		return super.delete(key);
	}

	/**
	 * delete_execute
	 */
	@At
	@Ok(View.SFTL)
	@Err("sftl:delete")
	public Object delete_execute(@Param Resource key) {
		return super.delete_execute(key);
	}

}

