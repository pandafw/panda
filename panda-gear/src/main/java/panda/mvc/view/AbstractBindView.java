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
import panda.mvc.bean.QueryerEx;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.filter.FileItemPropertyFilter;
import panda.mvc.bind.filter.FilterPropertyFilter;
import panda.mvc.bind.filter.PagerPropertyFilter;
import panda.mvc.bind.filter.QueryerPropertyFilter;
import panda.mvc.bind.filter.SorterPropertyFilter;
import panda.vfs.FileItem;
import panda.vfs.dao.DaoFileItem;
import panda.vfs.local.LocalFileItem;


public abstract class AbstractBindView extends AbstractDataView {
	protected static final String SEPERATOR = ", ";

	protected static final String DATE_FORMAT_LONG = "long";

	protected static final String CYCLE_DETECT_STRICT = "strict";
	
	protected static final String CYCLE_DETECT_LENIENT = "lenient";
	
	protected static final String CYCLE_DETECT_NOPROP = "noprop";
	
	protected String dateFormat = DATE_FORMAT_LONG;

	protected String cycleDetect = CYCLE_DETECT_NOPROP;

	protected Boolean shortName = false;
	
	protected Boolean ignoreTransient = true;

	protected Boolean prettyPrint = false;
	
	/**
	 * Constructor.
	 * @param location the location
	 */
	public AbstractBindView(String location) {
		super(location);
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
	public Boolean getIgnoreTransient() {
		return ignoreTransient;
	}

	/**
	 * @param ignoreTransient the ignoreTransient to set
	 */
	public void setIgnoreTransient(Boolean ignoreTransient) {
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
	public Boolean getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(Boolean shortName) {
		this.shortName = shortName;
	}

	/**
	 * @return the prettyPrint
	 */
	public Boolean getPrettyPrint() {
		return prettyPrint;
	}

	/**
	 * @param prettyPrint the prettyPrint to set
	 */
	public void setPrettyPrint(Boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	/**
	 * @return the properties
	 */
	public String getProperties() {
		return getLocation();
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		setLocation(properties);
	}

	@SuppressWarnings("unchecked")
	public void render(ActionContext ac) {
		Object o = ac.getError();
		if (o == null || !(o instanceof Throwable)) {
			o = ac.getRequest().getAttribute("exception");
		}

		if (o != null && o instanceof Throwable) {
			Throwable e = (Throwable)o;

			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result.put("success", false);

			Map<String, Object> em = new HashMap<String, Object>();
			em.put("message", e.getMessage());
			if (ac.getAssist().isDebugEnabled()) {
				em.put("stackTrace", Exceptions.getStackTrace(e));
			}
			
			result.put("exception", em);

			writeResult(ac, result);
			return;
		}

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
		result.put("params", ac.getParams());
		result.put("result", ac.getResult());

		if (Strings.isNotEmpty(location)) {
			@SuppressWarnings("rawtypes")
			BeanHandler acb = Mvcs.getBeans().getBeanHandler(ac.getClass());

			List<String> pnl = toList(location);
			for (String pn : pnl) {
				Object value = acb.getBeanValue(ac, pn);
				if (value != null) {
					result.put(pn, value);
				}
			}
		}

		writeResult(ac, result);
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
		if (!Strings.isBlank(str)) {
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
			as.registerSourceAdapter(Date.class, new DateAdapter(dateFormat));
			as.registerSourceAdapter(Calendar.class, new CalendarAdapter(dateFormat));
		}

		as.registerPropertyFilter(Filter.class, new FilterPropertyFilter(shortName));
		as.registerPropertyFilter(Pager.class, new PagerPropertyFilter(shortName));
		as.registerPropertyFilter(Queryer.class, new QueryerPropertyFilter(shortName));
		as.registerPropertyFilter(QueryerEx.class, new QueryerPropertyFilter(shortName));
		as.registerPropertyFilter(Sorter.class, new SorterPropertyFilter(shortName));

		FileItemPropertyFilter fipf = new FileItemPropertyFilter();
		as.registerPropertyFilter(FileItem.class, fipf);
		as.registerPropertyFilter(LocalFileItem.class, fipf);
		as.registerPropertyFilter(DaoFileItem.class, fipf);
		
		as.setPrettyPrint(prettyPrint);
	}
}

