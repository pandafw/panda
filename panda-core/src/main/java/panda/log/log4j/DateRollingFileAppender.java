package panda.log.log4j;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

import panda.io.FileNames;
import panda.io.Files;
import panda.lang.Strings;
import panda.lang.time.FastDateFormat;
import panda.lang.time.RollingCalendar;

/**
 * DateRollingFileAppender extends {@link FileAppender} so that the underlying file is rolled over
 * at a user chosen frequency. DateRollingFileAppender has been observed to exhibit synchronization
 * issues and data loss. The log4j extras companion includes alternatives which should be considered
 * for new deployments and which are discussed in the documentation for
 * org.apache.log4j.rolling.RollingFileAppender.
 * <p>
 * The rolling schedule is specified by the <b>DatePattern</b> option. This pattern should follow
 * the {@link SimpleDateFormat} conventions. In particular, you <em>must</em> escape literal text
 * within a pair of single quotes. A formatted version of the date pattern is used as the suffix for
 * the rolled file name.
 * <p>
 * For example, if the <b>File</b> option is set to <code>/foo/bar.log</code> and the
 * <b>DatePattern</b> set to <code>'.'yyyy-MM-dd</code>, on 2001-02-16 at midnight, the logging file
 * <code>/foo/bar.log</code> will be copied to <code>/foo/bar.log.2001-02-16</code> and logging for
 * 2001-02-17 will continue in <code>/foo/bar.log</code> until it rolls over the next day.
 * <p>
 * Is is possible to specify monthly, weekly, half-daily, daily, hourly, or minutely rollover
 * schedules.
 * <p>
 * <table border="1" cellpadding="2">
 * <tr>
 * <th>DatePattern</th>
 * <th>Rollover schedule</th>
 * <th>Example</th>
 * <tr>
 * <td><code>'.'yyyy-MM</code>
 * <td>Rollover at the beginning of each month</td>
 * <td>At midnight of May 31st, 2002 <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-05</code>. Logging for the month of June will be output to
 * <code>/foo/bar.log</code> until it is also rolled over the next month.
 * <tr>
 * <td><code>'.'yyyy-ww</code>
 * <td>Rollover at the first day of each week. The first day of the week depends on the locale.</td>
 * <td>Assuming the first day of the week is Sunday, on Saturday midnight, June 9th 2002, the file
 * <i>/foo/bar.log</i> will be copied to <i>/foo/bar.log.2002-23</i>. Logging for the 24th week of
 * 2002 will be output to <code>/foo/bar.log</code> until it is rolled over the next week.
 * <tr>
 * <td><code>'.'yyyy-MM-dd</code>
 * <td>Rollover at midnight each day.</td>
 * <td>At midnight, on March 8th, 2002, <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-03-08</code>. Logging for the 9th day of March will be output to
 * <code>/foo/bar.log</code> until it is rolled over the next day.
 * <tr>
 * <td><code>'.'yyyy-MM-dd-a</code>
 * <td>Rollover at midnight and midday of each day.</td>
 * <td>At noon, on March 9th, 2002, <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2002-03-09-AM</code>. Logging for the afternoon of the 9th will be output to
 * <code>/foo/bar.log</code> until it is rolled over at midnight.
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH</code>
 * <td>Rollover at the top of every hour.</td>
 * <td>At approximately 11:00.000 o'clock on March 9th, 2002, <code>/foo/bar.log</code> will be
 * copied to <code>/foo/bar.log.2002-03-09-10</code>. Logging for the 11th hour of the 9th of March
 * will be output to <code>/foo/bar.log</code> until it is rolled over at the beginning of the next
 * hour.
 * <tr>
 * <td><code>'.'yyyy-MM-dd-HH-mm</code>
 * <td>Rollover at the beginning of every minute.</td>
 * <td>At approximately 11:23,000, on March 9th, 2001, <code>/foo/bar.log</code> will be copied to
 * <code>/foo/bar.log.2001-03-09-10-22</code>. Logging for the minute of 11:23 (9th of March) will
 * be output to <code>/foo/bar.log</code> until it is rolled over the next minute.
 * </table>
 * <p>
 * Do not use the colon ":" character in anywhere in the <b>DatePattern</b> option. The text before
 * the colon is interpeted as the protocol specificaion of a URL which is probably not what you
 * want.
 * 
 */
public class DateRollingFileAppender extends FileAppender {

	private String path;
	
	/**
	 * The date pattern. By default, the pattern is set to "'.'yyyyMMdd" meaning daily rollover.
	 */
	private String datePattern = "'.'yyyyMMdd";

	/**
	 * max log files.
	 */
	private int maxFiles = 0;

	/**
	 * The next time we estimate a rollover should occur.
	 */
	private long next;

	private FastDateFormat sdf;

	private RollingCalendar rc = new RollingCalendar();

	/**
	 * The default constructor does nothing.
	 */
	public DateRollingFileAppender() {
	}

