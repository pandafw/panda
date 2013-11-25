package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import panda.io.FileNames;
import panda.io.Streams;
import panda.tool.poi.xls.XlsTextReplacer;
import panda.util.tool.AbstractFileTool;

/**
 * 
 */
public class ExcelReplacer extends AbstractFileTool {
	public static class Main extends AbstractFileTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main m = new Main();
			ExcelReplacer c = new ExcelReplacer();
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
	public ExcelReplacer() {
		includes = new String[] { "**/*.xls" };
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
	protected void processFile(File fxls) throws Exception {
		File fxml = new File(FileNames.removeExtension(fxls) + ".xml");
		if (!fxml.exists()) {
			println0("Skip " + fxls.getPath());
			return;
		}

		println0("Replacing " + fxls.getPath());
		File ftx = getTargetFile(fxls, suffix + ".xls");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		HSSFWorkbook wb = null;

		try {
			fis = new FileInputStream(fxls);

			wb = new HSSFWorkbook(fis);

			XlsTextReplacer xr = new XlsTextReplacer();
			
			xr.replaceFromXml(wb, fxml);

			Streams.safeClose(fis);
			fis = null;
			
			fos = new FileOutputStream(ftx);
			wb.write(fos);
			
			cntSucceed++;
		} 
		finally {
			Streams.safeClose(fis);
			Streams.safeClose(fos);
		}
	}
}
