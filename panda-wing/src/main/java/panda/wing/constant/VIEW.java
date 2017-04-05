package panda.wing.constant;

import panda.mvc.View;
import panda.mvc.view.SitemeshFreemarkerView;

/**
 * View constants
 */
public interface VIEW {
	public static final String ALT_INPUT = "alt:input";

	public static final String FTL_INPUT = "ftl:~input";
	
	public static final String SFTL_INPUT = "sftl:~input";

	public static final View FTL_VIEW_INPUT = new SitemeshFreemarkerView("~input");
	public static final View SFTL_VIEW_INPUT = new SitemeshFreemarkerView("~input");
}
