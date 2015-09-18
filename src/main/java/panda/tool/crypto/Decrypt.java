package panda.tool.crypto;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import panda.lang.Arrays;
import panda.lang.Encrypts;
import panda.util.tool.AbstractCommandTool;


/**
 */
public class Decrypt {
	/**
	 * Main & Ant entry class fir JSMinify
	 */
	public static class Main extends AbstractCommandTool {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("k", "key", "Encrypt key");
			addCommandLineOption("t", "transform", "Encrypt transform");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("k")) {
				setParameter("key", cl.getOptionValue("k").trim());
			}
			
			if (cl.hasOption("t")) {
				setParameter("transform", cl.getOptionValue("t").trim());
			}

			if (Arrays.isEmpty(cl.getArgs())) {
				throw new ParseException("Decrypt arguments is required.");
			}
			setParameter("args", cl.getArgs());
		}

		/**
		 * main
		 * @param args arugments
		 */
		public static void main(String args[]) {
			Main m = new Main();
			Decrypt e = new Decrypt();
			m.execute(e, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String key = Encrypts.DEFAULT_KEY;
	private String transform = Encrypts.DEFAULT_TRANSFORM;
	private String[] args;
	
	/**
	 * @param key the key to set
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @param transform the transform to set
	 */
	public void setTransform(String transform) {
		this.transform = transform;
	}

	/**
	 * @param args the args to set
	 */
	public void setArgs(String[] args) {
		this.args = args;
	}

	/**
	 * execute
	 */
	public void execute() throws Exception {
		if (Arrays.isEmpty(args)) {
			throw new IllegalArgumentException("Decrypt arguments is required.");
		}

		for (String s : args) {
			System.out.println(s + " -> " + Encrypts.decrypt(s, key, transform));
		}
	}
}
