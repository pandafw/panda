package panda.idx.gae;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;

import panda.idx.Indexer;
import panda.idx.IndexerManager;


public class GaeIndexerManager implements IndexerManager {
	public static final String DEFAULT = "default";
	
	protected Map<String, GaeIndexer> indexes;
	protected Locale locale;

	public GaeIndexerManager() {
		indexes = new HashMap<String, GaeIndexer>();
	}

	protected IndexSpec getIndexSpec(String name) {
		return IndexSpec.newBuilder().setName(name).build();
	}
	
	protected Index getIndex(String name) {
		IndexSpec is = getIndexSpec(name);
		Index index = SearchServiceFactory.getSearchService().getIndex(is);
		return index;
	}
	
	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale the locale to set
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public synchronized Indexer getIndexer() {
		return getIndexer(DEFAULT);
	}
	
	@Override
	public synchronized Indexer getIndexer(String name) {
		GaeIndexer gi = indexes.get(name);
		if (gi == null) {
			gi = new GaeIndexer(name, getIndex(name), locale);
			indexes.put(name, gi);
		}
		return gi;
	}

	protected synchronized void addIndexer(GaeIndexer gi) {
		indexes.put(gi.name(), gi);
	}

	@Override
	public synchronized void dropIndexer() {
		dropIndexer(DEFAULT);
	}
	
	@Override
	public synchronized void dropIndexer(String name) {
		GaeIndexer gi = indexes.remove(name);
		if (gi == null) {
			return;
		}
		
		gi.drop();
	}
	
	public synchronized void close() {
		indexes.clear();
	}
}
