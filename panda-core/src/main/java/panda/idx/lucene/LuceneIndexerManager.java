package panda.idx.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import panda.idx.IndexException;
import panda.idx.Indexer;
import panda.idx.IndexerManager;


public class LuceneIndexerManager implements IndexerManager {
	public static final String DEFAULT = "default";
	
	protected Map<String, LuceneIndexer> indexes;

	public LuceneIndexerManager() {
		indexes = new HashMap<String, LuceneIndexer>();
	}

	protected Directory getDirectory(String name) {
		try {
			return FSDirectory.open(Paths.get(name));
		}
		catch (IOException e) {
			throw new IndexException("Failed to get lucene directory: " + name, e);
		}
	}
	
	protected Analyzer getAnalyzer(String name) {
		return new StandardAnalyzer();
	}
	
	@Override
	public synchronized Indexer getIndexer() {
		return getIndexer(DEFAULT);
	}
	
	@Override
	public synchronized Indexer getIndexer(String name) {
		LuceneIndexer li = indexes.get(name);
		if (li == null) {
			li = new LuceneIndexer(name, getDirectory(name), getAnalyzer(name));
			indexes.put(name, li);
		}
		return li;
	}

	protected synchronized void addIndexer(LuceneIndexer li) {
		indexes.put(li.name(), li);
	}

	@Override
	public synchronized void dropIndexer() {
		dropIndexer(DEFAULT);
	}
	
	@Override
	public synchronized void dropIndexer(String name) {
		LuceneIndexer li = indexes.remove(name);
		if (li == null) {
			return;
		}
		
		li.drop();
	}
	
	public synchronized void close() throws IOException {
		for (LuceneIndexer li : indexes.values()) {
			li.close();
		}
		indexes.clear();
	}
}
