package panda.mvc.view;

import panda.ioc.Ioc;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.ViewCreator;
import panda.mvc.impl.DefaultViewCreator;
import panda.net.http.HttpStatus;

/**
 * Views
 */
public class Views {
	public static final String ALT_INPUT = "alt:input";
	public static final String FTL_INPUT = "ftl:~input";
	public static final String SFTL_INPUT = "sftl:~input";

	public static final String SC_FORBIDDEN = View.SC + View.SEP + HttpStatus.SC_FORBIDDEN;
	public static final String SC_NOT_FOUND = View.SC + View.SEP + HttpStatus.SC_NOT_FOUND;
	public static final String SC_INTERNAL_ERROR = View.SC + View.SEP + HttpStatus.SC_INTERNAL_SERVER_ERROR;

	public static final String SE_FORBIDDEN = View.ERR + View.SEP + HttpStatus.SC_FORBIDDEN;
	public static final String SE_NOT_FOUND = View.ERR + View.SEP + HttpStatus.SC_NOT_FOUND;
	public static final String SE_INTERNAL_ERROR = View.ERR + View.SEP + HttpStatus.SC_INTERNAL_SERVER_ERROR;

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
		return createView(ac, View.NONE);
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
		return createView(ac, View.REDIRECT);
	}

	public static View redirect(ActionContext ac, String location) {
		return createView(ac, View.REDIRECT + View.SEP + location);
	}
	
	public static View ftl(ActionContext ac) {
		return createView(ac, View.FTL);
	}
	
	public static View ftl(ActionContext ac, String location) {
		return createView(ac, View.FTL + View.SEP + location);
	}

	public static View ftlInput(ActionContext ac) {
		return createView(ac, FTL_INPUT);
	}
	
	public static View sftl(ActionContext ac) {
		return createView(ac, View.SFTL);
	}
	
	public static View sftl(ActionContext ac, String location) {
		return createView(ac, View.SFTL + View.SEP + location);
	}

	public static View sftlInput(ActionContext ac) {
		return createView(ac, SFTL_INPUT);
	}

	public static View jsp(ActionContext ac) {
		return createView(ac, View.JSP);
	}

	public static View sjsp(ActionContext ac) {
		return createView(ac, View.SJSP);
	}

	public static View json(ActionContext ac) {
		return createView(ac, View.JSON);
	}

	public static View xml(ActionContext ac) {
		return createView(ac, View.XML);
	}

	public static View sjson(ActionContext ac) {
		return createView(ac, View.SJSON);
	}

	public static View sxml(ActionContext ac) {
		return createView(ac, View.SXML);
	}
	
	public static View csv(ActionContext ac) {
		return createView(ac, View.CSV);
	}
	
	public static View csv(ActionContext ac, String filename) {
		return createView(ac, View.CSV + View.SEP + filename);
	}
	
	public static View tsv(ActionContext ac) {
		return createView(ac, View.TSV);
	}

	public static View tsv(ActionContext ac, String filename) {
		return createView(ac, View.TSV + View.SEP + filename);
	}
	
	public static View xls(ActionContext ac) {
		return createView(ac, View.XLS);
	}

	public static View xls(ActionContext ac, String filename) {
		return createView(ac, View.XLS + View.SEP + filename);
	}

	public static View xlsx(ActionContext ac) {
		return createView(ac, View.XLSX);
	}

	public static View xlsx(ActionContext ac, String filename) {
		return createView(ac, View.XLSX + View.SEP + filename);
	}
}
