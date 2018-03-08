package panda.mvc;


/**
 * View
 */
public interface View {
	public static final char SEP = ':';
	
	public static final String ALT = "~>";
	public static final String ALT2 = "alt";
	
	/** 
	 * Servlet Error View
	 */
	public static final String ERR = "err";
	
	/** 
	 * Http Status Code View
	 */
	public static final String SC = "sc";
	
	public static final String CSV = "csv";
	public static final String TSV = "tsv";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String JSON = "json";
	public static final String XML = "xml";
	
	public static final String JSP = "jsp";
	public static final String SJSP = "sjsp";
	public static final String REDIRECT = ">>";
	public static final String REDIRECT2 = "redirect";

	public static final String FTL = "ftl";
	public static final String SFTL = "sftl";
	public static final String FORWARD = "->";
	public static final String FORWARD2 = "forward";
	public static final String RAW = "raw";

	public static final String VOID = "void";
	public static final String NONE = "none";
	public static final String NULL = "null";

	void setArgument(String arg);
	
	void render(ActionContext ac);
}
