package panda.app.index;

import panda.idx.Indexer;
import panda.idx.IndexerManager;


public interface RevisionedIndexerManager extends IndexerManager {
	public Indexer newIndexer();
	
	public Indexer newIndexer(String name);
	
	public void setIndexer(Indexer indexer);
}
