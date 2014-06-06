package panda.log.impl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import panda.io.FileNames;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.log.Log;
import panda.log.LogLevel;


public class FileLogAdapter extends AbstractLogAdapter {
	protected SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

	protected long next = System.currentTimeMillis() - 1;
	
	protected String path;

	protected Writer writer;
	
	public Log getLogger(String name) {
		return new FileLog(name);
	}

	@Override
	public void init(Properties props) {
		super.init(props);
		
		if (Strings.isEmpty(path)) {
			throw new IllegalArgumentException(getClass().getName() + ".file is invalid!");
		}

		rollOver();
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("file".equalsIgnoreCase(name)) {
			setPath(value);
		}
	}

	protected void setPath(String value) {
		path = value;
	}

	protected void rollOver() {
		long now = System.currentTimeMillis();
		if (writer == null || now > next) {
			if (writer != null) {
				Streams.safeClose(writer);
			}
			
			String date = sdf.format(new Date(now));
			String file = FileNames.removeExtension(path) + '.' + date + '.' + FileNames.getExtension(path);
			try {
				FileOutputStream fos = new FileOutputStream(file, true);
				writer = new OutputStreamWriter(fos, "UTF-8");
				
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(now);
				cal.add(Calendar.DAY_OF_MONTH, 1);
				DateTimes.truncate(cal, Calendar.DAY_OF_MONTH);
				
				next = cal.getTimeInMillis() - 1;
			}
			catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected synchronized void safeLog(LogLevel level, String msg) {
		try {
			rollOver();
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * File log
	 */
	private class FileLog extends AbstractLog {
		private String name;
		
		FileLog(String name) {
			this.name = name;
			level = getLogLevel(name);
		}

		@Override
		protected void safeLog(LogLevel level, String msg, Throwable t) {
			StringBuilder sb = new StringBuilder();
			sb.append(DateTimes.datetimeFormat().format(DateTimes.getDate()))
				.append(' ') 
				.append(level.toString())
				.append(' ')
				.append(Strings.rightPad(Thread.currentThread().getName(), 10))
				.append(' ')
				.append(Strings.rightPad(name, 30))
				.append(msg)
				.append('\n');

			if (t != null) {
				sb.append(Exceptions.getStackTrace(t));
			}

			FileLogAdapter.this.safeLog(level, msg);
		}
	}
}
