package panda.mvc;


/**
 * 视图接口
 */
public interface View {
	public static final String ATTR_OBJECT = "obj";

	public static final String ATTR_REQ_MAP = "req";
	public static final String ATTR_SES_MAP = "ses";
	public static final String ATTR_APP_MAP = "app";
	public static final String ATTR_PATHARGS = "path";
	public static final String ATTR_ARGUMENTS = "args";

	void render(ActionContext ac) throws Throwable;

}
