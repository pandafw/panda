package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hslf.usermodel.SlideShow;

import panda.io.Streams;
import panda.tool.poi.ppt.PptTextExtractor;
import panda.util.tool.AbstractFileTool;

/**
 * 
 */
public class PowerPointExtractor extends AbstractFileTool {
	public static class Main extends AbstractFileTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main m = new Main();
			PowerPointExtractor c = new PowerPointExtractor();
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
			
			addCommandLineFlag("es", "summary", "extract summary");
			addCommandLineFlag("eh", "header", "extract header");
			addCommandLineFlag("ef", "footer", "extract footer");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);

			if (cl.hasOption("es")) {
				setParameter("extractSummary", true);
			}

			if (cl.hasOption("eh")) {
				setParameter("extractHeader", true);
			}
			
			if (cl.hasOption("ef")) {
				setParameter("extractFooter", true);
			}
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------

	private int cntSucceed = 0;
	private PptTextExtractor pptTextExtractor = new PptTextExtractor(); 
	
	/**
	 * Constructor
	 */
	public PowerPointExtractor() {
		includes = new String[] { "**/*.ppt" };
	}

	/**
	 * @param extractSummary the extractSummary to set
	 */
	public void setExtractSummary(boolean extractSummary) {
		pptTextExtractor.setExtractSummary(extractSummary);
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	public void setExtractHeader(boolean extractHeader) {
		pptTextExtractor.setExtractHeader(extractHeader);
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	public void setExtractFooter(boolean extractFooter) {
		pptTextExtractor.setExtractFooter(extractFooter);
	}

	@Override
	protected void afterProcess() throws Exception {
		super.afterProcess();
		
		println0(cntSucceed + " of " + cntFile + " files extracted successfully");
	}

	@Override
	protected void processFile(File file) throws Exception {
		println0("Extracting " + file.getPath());

		File xml = getTargetFile(file, ".xml");
		FileInputStream fis = null;
		FileOutputStream fos = null;
		SlideShow ss = null;

		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(xml);

			ss = new SlideShow(fis);

			pptTextExtractor.extractToXml(ss, fos);
			
			cntSucceed++;
		} 
		finally {
			Streams.safeClose(fis);
			Streams.safeClose(fos);
		}
	}
}
