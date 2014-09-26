package panda.wing.entity;

import java.io.Serializable;

import panda.wing.constant.VC;

public abstract class Bean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * copy properties from the specified object.
	 */
	public void copy(Bean src) {
	}

	/**
	 * is this data valid
	 * @return true if bean is valid
	 */
	public static boolean isValid(IStatus bean) {
		return bean.getStatus() == null || VC.STATUS_X != bean.getStatus();
	}
	
	/**
	 * is this data invalid
	 * @return true if bean is invalid
	 */
	public static boolean isInvalid(IStatus bean) {
		return bean.getStatus() != null && VC.STATUS_X == bean.getStatus();
	}

	/**
	 * is this data updated
	 * @param lhs left bean
	 * @param rhs right bean
	 * @return true if lhs.utime != rhs.utime
	 */
	public static boolean isUpdated(IUpdate lhs, IUpdate rhs) {
		if (lhs.getUusid() == null 
				|| lhs.getUtime() == null 
				|| rhs.getUusid() == null 
				|| rhs.getUtime() == null) {
			return false;
		}
		
		if (!lhs.getUusid().equals(rhs.getUusid())) {
			return true;
		}

		return lhs.getUtime().getTime() != rhs.getUtime().getTime();
	}
}

