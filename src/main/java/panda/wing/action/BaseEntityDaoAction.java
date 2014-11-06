package panda.wing.action;

import panda.wing.constant.RC;
import panda.wing.entity.Bean;
import panda.wing.entity.IUpdate;

/**
 * @param <T> data type
 */
public class BaseEntityDaoAction<T> extends AbstractEntityDaoAction<T> {
	/**
	 * Constructor
	 */
	public BaseEntityDaoAction() {
	}

	/**
	 * checkUpdated
	 * @param data data
	 * @param srcData source data
	 * @return true if check successfully
	 */
	@Override
	protected boolean checkUpdated(T data, T srcData) {
		if (data instanceof IUpdate) {
			IUpdate cb = (IUpdate)data;
			IUpdate sb = (IUpdate)srcData;
			if (Bean.isUpdated(cb, sb)) {
				cb.setUusid(sb.getUusid());
				cb.setUtime(sb.getUtime());
				addActionWarning(getText(RC.ERROR_DATA_IS_UPDATED, RC.ERROR_DATA_IS_UPDATED, sb));
				return false;
			}
		}
		return true;
	}

	/**
	 * startInsert
	 * @param data data
	 */
	@Override
	protected void startInsert(T data) {
		super.startInsert(data);

		assist().initCommonFields(data);
	}

	/**
	 * startUpdate
	 * @param data data
	 * @param srcData srcData
	 */
	@Override
	protected void startUpdate(T data, T srcData) {
		super.startUpdate(data, srcData);
		
		assist().initUpdateFields(data, srcData);
	}

	//--------------------------------------------------------------------------
	/**
	 * list
	 */
	protected Object list() {
		if (get_save() == null) {
			set_save(true);
		}
		return doList();
	}
	
	/**
	 * list_csv
	 */
	protected Object list_csv() {
		if (get_load() == null) {
			set_load(false);
		}
		if (get_save() == null) {
			set_save(false);
		}
		return doList();
	}
	
	/**
	 * list_print
	 */
	protected Object list_print() {
		if (get_load() == null) {
			set_load(false);
		}
		if (get_save() == null) {
			set_save(false);
		}
		return doList();
	}
	
	/**
	 * list_popup
	 */
	protected Object list_popup() {
		addLimitToPager();
		if (get_save() == null) {
			set_save(true);
		}
		return doList();
	}
	
	//--------------------------------------------------------------------------
	/**
	 * bdelete
	 */
	protected Object bdelete() {
		doBulkDeleteSelect();
		return null;
	}

	/**
	 * bdelete_execute
	 */
	protected Object bdelete_execute() {
		doBulkDeleteExecute();
		return null;
	}

	/**
	 * bupdate
	 */
	protected Object bupdate() {
		doBulkUpdateSelect();
		return null;
	}

	/**
	 * bupdate_execute
	 */
	protected Object bupdate_execute() {
		doBulkUpdateExecute();
		return null;
	}
	
//	/**
//	 * bedit
//	 * @return SUCCESS
//	 */
//	protected String bedit() {
//		return doBulkEditInput();
//	}
//
//	/**
//	 * bedit_input
//	 * @return SUCCESS
//	 */
//	protected String bedit_input() {
//		return doBulkEditInput();
//	}
//
//	/**
//	 * bedit_confirm
//	 * @return SUCCESS
//	 */
//	protected String bedit_confirm() {
//		return doBulkEditConfirm();
//	}
//
//	/**
//	 * bedit_execute
//	 * @return SUCCESS
//	 */
//	protected String bedit_execute() {
//		return doBulkEditExecute();
//	}

	//--------------------------------------------------------------------------
	/**
	 * view
	 */
	protected Object view() {
		doViewSelect();
		return null;
	}
	
	/**
	 * view_input
	 */
	protected Object view_input() {
		doViewInput();
		return null;
	}

	/**
	 * print
	 */
	protected Object print() {
		doViewSelect();
		return null;
	}

	/**
	 * print_input
	 */
	protected Object print_input() {
		doViewInput();
		return null;
	}

	/**
	 * copy
	 */
	protected Object copy() {
		doInsertSelect();
		return null;
	}

	/**
	 * copy_input
	 */
	protected Object copy_input() {
		doInsertInput();
		return null;
	}

	/**
	 * copy_confirm
	 */
	protected Object copy_confirm() {
		doInsertConfirm();
		return null;
	}

	/**
	 * copy_execute
	 */
	public Object copy_execute() {
		doInsertExecute();
		return null;
	}

	/**
	 * insert
	 */
	protected Object insert() {
		doInsertClear();
		return null;
	}

	/**
	 * insert_input
	 */
	protected Object insert_input() {
		doInsertInput();
		return null;
	}

	/**
	 * insert_confirm
	 */
	protected Object insert_confirm() {
		doInsertConfirm();
		return null;
	}

	/**
	 * insert_execute
	 */
	protected Object insert_execute() {
		doInsertExecute();
		return null;
	}

	/**
	 * update
	 */
	protected Object update() {
		doUpdateSelect();
		return null;
	}

	/**
	 * update_input
	 */
	protected Object update_input() {
		doUpdateInput();
		return null;
	}

	/**
	 * update_confirm
	 */
	public Object update_confirm() {
		doUpdateConfirm();
		return null;
	}

	/**
	 * update_execute
	 */
	protected Object update_execute() {
		doUpdateExecute();
		return null;
	}

	/**
	 * delete
	 */
	protected Object delete() {
		doDeleteSelect();
		return null;
	}

	/**
	 * delete_execute
	 */
	protected Object delete_execute() {
		doDeleteExecute();
		return null;
	}

}
