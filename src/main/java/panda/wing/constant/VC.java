package panda.wing.constant;

import panda.wing.processor.AuthenticateProcessor;


/**
 */
public interface VC {
	//--------------------------------------------------------
	// status
	//--------------------------------------------------------
	/**
	 * STATUS_0 = '0';
	 */
	public final static char STATUS_0 = '0';

	/**
	 * STATUS_X = 'D';
	 */
	public final static char STATUS_X = 'X';

	//--------------------------------------------------------
	// locale
	//--------------------------------------------------------
	/**
	 * LOCALE_ALL = "*";
	 */
	public final static String LOCALE_ALL = "*";

	//--------------------------------------------------------
	// permission
	//--------------------------------------------------------
	/**
	 * PERMISSION_ALL = "*";
	 */
	public final static String PERMISSION_ALL = AuthenticateProcessor.PERMISSION_ALL;
	
	/**
	 * PERMISSION_NONE = "-";
	 */
	public final static String PERMISSION_NONE = AuthenticateProcessor.PERMISSION_NONE;

	//----------------------------------------------------
	// USER CONSTANTS
	//----------------------------------------------------
	/**
	 * UNKNOWN_USID = 0L;
	 */
	public final static Long UNKNOWN_USID = 0L;

	/**
	 * GUESTS_ID = 999L;
	 */
	public final static Long GUESTS_ID = 999L;

	/**
	 * ADMIN_LEVEL = 0;
	 */
	public final static Integer ADMIN_LEVEL = 0;

	/**
	 * GUEST_LEVEL = 999;
	 */
	public final static Integer GUEST_LEVEL = 999;

}
