package panda.mvc.view.tag;

import java.io.Writer;

import panda.io.Streams;
import panda.ioc.annotation.IocBean;
import panda.lang.Exceptions;
import panda.log.Log;
import panda.log.Logs;

/**
 * Render a log message.
 */
@IocBean(singleton=false)
public class CLog extends Component {
	protected String category;
	protected String level;

	/**
	 * @return true to use body
	 */
	public boolean usesBody() {
		// overriding this to true such that EVAL_BODY_BUFFERED is return and
		// bodyContent will be valid hence, text between start & end tag will
		// be honoured as default message (WW-1268)
		return true;
	}

	private String addMsg(String msg, Object add) {
		if (msg == null) {
			msg = String.valueOf(add);
		}
		else {
			msg += Streams.LINE_SEPARATOR + add; 
		}
		return msg;
	}
	
	/**
	 * @see panda.mvc.view.tag.Component#end(java.io.Writer, java.lang.String)
	 */
	public boolean end(Writer writer, String body) {
		String msg = body;

		Throwable ex = context.getError();
		if (ex != null) {
			String trace = Exceptions.getStackTrace(ex);
			msg = addMsg(msg, trace);
		}

		if (msg != null) {
			String logLevel = level;
			
			Log log = Logs.getLog(category == null ? CLog.class.getName() : category);

			if ("fatal".equalsIgnoreCase(logLevel)) {
				log.fatal(msg);
			}
			else if ("error".equalsIgnoreCase(logLevel)) {
				log.error(msg);
			}
			else if ("warn".equalsIgnoreCase(logLevel)) {
				log.warn(msg);
			}
			else if ("info".equalsIgnoreCase(logLevel)) {
				log.debug(msg);
			}
			else if ("debug".equalsIgnoreCase(logLevel)) {
				log.debug(msg);
			}
			else if ("trace".equalsIgnoreCase(logLevel)) {
				log.trace(msg);
			}
			else if (ex != null) {
				log.error(msg);
			}
			else {
				log.info(msg);
			}
		}

		return super.end(writer, "");
	}

	/**
	 * @param category the log category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * @param level the log level to set
	 */
	public void setLevel(String level) {
		this.level = level;
	}

}
