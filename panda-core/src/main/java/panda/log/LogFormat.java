package panda.log;

import java.util.ArrayList;
import java.util.List;

import panda.io.Streams;
import panda.lang.Numbers;
import panda.lang.Strings;
import panda.lang.time.DateTimes;
import panda.lang.time.FastDateFormat;

/**
 * Log Format
 * 
 * %c: log name(category)
 * %m: message
 * %n: the platform dependent line separator (windows: "\r\n", linux: "\n")
 * %l: log level
 * %p: log level (Log4j compatible)
 * %t: thread id
 * %C: caller class name (!!SLOW!!)
 * %L: caller source line number (!!SLOW!!)
 * %M: caller class method (!!SLOW!!)
 * %d{format}: date
 */
public abstract class LogFormat {
	public abstract String format(LogEvent event);
	
	public final static LogFormat DEFAULT = new SimpleLogFormat("%d{yyyy-MM-dd HH:mm:ss} %-5p %c - %m%n");
	
	public static class SimpleLogFormat extends LogFormat {
		private List<LogFormat> formats;

		public SimpleLogFormat(String pattern) {
			formats = new ArrayList<LogFormat>();

			int s = 0;
			for (int i = 0; i < pattern.length(); i++) {
				char c = pattern.charAt(i);
				if (c != '%') {
					continue;
				}
				
				// string
				if (s < i) {
					formats.add(new StringLogFormat(pattern.substring(s, i)));
				}

				i++;
				s = i;
				if (i >= pattern.length()) {
					break;
				}

				// padding
				int padding = 0;
				int j = i;
				for (; j < pattern.length(); j++) {
					char c2 = pattern.charAt(j);
					if (!(c2 == '-' || (c2 >= '0' && c2 <= '9'))) {
						break;
					}
				}
				if (j > i) {
					padding = Numbers.toInt(pattern.substring(i, j), 0);
				}

				i = j;
				if (i >= pattern.length()) {
					break;
				}

				// symbol
				LogFormat format = null;
				char symbol = pattern.charAt(i);
				switch (symbol) {
				case 'c':
					format = new NameLogFormat(padding);
					break;
				case 'm':
					format = new MessageLogFormat(padding);
					break;
				case 'n':
					format = new StringLogFormat(Streams.LINE_SEPARATOR);
					break;
				case 'l':
				case 'p':
					format = new LevelLogFormat(padding);
					break;
				case 't':
					format = new ThreadLogFormat(padding);
					break;
				case 'C':
					format = new ClassLogFormat(padding);
					break;
				case 'M':
					format = new MethodLogFormat(padding);
					break;
				case 'L':
					format = new LineNoLogFormat(padding);
					break;
				case 'd':
					format = new DateLogFormat(padding);
					if (i + 1 < pattern.length() && pattern.charAt(i + 1) == '{') {
						int e = pattern.indexOf('}', i + 2);
						if (e > i + 3) {
							((DateLogFormat)format).setFormat(pattern.substring(i + 2, e));
							i = e;
						}
					}
					break;
				}
				
				if (format != null) {
					formats.add(format);
					s = i + 1;
				}
			}
			
			if (s < pattern.length()) {
				formats.add(new StringLogFormat(pattern.substring(s)));
			}
		}
		
		public String format(LogEvent event) {
			StringBuilder sb = new StringBuilder();
			for (LogFormat lf : formats) {
				sb.append(lf.format(event));
			}
			return sb.toString();
		}
	}

	
	public static class StringLogFormat extends LogFormat {
		private String str;
		
		public StringLogFormat(String str) {
			this.str = str;
		}

		@Override
		public String format(LogEvent event) {
			return str;
		}
	}

	public static abstract class SymbolLogFormat extends LogFormat {
		protected int padding;

		/**
		 * @return the padding
		 */
		public int getPadding() {
			return padding;
		}

		/**
		 * @param padding the padding to set
		 */
		public void setPadding(int padding) {
			this.padding = padding;
		}

		@Override
		public String format(LogEvent event) {
			String str = value(event);
			if (padding == 0) {
				return str;
			}
			if (padding < 0) {
				return Strings.rightPad(str, -padding);
			}
			return Strings.leftPad(str, padding);
		}

		protected abstract String value(LogEvent event);
	}
	
	public static class DateLogFormat extends SymbolLogFormat {
		private FastDateFormat format = DateTimes.isoDatetimeNotFormat();
		
		public DateLogFormat(int padding) {
			this.padding = padding;
		}

		/**
		 * @param format the format to set
		 */
		public void setFormat(String format) {
			try {
				this.format = FastDateFormat.getInstance(format);
			}
			catch (Exception e) {
				LogLog.error("Invalid date format: " + format, e);
			}
		}

		@Override
		protected String value(LogEvent event) {
			return format.format(event.getMillis());
		}
	}
	
	public static class NameLogFormat extends SymbolLogFormat {
		public NameLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			return event.getName();
		}
	}
	
	public static class MessageLogFormat extends SymbolLogFormat {
		public MessageLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			return event.getMessage();
		}
	}
	
	public static class LevelLogFormat extends SymbolLogFormat {
		public LevelLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			return event.getLevel().toString();
		}
	}
	
	public static class ThreadLogFormat extends SymbolLogFormat {
		public ThreadLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			return String.valueOf(event.getThreadId());
		}
	}
	
	public static class ClassLogFormat extends SymbolLogFormat {
		public ClassLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			event.inferCaller();
			return event.getCallClass();
		}
	}
	
	public static class MethodLogFormat extends SymbolLogFormat {
		public MethodLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			event.inferCaller();
			return event.getCallMethod();
		}
	}
	
	public static class LineNoLogFormat extends SymbolLogFormat {
		public LineNoLogFormat(int padding) {
			this.padding = padding;
		}

		@Override
		protected String value(LogEvent event) {
			event.inferCaller();
			return String.valueOf(event.getCallLineNo());
		}
	}
}
