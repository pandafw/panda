package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hslf.usermodel.SlideShow;

import panda.args.Option;
import panda.io.Streams;
import panda.tool.AbstractFileTool;
import panda.tool.poi.ppt.PptTextExtractor;

/**
 * 
 */
public class PowerPointExtractor extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new PowerPointExtractor().execute(args);
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

	@Option(opt='t', option="target", arg="FILE", usage="The target file or directory which contains target files")
	public void setTarget(String target) {
		super.setTarget(target);
	}

	/**
	 * @param extractSummary the extractSummary to set
	 */
	@Option(opt='S', option="summary", usage="Extract summary")
	public void setExtractSummary(boolean extractSummary) {
		pptTextExtractor.setExtractSummary(extractSummary);
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	@Option(opt='H', option="header", usage="Extract header")
	public void setExtractHeader(boolean extractHeader) {
		pptTextExtractor.setExtractHeader(extractHeader);
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	@Option(opt='F', option="footer", usage="Extract footer")
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
