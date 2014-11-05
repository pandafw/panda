package panda.wing.action.property;

import java.util.List;
import panda.wing.action.BaseEntityDaoAction;
import panda.wing.entity.Property;

public class PropertyAction extends BaseEntityDaoAction<Property> {

	/**
	 * Constructor
	 */
	public PropertyAction() {
		setType(Property.class);
	}

	/*----------------------------------------------------------------------*
	 * Getter & Setter
	 *----------------------------------------------------------------------*/
	/**
	 * @return the property
	 */
	public Property getD() {
		return super.getData();
	}

	/**
	 * @param property the property to set
	 */
	public void setD(Property property) {
		super.setData(property);
	}

	/**
	 * @return the property list
	 */
	public List<Property> getDs() {
		return super.getDataList();
	}

	/**
	 * @param propertyList the propertyList to set
	 */
	public void setDs(List<Property> propertyList) {
		super.setDataList(propertyList);
	}


	/*----------------------------------------------------------------------*
	 * Actions
	 *----------------------------------------------------------------------*/
	/**
	 * list
	 */
	public Object list() {
		return super.list();
	}
	
	/**
	 * list_csv
	 */
	public Object list_csv() {
		return super.list_csv();
	}
	
	/**
	 * list_print
	 */
	public Object list_print() {
		return super.list_print();
	}
	
	/**
	 * bdelete
	 */
	public Object bdelete() {
		return super.bdelete();
	}

	/**
	 * bdelete_execute
	 */
	public Object bdelete_execute() {
		return super.bdelete_execute();
	}
	
	/**
	 * view
	 */
	public Object view() {
		return super.view();
	}

	/**
	 * view_input
	 */
	public Object view_input() {
		return super.view_input();
	}

	/**
	 * print
	 */
	public Object print() {
		return super.print();
	}

	/**
	 * print_input
	 */
	public Object print_input() {
		return super.print_input();
	}

	/**
	 * copy
	 */
	public Object copy() {
		return super.copy();
	}

	/**
	 * copy_input
	 */
	public Object copy_input() {
		return super.copy_input();
	}

	/**
	 * copy_confirm
	 */
	public Object copy_confirm() {
		return super.copy_confirm();
	}

	/**
	 * copy_execute
	 */
	public Object copy_execute() {
		return super.copy_execute();
	}

	/**
	 * insert
	 */
	public Object insert() {
		return super.insert();
	}

	/**
	 * insert_input
	 */
	public Object insert_input() {
		return super.insert_input();
	}

	/**
	 * insert_confirm
	 */
	public Object insert_confirm() {
		return super.insert_confirm();
	}

	/**
	 * insert_execute
	 */
	public Object insert_execute() {
		return super.insert_execute();
	}

	/**
	 * update
	 */
	public Object update() {
		return super.update();
	}

	/**
	 * update_input
	 */
	public Object update_input() {
		return super.update_input();
	}

	/**
	 * update_confirm
	 */
	public Object update_confirm() {
		return super.update_confirm();
	}

	/**
	 * update_execute
	 */
	public Object update_execute() {
		return super.update_execute();
	}

	/**
	 * delete
	 */
	public Object delete() {
		return super.delete();
	}

	/**
	 * delete_execute
	 */
	public Object delete_execute() {
		return super.delete_execute();
	}

}