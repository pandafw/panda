package panda.gems.admin.action;

import java.io.File;
import java.io.InputStream;

import panda.app.action.BaseAction;
import panda.app.auth.Auth;
import panda.app.constant.AUTH;
import panda.io.Files;
import panda.io.Streams;
import panda.lang.Processors;
import panda.lang.Processors.Waiter;
import panda.lang.Randoms;
import panda.lang.Strings;
import panda.lang.Systems;
import panda.lang.time.StopWatch;
import panda.mvc.annotation.At;
import panda.mvc.annotation.To;
import panda.mvc.annotation.TokenProtect;
import panda.mvc.annotation.param.Param;
import panda.mvc.view.Views;


@Auth(AUTH.SUPER)
@At("${!!super_path|||'/super'}/shell")
@To(Views.SFTL)
public class ShellAction extends BaseAction {
	public static class Result {
		private int code = -1;
		private String stdout;
		private String stderr;
		private String elapsed;

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
	@TokenProtect
	@To(Views.SJSON)
	public Object exec(@Param("cmd") String cmd, @Param("wait") int wait) throws Exception {
		if (Strings.isEmpty(cmd)) {
			return null;
		}

		File tf = File.createTempFile(Randoms.randUUID32(), Systems.IS_OS_WINDOWS ? ".bat" : ".sh");
		tf.setExecutable(true);
		Files.write(tf, Systems.IS_OS_WINDOWS ? cmd : Strings.remove(cmd, '\r'));

		// check wait seconds
		if (wait < 1 || wait > 300) {
			wait = 60;
		}
		wait *= 1000;

		Process p = null;
		Result r = new Result();

		try {
			StopWatch watch = new StopWatch();
			p = Runtime.getRuntime().exec(tf.getAbsolutePath());
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
		finally {
			if (p != null) {
				p.destroy();
			}
			tf.delete();
		}
	}

}
