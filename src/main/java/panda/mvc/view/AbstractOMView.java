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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import panda.bind.AbstractSerializer;
import panda.bind.adapter.DateAdapter;
import panda.ioc.Ioc;
import panda.lang.Charsets;
import panda.lang.CycleDetectStrategy;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.mvc.ActionContext;
import panda.mvc.View;
import panda.mvc.aware.ActionAware;
import panda.mvc.aware.ParamAware;
import panda.mvc.bean.CompositeQuery;
import panda.mvc.bean.Filter;
import panda.mvc.bean.Pager;
import panda.mvc.bean.Sorter;
import panda.mvc.bind.filter.CompositeQueryPropertyFilter;
import panda.mvc.bind.filter.FilterPropertyFilter;
import panda.mvc.bind.filter.PagerPropertyFilter;
import panda.mvc.bind.filter.SorterPropertyFilter;
import panda.servlet.HttpServletSupport;
import panda.servlet.HttpServlets;


public abstract class AbstractOMView implements View {
	protected static final String DEFAULT_CONTENT_TYPE = "text/plain";

	protected static final String SEPERATOR = ", ";

	protected static final String DATE_FORMAT_LONG = "long";

	protected static final String CYCLE_DETECT_STRICT = "strict";
	
	protected static final String CYCLE_DETECT_LENIENT = "lenient";
	
	protected static final String CYCLE_DETECT_NOPROP = "noprop";
	
	protected String dateFormat = DATE_FORMAT_LONG;

	protected int expiry = 0;

	protected String contentType = DEFAULT_CONTENT_TYPE;

	protected String encoding = Charsets.UTF_8;

	protected String cycleDetect = CYCLE_DETECT_NOPROP;

	protected Boolean shortName = false;
	
	protected Boolean ignoreTransient = true;

	protected Boolean prettyPrint = false;
	
	protected String properties;
	
	protected String location;

	/**
	 * Constructor.
	 */
	public AbstractOMView() {
		super();
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
	 * @return the contentType
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * @param contentType the contentType to set
	 */
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/**
	 * @return the expiry
	 */
	public int getExpiry() {
		return expiry;
	}

	/**
	 * @param expiry the expiry to set
	 */
	public void setExpiry(int expiry) {
		this.expiry = expiry;
	}

	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}

	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
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
		return properties;
	}

	/**
	 * @param properties the properties to set
	 */
	public void setProperties(String properties) {
		this.properties = properties;
	}

	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * write result
	 * @param writer response writer
	 * @param result result object
	 * @throws IOException
	 */
	protected void writeResult(PrintWriter writer, Object result) throws IOException {
		if (result != null) {
			writer.write(result.toString());
		}
	}
	
	/**
	 * write result
	 * @param ac action context
	 * @param result result object
	 * @throws IOException
	 */
	protected void writeResult(ActionContext ac, Object result) throws IOException {
		HttpServletRequest request = ac.getRequest();
		HttpServletResponse response = ac.getResponse();

		HttpServletSupport hss = new HttpServletSupport(request, response);
		hss.setExpiry(expiry);
		hss.setCharset(encoding);
		hss.setContentType(contentType);
		hss.setBom(true);
		hss.writeResponseHeader();

		PrintWriter writer = response.getWriter();
		writeResult(writer, result);
		writer.flush();
	}

	public void render(ActionContext ac) throws IOException {
		Object o = ac.getError();
		if (o == null || !(o instanceof Throwable)) {
			o = ac.getRequest().getAttribute("exception");
		}

		if (o != null && o instanceof Throwable) {
			Throwable e = (Throwable)o;

			HttpServlets.logException(ac.getRequest(), e);

			Map<String, Object> result = new LinkedHashMap<String, Object>();
			result.put("success", false);

			Map<String, Object> em = new HashMap<String, Object>();
			em.put("message", e.getMessage());
			em.put("stackTrace", Exceptions.getStackTrace(e));

			result.put("exception", em);

			writeResult(ac, result);
			return;
		}

		if (Strings.isBlank(properties)) {
			properties = location;
		}

		Map<String, Object> result = new LinkedHashMap<String, Object>();

		Boolean success = true;
		
		// put success to first position
		result.put("success", success);
		
		ParamAware pva = ac.getParamAware();
		if (pva != null && pva.hasErrors()) {
			success = false;
			result.put("param", pva);
		}
		
		ActionAware ava = ac.getActionAware();
		if (ava != null && !ava.isEmpty()) {
			if (ava.hasErrors()) {
				success = false;
			}
			result.put("action", ava);
		}
		
		result.put("success", success);
		result.put("result", ac.getResult());

		if (properties != null) {
			Ioc ioc = ac.getIoc();
			if (ioc != null) {
				List<String> pnl = toList(properties);
				for (String pn : pnl) {
					Object value = ioc.get(null, pn);
					if (value != null) {
						result.put(pn, value);
					}
				}
			}
		}

		writeResult(ac, result);
	}

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

		DateAdapter da = new DateAdapter();
		if (DATE_FORMAT_LONG.compareToIgnoreCase(dateFormat) == 0) {
			da.setToTime(true);
		}
		else {
			da.setDateFormat(dateFormat);
		}
		as.registerSourceAdapter(Date.class, da);
		as.registerSourceAdapter(Calendar.class, da);

		as.registerPropertyFilter(Filter.class, new FilterPropertyFilter(shortName));
		as.registerPropertyFilter(Pager.class, new PagerPropertyFilter(shortName));
		as.registerPropertyFilter(CompositeQuery.class, new CompositeQueryPropertyFilter(shortName));
		as.registerPropertyFilter(Sorter.class, new SorterPropertyFilter(shortName));
		
		as.setPrettyPrint(prettyPrint);
	}
}