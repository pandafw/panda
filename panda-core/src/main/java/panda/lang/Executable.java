package panda.lang;


public abstract class Executable implements Runnable {
	public void run() {
		try {
			exec();
		}
		catch (Throwable e) {
			Exceptions.rethrowRuntime(e);
		}
	}
	
	protected abstract void exec() throws Exception;
}
