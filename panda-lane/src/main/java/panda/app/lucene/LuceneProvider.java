package panda.app.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import panda.app.AppConstants;
import panda.app.constant.SET;
import panda.app.util.AppSettings;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.ioc.util.ObjectProvider;
import panda.lang.Classes;
import panda.lang.Exceptions;
import panda.lang.Numbers;
import panda.lang.Systems;
import panda.log.Log;
import panda.log.Logs;


@IocBean(create="initialize", depose="destroy")
public class LuceneProvider implements ObjectProvider<LuceneWrapper> {
	private static final Log log = Logs.getLog(LuceneProvider.class);
	
	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=AppConstants.LUCENE_STORE, required=false)
	protected String store;
	
	@IocInject(value=AppConstants.LUCENE_LOCATION, required=false)
	protected String location = "web://WEB-INF/_lucene";
	
	@IocInject(value=AppConstants.LUCENE_ANALYZER, required=false)
	protected String analyzer = StandardAnalyzer.class.getName();
	
	protected LuceneWrapper luceneWrapper;

	public boolean isInMemory() {
		return (Systems.IS_OS_APPENGINE || "mem".equalsIgnoreCase(getLuceneStore()));
	}

	/**
	 * initialize
	 * @throws IOException if an IO error occurs
	 */
	public void initialize() throws IOException {
		if (isInMemory()) {
			initMemoryLucene();
		}
		else {
			initLocalLucene();
		}
	}

	public LuceneWrapper newLuceneWrapper() {
		if (isInMemory()) {
			return newMemoryLucene();
		}
		return newLocalLucene(0);
	}
	
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
		log.info("set lucene: " + luceneWrapper);

		LuceneWrapper lw = this.luceneWrapper;
		this.luceneWrapper = luceneWrapper;
		
		// clear old lucene
		if (lw != null) {
			try {
				lw.close();
				lw.clean();
			}
			catch (IOException e) {
				log.warn("Failed to close lucene: " + lw, e);
			}
		}
	}

	/**
	 * destroy
	 */
	public void destroy() {
		closeLucene();
	}

	//-----------------------------------------------
	protected String getLuceneStore() {
		return settings.getProperty(SET.LUCENE_STORE, store);
	}
	
	protected File getLuceneLocation() throws IOException {
		String path = settings.getPropertyAsPath(SET.LUCENE_LOCATION, location);
		File file = new File(path);
		Files.makeDirs(file);
		return file;
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

	protected void initMemoryLucene() {
		setLuceneWrapper(newMemoryLucene());
	}
	
	protected void initLocalLucene() throws IOException {
		long revision = getLatestLuceneRevision();
		
		setLuceneWrapper(newLocalLucene(revision));
		
		cleanLocalLuceneRevisions(revision);
	}
	
	@SuppressWarnings("unchecked")
	protected Class<? extends Analyzer> getLuceneAnalyzerType() {
		try {
			return (Class<? extends Analyzer>)Classes.getClass(analyzer);
		}
		catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected LuceneWrapper newMemoryLucene() {
		Directory directory = new RAMDirectory();
		LuceneWrapper lw = new LuceneWrapper(directory, getLuceneAnalyzerType());
		return lw;
	}

	protected LuceneWrapper newLocalLucene(long revision) {
		try {
			File lucenePath = getLuceneLocation();

			File latestPath = new File(lucenePath, String.valueOf(revision));
			
			Directory directory = FSDirectory.open(Paths.get(latestPath.getPath()));

			LuceneWrapper lw = new LuceneWrapper(directory, getLuceneAnalyzerType());
			
			return lw;
		}
		catch (IOException e) {
			throw Exceptions.wrapThrow(e);
		}
	}
	

	//----------------------------------------
	// ObjectProvider implements
	//
	@Override
	public LuceneWrapper getObject() {
		return getLuceneWrapper();
	}
}
