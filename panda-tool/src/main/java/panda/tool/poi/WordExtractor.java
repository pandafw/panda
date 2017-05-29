package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hwpf.HWPFDocument;

import panda.args.Option;
import panda.io.Streams;
import panda.tool.AbstractFileTool;
import panda.tool.poi.doc.DocTextExtractor;

/**
 * 
 */
public class WordExtractor extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new WordExtractor().execute(args);
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
	@Option(opt='S', option="summary", usage="Extract summary")
	public void setExtractSummary(boolean extractSummary) {
		docTextExtractor.setExtractSummary(extractSummary);
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	@Option(opt='H', option="header", usage="Extract header")
	public void setExtractHeader(boolean extractHeader) {
		docTextExtractor.setExtractHeader(extractHeader);
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	@Option(opt='F', option="footer", usage="Extract footer")
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
