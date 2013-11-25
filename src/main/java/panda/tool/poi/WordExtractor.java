package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hwpf.HWPFDocument;

import panda.io.Streams;
import panda.tool.poi.doc.DocTextExtractor;
import panda.util.tool.AbstractFileTool;

/**
 * 
 */
public class WordExtractor extends AbstractFileTool {
	public static class Main extends AbstractFileTool.Main {
		/**
		 * @param args arguments
		 */
		public static void main(String[] args) {
			Main m = new Main();
			WordExtractor c = new WordExtractor();
			m.execute(c, args);
		}

		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();

			addCommandLineFlag("es", "summary", "extract summary");
			addCommandLineFlag("eh", "header", "extract header");
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
		}
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	
	private int cntSucceed = 0;
	private DocTextExtractor docTextExtractor = new DocTextExtractor(); 
	
	/**
	 * Constructor
	 */
	public WordExtractor() {
		includes = new String[] { "**/*.doc" };
	}

	/**
	 * @param extractSummary the extractSummary to set
	 */
	public void setExtractSummary(boolean extractSummary) {
		docTextExtractor.setExtractSummary(extractSummary);
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	public void setExtractHeader(boolean extractHeader) {
		docTextExtractor.setExtractHeader(extractHeader);
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	public void setExtractFooter(boolean extractFooter) {
		docTextExtractor.setExtractFooter(extractFooter);
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
		HWPFDocument doc = null;

		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(xml);

			doc = new HWPFDocument(fis);

			docTextExtractor.extractToXml(doc, fos);
			
			cntSucceed++;
		} 
		finally {
			Streams.safeClose(fis);
			Streams.safeClose(fos);
		}
	}
}
