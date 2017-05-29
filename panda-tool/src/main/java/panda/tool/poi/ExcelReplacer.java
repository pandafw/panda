package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import panda.args.Option;
import panda.io.FileNames;
import panda.io.Streams;
import panda.tool.AbstractFileTool;
import panda.tool.poi.xls.XlsTextReplacer;

/**
 * 
 */
public class ExcelReplacer extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new ExcelReplacer().execute(args);
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

	@Option(opt='t', option="target", arg="FILE", usage="The target file or directory which contains target files")
	public void setTarget(String target) {
		super.setTarget(target);
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
	@Option(opt='x', option="suffix", arg="SUFFIX", usage="The suffix of target file name")
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
