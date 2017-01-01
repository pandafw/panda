package panda.lang;

import panda.lang.reflect.Fields;

public class Processors {
	public static long getPid(Process process) {
		try {
			return (Long)Fields.readField(process, "pid", true);
		}
		catch (Exception e) {
			return 0;
		}
	}
	
	public static class Waiter extends Thread {
		private int exitValue;
		private final Process process;

		public Waiter(Process process) {
			this.process = process;
		}

		public int exitValue() {
			return exitValue;
		}
		
		public void run() {
			try {
				exitValue = process.waitFor();
			}
			catch (InterruptedException ignore) {
				return;
			}
		}
	}

}
