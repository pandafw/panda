package panda.util.tool;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import panda.bean.Beans;
import panda.lang.Arrays;
import panda.lang.HandledException;
import panda.lang.Methods;
import panda.lang.Objects;
import panda.lang.Strings;

/**
 * Base main class
 * @author yf.frank.wang@gmail.com
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

	protected Object target;
	protected Options options;
	
	/**
	 * @return the target
	 */
	public Object getTarget() {
		return target;
	}

	/**
	 * @param target the target to set
	 */
	public void setTarget(Object target) {
		this.target = target;
	}

	protected void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(this.getClass().getName(), options);
	}
	
	protected void errorRequired(Options options, String name) {
		printHelp(options);
		throw new HandledException("parameter [" + name + "] is required.");
	}

	protected void setParameter(String name, Object value) {
		Beans.setProperty(target, name, value);
	}
	
	protected Object getParameter(String name) {
		return Beans.getProperty(target, name);
	}
	
	protected Option addCommandLineFlag(String opt, String name, String description) {
		return addCommandLineOption(opt, name, description, false, false);
	}
	
	protected Option addCommandLineOption(String opt, String name, String description) {
		return addCommandLineOption(opt, name, description, true, false);
	}
	
	protected Option addCommandLineOption(String opt, String name, String description, boolean required) {
		return addCommandLineOption(opt, name, description, true, required);
	}
	
	private Option addCommandLineOption(String opt, String name, String description, boolean hasArg, boolean required) {
		String def = getDefaultParameter(name);
		if (def != null) {
			description += def;
		}

		Option option = new Option(opt, description);

		// set the option properties
		option.setLongOpt(name);
		option.setArgName(name);
		option.setArgs(hasArg ? 1 : 0);
		option.setRequired(required);
		
		options.addOption(option);
		
		return option;
	}

	private String getDefaultParameter(String name) {
		Object v = getParameter(name);
		if (Objects.isEmpty(v)) {
			return null;
		}
		return " (default = " + v.toString() + ")";
	}
	
	protected void addCommandLineOptions() throws Exception {
		addCommandLineFlag("?", "help", "Print help");
	}

	protected void getCommandLineOptions(CommandLine cl) throws Exception {
	}

	/**
	 * Set arguments with target's setter method and invoke the method: target.execute. 
	 * @param target target
	 * @param args arguments
	 */
	public void execute(Object target, String[] args) {
		this.target = target;
		this.options = new Options();

		try {
			addCommandLineOptions();
			
			CommandLineParser clp = new PosixParser();
			CommandLine cl = clp.parse(options, args);

			if (cl.hasOption("?")) {
				printHelp(options);
				System.exit(1);
			}
			else {
				getCommandLineOptions(cl);
				Methods.invokeExactMethod(target, "execute", null);
			}
		}
		catch (ParseException e) {
			System.err.println(e.getMessage());
			printHelp(options);
			System.exit(2);
		}
		catch (HandledException e) {
			System.exit(3);
		}
		catch (Throwable e) {
			e.printStackTrace();
			System.exit(4);
		}
	}
}
