package panda.app.entity;

import panda.app.constant.VAL;

public abstract class Bean {
	/**
	 * copy properties from the specified object.
	 * @param src the source object to copy
	 */
	public void copy(Bean src) {
	}

	/**
	 * is this data active
	 * @param bean the bean object
	 * @return true if bean is valid
	 */
	public static boolean isActive(IStatus bean) {
		return bean.getStatus() != null && VAL.STATUS_ACTIVE == bean.getStatus();
	}
	
	/**
	 * is this data disabled
	 * @param bean the bean object
	 * @return true if bean is disabled
	 */
	public static boolean isDisabled(IStatus bean) {
		return bean.getStatus() != null && VAL.STATUS_DISABLED == bean.getStatus();
	}
	
	/**
	 * is this data trashed
	 * @param bean the bean object
	 * @return true if bean is trashed
	 */
	public static boolean isTrashed(IStatus bean) {
		return bean.getStatus() != null && VAL.STATUS_TRASHED == bean.getStatus();
	}

	/**
	 * is this data changed
	 * @param lhs left bean
	 * @param rhs right bean
	 * @return true if lhs.utime != rhs.utime
	 */
	public static boolean isChanged(IUpdatedBy lhs, IUpdatedBy rhs) {
		if (lhs.getUpdatedBy() == null 
				|| lhs.getUpdatedAt() == null 
				|| rhs.getUpdatedBy() == null 
				|| rhs.getUpdatedAt() == null) {
			return false;
		}
		
		if (!lhs.getUpdatedBy().equals(rhs.getUpdatedBy())) {
			return true;
		}

		return (lhs.getUpdatedAt().getTime() / 1000) != (rhs.getUpdatedAt().getTime() / 1000);
	}
}

