package panda.wing.constant;



/**
 */
public interface VC {
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
	 * STATUS_RECYCLED = 'R';
	 */
	public final static char STATUS_RECYCLED = 'R';

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
	// LIST
	//----------------------------------------------------
	/**
	 * DEFAULT_LIST_PAGE_ITEMS = 20;
	 */
	public final static long DEFAULT_LIST_PAGE_ITEMS = 20;
	
	/**
	 * DEFAULT_POPUP_PAGE_ITEMS = 10;
	 */
	public final static long DEFAULT_POPUP_PAGE_ITEMS = 10;

	/**
	 * DEFAULT_MAX_PAGE_ITEMS = 300;
	 */
	public final static long DEFAULT_MAX_PAGE_ITEMS = 300;
}
