package panda.gems.bundle.template.action;

import panda.app.action.crud.GenericEditAction;
import panda.gems.bundle.template.entity.Template;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

public abstract class TemplateEditAction extends GenericEditAction<Template> {

	/**
	 * Constructor
	 */
	public TemplateEditAction() {
		setType(Template.class);
		setDisplayFields(Template.ID, Template.NAME, Template.LOCALE, Template.SOURCE, Template.STATUS, Template.UPDATED_AT, Template.UPDATED_BY, Template.UPDATED_BY_NAME, Template.UPDATED_BY_USER);
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
	public Object view(@Param Template key) {
		return super.view(key);
	}

	/**
	 * view_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~view", error="sftl:~view")
	public Object view_input(@Param Template data) {
		return super.view_input(data);
	}

	/**
	 * view_json
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SJSON)
	public Object view_json(@Param Template key) {
		return super.view(key);
	}

	/**
	 * view_xml
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SXML)
	public Object view_xml(@Param Template key) {
		return super.view(key);
	}

	/**
	 * print
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object print(@Param Template key) {
		return super.print(key);
	}

	/**
	 * print_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~print", error="sftl:~print")
	public Object print_input(@Param Template data) {
		return super.print_input(data);
	}

	/**
	 * add
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object add() {
		return super.add();
	}

	/**
	 * add_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~add", error="sftl:~add")
	public Object add_input(@Param Template data) {
		return super.add_input(data);
	}

	/**
	 * add_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~add")
	@TokenProtect
	public Object add_confirm(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.add_confirm(data);
	}

	/**
	 * add_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~add")
	@TokenProtect
	public Object add_execute(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.add_execute(data);
	}

	/**
	 * copy
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object copy(@Param Template key) {
		return super.copy(key);
	}

	/**
	 * copy_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~copy", error="sftl:~copy")
	public Object copy_input(@Param Template data) {
		return super.copy_input(data);
	}

	/**
	 * copy_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~copy")
	@TokenProtect
	public Object copy_confirm(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.copy_confirm(data);
	}

	/**
	 * copy_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~copy")
	@TokenProtect
	public Object copy_execute(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.copy_execute(data);
	}

	/**
	 * edit
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object edit(@Param Template key) {
		return super.edit(key);
	}

	/**
	 * edit_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~edit", error="sftl:~edit")
	public Object edit_input(@Param Template data) {
		return super.edit_input(data);
	}

	/**
	 * edit_confirm
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~edit")
	@TokenProtect
	public Object edit_confirm(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.edit_confirm(data);
	}

	/**
	 * edit_execute
	 * @param data the input data
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~edit")
	@TokenProtect
	public Object edit_execute(@Param 
			@RequiredValidate(fields={ "name", "locale" })
			@VisitValidate
			Template data) {
		return super.edit_execute(data);
	}

	/**
	 * delete
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object delete(@Param Template key) {
		return super.delete(key);
	}

	/**
	 * delete_execute
	 * @param key the input key
	 * @return result or view
	 */
	@At(method=HttpMethod.POST)
	@To(value=Views.SFTL, error="sftl:~delete")
	@TokenProtect
	public Object delete_execute(@Param Template key) {
		return super.delete_execute(key);
	}

}

