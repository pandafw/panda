package panda.mvc;


/**
 * 视图接口
 */
public interface View {
	public static final String JSP = "jsp";
	public static final String JSON = "json";
	public static final String XML = "xml";
	public static final String REDIRECT = "redirect";
	public static final String REDIRECT2 = ">>";
	public static final String IOC = "ioc";
	public static final String HTTP = "http";
	public static final String FREEMARKER = "ftl";
	public static final String FREEMARKER_INPUT = "ftl:~input";
	public static final String FORWARD = "forward";
	public static final String FORWARD2 = "->";
	public static final String RAW = "raw";
	public static final String VOID = "void";

	void render(ActionContext ac);
}
