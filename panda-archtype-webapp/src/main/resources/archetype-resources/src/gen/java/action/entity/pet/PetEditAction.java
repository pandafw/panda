package ${package}.action.entity.pet;

import panda.app.action.crud.GenericEditAction;
import panda.dao.query.DataQuery;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.annotation.validate.RequiredValidate;
import panda.mvc.annotation.validate.VisitValidate;
import panda.mvc.view.Views;
import panda.net.http.HttpMethod;

import ${package}.entity.Pet;
import ${package}.entity.query.PetQuery;

@At("/pet")
public class PetEditAction extends GenericEditAction<Pet> {

	/**
	 * Constructor
	 */
	public PetEditAction() {
		setType(Pet.class);
		setDisplayFields(Pet.ID, Pet.NAME, Pet.GENDER, Pet.BIRTHDAY, Pet.AMOUNT, Pet.PRICE, Pet.SHOP_NAME, Pet.SHOP_TELEPHONE, Pet.SHOP_LINK, Pet.DESCRIPTION, Pet.STATUS, Pet.UPDATED_AT, Pet.UPDATED_BY, Pet.UPDATED_BY_NAME, Pet.UPDATED_BY_USER);
	}


	/*----------------------------------------------------------------------*
	 * Joins
	 *----------------------------------------------------------------------*/
	/**
	 * add query joins
	 * @param dq data query
	 */
	@Override
	protected void addQueryJoins(DataQuery<Pet> dq) {
		super.addQueryJoins(dq);

		PetQuery eq = new PetQuery(dq);
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
	public Object view(@Param Pet key) {
		return super.view(key);
	}

	/**
	 * view_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~view", error="sftl:~view")
	public Object view_input(@Param Pet data) {
		return super.view_input(data);
	}

	/**
	 * view_json
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SJSON)
	public Object view_json(@Param Pet key) {
		return super.view(key);
	}

	/**
	 * view_xml
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SXML)
	public Object view_xml(@Param Pet key) {
		return super.view(key);
	}

	/**
	 * print
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object print(@Param Pet key) {
		return super.print(key);
	}

	/**
	 * print_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~print", error="sftl:~print")
	public Object print_input(@Param Pet data) {
		return super.print_input(data);
	}

	/**
	 * copy
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object copy(@Param Pet key) {
		return super.copy(key);
	}

	/**
	 * copy_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~copy", error="sftl:~copy")
	public Object copy_input(@Param Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.copy_execute(data);
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
	public Object add_input(@Param Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.add_execute(data);
	}

	/**
	 * add_json
	 * @param data the input data
	 * @return result
	 */
	@At
	@To(Views.SJSON)
	public Object add_json(@Param 
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.add_execute(data, true);
	}

	/**
	 * add_xml
	 * @param data the input data
	 * @return result
	 */
	@At
	@To(Views.SXML)
	public Object add_xml(@Param 
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.add_execute(data, true);
	}

	/**
	 * edit
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object edit(@Param Pet key) {
		return super.edit(key);
	}

	/**
	 * edit_input
	 * @param data the input data
	 * @return result or view
	 */
	@At
	@To(value="sftl:~edit", error="sftl:~edit")
	public Object edit_input(@Param Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
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
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.edit_execute(data);
	}

	/**
	 * edit_json
	 * @param data the input data
	 * @return result
	 */
	@At
	@To(Views.SJSON)
	public Object edit_json(@Param 
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.edit_execute(data, true);
	}

	/**
	 * edit_xml
	 * @param data the input data
	 * @return result
	 */
	@At
	@To(Views.SXML)
	public Object edit_xml(@Param 
			@RequiredValidate(fields={ "name", "amount" })
			@VisitValidate
			Pet data) {
		return super.edit_execute(data, true);
	}

	/**
	 * delete
	 * @param key the input key
	 * @return result or view
	 */
	@At
	@To(value=Views.SFTL, error=Views.SFTL)
	public Object delete(@Param Pet key) {
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
	public Object delete_execute(@Param Pet key) {
		return super.delete_execute(key);
	}

	/**
	 * delete_json
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SJSON)
	public Object delete_json(@Param Pet key) {
		return super.delete_execute(key, true);
	}

	/**
	 * delete_xml
	 * @param key the input key
	 * @return result
	 */
	@At
	@To(Views.SXML)
	public Object delete_xml(@Param Pet key) {
		return super.delete_execute(key, true);
	}

}

