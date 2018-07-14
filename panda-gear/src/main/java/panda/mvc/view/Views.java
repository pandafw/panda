package panda.mvc.view;

import panda.ioc.Ioc;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.ViewCreator;
import panda.mvc.impl.DefaultViewCreator;
import panda.net.http.HttpStatus;

/**
 * View
 */
public class Views {
	public static final char SEP = ':';
	
	//-----------------------------------------------------------
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
	public static final String SJSON = "sjson";
	public static final String SXML = "sxml";
	
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

	//-----------------------------------------------------------
	public static final String ALT_INPUT = "alt:input";
	public static final String FTL_INPUT = "ftl:~input";
	public static final String SFTL_INPUT = "sftl:~input";

	public static final String SC_FORBIDDEN = SC + SEP + HttpStatus.SC_FORBIDDEN;
	public static final String SC_NOT_FOUND = SC + SEP + HttpStatus.SC_NOT_FOUND;
	public static final String SC_INTERNAL_ERROR = SC + SEP + HttpStatus.SC_INTERNAL_SERVER_ERROR;

	public static final String SE_FORBIDDEN = ERR + SEP + HttpStatus.SC_FORBIDDEN;
	public static final String SE_NOT_FOUND = ERR + SEP + HttpStatus.SC_NOT_FOUND;
	public static final String SE_INTERNAL_ERROR = ERR + SEP + HttpStatus.SC_INTERNAL_SERVER_ERROR;

	/**
	 * get view creator
	 * @param ioc IOC
	 * @return ViewCreator
	 */
	public static ViewCreator getViewCreator(Ioc ioc) {
		ViewCreator vc = ioc.getIfExists(ViewCreator.class);
		if (vc == null) {
			vc = new DefaultViewCreator();
		}
		return vc;
	}

	/**
	 * create a view
	 * @param ac action context
	 * @param viewer view description
	 * @return View
	 */
	public static View createView(ActionContext ac, String viewer) {
		if (Strings.isEmpty(viewer)) {
			return null;
		}

		View view = getViewCreator(ac.getIoc()).create(ac, viewer);
		if (view != null) {
			return view;
		}

		throw new IllegalArgumentException("Can not create view '" + viewer + "'");
	}

	public static View createDefaultView(ActionContext ac) {
		return getViewCreator(ac.getIoc()).createDefaultView(ac);
	}

	public static View createErrorView(ActionContext ac) {
		return getViewCreator(ac.getIoc()).createErrorView(ac);
	}

	public static View createFatalView(ActionContext ac) {
		return getViewCreator(ac.getIoc()).createFatalView(ac);
	}

	//----------------------------------------------------------
	public static View none(ActionContext ac) {
		return createView(ac, NONE);
	}

	public static View forbidden(ActionContext ac) {
		return createView(ac, SE_FORBIDDEN);
	}
	
	public static View notFound(ActionContext ac) {
		return createView(ac, SE_NOT_FOUND);
	}
	
	public static View internalError(ActionContext ac) {
		return createView(ac, SE_INTERNAL_ERROR);
	}

	public static View redirect(ActionContext ac) {
		return createView(ac, REDIRECT);
	}

	public static View redirect(ActionContext ac, String location) {
		return createView(ac, REDIRECT + SEP + location);
	}
	
	public static View ftl(ActionContext ac) {
		return createView(ac, FTL);
	}
	
	public static View ftl(ActionContext ac, String location) {
		return createView(ac, FTL + SEP + location);
	}

	public static View ftlInput(ActionContext ac) {
		return createView(ac, FTL_INPUT);
	}
	
	public static View sftl(ActionContext ac) {
		return createView(ac, SFTL);
	}
	
	public static View sftl(ActionContext ac, String location) {
		return createView(ac, SFTL + SEP + location);
	}

	public static View sftlInput(ActionContext ac) {
		return createView(ac, SFTL_INPUT);
	}

	public static View jsp(ActionContext ac) {
		return createView(ac, JSP);
	}

	public static View sjsp(ActionContext ac) {
		return createView(ac, SJSP);
	}

	public static View json(ActionContext ac) {
		return createView(ac, JSON);
	}

	public static View xml(ActionContext ac) {
		return createView(ac, XML);
	}

	public static View sjson(ActionContext ac) {
		return createView(ac, SJSON);
	}

	public static View sxml(ActionContext ac) {
		return createView(ac, SXML);
	}
	
	public static View csv(ActionContext ac) {
		return createView(ac, CSV);
	}
	
	public static View csv(ActionContext ac, String filename) {
		return createView(ac, CSV + SEP + filename);
	}
	
	public static View tsv(ActionContext ac) {
		return createView(ac, TSV);
	}

	public static View tsv(ActionContext ac, String filename) {
		return createView(ac, TSV + SEP + filename);
	}
	
	public static View xls(ActionContext ac) {
		return createView(ac, XLS);
	}

	public static View xls(ActionContext ac, String filename) {
		return createView(ac, XLS + SEP + filename);
	}

	public static View xlsx(ActionContext ac) {
		return createView(ac, XLSX);
	}

	public static View xlsx(ActionContext ac, String filename) {
		return createView(ac, XLSX + SEP + filename);
	}
}
