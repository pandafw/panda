package panda.tool.poi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hwpf.HWPFDocument;

import panda.args.Option;
import panda.io.FileNames;
import panda.io.Streams;
import panda.tool.AbstractFileTool;
import panda.tool.poi.doc.DocTextReplacer;

/**
 * 
 */
public class WordReplacer extends AbstractFileTool {
	/**
	 * @param args arguments
	 */
	public static void main(String[] args) {
		new WordReplacer().execute(args);
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
