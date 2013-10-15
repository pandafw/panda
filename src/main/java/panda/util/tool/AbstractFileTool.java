package panda.util.tool;

import java.io.File;

import org.apache.commons.cli.CommandLine;

import panda.io.Files;
import panda.lang.HandledException;
import panda.lang.Numbers;
import panda.lang.Strings;

/**
 * Base class for file process.
 * @author yf.frank.wang@gmail.com
 */
public abstract class AbstractFileTool {
	/**
	 * Base main class for code generator. Parse basic command line options.
	 */
	protected abstract static class Main extends AbstractCommandTool {
		protected boolean hasTarget = false;
		
		@Override
		protected void addCommandLineOptions() throws Exception {
			super.addCommandLineOptions();

			addCommandLineOption("s", "source", "The source file or directory which contains source files");

			addCommandLineOption("i", "includes", "The pattern of source files to import");

			addCommandLineOption("e", "excludes", "The pattern of source files to exclude");

			if (hasTarget) {
				addCommandLineOption("t", "target", "The target file or directory which contains target files");
			}

			addCommandLineFlag("r", "recursive", "Recursively scan sub directories");

			addCommandLineFlag("ie", "ignore", "Continue processing when error occurs");

			addCommandLineOption("v", "verbose", "Print information level (1-5)");
		}

		@Override
		protected void getCommandLineOptions(CommandLine cl) throws Exception {
			super.getCommandLineOptions(cl);
			
			if (cl.hasOption("s")) {
				setParameter("source", cl.getOptionValue("s"));
			}

			if (cl.hasOption("i")) {
				setParameter("includes", cl.getOptionValue("i"));
			}

			if (cl.hasOption("e")) {
				setParameter("excludes", cl.getOptionValue("e"));
			}
			
			if (cl.hasOption("t")) {
				setParameter("target", cl.getOptionValue("t"));
			}

			if (cl.hasOption("r")) {
				setParameter("recursive", true);
			}
			
			if (cl.hasOption("ie")) {
				setParameter("ignoreError", true);
			}
			
			if (cl.hasOption("v")) {
				String v = cl.getOptionValue("v").trim();
				setParameter("verbose", Numbers.toInt(v, 5));
			}
		}
	}
	
	/**
	 * Constructor
	 */
	public AbstractFileTool() {
	}

	//---------------------------------------------------------------------------------------
	// properties
	//---------------------------------------------------------------------------------------
	protected File source = new File(".");
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
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = new File(source);
	}

	/**
	 * @param includes the includes to set
	 */
	public void setIncludes(String includes) {
		this.includes = Strings.trimAll(Strings.split(includes, ','));
	}

	/**
	 * @param excludes the excludes to set
	 */
	public void setExcludes(String excludes) {
		this.excludes = Strings.trimAll(Strings.split(excludes, ','));
	}

	/**
	 * @param recursive the recursive to set
	 */
	public void setRecursive(boolean recursive) {
		this.recursive = recursive;
	}

	/**
	 * @param verbose the verbose to set
	 */
	public void setVerbose(int verbose) {
		this.verbose = verbose;
	}
	
	/**
	 * @return the ignoreError
	 */
	public boolean isIgnoreError() {
		return ignoreError;
	}

	/**
	 * @param ignoreError the ignoreError to set
	 */
	public void setIgnoreError(boolean ignoreError) {
		this.ignoreError = ignoreError;
	}

	/**
	 * execute
	 * @throws Exception if an error occurs
	 */
	public void execute() throws Exception {
		try {
			checkParameters();
			printParameters();
			beforeProcess();
			doProcess();
			afterProcess();
		}
		catch (HandledException e) {
			System.err.println(e.getMessage());
			throw e;
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
				if (Files.pathMatch(Files.removeLeadingPath(source, f), s)) {
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
			if (Files.pathMatch(Files.removeLeadingPath(source, f), s)) {
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
			des = new File(Files.stripFileNameExtension(src) + ext);
		}
		else {
			if (source.isFile()) {
				if (target.isFile()) {
					des = target;
				}
				else if (target.isDirectory()) {
					des = new File(target, Files.getFileNameBase(src) + ext);
				}
				else if (Strings.isEmpty(Files.getFileNameExtension(target))) {
					// assume this is a directory
					target.mkdirs();
					des = new File(target, Files.getFileNameBase(src) + ext);
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
				
				String sname = Files.removeLeadingPath(source, src);
				sname = Files.stripFileNameExtension(sname) + ext;
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
		AbstractCommandTool.checkRequired(includes, "includes");
		
		for (int i = 0; i < includes.length; i++) {
			String s = includes[i];
			includes[i] = Strings.replaceChars(s, '/', File.separatorChar);
		}
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

	protected void finalProcess() throws Exception {
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
