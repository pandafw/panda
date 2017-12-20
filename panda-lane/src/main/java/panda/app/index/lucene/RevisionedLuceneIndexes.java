package panda.app.index.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import panda.app.constant.MVC;
import panda.app.constant.SET;
import panda.app.index.RevisionedIndexes;
import panda.app.util.AppSettings;
import panda.idx.IndexException;
import panda.idx.Indexer;
import panda.idx.Indexes;
import panda.idx.lucene.LuceneIndexer;
import panda.idx.lucene.LuceneIndexes;
import panda.io.FileNames;
import panda.io.Files;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Classes;
import panda.lang.Numbers;
import panda.log.Log;
import panda.log.Logs;


@IocBean(type=Indexes.class, create="initialize", depose="close")
public class RevisionedLuceneIndexes extends LuceneIndexes implements RevisionedIndexes {
	private static final Log log = Logs.getLog(RevisionedLuceneIndexes.class);
	
	@IocInject
	protected AppSettings settings;
	
	@IocInject(value=MVC.LUCENE_LOCATION, required=false)
	protected String location = "web://WEB-INF/_lucene";
	
	@IocInject(value=MVC.LUCENE_ANALYZER, required=false)
	protected String analyzer = StandardAnalyzer.class.getName();
	
	/**
	 * initialize
	 * @throws IOException if an IO error occurs
	 */
	public void initialize() throws IOException {
		cleanOldRevisionDirectory();
	}

	@Override
	protected Directory getDirectory(String name) {
		try {
			long lv = getLatestRevision(name);

			String root = getLuceneLocation();
			String folder = name + "." + lv;
			return FSDirectory.open(Paths.get(root, folder));
		}
		catch (IOException e) {
			throw new IndexException("Failed to get lucene directory: " + name, e);
		}
	}

	//-----------------------------------------------
	protected String getLuceneLocation() throws IOException {
		String path = settings.getPropertyAsPath(SET.LUCENE_LOCATION, location);
		Files.makeDirs(path);
		return path;
	}
	
	protected long getLatestRevision(String name) throws IOException {
		File root = new File(getLuceneLocation());
		
		long latest = 1;
		for (String s : root.list()) {
			String n = FileNames.getBaseName(s);
			if (!name.equals(n)) {
				continue;
			}
			
			String e = FileNames.getExtension(s);
			Long i = Numbers.toLong(e);
			File f = new File(root, s);
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

	private void removeLuceneDirectory(File root, String folder) {
		File f = new File(root, folder);
		if (f.exists()) {
			log.info("Remove old lucene directory: " + f);
			try {
				Files.forceDelete(f);
			}
			catch (IOException e) {
				log.warn("Failed delete " + f.getPath(), e);
			}
		}
	}
	
	private void cleanOldRevisionDirectory() throws IOException {
		File root = new File(getLuceneLocation());

		Map<String, Long> vs = new HashMap<String, Long>();
		for (String s : root.list()) {
			String n = FileNames.getBaseName(s);
			String e = FileNames.getExtension(s);
			Long i = Numbers.toLong(e);
			
			File f = new File(root, s);
			if (i == null || !Files.isDirectory(f)) {
				// Remove invalid lucene file
				removeLuceneDirectory(root, s);
				continue;
			}

			Long latest = vs.get(n);
			if (latest == null) {
				vs.put(n, i);
			}
			else if (latest.longValue() < i) {
				vs.put(n, i);
				removeLuceneDirectory(root, n + "." + latest);
			}
			else {
				removeLuceneDirectory(root, s);
			}
		}
	}

	@Override
	protected Analyzer getAnalyzer(String name) {
		return (Analyzer)Classes.born(analyzer);
	}
	
	//-------------------------------------------------------------------
	@Override
	public Indexer newIndexer() {
		return newIndexer(DEFAULT);
	}
	
	@Override
	public synchronized Indexer newIndexer(String name) {
		try {
			long v = getLatestRevision(name) + 1;

			String root = getLuceneLocation();
			String folder = name + '.' + v;
			
			Directory directory = FSDirectory.open(Paths.get(root, folder));

			LuceneIndexer li = new LuceneIndexer(name, directory, getAnalyzer(name));
			
			return li;
		}
		catch (IOException e) {
			throw new IndexException("Failed to create a new lucene indexer: " + name, e);
		}
	}
	
	@Override
	public synchronized void setIndexer(Indexer indexer) {
		LuceneIndexer li = indexes.get(indexer.name());
		log.info("old lucene indexer: " + li);
		if (li != null) {
			if (((FSDirectory)li.getDirectory()).getDirectory().equals(((FSDirectory)((LuceneIndexer)indexer).getDirectory()).getDirectory())) {
				log.error("Failed to set same same FSDirectory indexer");
				return;
			}
			log.info("drop lucene indexer: " + indexer);
			dropIndexer(indexer.name());
		}

		log.info("set lucene indexer: " + indexer);
		addIndexer((LuceneIndexer)indexer);
	}
}