	/**
	 * Instantiate a <code>DateRollingFileAppender</code> and open the file designated by
	 * <code>filename</code>. The opened filename will become the ouput destination for this
	 * appender.
	 * 
	 * @param layout the Layout
	 * @param filename the file name
	 * @param datePattern the date pattern
	 * @throws IOException if an IO error occurs
	 */
	public DateRollingFileAppender(Layout layout, String filename, String datePattern) throws IOException {
		super(layout, filename, true);
		this.datePattern = datePattern;
		activateOptions();
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * The <b>DatePattern</b> takes a string in the same format as expected by
	 * {@link SimpleDateFormat}. This options determines the rollover schedule.
	 * 
	 * @param pattern the pattern
	 */
	public void setDatePattern(String pattern) {
		datePattern = pattern;
	}

	/** @return the value of the <b>DatePattern</b> option. */
	public String getDatePattern() {
		return datePattern;
	}

	/**
	 * @return the maxFiles
	 */
	public int getMaxFiles() {
		return maxFiles;
	}

	/**
	 * @param maxFiles the maxFiles to set
	 */
	public void setMaxFiles(int maxFiles) {
		this.maxFiles = maxFiles;
	}

	
	public void activateOptions() {
		if (Strings.isEmpty(path) && Strings.isNotEmpty(fileName)) {
			path = fileName;
		}
		
		if (datePattern != null && path != null) {
			sdf = FastDateFormat.getInstance(datePattern);
			rc.setRollingPattern(datePattern);
			printPeriodicity(rc.getRollingType());
		}
		else {
			LogLog.error("Either File or DatePattern options are not set for appender [" + name + "].");
		}
	}

	/**
	 * Rollover the current file to a new file.
	 */
	private void rollOver() throws IOException {
		long now = System.currentTimeMillis();
		if (qw != null && now < next) {
			return;
		}
		
		if (qw != null) {
			this.closeFile();
			qw = null;
		}
		
		String dir = FileNames.getParent(new File(path).getAbsolutePath());
		String date = sdf.format(now);
		final String base = FileNames.getBaseName(path);
		final String ext = FileNames.getExtension(path);
		final String flog = FileNames.concat(dir, base + date + (Strings.isEmpty(ext) ? "" : '.' + ext));
		try {
			Files.makeDirs(dir);

			this.setFile(flog, fileAppend, bufferedIO, bufferSize);

			next = rc.getNextCheckMillis(now);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		if (maxFiles > 0) {
			final long old = rc.getNextCheckMillis(now, - maxFiles);

			new File(dir).listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {
					if (!flog.equals(file)) {
						String fn = FileNames.getBaseName(file);
						if (fn.startsWith(base)) {
							String ds = fn.substring(base.length());
							try {
								Date d = sdf.parse(ds);
								if (d.getTime() < old) {
									file.delete();
								}
							}
							catch (ParseException e) {
								// skip
							}
						}
					}
					
					return false;
				}
			});
		}
	}

	/**
	 * This method differentiates DateRollingFileAppender from its super class.
	 * <p>
	 * Before actually logging, this method will check whether it is time to do a rollover. If it
	 * is, it will schedule the next rollover time and then rollover.
	 */
	@Override
	protected void subAppend(LoggingEvent event) {
		try {
			rollOver();
		}
		catch (IOException ioe) {
			if (ioe instanceof InterruptedIOException) {
				Thread.currentThread().interrupt();
			}
			LogLog.error("rollOver() failed.", ioe);
		}
		super.subAppend(event);
	}

	/**
	 * This method determines if there is a sense in attempting to append.
	 * <p>
	 * It checks whether there is a set output target and also if there is a set layout. If these
	 * checks fail, then the boolean value <code>false</code> is returned.
	 */
	@Override
	protected boolean checkEntryConditions() {
		if (this.closed) {
			LogLog.warn("Not allowed to write to a closed appender.");
			return false;
		}

		if (this.layout == null) {
			errorHandler.error("No layout set for the appender named [" + name + "].");
			return false;
		}
		return true;
	}

	/**
	 * debug method
	 * @param type
	 */
	private void printPeriodicity(int type) {
		switch (type) {
		case RollingCalendar.TOP_OF_MINUTE:
			LogLog.debug("Appender [" + name + "] to be rolled every minute.");
			break;
		case RollingCalendar.TOP_OF_HOUR:
			LogLog.debug("Appender [" + name + "] to be rolled on top of every hour.");
			break;
		case RollingCalendar.TOP_OF_DAY:
			LogLog.debug("Appender [" + name + "] to be rolled at midnight.");
			break;
		case RollingCalendar.TOP_OF_WEEK:
			LogLog.debug("Appender [" + name + "] to be rolled at start of week.");
			break;
		case RollingCalendar.TOP_OF_MONTH:
			LogLog.debug("Appender [" + name + "] to be rolled at start of every month.");
			break;
		default:
			LogLog.warn("Unknown periodicity for appender [" + name + "].");
		}
	}
}


