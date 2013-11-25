package panda.tool.chardet;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.cli.CommandLine;

import panda.io.Streams;
import panda.lang.Charsets;
import panda.lang.HandledException;
import panda.lang.Strings;
import panda.lang.chardet.LangHint;
import panda.util.tool.AbstractCommandTool;

/**
 * A class used for detect text encoding
 */
public class CharDetect {
	/**
	 * Main & Ant entry class fir JSMinify
	 */
	public static class Main extends AbstractCommandTool {
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();
			
			addCommandLineOption("f", "file", "Source file.");
			addCommandLineOption("u", "url", "URL.");
			addCommandLineOption("h", "hint", "Language hint. (ja | zh | cn | tw | ko)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("f")) {
				setParameter("file", cl.getOptionValue("f").trim());
			}
			
			if (cl.hasOption("u")) {
				setParameter("url", cl.getOptionValue("u").trim());
			}
			
			if (!cl.hasOption("f") && !cl.hasOption("u")) {
				errorRequired(options, "file/url");
			}
			
			if (cl.hasOption("h")) {
				setParameter("hint", cl.getOptionValue("h").trim());
			}
		}

		/**
		 * main
		 * @param args arugments
		 */
		public static void main(String args[]) {
			Main m = new Main();
			CharDetect cd = new CharDetect();
			m.execute(cd, args);
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	private String file;
	private String url;
	private LangHint hint;
	

	/**
	 * @param file the file to set
	 */
	public void setFile(String file) {
		this.file = file;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @param hint the hint to set
	 */
	public void setHint(String hint) {
		this.hint = LangHint.parse(hint);
	}

	protected void checkParameters() throws Exception {
		if (Strings.isEmpty(file) && Strings.isEmpty(url)) {
			throw new IllegalArgumentException("parameter [file] or [url] is required.");
		}
	}

	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		InputStream is = null;
		try {
			checkParameters();
			if (Strings.isNotEmpty(file)) {
				is = new FileInputStream(file);
			}
			else {
				is = new URL(url).openStream();
			}
			is = Streams.buffer(is);
			String charset = Charsets.detectCharset(is, hint);
			
			System.out.println(charset);
		}
		catch (HandledException e) {
			System.err.println(e.getMessage());
			throw e;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new HandledException(e);
		}
		finally {
			Streams.safeClose(is);
		}
	}
}
