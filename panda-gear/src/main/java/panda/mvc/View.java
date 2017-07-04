package panda.mvc;


/**
 * View
 */
public interface View {
	public static final String CSV = "csv";
	public static final String TSV = "tsv";
	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";
	public static final String JSON = "json";
	public static final String XML = "xml";
	
	public static final String JSP = "jsp";
	public static final String REDIRECT = "redirect";
	public static final String REDIRECT2 = ">>";
	public static final String IOC = "ioc";
	public static final String HTTP = "http";
	public static final String FTL = "ftl";
	public static final String SFTL = "s" + FTL;
	public static final String FORWARD = "forward";
	public static final String FORWARD2 = "->";
	public static final String RAW = "raw";

	public static final String ALT = "alt";
	public static final String ALT2 = "~>";

	public static final String VOID = "void";
	public static final String NONE = "none";
	public static final String NULL = "null";

	void render(ActionContext ac);
}
