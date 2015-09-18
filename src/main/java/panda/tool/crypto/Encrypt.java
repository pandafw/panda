package panda.tool.crypto;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import panda.lang.Arrays;
import panda.lang.Encrypts;
import panda.util.tool.AbstractCommandTool;


/**
 */
public class Encrypt {
	/**
	 * Main & Ant entry class fir JSMinify
	 */
	public static class Main extends AbstractCommandTool {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("k", "key", "Encrypt key");
			addCommandLineFlag("d", "dec", "Decrypt");
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

			if (cl.hasOption("d")) {
				setParameter("decrypt", true);
			}

			if (Arrays.isEmpty(cl.getArgs())) {
				throw new ParseException("Encrypt arguments is required.");
			}
			setParameter("args", cl.getArgs());
		}

		/**
		 * main
		 * @param args arugments
		 */
		public static void main(String args[]) {
			Main m = new Main();
			Encrypt e = new Encrypt();
			m.execute(e, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String key = Encrypts.DEFAULT_KEY;
	private String transform = Encrypts.DEFAULT_TRANSFORM;
	private boolean decrypt = false;
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
	 * @param decrypt the decrypt to set
	 */
	protected void setDecrypt(boolean decrypt) {
		this.decrypt = decrypt;
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
			throw new IllegalArgumentException("Encrypt arguments is required.");
		}

		for (String s : args) {
			if (decrypt) {
				System.out.println(s + " -> " + Encrypts.decrypt(s, key, transform));
			}
			else {
				System.out.println(s + " -> " + Encrypts.encrypt(s, key, transform));
			}
		}
	}
}
