package panda.app.constant;



/**
 * Value constants
 */
public interface VAL {
	//--------------------------------------------------------
	// status
	//--------------------------------------------------------
	/**
	 * STATUS_ACTIVE = 'A';
	 */
	public final static char STATUS_ACTIVE = 'A';

	/**
	 * STATUS_DISABLED = 'D';
	 */
	public final static char STATUS_DISABLED = 'D';

	/**
	 * STATUS_TRASHED = 'T';
	 */
	public final static char STATUS_TRASHED = 'T';

	//--------------------------------------------------------
	// locale
	//--------------------------------------------------------
	/**
	 * LOCALE_ALL = "*";
	 */
	public final static String LOCALE_ALL = "*";

	//----------------------------------------------------
	// USER
	//----------------------------------------------------
	/**
	 * SYSTEM_UID = 0L;
	 */
	public final static Long SYSTEM_UID = 0L;

	
	//----------------------------------------------------
	// List
	//
	/**
	 * DEFAULT_LIST_PAGESIZE = 20;
	 */
	public final static long DEFAULT_LIST_PAGESIZE = 20;

	/**
	 * MAXIMUM_LIST_PAGESIZE = 300;
	 */
	public final static long MAXIMUM_LIST_PAGESIZE = 300;
	
	//----------------------------------------------------
	// Popup
	//
	/**
	 * DEFAULT_POPUP_PAGESIZE = 10;
	 */
	public final static long DEFAULT_POPUP_PAGESIZE = 10;

	/**
	 * MAXIMUM_POPUP_PAGESIZE = 300;
	 */
	public final static long MAXIMUM_POPUP_PAGESIZE = 300;
	
	//----------------------------------------------------
	// Export
	//
	/**
	 * DEFAULT_EXPORT_PAGESIZE = 2000;
	 */
	public final static long DEFAULT_EXPORT_PAGESIZE = 2000;
	
	/**
	 * MAXIMUM_EXPORT_PAGESIZE = 10000;
	 */
	public final static long MAXIMUM_EXPORT_PAGESIZE = 10000;
}
