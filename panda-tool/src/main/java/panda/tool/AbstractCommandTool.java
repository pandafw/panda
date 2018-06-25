package panda.tool;

import java.io.File;

import panda.args.CmdLineException;
import panda.args.CmdLineParser;
import panda.args.Option;
import panda.lang.Arrays;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * Base main class
 */
public abstract class AbstractCommandTool {
	public static void checkExists(File param, String name) {
		if (param == null) {
			throw new IllegalArgumentException("parameter [" + name + "] is required.");
		}
		if (!param.exists()) {
			throw new IllegalArgumentException("file [" + name + "]: " + param.getPath() + " does not exists.");
		}
	}
	
	public static void checkRequired(Object param, String name) {
		if (Objects.isEmpty(param)) {
			throw new IllegalArgumentException("parameter [" + name + "] is required.");
		}
	}
	
	public static void checkRequired(String param, String name) {
		if (Strings.isEmpty(param)) {
			throw new IllegalArgumentException("parameter [" + name + "] is required.");
		}
	}
	
	public static void checkRequired(Object[] param, String name) {
		if (Arrays.isEmpty(param)) {
			throw new IllegalArgumentException("parameter [" + name + "] is required.");
		}
	}

	protected int exitCode;
	protected boolean showHelp;
	
	/**
	 * @return the showHelp
	 */
	public boolean isShowHelp() {
		return showHelp;
	}

	/**
	 * @param showHelp the showHelp to set
	 */
	@Option(opt='?', option="help", usage="Print usage.")
	public void setShowHelp(boolean showHelp) {
		this.showHelp = showHelp;
	}

	/**
	 * @param args arguments
	 * @return exit code
	 */
	public int execute(String[] args) {
		CmdLineParser clp = new CmdLineParser(this);
		try {
			clp.parse(args);

			if (isShowHelp()) {
				System.out.println(clp.usage());
				return exitCode;
			}
			
			clp.validate();
		}
		catch (CmdLineException e) {
			System.out.println("ERROR: " + e.getMessage());
			System.out.println();
			System.out.println(clp.usage());
			exitCode = 1;
			return exitCode;
		}

		execute();

		return exitCode;
	}
	
	public abstract void execute();
}
