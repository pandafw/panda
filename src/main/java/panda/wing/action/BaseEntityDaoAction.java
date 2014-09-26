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
	 * @return the assist
	 */
	protected BaseActionAssist assist() {
		return (BaseActionAssist)super.getAssist();
	}

	/**
	 * @return the consts
	 */
	protected BaseActionConsts consts() {
		return (BaseActionConsts)super.getConsts();
	}

	//--------------------------------------------------------------------------
	/**
	 * hasPermission
	 * @param path path
	 * @return true if action has access permit
	 */
	public boolean hasPermission(String path) {
		return assist().hasPermission(path);
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
	 * @return SUCCESS
	 */
	protected String list() {
		if (get_save() == null) {
			set_save(true);
		}
		return doList();
	}
	
	/**
	 * list_csv
	 * @return SUCCESS
	 */
	protected String list_csv() {
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
	 * @return SUCCESS
	 */
	protected String list_print() {
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
	 * @return SUCCESS
	 */
	protected String list_popup() {
		addLimitToPager();
		if (get_save() == null) {
			set_save(true);
		}
		return doList();
	}
	
	//--------------------------------------------------------------------------
	/**
	 * bdelete
	 * @return SUCCESS
	 */
	protected String bdelete() {
		doBulkDeleteSelect();
		return SUCCESS;
	}

	/**
	 * bdelete_execute
	 * @return SUCCESS
	 */
	protected String bdelete_execute() {
		doBulkDeleteExecute();
		return SUCCESS;
	}

	/**
	 * bupdate
	 * @return SUCCESS
	 */
	protected String bupdate() {
		doBulkUpdateSelect();
		return SUCCESS;
	}

	/**
	 * bupdate_execute
	 * @return SUCCESS
	 */
	protected String bupdate_execute() {
		doBulkUpdateExecute();
		return SUCCESS;
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
	 * @return SUCCESS
	 */
	protected String view() {
		doViewSelect();
		return SUCCESS;
	}
	
	/**
	 * view_input
	 * @return SUCCESS
	 */
	protected String view_input() {
		doViewInput();
		return SUCCESS;
	}

	/**
	 * print
	 * @return SUCCESS
	 */
	protected String print() {
		doViewSelect();
		return SUCCESS;
	}

	/**
	 * print_input
	 * @return SUCCESS
	 */
	protected String print_input() {
		doViewInput();
		return SUCCESS;
	}

	/**
	 * copy
	 * @return SUCCESS
	 */
	protected String copy() {
		doInsertSelect();
		return SUCCESS;
	}

	/**
	 * copy_input
	 * @return SUCCESS
	 */
	protected String copy_input() {
		doInsertInput();
		return SUCCESS;
	}

	/**
	 * copy_confirm
	 * @return SUCCESS
	 */
	protected String copy_confirm() {
		doInsertConfirm();
		return SUCCESS;
	}

	/**
	 * copy_execute
	 * @return SUCCESS
	 */
	public String copy_execute() {
		doInsertExecute();
		return SUCCESS;
	}

	/**
	 * insert
	 * @return SUCCESS
	 */
	protected String insert() {
		doInsertClear();
		return SUCCESS;
	}

	/**
	 * insert_input
	 * @return SUCCESS
	 */
	protected String insert_input() {
		doInsertInput();
		return SUCCESS;
	}

	/**
	 * insert_confirm
	 * @return SUCCESS
	 */
	protected String insert_confirm() {
		doInsertConfirm();
		return SUCCESS;
	}

	/**
	 * insert_execute
	 * @return SUCCESS
	 */
	protected String insert_execute() {
		doInsertExecute();
		return SUCCESS;
	}

	/**
	 * update
	 * @return SUCCESS
	 */
	protected String update() {
		doUpdateSelect();
		return SUCCESS;
	}

	/**
	 * update_input
	 * @return SUCCESS
	 */
	protected String update_input() {
		doUpdateInput();
		return SUCCESS;
	}

	/**
	 * update_confirm
	 * @return SUCCESS
	 */
	public String update_confirm() {
		doUpdateConfirm();
		return SUCCESS;
	}

	/**
	 * update_execute
	 * @return SUCCESS
	 */
	protected String update_execute() {
		doUpdateExecute();
		return SUCCESS;
	}

	/**
	 * delete
	 * @return SUCCESS
	 */
	protected String delete() {
		doDeleteSelect();
		return SUCCESS;
	}

	/**
	 * delete_execute
	 * @return SUCCESS
	 */
	protected String delete_execute() {
		doDeleteExecute();
		return SUCCESS;
	}

}
