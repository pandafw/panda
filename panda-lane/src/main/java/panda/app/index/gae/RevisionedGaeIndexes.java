package panda.app.index.gae;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.google.appengine.api.search.GetIndexesRequest;
import com.google.appengine.api.search.GetResponse;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchService;
import com.google.appengine.api.search.SearchServiceFactory;

import panda.app.constant.MVC;
import panda.app.index.RevisionedIndexes;
import panda.app.util.AppSettings;
import panda.idx.IndexException;
import panda.idx.Indexer;
import panda.idx.Indexes;
import panda.idx.gae.GaeIndexer;
import panda.idx.gae.GaeIndexes;
import panda.io.FileNames;
import panda.ioc.annotation.IocBean;
import panda.ioc.annotation.IocInject;
import panda.lang.Numbers;
import panda.log.Log;
import panda.log.Logs;


@IocBean(type=Indexes.class, create="initialize", depose="close")
public class RevisionedGaeIndexes extends GaeIndexes implements RevisionedIndexes {
	private static final Log log = Logs.getLog(RevisionedGaeIndexes.class);
	
	@IocInject
	protected AppSettings settings;
	
	/**
	 * initialize
	 * @throws IOException if an IO error occurs
	 */
	public void initialize() throws IOException {
		cleanOldRevisionIndex();
	}

	@Override
	protected IndexSpec getIndexSpec(String name) {
		long lv = getLatestRevision(name);
		return IndexSpec.newBuilder().setName(name + "." + lv).build();
	}

	protected long getLatestRevision(String name) {
		SearchService ss = SearchServiceFactory.getSearchService();
		GetResponse<Index> gr = ss.getIndexes(GetIndexesRequest.newBuilder());

		long latest = 1;
		for (Index index : gr) {
			String s = index.getName();
			String fn = FileNames.getBaseName(s);
			if (!name.equals(fn)) {
				continue;
			}
			
			String fe = FileNames.getExtension(s);
			Long i = Numbers.toLong(fe);
			if (i == null) {
				log.warn("Ignore invalid gae index: " + s);
				continue;
			}

			if (i > latest) {
				latest = i;
			}
		}
		return latest;
	}

	@SuppressWarnings("deprecation")
	private void deleteIndex(Index index) {
		try {
			log.info("Delete gae index " + index.getName());
			index.deleteSchema();
		}
		catch (Exception e) {
			log.warn("Failed delete gae index " + index.getName(), e);
		}
	}
	
	private void cleanOldRevisionIndex() throws IOException {
		SearchService searchService = SearchServiceFactory.getSearchService();
		GetResponse<Index> response = searchService.getIndexes(GetIndexesRequest.newBuilder());

		Map<String, Index> vs = new HashMap<String, Index>();
		for (Index index : response) {
			String s = index.getName();
			String n = FileNames.getBaseName(s);
			String e = FileNames.getExtension(s);
			Long i = Numbers.toLong(e);
			
			if (i == null) {
				// Remove invalid gae index
				deleteIndex(index);
				continue;
			}

			Index latest = vs.get(n);
			if (latest == null) {
				vs.put(n, index);
			}
			else {
				String le = FileNames.getExtension(latest.getName());
				long lv = Numbers.toLong(le, 0L);
				if (lv < i) {
					vs.put(n, index);
					deleteIndex(latest);
				}
				else {
					deleteIndex(index);
				}
			}
		}
	}

	//-------------------------------------------------------------------
	@IocInject(value=MVC.GAE_SEARCH_LOCALE, required=false)
	public void setLocale(Locale locale) {
		super.setLocale(locale);
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
			IndexSpec is = IndexSpec.newBuilder().setName(name + "." + v).build();
			Index index = SearchServiceFactory.getSearchService().getIndex(is);
			GaeIndexer gi = new GaeIndexer(name, index, getLocale());
			return gi;
		}
		catch (Exception e) {
			throw new IndexException("Failed to create a new gae indexer: " + name, e);
		}
	}
	
	@Override
	public synchronized void setIndexer(Indexer indexer) {
		GaeIndexer gi = indexes.get(indexer.name());
		log.info("old gae indexer: " + gi);
		
		if (gi != null) {
			if (gi.getIndex().getName().equals(((GaeIndexer)indexer).getIndex().getName())) {
				log.error("Failed to set same same gae indexer");
				return;
			}

			log.info("drop gae indexer: " + gi);
			gi.drop();
		}

		log.info("set gae indexer: " + indexer);
		addIndexer((GaeIndexer)indexer);
	}
}
