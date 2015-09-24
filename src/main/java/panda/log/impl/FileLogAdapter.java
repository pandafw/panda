package panda.log.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import panda.io.FileNames;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;
import panda.lang.time.RollingCalendar;
import panda.log.Log;
import panda.log.LogLevel;


public class FileLogAdapter extends AbstractLogAdapter {
	protected FastDateFormat sdf;

	protected long next = System.currentTimeMillis();
	
	protected String path;

	protected String datePattern = "'.'yyyyMMdd";
	
	protected String encoding = "UTF-8";
	
	/**
	 * -1: immediate flush
	 *  0: no buffer
	 */
	protected int bufferSize = -1;
	
	protected int maxFiles = 0;
	
	protected Writer writer;
	
	protected RollingCalendar rc;
	
	public Log getLogger(String name) {
		return new FileLog(this, name);
	}

	@Override
	public void init(Properties props) {
		super.init(props);
		
		validatePath();

		rc = new RollingCalendar();
		rc.setRollingPattern(datePattern);

		sdf = FastDateFormat.getInstance(datePattern);
	}

	@Override
	protected void setProperty(String name, String value) {
		if ("file".equalsIgnoreCase(name) || "path".equalsIgnoreCase(name)) {
			setPath(value);
		}
		else if ("maxFiles".equalsIgnoreCase(name)) {
			setMaxFiles(Numbers.toInt(value, 0));
		}
		else if ("datePattern".equalsIgnoreCase(name)) {
			setDatePattern(value);
		}
		else if ("encoding".equalsIgnoreCase(name)) {
			setEncoding(value);
		}
		else if ("bufferSize".equalsIgnoreCase(name)) {
			setBufferSize(Numbers.toInt(value, 0));
		}
	}

	protected void setPath(String value) {
		path = value;
	}

	public void setMaxFiles(int maxFiles) {
		this.maxFiles = maxFiles;
	}

	public void setDatePattern(String datePattern) {
		this.datePattern = datePattern;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	protected void validatePath() {
		if (Strings.isEmpty(path)) {
			throw new IllegalArgumentException(getClass().getName() + ".path is invalid!");
		}
	}

	protected String getAbsolutePath() {
		return new File(path).getAbsolutePath();
	}
	
	protected void rollOver() {
		long now = System.currentTimeMillis();
		if (writer != null && now < next) {
			return;
		}
		
		if (writer != null) {
			Streams.safeClose(writer);
			writer = null;
		}
		
		String path = getAbsolutePath();
		String dir = FileNames.getParent(path);
		String date = sdf.format(now);
		final String base = FileNames.getBaseName(path);
		final String ext = FileNames.getExtension(path);
		final File flog = new File(dir, base + date + (Strings.isEmpty(ext) ? "" : '.' + ext));
		try {
			Files.makeDirs(dir);

			FileOutputStream fos = new FileOutputStream(flog, true);
			writer = new OutputStreamWriter(fos, encoding);
			if (bufferSize > 0) {
				writer = new BufferedWriter(writer, bufferSize);
			}
			
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

	protected synchronized void print(String msg) {
		try {
			rollOver();
			writer.write(msg);
			if (bufferSize == -1) {
				writer.flush();
				
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * File log
	 */
	protected static class FileLog extends AbstractLog {
		protected FileLogAdapter adapter;
		protected String name;
		
		protected FileLog(FileLogAdapter adapter, String name) {
			super(name);
			this.adapter = adapter;
			this.name = name;
		}

		@Override
		public void log(LogLevel level, String msg, Throwable t) {
			StringBuilder sb = new StringBuilder();
			sb.append(DateTimes.timestampFormat().format(DateTimes.getDate()))
				.append(' ')
				.append(Strings.rightPad(level.toString(), 5))
				.append(" [")
				.append(Thread.currentThread().getName())
				.append("] ")
				.append(name)
				.append(" - ")
				.append(msg)
				.append('\n');

			if (t != null) {
				sb.append(Exceptions.getStackTrace(t)).append('\n');
			}

			adapter.print(sb.toString());
		}
	}
}
