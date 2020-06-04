package panda.mvc.view;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import panda.bean.BeanHandler;
import panda.bind.AbstractSerializer;
import panda.bind.adapter.CalendarAdapter;
import panda.bind.adapter.DateAdapter;
import panda.lang.CycleDetectStrategy;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.Mvcs;
import panda.mvc.alert.ActionAlert;
import panda.mvc.alert.ParamAlert;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Queryer;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.adapter.FilterAdapter;
import panda.mvc.bind.adapter.PagerAdapter;
import panda.mvc.bind.adapter.QueryerAdapter;
import panda.mvc.bind.adapter.SorterAdapter;
import panda.net.http.HttpStatus;


public abstract class BindView extends DataView {
	protected static final String SEPERATOR = ", ";

	protected static final String DATE_FORMAT_LONG = "long";

	protected static final String CYCLE_DETECT_STRICT = "strict";
	
	protected static final String CYCLE_DETECT_LENIENT = "lenient";
	
	protected static final String CYCLE_DETECT_NOPROP = "noprop";
	
	protected String dateFormat = DATE_FORMAT_LONG;

	protected String cycleDetect = CYCLE_DETECT_NOPROP;

	protected boolean shortName = false;
	
	protected boolean ignoreTransient = true;

	protected boolean prettyPrint = false;

	protected boolean sitemesh = false;

	protected boolean includeParams = false;

	protected String fields;

	/**
	 * Constructor.
	 */
	public BindView() {
		setBom(true);
	}

	/**
	 * @return the cycleDetect
	 */
	public String getCycleDetect() {
		return cycleDetect;
	}

	/**
	 * @param cycleDetect the cycleDetect to set
	 */
	public void setCycleDetect(String cycleDetect) {
		this.cycleDetect = cycleDetect;
	}

	/**
	 * @return the ignoreTransient
	 */
	public boolean isIgnoreTransient() {
		return ignoreTransient;
	}

	/**
	 * @param ignoreTransient the ignoreTransient to set
	 */
	public void setIgnoreTransient(boolean ignoreTransient) {
		this.ignoreTransient = ignoreTransient;
	}

	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = Strings.lowerCase(dateFormat);
	}

	/**
	 * @return the shortName
	 */
	public boolean isShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(boolean shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the prettyPrint
	 */
	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	/**
	 * @param prettyPrint the prettyPrint to set
	 */
	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	/**
	 * @return the sitemesh
	 */
	public boolean isSitemesh() {
		return sitemesh;
	}

	/**
	 * @param sitemesh the sitemesh to set
	 */
	public void setSitemesh(boolean sitemesh) {
		this.sitemesh = sitemesh;
	}

	/**
	 * @return the includeParams
	 */
	public boolean isIncludeParams() {
		return includeParams;
	}

	/**
	 * @param includeParams the includeParams to set
	 */
	public void setIncludeParams(boolean includeParams) {
		this.includeParams = includeParams;
	}

	/**
	 * @return the fields
	 */
	public String getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(String fields) {
		this.fields = fields;
	}

	@Override
	public void render(ActionContext ac) {
		Throwable ex = ac.getError();
		if (ex != null) {
			writeError(ac, ex);
			return;
		}

		Object result = sitemesh ? sitemeshResult(ac)  : ac.getResult();

		writeResult(ac, result);
	}

	protected void writeError(ActionContext ac, Throwable ex) {
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		result.put("success", false);

		Map<String, Object> em = new HashMap<String, Object>();
		em.put("message", ex.getMessage());
		if (ac.isAppDebug()) {
			em.put("stackTrace", Exceptions.getStackTrace(ex));
		}
		
		result.put("exception", em);

		ac.getResponse().setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
		writeResult(ac, result);
	}

	@SuppressWarnings("unchecked")
	protected Object sitemeshResult(ActionContext ac) {
		Boolean success = true;
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Object> alerts = new LinkedHashMap<String, Object>();

		// put success to first position
		result.put("success", success);
		
		ParamAlert pva = ac.getParamAlert();
		if (pva != null && pva.hasErrors()) {
			success = false;
			alerts.put("params", pva);
		}
		
		ActionAlert ava = ac.getActionAlert();
		if (ava != null && !ava.isEmpty()) {
			if (ava.hasErrors()) {
				success = false;
			}
			alerts.put("action", ava);
		}
		if (!alerts.isEmpty()) {
			result.put("alerts", alerts);
		}
		
		result.put("success", success);
		if (includeParams) {
			result.put("params", ac.getReqParams());
		}
		result.put("result", ac.getResult());

		if (Strings.isNotEmpty(fields)) {
			@SuppressWarnings("rawtypes")
			BeanHandler acb = Mvcs.getBeans().getBeanHandler(ac.getClass());

			List<String> pnl = toList(fields);
			for (String pn : pnl) {
				Object value = acb.getBeanValue(ac, pn);
				if (value != null) {
					result.put(pn, value);
				}
			}
		}
		return result;
	}
	
	/**
	 * write result
	 * @param ac action context
	 * @param result result object
	 */
	protected void writeResult(ActionContext ac, Object result) {
		try {
			writeHeader(ac);

			PrintWriter writer = ac.getResponse().getWriter();
			writeResult(ac, writer, result);
			writer.flush();
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}

	protected abstract void writeResult(ActionContext ac, PrintWriter writer, Object result) throws IOException;
	
	protected List<String> toList(String str) {
		List<String> list = new ArrayList<String>();
		if (Strings.isNotEmpty(str)) {
			String[] ss = Strings.split(str, SEPERATOR);
			for (String s : ss) {
				s = Strings.stripToNull(s);
				if (s != null) {
					list.add(s);
				}
			}
		}
		return list;
	}
	
	protected void setSerializerOptions(AbstractSerializer as) {
		if (cycleDetect != null) {
			if (CYCLE_DETECT_STRICT.equals(cycleDetect)) {
				as.setCycleDetectStrategy(CycleDetectStrategy.CYCLE_DETECT_STRICT);
			}
			else if (CYCLE_DETECT_LENIENT.equals(cycleDetect)) {
				as.setCycleDetectStrategy(CycleDetectStrategy.CYCLE_DETECT_LENIENT);
			}
			else if (CYCLE_DETECT_NOPROP.equals(cycleDetect)) {
				as.setCycleDetectStrategy(CycleDetectStrategy.CYCLE_DETECT_NOPROP);
			}
		}

		if (DATE_FORMAT_LONG.equalsIgnoreCase(dateFormat)) {
			as.setDateToMillis(true);
		}
		else {
			as.registerAdapter(Date.class, new DateAdapter(dateFormat));
			as.registerAdapter(Calendar.class, new CalendarAdapter(dateFormat));
		}

		if (shortName) {
			as.registerAdapter(Filter.class, FilterAdapter.s());
			as.registerAdapter(Pager.class, PagerAdapter.s());
			as.registerAdapter(Queryer.class, QueryerAdapter.s());
			as.registerAdapter(Sorter.class, SorterAdapter.s());
		}
		else {
			as.registerAdapter(Filter.class, FilterAdapter.i());
			as.registerAdapter(Pager.class, PagerAdapter.i());
			as.registerAdapter(Queryer.class, QueryerAdapter.i());
			as.registerAdapter(Sorter.class, SorterAdapter.i());
		}
		
		as.setPrettyPrint(prettyPrint);
	}
}

