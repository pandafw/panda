package panda.app.action.tool;

import java.io.InputStream;

import panda.app.action.AbstractAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.io.Streams;
import panda.lang.Processors;
import panda.lang.Processors.Waiter;
import panda.lang.Strings;
import panda.lang.time.StopWatch;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;


@Auth(AUTH.SUPER)
@At("${super_path}/cmd")
@To(Views.SFTL)
public class CommandAction extends AbstractAction {
	public static class Result {
		private String command;
		private int code = -1;
		private String stdout;
		private String stderr;
		private String elapsed;

		public String getCommand() {
			return command;
		}
		public void setCommand(String command) {
			this.command = command;
		}
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getStdout() {
			return stdout;
		}
		public void setStdout(String stdout) {
			this.stdout = stdout;
		}
		public String getStderr() {
			return stderr;
		}
		public void setStderr(String stderr) {
			this.stderr = stderr;
		}
		public String getElapsed() {
			return elapsed;
		}
		public void setElapsed(String elapsed) {
			this.elapsed = elapsed;
		}
		
	}

	@At("")
	@To(Views.SFTL)
	public void input() {
	}

	@At
	@To(Views.SJSON)
	public Object json(@Param("cmd") String cmd, @Param("wait") int wait) {
		return exec(cmd, wait);
	}
	
	@At
	@To(Views.SXML)
	public Object xml(@Param("cmd") String cmd, @Param("wait") int wait) {
		return exec(cmd, wait);
	}

	protected Object exec(String cmd, int wait) {
		if (Strings.isEmpty(cmd)) {
			return null;
		}

		// check wait seconds
		if (wait < 1 || wait > 300) {
			wait = 60;
		}
		wait *= 1000;

		Process p = null;
		Result r = new Result();
		r.setCommand(cmd);

		StopWatch watch = new StopWatch();
		
		try {
			p = Runtime.getRuntime().exec(cmd);
			Waiter w = new Waiter(p);
			w.start();
			
			StringBuilder so = new StringBuilder();
			StringBuilder se = new StringBuilder();
			InputStream is = p.getInputStream();
			InputStream es = p.getErrorStream();

			while (w.isAlive()) {
				w.join(100);
				Streams.copy(is, so);
				Streams.copy(es, so);

				if (watch.getTime() > wait) {
					throw new RuntimeException("Process [" + Processors.getPid(p) + "] of Command [ " + cmd + " ] is timeout for " + (wait / 1000) + " seconds.");
				}
			}
			
			Streams.copy(is, so);
			Streams.copy(es, se);
			
			r.code = w.exitValue();
			r.stdout = so.toString();
			r.stderr = se.toString();
			r.elapsed = watch.toString();
			return r;
		}
		catch (Exception e) {
			getContext().setError(e);
			
			if (p != null) {
				p.destroy();
			}
			return null;
		}
		finally {
		}
	}

}
