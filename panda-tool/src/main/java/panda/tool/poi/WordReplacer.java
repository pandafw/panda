package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hwpf.HWPFDocument;

import panda.io.FileNames;
import panda.io.Streams;
import panda.tool.poi.doc.DocTextReplacer;
import panda.util.tool.AbstractFileTool;

/**
 * 
 */
public class WordReplacer extends AbstractFileTool {
	public static class Main extends AbstractFileTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main m = new Main();
			WordReplacer c = new WordReplacer();
			m.execute(c, args);
		}

		/**
		 * Constructor 
		 */
		public Main() {
			hasTarget = true;
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();

			addCommandLineOption("x", "suffix", "The suffix of target file name");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);

			if (cl.hasOption("x")) {
				setParameter("suffix", cl.getOptionValue("x"));
			}
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected String suffix = "";
	
	private int cntSucceed = 0;
	
	/**
	 * Constructor
	 */
	public WordReplacer() {
		includes = new String[] { "**/*.doc" };
	}

	/**
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * @param suffix the suffix to set
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		println0(cntSucceed + " of " + cntFile + " files replaced successfully");
	}

	@Override
	protected void processFile(File fdoc) throws Exception {
		File fxml = new File(FileNames.removeExtension(fdoc) + ".xml");
		if (!fxml.exists()) {
			println0("Skip " + fdoc.getPath());
			return;
		}

		println0("Replacing " + fdoc.getPath());
		File fdes = getTargetFile(fdoc, suffix + ".doc");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		HWPFDocument doc = null;

		try {
			fis = new FileInputStream(fdoc);

			doc = new HWPFDocument(fis);

			DocTextReplacer dr = new DocTextReplacer();
			
			dr.replaceFromXml(doc, fxml);

			Streams.safeClose(fis);
			fis = null;
			
			fos = new FileOutputStream(fdes);
			doc.write(fos);
			
			cntSucceed++;
		} 
		finally {
			Streams.safeClose(fis);
			Streams.safeClose(fos);
		}
	}
}
