package panda.log.log4j;

import org.apache.log4j.spi.Filter;
import org.apache.log4j.spi.LoggingEvent;

import panda.lang.Strings;

public class LogNameFilter extends Filter {
	private String[] includes;
	private String[] excludes;
	
	/**
	 * Construct
	 */
	public LogNameFilter() {
	}

	/**
	 * @return the includes
	 */
	public String getIncludes() {
		return Strings.join(includes, ' ');
	}
	
	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(String includes) {
		this.includes = Strings.split(includes, ", ");
	}

	/**
	 * @return the excludes
	 */
	public String getExcludes() {
		return Strings.join(excludes, ' ');
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(String excludes) {
		this.excludes = Strings.split(excludes, ", ");
	}

	protected boolean checkLogName(LoggingEvent event) {
		String name = event.getLoggerName();

		if (excludes != null) {
			for (String c : excludes) {
				if (c.startsWith(name)) {
					return false;
				}
			}
		}

		if (includes != null) {
			for (String c : includes) {
				if (c.startsWith(name)) {
					return true;
				}
			}
			return false;
		}
		
		return true;
	}

	@Override
	public int decide(LoggingEvent event) {
		if (checkLogName(event)) {
			return Filter.NEUTRAL;
		}

		return Filter.DENY;
	}
}
