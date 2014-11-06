package panda.wing.action;

/**
 */
public interface ActionRC {
	//------------------------------------------------------------
	// UI SETTINGS
	//------------------------------------------------------------
	/** The input confirm */
	public static final String UI_INPUT_CONFIRM = "ui-input-confirm";

	/** The input step */
	public static final String UI_INPUT_STEP = "ui-input-step";

	/** The list countable */
	public static final String UI_LIST_COUNTABLE = "ui-list-countable";

	//------------------------------------------------------------
	// LIST SETTINGS
	//------------------------------------------------------------
	
	/**
	 * PAGER_MAX_LIMIT = "pager-max-limit";
	 */
	public final static String PAGER_MAX_LIMIT = "pager-max-limit";

	/**
	 * SORTER_COLUMN_SUFFIX = "-sorter-column";
	 */
	public final static String SORTER_COLUMN_SUFFIX = "-sorter-column";
	
	/**
	 * LIST_SORTER_COLUMN = "list-sorter-column";
	 */
	public final static String LIST_SORTER_COLUMN = "list-sorter-column";
	
	/**
	 * SORTER_DIRECTION_SUFFIX = "-sorter-direction";
	 */
	public final static String SORTER_DIRECTION_SUFFIX = "-sorter-direction";
	
	/**
	 * LIST_SORTER_DIRECTION = "list-sorter-direction";
	 */
	public final static String LIST_SORTER_DIRECTION = "list-sorter-direction";

	/**
	 * PAGER_LIMIT_SUFFIX = "-pager-limit";
	 */
	public final static String PAGER_LIMIT_SUFFIX = "-pager-limit";
	
	/**
	 * LIST_PAGER_LIMIT = "list-pager-limit";
	 */
	public final static String LIST_PAGER_LIMIT = "list-pager-limit";
	
	/**
	 * DEFAULT_LIST_PAGER_LIMIT = 10;
	 */
	public final static long DEFAULT_LIST_PAGER_LIMIT = 20;
	
	/**
	 * DEFAULT_POPUP_PAGER_LIMIT = 10;
	 */
	public final static long DEFAULT_POPUP_PAGER_LIMIT = 10;
	
	//------------------------------------------------------------
	// CONFIRM MESSAGES
	//------------------------------------------------------------
	/**
	 * CONFIRM_DATA_OVERWRITE = "confirm-data-overwrite";
	 */
	public final static String CONFIRM_DATA_OVERWRITE = "confirm-data-overwrite";
	
	//------------------------------------------------------------
	// ERROR MESSAGES
	//------------------------------------------------------------
	/**
	 * ERROR_DATA_NOTFOUND = "error-data-notfound";
	 */
	public final static String ERROR_DATA_NOTFOUND = "error-data-notfound";
	
	/**
	 * ERROR_DATA_LIST_EMPTY = "error-data-list-empty";
	 */
	public final static String ERROR_DATA_LIST_EMPTY = "error-data-list-empty";
	
	/**
	 * ERROR_DATA_DUPLICATE = "error-data-duplicate";
	 */
	public final static String ERROR_DATA_DUPLICATE = "error-data-duplicate";
	
	/**
	 * ERROR_FIELDVALUE_NOTNULL = "error-fieldvalue-notnull";
	 */
	public final static String ERROR_FIELDVALUE_NOTNULL = "error-fieldvalue-notnull";
	
	/**
	 * ERROR_FIELDVALUE_REQUIRED = "error-fieldvalue-required";
	 */
	public final static String ERROR_FIELDVALUE_REQUIRED = "error-fieldvalue-required";
	
	/**
	 * ERROR_FIELDVALUE_INCORRECT = "error-fieldvalue-incorrect";
	 */
	public final static String ERROR_FIELDVALUE_INCORRECT = "error-fieldvalue-incorrect";
	
	/**
	 * ERROR_FIELDVALUE_DUPLICATE = "error-fieldvalue-duplicate";
	 */
	public final static String ERROR_FIELDVALUE_DUPLICATE = "error-fieldvalue-duplicate";
	
	/**
	 * ERROR_FILE_NOTFOUND = "error-file-notfound";
	 */
	public final static String ERROR_FILE_NOTFOUND = "error-file-notfound";

	/**
	 * ERROR_UNLOGIN = "error-unlogin";
	 */
	public final static String ERROR_UNLOGIN = "error-unlogin";

	/**
	 * ERROR_UNSECURE = "error-unsecure";
	 */
	public final static String ERROR_UNSECURE = "error-unsecure";

	/**
	 * ERROR_NOPERMIT = "error-nopermit";
	 */
	public final static String ERROR_NOPERMIT = "error-nopermit";

	/**
	 * ERROR_DATA_IS_UPDATED = "error-data-is-updated";
	 */
	public final static String ERROR_DATA_IS_UPDATED = "error-data-is-updated";

	/**
	 * ERROR_DATA_IS_INVALID = "error-data-is-invalid";
	 */
	public final static String ERROR_DATA_IS_INVALID = "error-data-is-invalid";

	/**
	 * ERROR_SENDMAIL = "error-sendmail";
	 */
	public final static String ERROR_SENDMAIL = "error-sendmail";

	//------------------------------------------------------------
	// MESSAGES
	//------------------------------------------------------------
	/**
	 * MESSAGE_UPLOADING = "message-uploading";
	 */
	public final static String MESSAGE_UPLOADING = "message-uploading";
	
	/**
	 * MESSAGE_PROCESSING = "message-processing";
	 */
	public final static String MESSAGE_PROCESSING = "message-processing";

	/**
	 * MESSAGE_PROCESSED = "message-processed";
	 */
	public final static String MESSAGE_PROCESSED = "message-processed";
}
