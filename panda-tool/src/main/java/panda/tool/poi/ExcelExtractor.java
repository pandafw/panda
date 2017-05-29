package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import panda.args.Option;
import panda.io.Streams;
import panda.tool.AbstractFileTool;
import panda.tool.poi.xls.XlsTextExtractor;

/**
 * 
 */
public class ExcelExtractor extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new ExcelExtractor().execute(args);
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------

	private int cntSucceed = 0;
	private XlsTextExtractor xlsTextExtractor = new XlsTextExtractor(); 
	
	/**
	 * Constructor
	 */
	public ExcelExtractor() {
		includes = new String[] { "**/*.xls" };
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
		xlsTextExtractor.setExtractSummary(extractSummary);
	}

	/**
	 * @param extractHeader the extractHeader to set
	 */
	@Option(opt='H', option="header", usage="Extract header")
	public void setExtractHeader(boolean extractHeader) {
		xlsTextExtractor.setExtractHeader(extractHeader);
	}

	/**
	 * @param extractFooter the extractFooter to set
	 */
	@Option(opt='F', option="footer", usage="Extract footer")
	public void setExtractFooter(boolean extractFooter) {
		xlsTextExtractor.setExtractFooter(extractFooter);
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
		HSSFWorkbook wb = null;

		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(xml);

			wb = new HSSFWorkbook(fis);

			xlsTextExtractor.extractToXml(wb, fos);
			
			cntSucceed++;
		} 
		finally {
			Streams.safeClose(fis);
			Streams.safeClose(fos);
		}
	}
}
