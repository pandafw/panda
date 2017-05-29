package panda.tool;

import java.io.File;

import panda.args.Option;
import panda.io.FileNames;
import panda.lang.HandledException;
import panda.lang.Strings;
import panda.lang.Systems;

/**
 * Base class for file process.
 */
public abstract class AbstractFileTool extends AbstractCommandTool {
	/**
	 * Constructor
	 */
	public AbstractFileTool() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected File source = Systems.getUserDir();
	protected File target;
	protected String[] includes = new String[] { "**/*" };
	protected String[] excludes;
	protected boolean recursive = false;
	protected boolean ignoreError = false;
	protected int verbose = 0;

	protected int cntFile = 0;

	/**
	 * @return the source
	 */
	public String getSource() {
		return source.getPath();
	}

	/**
	 * @return the includes
	 */
	public String getIncludes() {
		return Strings.join(includes, ",");
	}

	/**
	 * @return the excludes
	 */
	public String getExcludes() {
		return Strings.join(excludes, ",");
	}

	/**
	 * @return the verbose
	 */
	public int getVerbose() {
		return verbose;
	}

	/**
	 * @return the recursive
	 */
	public boolean isRecursive() {
		return recursive;
	}
	
	/**
	 * @return the ignoreError
	 */
	public boolean isIgnoreError() {
		return ignoreError;
	}

	/**
	 * @param source the source to set
	 */
	@Option(opt='s', option="source", arg="FILE", usage="The source file or directory which contains source files")
	public void setSource(String source) {
		this.source = new File(source);
	}

	/**
	 * @param includes the includes to set
	 */
	@Option(opt='i', option="includes", arg="PATTERN", usage="The pattern of source files to import")
	public void setIncludes(String includes) {
		this.includes = Strings.trimAll(Strings.split(includes, ','));
		for (int i = 0; i < this.includes.length; i++) {
			String s = this.includes[i];
			this.includes[i] = Strings.replaceChars(s, '/', File.separatorChar);
		}
	}

	protected void setTarget(String target) {
		this.target = new File(target);
	}

	/**
	 * @param excludes the excludes to set
	 */
	@Option(opt='e', option="excludes", arg="PATTERN", usage="The pattern of source files to exclude")
	public void setExcludes(String excludes) {
		this.excludes = Strings.trimAll(Strings.split(excludes, ','));
	}

	/**
	 * @param recursive the recursive to set
	 */
	@Option(opt='r', option="recursive", usage="Recursively scan sub directories")
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * @param ignoreError the ignoreError to set
	 */
	@Option(opt='q', option="quiet", usage="Continue processing when error occurs")
	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

	/**
	 * @param verbose the verbose to set
	 */
	@Option(opt='v', option="verbose", arg="LEVEL", usage="Print information level (1-5)")
	public void setVerbose(int verbose) {
		this.verbose = verbose;
	}

	/**
	 * execute
	 */
	@Override
	public void execute() {
		try {
			checkParameters();
			printParameters();
			beforeProcess();
			doProcess();
			afterProcess();
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new HandledException(e);
		}
		finally {
			finalProcess();
		}
	}

	protected boolean isExclude(File f) {
		if (source.equals(f)) {
			return false;
		}
		if (excludes != null) {
			for (String s : excludes) {
				if (FileNames.pathMatch(FileNames.removeLeadingPath(source, f), s)) {
					return true;
				}
			}
		}
		return false;
	}

	protected boolean isInclude(File f) {
		if (source.equals(f)) {
			return true;
		}
		for (String s : includes) {
			if (FileNames.pathMatch(FileNames.removeLeadingPath(source, f), s)) {
				return true;
			}
		}
		return false;
	}
	
	protected void process(File f) throws Exception {
		if (f.isHidden()) {
			return;
		}
		else if (f.isDirectory()) {
			if (f.equals(source) || (recursive && !isExclude(f))) {
				File[] sfs = f.listFiles();
				for (File sf : sfs) {
					process(sf);
				}
			}
		}
		else if (f.isFile()) {
			if (!isExclude(f) && isInclude(f)) {
				cntFile++;
				if (ignoreError) {
					try {
						processFile(f);
					}
					catch (Throwable e) {
						e.printStackTrace();
					}
				}
				else {
					processFile(f);
				}
			}
		}
	}

	protected File getTargetFile(File src, String ext) {
		File des;
		
		if (target == null) {
			des = new File(FileNames.removeExtension(src) + ext);
		}
		else {
			if (source.isFile()) {
				if (target.isFile()) {
					des = target;
				}
				else if (target.isDirectory()) {
					des = new File(target, FileNames.getBaseName(src) + ext);
				}
				else if (Strings.isEmpty(FileNames.getExtension(target))) {
					// assume this is a directory
					target.mkdirs();
					des = new File(target, FileNames.getBaseName(src) + ext);
				}
				else {
					File parent = target.getParentFile();
					if (!parent.exists()) {
						parent.mkdirs();
					}
					des = target;
				}
			}
			else {
				if (target.isFile()) {
					target = target.getParentFile();
				}
				
				String sname = FileNames.removeLeadingPath(source, src);
				sname = FileNames.removeExtension(sname) + ext;
				des = new File(target, sname);
				File parent = des.getParentFile();
				if (!parent.exists()) {
					parent.mkdirs();
				}
			}
		}
		
		return des;
	}
	
	protected void checkParameters() throws Exception {
	}

	protected void printParameters() {
		println4("source = " + getSource());
		println4("includes = " + getIncludes());
		println4("excludes = " + getExcludes());
		println4("recursive = " + isRecursive());
	}
	
	protected abstract void processFile(File file) throws Exception;

	protected void doProcess() throws Exception {
		process(source);
	}
	
	protected void beforeProcess() throws Exception {
		cntFile = 0;
	}

	protected void afterProcess() throws Exception {
	}

	protected void finalProcess() {
	}

	protected void print(int level, String s) {
		if (verbose >= level) {
			System.out.print(s);
		}
	}
	protected void print0(String s) {
		print(0, s);
	}
	protected void print1(String s) {
		print(1, s);
	}
	protected void print2(String s) {
		print(2, s);
	}
	protected void print3(String s) {
		print(3, s);
	}
	protected void print4(String s) {
		print(4, s);
	}
	protected void print5(String s) {
		print(5, s);
	}

	protected void println(int level, String s) {
		if (verbose >= level) {
			System.out.println(s);
		}
	}
	protected void println0(String s) {
		println(0, s);
	}
	protected void println1(String s) {
		println(1, s);
	}
	protected void println2(String s) {
		println(2, s);
	}
	protected void println3(String s) {
		println(3, s);
	}
	protected void println4(String s) {
		println(4, s);
	}
	protected void println5(String s) {
		println(5, s);
	}

	protected void title(int level, String s) {
		if (verbose >= level) {
			System.out.println("----------------------------------------------------------------------");
			System.out.println(s);
			System.out.println("----------------------------------------------------------------------");
		}
	}
	protected void title0(String s) {
		title(0, s);
	}
	protected void title1(String s) {
		title(1, s);
	}
	protected void title2(String s) {
		title(2, s);
	}
	protected void title3(String s) {
		title(3, s);
	}
	protected void title4(String s) {
		title(4, s);
	}
	protected void title5(String s) {
		title(5, s);
	}

}
