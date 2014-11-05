package panda.wing;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import panda.bean.Beans;
import panda.io.Files;
import panda.io.Settings;
import panda.io.resource.SqlResourceBundleMaker;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.reflect.Methods;
import panda.log.Log;
import panda.log.Logs;
import panda.mvc.Setup;
import panda.task.TaskExecutor;
import panda.task.TaskScheduler;
import panda.tpl.ftl.SqlTemplateLoader;
import panda.wing.lucene.LuceneWrapper;


@IocBean(type=Setup.class)
public class ServletApplet implements Setup {
	private static final Log log = Logs.getLog(ServletApplet.class);
	
	//--------------------------------------------------------------
	@IocInject
	protected Settings properties;
	
	@IocInject(required=false)
	protected Beans beans = Beans.i();
	
	protected LuceneWrapper luceneWrapper;
	
	/**
	 * @return the lucene wrapper
	 */
	public LuceneWrapper getLuceneWrapper() {
		return luceneWrapper;
	}

	/**
	 * @param luceneWrapper the lucene wrapper to set
	 */
	public void setLuceneWrapper(LuceneWrapper luceneWrapper) {
		File path = new File(luceneWrapper.getPath());
		if ("0".equals(path.getName())) {
			try {
				luceneWrapper.close();
				long lv = getLatestLuceneRevision() + 1;
				File npath = new File(getLuceneLocation(), String.valueOf(lv));
				Files.moveDir(path, npath);
				
				luceneWrapper = new LuceneWrapper(npath.getPath(), getLuceneAnalyzerType());
				luceneWrapper.init();
			}
			catch (IOException e) {
				throw Exceptions.wrapThrow(e);
			}
		}
		
		LuceneWrapper lw = this.luceneWrapper;

		log.info("set lucene: " + luceneWrapper);
		this.luceneWrapper = luceneWrapper;
		
		// clear old lucene
		if (lw != null) {
			try {
				lw.clean();
			}
			catch (IOException e) {
				log.warn("Failed to close lucene: " + lw, e);
			}
		}
	}

	/**
	 * initialize
	 */
	public void init() {
	}

	/**
	 * destroy
	 */
	public void destroy() {
		closeLucene();
		
		Beans.i().clear();
	}

	//-----------------------------------------------
	protected File getLuceneLocation() throws IOException {
		String path = getPropertyAsPath("lucene.location", "web://WEB-INF/_lucene");
		File file = new File(path);
		Files.makeDirs(file);
		return file;
	}
	
	protected void initLucene() throws Exception {
		if (isGaeSupport()) {
			initGaeLucene();
		}
		else {
			initLocalLucene();
		}
	}

	protected void closeLucene() {
		try {
			if (luceneWrapper != null) {
				luceneWrapper.close();
				luceneWrapper = null;
			}
		}
		catch (IOException e) {
			log.error("Failed to close lucene", e);
		}
	}
	
	protected long getLatestLuceneRevision() throws IOException {
		File lucenePath = getLuceneLocation();
		
		long latest = 1;
		for (String s : lucenePath.list()) {
			File f = new File(lucenePath, s);
			Long i = Numbers.toLong(s);
			if (i == null || !Files.isDirectory(f)) {
				log.warn("Ignore invalid lucene file: " + s);
				continue;
			}

			if (i > latest) {
				latest = i;
			}
		}
		return latest;
	}

	protected void cleanLocalLuceneRevisions(long latest) throws IOException {
		File lucenePath = getLuceneLocation();

		for (String s : lucenePath.list()) {
			File f = new File(lucenePath, s);
			Long i = Numbers.toLong(s);
			if (i == null || !Files.isDirectory(f)) {
				log.warn("Remove invalid lucene file: " + s);
				Files.forceDelete(f);
				continue;
			}

			if (i < latest) {
				if (f.exists()) {
					log.info("Remove old lucene file: " + i);
					Files.forceDelete(f);
				}
			}
		}
	}

	protected void initLocalLucene() throws IOException {
		long revision = getLatestLuceneRevision();
		
		setLuceneWrapper(newLocalLucene(revision));
		
		cleanLocalLuceneRevisions(revision);
	}

	public LuceneWrapper newLocalLucene() {
		return newLocalLucene(0);
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends Analyzer> getLuceneAnalyzerType() {
		String cls = getProperty("lucene.analyzer", StandardAnalyzer.class.getName());
		try {
			return (Class<? extends Analyzer>)Classes.getClass(cls);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected LuceneWrapper newLocalLucene(long revision) {
		try {
			File lucenePath = getLuceneLocation();

			File latestPath = new File(lucenePath, String.valueOf(revision));
			
			LuceneWrapper lw = new LuceneWrapper(latestPath.getPath(), getLuceneAnalyzerType());
			lw.init();
			
			return lw;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	
}
